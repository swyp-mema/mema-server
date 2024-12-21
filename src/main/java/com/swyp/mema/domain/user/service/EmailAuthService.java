package com.swyp.mema.domain.user.service;

import com.swyp.mema.domain.user.dto.request.EmailCheckReq;
import com.swyp.mema.domain.user.exception.EmailAuthCodeFail;
import com.swyp.mema.domain.user.exception.EmailAuthSessionNotexist;
import com.swyp.mema.global.config.emailsender.NaverEmailConfig;
import com.swyp.mema.global.config.env.EnvConfig;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Properties;


@Service
public class EmailAuthService {

    @Autowired
    private final Properties props;
    private final NaverEmailConfig.SimpleAuthenticator authenticator;
//    private final String emailSender = "Mema@naver.com";

    public EmailAuthService(EnvConfig envConfig, JavaMailSender javaMailSender, NaverEmailConfig.SimpleAuthenticator authenticator) {
        this.authenticator = authenticator;
        props = System.getProperties();
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
            setSession(request, response,code);
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

        System.out.println("code check request headers:" + request.getHeaderNames().toString());
        System.out.println("code check request cookies:" + request.getCookies());
        HttpSession session = request.getSession();
        System.out.println("mailAuth:" + session.getAttribute("mailAuth") + ", session id :" + session.getId());

//        if(session == null || session.getAttribute("code") == null){
//
//            // 인증제한시간 초과 혹은 잘못된 요청
//            throw new EmailAuthSessionNotexist();
//        }
        String code = session.getAttribute("mailAuth").toString();
        if(code == null){

            // 인증제한시간 초과 혹은 잘못된 요청
            throw new EmailAuthSessionNotexist();
        }

        if (!Objects.equals(emailCheckReq.getCode(), code)) {

            // 인증 코드 틀림
            throw new EmailAuthCodeFail();
        }

        //인증 성공
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
     * @param request
     * @param code 인증코드
     */
    private void setSession(HttpServletRequest request, HttpServletResponse response, int code){

        HttpSession session = request.getSession();
        session.setAttribute("mailAuth",code);
        session.setMaxInactiveInterval(5 * 60 * 1000);
        Cookie cookie = new Cookie("JSESSIONID", session.getId());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(5 * 60 * 1000);
        response.addCookie(cookie);
        System.out.println("send mail response headers: " + response.getHeaderNames());
        System.out.println("send mail response headers: " + response.getHeaders("Set-Cookie"));
    }
}
