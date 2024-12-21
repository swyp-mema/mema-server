package com.swyp.mema.global.config.emailsender;

import com.swyp.mema.global.config.env.EnvConfig;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Configuration
public class NaverEmailConfig {

    private final String USER_EMAIL;
    private final String USER_PASSWORD;

    public NaverEmailConfig(EnvConfig envConfig) {
        USER_EMAIL = envConfig.getMailAddress();
        USER_PASSWORD = envConfig.getMailPassword();
    }

    @Primary
    @Bean(name = "naverEmailProperties")
    public Properties emailProperties(){
        Properties props = new Properties();
        props.put("mail.username", USER_EMAIL);
        props.put("mail.host", "smtp.naver.com");
        props.put("mail.port", "465");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.debug", "faluse");
        props.put("mail.smtp.ssl.trust","smtp.naver.com");
        props.put("mail.smtp.ssl.enable","true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "false");
        return props;
    }

    @Component
    public class SimpleAuthenticator extends Authenticator {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            // TODO : 보안을 위해 수정필요
            return new PasswordAuthentication(USER_EMAIL, USER_PASSWORD);
        }
    }
}
