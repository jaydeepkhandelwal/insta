package com.oci.insta.event;

import com.oci.insta.entities.event.ReviewEvent;
import com.oci.insta.entities.models.Review;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class ReviewEventProducer extends BaseKafkaProducer<ReviewEvent> {

    @Autowired
    private KafkaTemplate<String, ReviewEvent> kafkaTemplate;

    @Override
    public void sendEvent(ReviewEvent message) throws IOException, InterruptedException {
        log.info("Sending event: to Kafka, message: {}", message);
        //kafkaTemplate.send(MessageBuilder.withPayload(message).setHeader(KafkaHeaders.TOPIC, getTopicName()).build());

    }

    @Override
    public String getTopicName() {
       return "reviews";
    }
}
