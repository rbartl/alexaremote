package com.edvbartl.alexafb.services

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct

@Service
class MqttService implements MqttCallback,InitializingBean {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private MqttConnectOptions conOpt;
    private MqttClient client;
    MemoryPersistence dataStore

    def serviceMethod() {

    }

    void startupClient() throws Exception {
        log.error("sdfsdf")
        log.info ("starting Mqtt client")
        if (!dataStore) {
            MemoryPersistence dataStore = new MemoryPersistence();
        }
        conOpt = new MqttConnectOptions();
        conOpt.setPassword("secret1".toCharArray())
        conOpt.setUserName("username")

        // Construct an MQTT blocking mode client
        client = new MqttClient("tcp://iot.localdomain:1883", "alexaapp", dataStore);
        client.connect(conOpt)

        // Set this wrapper as the callback handler
        client.setCallback(this);

        client.subscribe("+/hubs/harmony/current_activity")
        client.subscribe("+/hubs/harmony/state")

        log.info("started client")

    }

    @Override
    void connectionLost(Throwable cause) {
        log.info("mqtt connection lost", cause)
        Thread.sleep(20000)
        startupClient()
    }

    @Override
    void messageArrived(String topic, MqttMessage message) throws Exception {
        log.info("message arrived" + message + "/" + topic)
    }

    @Override
    void deliveryComplete(IMqttDeliveryToken token) {
        log.info("delivery complete" + token)
    }

    @Override
    void afterPropertiesSet() throws Exception {
        startupClient()
    }

    void publish(String channel, String message)
    {
        String namespace = "rbharmony"
        String harmonyname = "harmony"
        if (channel)
            if (channel.contains("command")) {
                String queue = namespace + "/hubs/" + harmonyname + "/" + channel
                log.info("sending '" + message + "' to '" + queue + "'")
                try {
                    client.publish(queue, new MqttMessage(message.getBytes("UTF-8")))
                } catch (org.eclipse.paho.client.mqttv3.MqttException e) {
                    startupClient()
                    client.publish(queue, new MqttMessage(message.getBytes("UTF-8")))
                }
            }
    }
}
