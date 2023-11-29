package lu.sfeir.commerce.product.events;

import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class StockUpdatedDeserialiser implements Deserializer<StockUpdated> {
	
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public StockUpdated deserialize(String topic, byte[] data) {
		try {

			return objectMapper.readValue(new String(data,"UTF-8"), StockUpdated.class);

		}catch(Exception e) {
			
		}
		return null;
	}

}
