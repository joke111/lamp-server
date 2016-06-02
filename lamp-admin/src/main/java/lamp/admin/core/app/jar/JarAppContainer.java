package lamp.admin.core.app.jar;


import com.fasterxml.jackson.annotation.JsonTypeName;
import lamp.admin.core.app.base.AppContainerType;
import lamp.admin.core.app.simple.SimpleAppContainer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonTypeName(AppContainerType.Names.JAR)
public class JarAppContainer extends SimpleAppContainer {

	private String appFilename;

}
