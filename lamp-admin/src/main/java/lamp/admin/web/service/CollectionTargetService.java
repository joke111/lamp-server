package lamp.admin.web.service;

import lamp.admin.core.agent.service.TargetServerService;
import lamp.collector.core.service.HealthTargetService;
import lamp.collector.core.service.MetricsTargetService;
import lamp.common.metrics.HealthTarget;
import lamp.common.metrics.MetricsTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class CollectionTargetService implements HealthTargetService, MetricsTargetService {

    @Autowired
    private TargetServerService targetServerService;

    @Override
    public List<HealthTarget> getHealthTargets() {
        return targetServerService.getTargetServerList().stream().collect(Collectors.toList());
    }

    @Override
    public List<MetricsTarget> getMetricsTargets() {
        return targetServerService.getTargetServerList().stream().collect(Collectors.toList());
    }

}
