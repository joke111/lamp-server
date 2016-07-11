package lamp.monitoring.core.metrics.service;

import lamp.common.monitoring.model.MonitoringTargetMetrics;
import lamp.common.utils.ExceptionUtils;
import lamp.monitoring.core.alert.AlertRuleProcessor;
import lamp.monitoring.core.alert.model.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractMetricsAlertRuleProcessor<R extends AlertRule> implements AlertRuleProcessor<MonitoringTargetMetrics, R> {

	public AlertEvent doProcess(MonitoringTargetMetrics targetMetrics, R alertRule) {
		AlertRuleEvaluator<R, MonitoringTargetMetrics> alertRuleEvaluator = getAlertRuleEvaluator();
		if (alertRuleEvaluator.isAlertTarget(alertRule, targetMetrics)) {

			AlertEvent event = newAlertRuleMatchedEvent();
			event.setTimestamp(targetMetrics.getTimestamp());
			event.setTarget(targetMetrics);
			event.setRule(alertRule);

			try {
				AlertState state = alertRuleEvaluator.evaluate(alertRule, targetMetrics);
				event.setState(state);
			} catch (Throwable t) {
				event.setState(new AlertState(AlertStateCode.UNDETERMINED, ExceptionUtils.getStackTrace(t)));
			}
			return event;
		}
		return null;
	}

	protected abstract AlertEvent newAlertRuleMatchedEvent();

	protected abstract AlertRuleEvaluator<R, MonitoringTargetMetrics> getAlertRuleEvaluator();
}
