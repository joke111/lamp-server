package lamp.admin.core.host;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HostCredentials {

	private String username;

	private boolean usePassword = true;

	private String password;

	private String privateKey;
	private String passphrase;

	private int sshPort = 22;

	private int parallelInstallCount;

}
