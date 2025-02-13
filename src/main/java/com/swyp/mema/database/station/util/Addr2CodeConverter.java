package com.swyp.mema.database.station.util;

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
public class Addr2CodeConverter {

    String apikey = "CAB0D3D2-F705-30C0-BE31-5B9B5CDEADB7";
    String searchType = "road";
    String epsg = "epsg:4326";

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
//            System.out.println(jspa.parse(reader));
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
//            System.out.println("X 좌표: " + jspoint.get("x"));
//            System.out.println("Y 좌표: " + jspoint.get("y"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return Pair.of(lat, lot);
    }
}
