package com.swyp.mema.global.config.env;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {
    private final Dotenv dotenv;

    public EnvConfig() {
        // .env 파일의 사용자 지정 경로
        dotenv = Dotenv.configure()
                .directory("./") // 경로 지정
                .filename(".env")        // 파일 이름 지정
                .load();
    }

    public String getDatabaseHost() {
        return dotenv.get("DB_HOST");
    }

    public String getCilentIp() {
        return dotenv.get("CLIENT_IP");
    }
}
