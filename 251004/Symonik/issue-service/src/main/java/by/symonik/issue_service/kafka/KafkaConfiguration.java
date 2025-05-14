package by.symonik.issue_service.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfiguration {

    @Bean
    public NewTopic newInTopic() {
        return new NewTopic("InTopic", 1, (short) 1);
    }

    @Bean
    public NewTopic newOutTopic() {
        return new NewTopic("OutTopic", 1, (short) 1);
    }
}
