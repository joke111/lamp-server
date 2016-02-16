package lamp.admin.core.app.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class LocalAppFileDto {

	private Long id;
	private String name;
	private String description;
	private Long repositoryId;
	private String groupId;
	private String artifactId;
	private String baseVersion;
	private String version;

	private String pathname;
	private String filename;
	private LocalDateTime fileDate;
}
