package com.swyp.mema.database.station.util;

import com.swyp.mema.global.config.env.EnvConfig;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class AddrToCodeConverter {

    /**
     *
     * 주소를 위경도로 변환해주는 openAPI
     *
     */

    String apikey;
    String searchType = "road"; // 주소타입: 도로명 주소
    String epsg = "epsg:4326";

    public AddrToCodeConverter(EnvConfig envConfig) {
        apikey = envConfig.getAddr2Coor();
    }

    /**
     *      Public method
     *      도로명 주소 -> 위경도로 변환해주는 메서드
     *
     * @param addr 도로명 주소
     * @return  <위도, 경도>
     */
    public Pair<String, String> getCoord(String addr){

        StringBuilder sb = new StringBuilder("https://api.vworld.kr/req/address");
        sb.append("?logic=address");
        sb.append("&request=getCoord");
        sb.append("&format=json");
        sb.append("&crs=" + epsg);
        sb.append("&key=" + apikey);
        sb.append("&type=" + searchType);
        sb.append("&address=" + URLEncoder.encode(addr, StandardCharsets.UTF_8));

        String lat = "";
        String lot = "";

        try{
            URL url = new URL(sb.toString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));

            JSONParser jspa = new JSONParser();
            JSONObject jsob = (JSONObject) jspa.parse(reader);
            System.out.println(jsob);
            JSONObject jsrs = (JSONObject) jsob.get("response");
            JSONObject jsResult = (JSONObject) jsrs.get("result");
            if (jsResult == null) {
                log.info("Generated SubwayInfo API Request URL: {}", url + ", " + addr);
                return Pair.of("0.0", "0.0");
            }
            JSONObject jspoint = (JSONObject) jsResult.get("point");

            lat = jspoint.get("x").toString();
            lot = jspoint.get("y").toString();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return Pair.of(lat, lot);
    }
}
