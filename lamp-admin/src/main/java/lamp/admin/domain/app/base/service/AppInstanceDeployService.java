package lamp.admin.domain.app.base.service;

import lamp.admin.core.agent.AgentClient;
import lamp.admin.core.agent.AgentResponseErrorException;
import lamp.admin.core.app.base.App;
import lamp.admin.core.app.base.AppInstance;
import lamp.admin.core.app.base.AppInstanceStatus;
import lamp.admin.core.app.simple.SimpleAppContainer;
import lamp.admin.domain.agent.model.Agent;
import lamp.admin.domain.agent.service.AgentService;
import lamp.admin.domain.app.base.exception.AppInstanceException;
import lamp.admin.domain.app.base.model.AppInstanceDeployPolicy;
import lamp.admin.domain.resource.repo.service.AppResourceLoader;
import lamp.common.utils.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AppInstanceDeployService {

	@Autowired
	private AgentService agentService;
	@Autowired
	private AgentClient agentClient;

	@Autowired
	private AppInstanceService appInstanceService;

	@Autowired
	private AppResourceLoader appResourceLoader;


	public void deployAndStart(List<AppInstance> appInstances, AppInstanceDeployPolicy deployPolicy) {
		for (AppInstance appInstance : appInstances) {
			try {
				deployAndStart(appInstance);
			} catch (Exception e) {
				log.warn("App deploy failed", e);
			}
		}
	}

	public void deployAndStart(AppInstance appInstance) {
		Agent agent = agentService.getAgent(appInstance.getHostId());

		deployAndStart(agent, appInstance);

		start(agent, appInstance);
	}

	public void deployAndStart(Agent agent, AppInstance appInstance) {
		appInstanceService.updateAppInstanceStatus(appInstance, AppInstanceStatus.DEPLOYING);

		try {
			Resource resource = null;
			if (appInstance.getAppContainer() instanceof SimpleAppContainer) {
				resource = appResourceLoader.getResource(((SimpleAppContainer) appInstance.getAppContainer()).getAppResource());
			}
			agentClient.deployApp(agent, appInstance, resource);
			appInstanceService.updateAppInstanceStatus(appInstance, AppInstanceStatus.DEPLOYED);
		} catch (Exception e) {
			log.error("App Deploy failed", e);
			appInstanceService.updateAppInstanceStatus(appInstance, AppInstanceStatus.DEPLOY_FAILED, e.getMessage());
			throw new AppInstanceException(AppInstanceStatus.DEPLOY_FAILED, e);
		}
	}

	public void start(Agent agent, AppInstance appInstance) {
		appInstanceService.updateAppInstanceStatus(appInstance, AppInstanceStatus.STARTING);

		try {
			agentClient.start(agent, appInstance.getId());
		} catch (Exception e) {
			log.error("App Start failed", e);
			appInstanceService.updateAppInstanceStatus(appInstance, AppInstanceStatus.START_FAILED, ExceptionUtils.getStackTrace(e, 1000));
			throw new AppInstanceException(AppInstanceStatus.START_FAILED, e);
		}
	}


	public void redeploy(App app, List<AppInstance> appInstances, AppInstanceDeployPolicy deployPolicy) {
		Resource resource = null;
		if (app.getContainer() instanceof SimpleAppContainer) {
			resource = appResourceLoader.getResource(((SimpleAppContainer) app.getContainer()).getAppResource());
		}

		for (AppInstance appInstance : appInstances) {
			try {
				Agent agent = agentService.getAgent(appInstance.getHostId());

				agentClient.reployApp(agent, app, appInstance, resource);

				appInstance.setStatus(AppInstanceStatus.STARTING);

				try {
					agentClient.start(agent, appInstance.getId());
				} catch (Exception e) {
					log.warn("App start failed", e);
					appInstance.setStatus(AppInstanceStatus.START_FAILED);
					appInstance.setStatusMessage(ExceptionUtils.getStackTrace(e));
				}
			} catch (Exception e) {
				log.warn("App redeploy failed", e);
				appInstance.setStatus(AppInstanceStatus.DEPLOY_FAILED);
				appInstance.setStatusMessage(ExceptionUtils.getStackTrace(e));
			}

			appInstanceService.updateAppInstance(appInstance);
		}

	}

	public void undeploy(App app, List<AppInstance> appInstances) {
		for (AppInstance appInstance : appInstances) {
			undeploy(appInstance, true);
		}
	}


	public void undeploy(AppInstance appInstance, boolean forceStop) {
		Agent agent = agentService.getAgent(appInstance.getHostId());
		try {
			agentClient.undeployApp(agent, appInstance.getId(), forceStop);
			appInstanceService.delete(appInstance);
		} catch (AgentResponseErrorException e) {
			if ("APP_NOT_FOUND".equals(e.getAgentError().getCode())) {
				log.debug("undeploy : APP_NOT_FOUND =", e);
				appInstanceService.delete(appInstance);
			} else {
				throw e;
			}
		}
	}

}
