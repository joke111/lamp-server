package lamp.server.aladin.core.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "lamp_managed_app")
public class ManagedApp extends AbstractAuditingEntity {

	@Id
	private String id;
	private String name;
	private String description;

	@ManyToOne
	@JoinColumn(name = "target_server_id", nullable = false, updatable = false)
	private TargetServer targetServer;

	@ManyToOne
	@JoinColumn(name = "template_id", nullable = false, updatable = false)
	private AppTemplate appTemplate;

	private String groupId;
	private String artifactId;
	private String artifactName;
	private String version;

	private LocalDateTime registerDate;

}
