package lu.sfeir.commerce.product.events;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import lu.sfeir.commerce.product.services.ProductService;

@EnableKafka
@Configuration
public class StockKafkaConsumerConfig {
	@Value(value = "${kafka.bootstrapAddress}")
	private String bootstrapAddress;

	@Value(value = "${kafka.groupId}")
	private String groupId;

	@Autowired
	private StockUpdatedDeserialiser stockUpdatedDeserialiser;

	@Autowired
	private ProductService productService;
	
	@Bean
	public ConsumerFactory<String, StockUpdated> consumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
	   //  props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StockUpdatedDeserialiser.class);

		return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), stockUpdatedDeserialiser);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, StockUpdated> kafkaListenerContainerFactory() {

		ConcurrentKafkaListenerContainerFactory<String, StockUpdated> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

	@KafkaListener(topics = "stocks", groupId = "commerce")
	public void listenGroupFoo(StockUpdated event) {
		System.out.println("Received Message in group foo: " + event);
		productService.updateProductStock(event.getProductId(), event.getNumberAvailable());

	}

}
