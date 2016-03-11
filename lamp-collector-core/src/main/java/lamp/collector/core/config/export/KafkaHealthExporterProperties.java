package lamp.collector.core.config.export;

import lamp.collector.core.CollectorConstants;
import lamp.support.kafka.SimpleKafkaProducerProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = CollectorConstants.EXPORT_HEALTH_KAFKA_PREFIX)
public class KafkaHealthExporterProperties extends SimpleKafkaProducerProperties {

}
