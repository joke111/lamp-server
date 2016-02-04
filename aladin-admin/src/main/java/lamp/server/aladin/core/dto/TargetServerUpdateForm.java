package lamp.server.aladin.core.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class TargetServerUpdateForm {

	@NotNull
	private Long id;
	@NotEmpty
	private String name;
	private String description;

	@NotEmpty
	private String hostname;
	@NotEmpty
	private String address;

	private int sshPort = 22;

	private String authType;
	private String username;
	private String password;

	private Boolean agentInstalled;
	@NotEmpty
	private String agentInstallPath;

	private String agentStartCommandLine;

	private Boolean agentMonitor;
	private Long agentMonitorInterval;

}
