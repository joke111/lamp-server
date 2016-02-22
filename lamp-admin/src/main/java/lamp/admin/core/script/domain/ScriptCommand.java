package lamp.admin.core.script.domain;

import lamp.admin.core.base.domain.AbstractAuditingEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "lamp_script_command")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "command_type", discriminatorType = DiscriminatorType.STRING)
public class ScriptCommand extends AbstractAuditingEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "script_id")
	private Script script;

	private int seq;
	private String name;
	private String description;

	@Column(name = "command_type", insertable = false, updatable = false)
	private String type;
}
