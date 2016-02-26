package lamp.watch.server.core.service;

import lamp.watch.server.core.domain.AppMetrics;
import lamp.watch.server.core.support.kafka.JsonDeserializer;
import lamp.watch.server.core.support.kafka.JsonSerializer;
import lamp.watch.server.core.support.kafka.KafkaConsumerRunner;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class KafkaToKairosDbService {

	private ExecutorService executor;
	private KafkaConsumerRunner kafkaConsumerRunner;

	@PostConstruct
	public void init() {
		Properties props = new Properties();

		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
		props.put(JsonDeserializer.VALUE_TYPE, AppMetrics.class);

		List<String> topics = Arrays.asList("lamp-agent.metrics");

		kafkaConsumerRunner = new KafkaConsumerRunner<>(props, topics, this::handleConsumerRecords);

		executor = Executors.newFixedThreadPool(1);
		executor.execute(kafkaConsumerRunner);
	}

	protected void handleConsumerRecords(ConsumerRecords<String, AppMetrics> records) {
		records.forEach(this::handleConsumerRecord);
	}

	protected void handleConsumerRecord(ConsumerRecord<String, AppMetrics> record) {
		AppMetrics appMetrics = record.value();
		log.info("appMetrics = {}, {}", record.key(), record.value());
	}

	@PreDestroy
	public void close() {
		kafkaConsumerRunner.shutdown();

		executor.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
				executor.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!executor.awaitTermination(60, TimeUnit.SECONDS))
					log.error("Pool did not terminate");
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			executor.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}
}
