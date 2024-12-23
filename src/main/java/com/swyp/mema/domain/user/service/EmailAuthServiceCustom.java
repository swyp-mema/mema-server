package com.swyp.mema.domain.user.service;

import com.swyp.mema.domain.user.dto.request.EmailCheckReq;
import com.swyp.mema.domain.user.exception.EmailAuthCodeFailException;
import com.swyp.mema.domain.user.exception.EmailAuthSessionNotexistException;
import com.swyp.mema.global.config.emailsender.NaverEmailConfig;
import com.swyp.mema.global.config.env.EnvConfig;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;
import java.util.Properties;


@Service
public class EmailAuthServiceCustom {

    @Autowired
    private final Properties props;
    private final NaverEmailConfig.SimpleAuthenticator authenticator;
    private HashMap<String, String> customSession;
//    private final String emailSender = "Mema@naver.com";

    public EmailAuthServiceCustom(EnvConfig envConfig, JavaMailSender javaMailSender, NaverEmailConfig.SimpleAuthenticator authenticator) {
        this.authenticator = authenticator;
        props = System.getProperties();
        this.customSession = new HashMap<>();
    }

    public void sendMail(String recipientsEmail, HttpServletRequest request, HttpServletResponse response) {

        System.out.println("sendMail - "+recipientsEmail);
        // 인증 코드 생성
        int code = createNumber();

        Session mailSession = Session.getInstance(props, authenticator);
        Message message = new MimeMessage(mailSession);

        try{
            // 발신자, 수신자 설정
            System.out.println(props.getProperty("mail.username"));
            message.setFrom(new InternetAddress(props.getProperty("mail.username")));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientsEmail));

            // 메일 제목, 내용 설정
            message.setSubject(createSubject());
            message.setContent(createBody(code),"text/html; charset=utf-8");

            // 인증 세선 셜정
            setSession(recipientsEmail,String.valueOf(code));
            System.out.println("mailAuth: "+request.getSession().getAttribute("mailAuth") + ", session id :" + request.getSession().getId());

            // 전송
            Transport.send(message);
        } catch (MessagingException e) {
            System.out.println("error in mail creation");
            e.printStackTrace();
        }
    }

    /**
     * 메일 인증 코드 검증 함수
     * 오류시 throw error
     * 인증 성공시 return void
     * @param emailCheckReq dto
     * @param request
     */
    public void checkCode(EmailCheckReq emailCheckReq, HttpServletRequest request){

        if(customSession.get(emailCheckReq.getEmail()) == null){

            throw new EmailAuthSessionNotexistException();
        }

        if(!Objects.equals(customSession.get(emailCheckReq.getEmail()), emailCheckReq.getCode())){

            throw new EmailAuthCodeFailException();
        }
    }

    /**
     * 메일 제목 생성 함수
     * @return  메일 제목
     */
    private String createSubject(){

        return "Mema 이메일 인증";
    }

    /**
     * 메일 본문 생성 함수
     * @param code 인증코드
     * @return 메일 본문
     */
    private String createBody(int code){

        String body = "";
        body += "<h2>" + "Mema 인증번호 입니다." + "</h2>";
        body += "<h1>" + code + "</h1>";
        body += "<h3>" + "인증번호는 5분간 유효합니다." + "</h3>";
        return body;
    }

    /**
     * 인증코드 생성 함수
     * @return 랜덤 숫자 6글자
     */
    private int createNumber() {
        return (int)(Math.random() * (90000)) + 100000;
    }

    /**
     * 인증 세션 생성
     * 인증 유효시간 5분
     * @param
     * @param code 인증코드
     */
    private void setSession(String email, String code){

        customSession.put(email, String.valueOf(code));
    }
}
