package lamp.admin.domain.resource.repo.model.entity;


import lamp.admin.domain.resource.repo.model.AppRepoType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@DiscriminatorValue(value = AppRepoType.Values.URL)
@Table(name = "lamp_app_url_repository")
@PrimaryKeyJoinColumn(name = "id")
public class UrlAppRepoEntity extends AppRepoEntity {

	@Column(name = "baseUrl")
	private String baseUrl;

	@Column(name = "repository_auth_type")
	private String authType;
	@Column(name = "repository_auth_url")
	private String authUrl;
	@Column(name = "repository_username")
	private String username;
	@Column(name = "repository_password")
	private String password;


}
