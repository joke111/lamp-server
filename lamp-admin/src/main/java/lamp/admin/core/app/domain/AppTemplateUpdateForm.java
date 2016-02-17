package lamp.admin.core.app.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class AppTemplateUpdateForm {

	private String name;
	private String description;

	@NotNull
	private AppResourceType resourceType = AppResourceType.NONE;

	private Long repositoryId;
	private String groupId;
	private String artifactId;
	private String artifactName;
	private String version;

	private String appUrl;

	private AppProcessType processType = AppProcessType.DAEMON;
	private String appDirectory;
	private String workDirectory;
	private String logDirectory;

	private String pidFile;
	private String stdOutFile;
	private String stdErrFile;

	private String commandShell;
	private String startCommandLine;
	private String stopCommandLine;

	private String appFilename;

	private boolean monitor;

	private String commands;

	private ParametersType parametersType;
	private String parameters;

}
