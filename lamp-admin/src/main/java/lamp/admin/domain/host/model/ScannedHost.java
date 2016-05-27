package lamp.admin.domain.host.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ScannedHost {

	private String name;
	private String address;
	private boolean managed;

	private boolean connected;
	private Long responseTime;

}
