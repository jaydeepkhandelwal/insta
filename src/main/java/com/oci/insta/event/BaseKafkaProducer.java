package com.oci.insta.event;

import java.io.IOException;


public abstract class BaseKafkaProducer<T> {

    public abstract void sendEvent(T message)
            throws IOException, InterruptedException;

    public abstract String getTopicName();


}
