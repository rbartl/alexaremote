package com.edvbartl.alexafb.config;

import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.servlet.SpeechletServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by robert on 08.02.17.
 */
@Configuration
public class AlexaConfig {

    @Autowired
    private Speechlet alexaFbSpeechlet;

    @Bean
    public ServletRegistrationBean alexaFb() {

/*
        final Properties props = new Properties(System.getProperties());
        try (InputStream propsIn = alexaPropsResource.getInputStream()) {
            props.load(propsIn);
            System.setProperties(props);
        }
*/
        SpeechletServlet servlet;
        servlet = new SpeechletServlet();
        servlet.setSpeechlet(alexaFbSpeechlet);

        final ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(servlet, "/skill/index");

        return servletRegistrationBean;

    }
}
