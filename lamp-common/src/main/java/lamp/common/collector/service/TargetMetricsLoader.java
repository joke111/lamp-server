package lamp.common.collector.service;

import lamp.common.collector.model.MetricsTarget;
import lamp.common.collector.model.TargetMetrics;

public interface TargetMetricsLoader extends MetricsLoader {

	TargetMetrics getMetrics(MetricsTarget metricsTarget);

}
