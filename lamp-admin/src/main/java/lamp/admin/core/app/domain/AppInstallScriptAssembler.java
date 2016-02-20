package lamp.admin.core.app.domain;

import lamp.admin.core.script.domain.ScriptCommand;
import lamp.admin.core.script.domain.ScriptCommandsParser;
import lamp.admin.utils.assembler.AbstractAssembler;
import lamp.admin.utils.assembler.Populater;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class AppInstallScriptAssembler extends AbstractAssembler<AppInstallScriptCreateForm, AppInstallScript> implements Populater<AppInstallScriptUpdateForm, AppInstallScript> {

	@Autowired
	private ScriptCommandsParser scriptCommandsParser;

	@Override protected AppInstallScript doAssemble(AppInstallScriptCreateForm form) {

		AppInstallScript script = new AppInstallScript();
		BeanUtils.copyProperties(form, script, "commands");

		String commandsStr = form.getCommands();
		List<ScriptCommand> scriptCommands = scriptCommandsParser.parse(commandsStr);
		for (ScriptCommand scriptCommand : scriptCommands) {
			script.addCommand(scriptCommand);
		}

		return script;
	}

	@Override public void populate(AppInstallScriptUpdateForm form, AppInstallScript script) {
		log.info("AppInstallScriptUpdateForm = {}", form);
		BeanUtils.copyProperties(form, script, "commands");

		script.getCommands().clear();
		String commandsStr = form.getCommands();
		List<ScriptCommand> scriptCommands = scriptCommandsParser.parse(commandsStr);
		for (ScriptCommand scriptCommand : scriptCommands) {
			log.info("scriptCommand = {}", scriptCommand);
			script.addCommand(scriptCommand);
		}
	}
}
