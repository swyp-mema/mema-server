package com.swyp.mema.domain.midloc.service;

import com.swyp.mema.global.config.env.EnvConfig;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class OpenAIWorker {

    // OpenAI API 정보 (고정)
    final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions"; // 채팅 엔드포인트
    final String MODEL_NAME = "gpt-4-turbo"; //gpt-4o 모델
    final double TEMPERATURE = 1.0;   //1.0에 가까울수록 결과가 정확하고 정형적
    final int MAX_TOKEN = 500;
    private final EnvConfig envConfig;

    /**
     * 지피티한테 물어보기
     * @param prompt 프롬프트 적어서 주면
     * @return  지피티의 답변을 리턴해줌
     */
    public String callOpenAI(String prompt) {
        HttpURLConnection connection = null;
        try {
            // URL 및 연결 설정
            URL url = new URL(OPENAI_API_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + envConfig.getOpenAiApiKey());
            connection.setDoOutput(true);

            // JSON 요청 데이터 생성
            String requestData = new JSONObject()
                    .put("model", MODEL_NAME)
                    .put("messages", new JSONArray()
                            .put(new JSONObject().put("role", "system").put("content", "You are a helpful assistant."))
                            .put(new JSONObject().put("role", "user").put("content", prompt))
                    )
                    .put("temperature", TEMPERATURE)
                    .put("max_tokens", MAX_TOKEN)
                    .toString();

            // 데이터 전송
            try (OutputStream os = connection.getOutputStream()) {
                os.write(requestData.getBytes(StandardCharsets.UTF_8));
            }

            // 응답 처리
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line.trim());
                    }

                    // JSON 응답 파싱
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    return jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content").trim();
                }
            } else {
                return "API 호출 실패, 응답 코드: " + responseCode;
            }
        } catch (Exception e) {
            return "[에러] " + e.getMessage();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
