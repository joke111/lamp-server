package lamp.admin.domain.resource.repo.service;

import com.google.common.collect.Iterables;

import lamp.admin.domain.resource.repo.model.entity.LocalAppFileEntity;
import lamp.admin.domain.resource.repo.model.entity.LocalAppRepoEntity;


import lamp.admin.domain.base.exception.Exceptions;
import lamp.admin.domain.base.exception.LampErrorCode;
import lamp.admin.domain.resource.repo.repository.LocalAppFileEntityRepository;
import lamp.common.utils.CollectionUtils;
import lamp.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Optional;

@Component
public class LocalAppResourceLoader {

	@Autowired
	private LocalAppFileEntityRepository localAppFileEntityRepository;

	public Resource getResource(LocalAppRepoEntity localAppRepoEntity, String groupId, String artifactId, String version) {
		String repositoryId = localAppRepoEntity.getId();
		if (StringUtils.isBlank(version)) {
			version = getLastVersion(repositoryId, groupId, artifactId);
		}
		Optional<LocalAppFileEntity> localAppResourceFromDb = localAppFileEntityRepository.findOneByRepositoryIdAndGroupIdAndArtifactIdAndBaseVersion(repositoryId, groupId, artifactId, version);

		LocalAppFileEntity localAppFile = localAppResourceFromDb.orElseThrow(() -> Exceptions.newException(LampErrorCode.APP_RESOURCE_NOT_FOUND));
		File file = new File(localAppFile.getPathname());
		return new FileSystemAppResource(file, localAppFile.getFilename(), groupId, artifactId, version);
	}

	protected String getLastVersion(String repositoryId, String groupId, String artifactId) {
		// TODO 정상적으로 작동하도록 수정 바람
		Page<LocalAppFileEntity> resourceList = localAppFileEntityRepository.findAllByRepositoryIdAndGroupIdAndArtifactIdOrderByVersionDesc(repositoryId, groupId, artifactId, new PageRequest(0, 1));
		if (CollectionUtils.isEmpty(resourceList.getContent())) {
			throw Exceptions.newException(LampErrorCode.APP_RESOURCE_NOT_FOUND, repositoryId, groupId, artifactId);
		}
		LocalAppFileEntity lastVersionResource = Iterables.getFirst(resourceList.getContent(), null);
		return lastVersionResource.getBaseVersion();
	}

}
