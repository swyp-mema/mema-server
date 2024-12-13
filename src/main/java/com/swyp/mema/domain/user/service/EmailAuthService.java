package com.swyp.mema.domain.user.service;

import com.swyp.mema.domain.user.dto.request.EmailCheckReq;
import com.swyp.mema.global.config.env.EnvConfig;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmailAuthService {

    @Autowired
    private final EnvConfig envConfig;

    @Autowired
    private final JavaMailSender javaMailSender;

    Map<String, HashMap<String,Object>> emailSessionRepo;
    String senderEmail;

    public EmailAuthService(EnvConfig envConfig, JavaMailSender javaMailSender) {
        this.envConfig = envConfig;
        this.javaMailSender = javaMailSender;
        emailSessionRepo = new ConcurrentHashMap<>();
        senderEmail=envConfig.getCilentIp();
    }

    public String createSession(HttpServletResponse response) {

        HashMap<String, Object> sessionData = new HashMap<>();
        sessionData.put("time", System.currentTimeMillis());
        String sessionId = UUID.randomUUID().toString();
        emailSessionRepo.put(sessionId, sessionData);

        response.addHeader("sessionId", sessionId);
        return sessionId;
    }

    public int createNumber() {
        return (int)(Math.random() * (90000)) + 100000;
    }

    public MimeMessage createMail(String mail, int code, HttpServletResponse response) {

        MimeMessage message = javaMailSender.createMimeMessage();

        try{
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, mail);
            message.setSubject("Mema 이메일 인증");
            String body = "";
            body += "<h3>" + "Mema 인증번호 입니다." + "</h3>";
            body += "<h1>" + code + "</h1>";
            body += "<h3>" + "인증번호는 5분간 유효합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");

            String sessionId = createSession(response);
            emailSessionRepo.get(sessionId).put("code",code);
            System.out.println(emailSessionRepo.get(sessionId).toString());

        } catch (MessagingException e) {
            System.out.println("error in mail creation");
            e.printStackTrace();
        }



        return message;
    }

    public void sendMail(String mail, HttpServletResponse response) {

        System.out.println("sendMail - "+mail);

        int code = createNumber();
        MimeMessage message = createMail(mail, code, response);
        javaMailSender.send(message);
    }

    public boolean checkCode(EmailCheckReq emailCheckReq, HttpServletRequest request){

        //  헤더에서 세션 아이디 추출
        String sessionId = request.getHeader("sessionId");

        //  이메일 세션 저장소에서 세션 호출 및 검증
        String code = (String) emailSessionRepo.get(sessionId).get("code");
        return code.equals(emailCheckReq.getCode());
    }
}
