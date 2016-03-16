package lamp.admin.core.app.domain;

import lamp.admin.core.app.validation.MultipartFileNotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class LocalAppFileUploadForm {

	@NotEmpty
	private String name;
	private String description;

	private String repositoryId;
	@NotEmpty
	private String groupId;
	@NotEmpty
	private String artifactId;
	@NotEmpty
	private String version;

	private String filename;

	@MultipartFileNotEmpty
	private MultipartFile uploadFile;

	private Long refId;
}
