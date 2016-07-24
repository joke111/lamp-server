package lamp.admin.web.resource.controller;

import lamp.admin.LampAdminConstants;

import lamp.admin.domain.resource.repo.model.dto.LocalAppFileDto;
import lamp.admin.domain.resource.repo.model.entity.AppRepoEntity;
import lamp.admin.domain.resource.repo.model.form.LocalAppFileUploadForm;
import lamp.admin.domain.resource.repo.service.AppRepoService;
import lamp.admin.domain.resource.repo.service.LocalAppFileService;
import lamp.admin.web.AdminErrorCode;
import lamp.admin.web.MenuConstants;
import lamp.admin.web.support.FlashMessage;
import lamp.admin.web.support.annotation.MenuMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Slf4j
@MenuMapping(MenuConstants.APP_REPO)
@Controller
@RequestMapping(value = "/resource/repository/LOCAL/{id}")
public class LocalAppRepoFileController {

	@Autowired
	private AppRepoService appRepoService;

	@Autowired
	private LocalAppFileService localAppFileService;

	@RequestMapping(path = "/file", method = RequestMethod.GET)
	public String files(@PathVariable("id") String id, Pageable pageable, Model model) {
		Page<LocalAppFileDto> page = localAppFileService.getLocalAppFileList(id, pageable);
		model.addAttribute("page", page);
		return "resource/repository/local/file/list";
	}

	@RequestMapping(path = "/file/addHostEntity", method = RequestMethod.GET)
	public String create(@PathVariable("id") String id,
						 @ModelAttribute("editForm") LocalAppFileUploadForm editForm, Model model) {
		model.addAttribute(LampAdminConstants.ACTION_KEY, LampAdminConstants.ACTION_CREATE);
		return "resource/repository/local/file/edit";
	}

	@RequestMapping(path = "/file/addHostEntity", method = RequestMethod.POST)
	public String create(@PathVariable("id") String id,
						 @Valid @ModelAttribute("editForm") LocalAppFileUploadForm editForm,
			BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			return create(id, editForm, model);
		}

		localAppFileService.uploadLocalAppFile(id, editForm);
		redirectAttributes.addFlashAttribute(LampAdminConstants.FLASH_MESSAGE_KEY, FlashMessage.ofSuccess(AdminErrorCode.INSERT_SUCCESS));

		return "redirect:/resource/repository/LOCAL/{id}/file ";
	}

	@ModelAttribute("appRepository")
	protected AppRepoEntity getAppRepository(@PathVariable("id") String id) {
		return appRepoService.getAppRepo(id);
	}

}
