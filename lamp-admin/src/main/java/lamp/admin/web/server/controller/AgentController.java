package lamp.admin.web.server.controller;

import lamp.admin.domain.agent.model.AgentDto;
import lamp.admin.domain.agent.service.AgentService;
import lamp.admin.domain.base.model.JavaVirtualMachine;
import lamp.admin.web.MenuConstants;
import lamp.admin.web.support.annotation.MenuMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@MenuMapping(MenuConstants.AGENT)
@Controller
@RequestMapping("/server/agent")
public class AgentController {

	@Autowired
	private AgentService agentService;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public String list(Model model, Pageable pageable) {
		Page<AgentDto> page = agentService.getAgentDtoList(pageable);
		model.addAttribute("page", page);
		return "server/agent/list";
	}


	@RequestMapping(path = "/{id}/vm", method = RequestMethod.GET)
	public String vm(@PathVariable("id") String id, Model model) {
		List<JavaVirtualMachine> vmList = agentService.getJavaVmList(id);
		model.addAttribute("vmList", vmList);
		return "server/agent/vm/list";
	}

}
