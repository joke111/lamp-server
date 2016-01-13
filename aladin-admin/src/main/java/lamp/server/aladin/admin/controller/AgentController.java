package lamp.server.aladin.admin.controller;

import lamp.server.aladin.core.dto.AgentDto;
import lamp.server.aladin.core.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/agent")
@Controller
public class AgentController {

	@Autowired
	private AgentService agentService;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public String list(Model model, Pageable pageable) {
		Page<AgentDto> page = agentService.getAgentList(pageable);
		model.addAttribute("page", page);
		return "agent/list";
	}

}
