package lamp.admin.domain.host.service.form;

import lamp.admin.core.host.ScannedHost;
import lamp.admin.domain.host.model.HostCredentials;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class HostAddForm {

	@NotNull
	private Long clusterId;

	private List<ScannedHost> scannedHosts;

	private HostCredentials hostCredentials;

}
