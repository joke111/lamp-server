package lamp.watcher.kairosdb.service;

import lamp.common.monitoring.MonitoringTarget;
import lamp.common.utils.BooleanUtils;
import lamp.watcher.core.service.MonitoringTargetService;
import lamp.watcher.monitoring.collection.service.MonitoringHealthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

@Slf4j
public class MonitoringHealthScheduledService {

	@Autowired
	private MonitoringTargetService monitoringTargetService;

	@Autowired
	private MonitoringHealthService monitoringHealthCollectionService;

	public void monitoring() {
		Collection<MonitoringTarget> monitoringTargets = monitoringTargetService.getMonitoringTargetsForHealthCollection();
		monitoringTargets.stream()
				.filter(a -> BooleanUtils.isTrue(a.getHealthCollectionEnabled()))
				.forEach(this::monitoring);
	}

	protected void monitoring(MonitoringTarget monitoringTarget) {
		try {
			monitoringHealthCollectionService.monitoring(monitoringTarget);
		} catch (Exception e) {
			log.warn("Health Monitoring Failed", e);
		}
	}

}
