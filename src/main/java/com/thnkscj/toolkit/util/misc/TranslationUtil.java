package com.thnkscj.toolkit.util.misc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TranslationUtil {

    public static String translate(String source, String target, String text) throws Exception {

        String urlStr = "https://script.google.com/macros/s/AKfycbxVDgMnrlrWEyxZP0SkMzGGDjGeEqfD7bnPPjfhblUxtRsjs_cS/exec" + "?q=" + URLEncoder.encode(text, "UTF-8") + "&target=" + target + "&source=" + source;
        URL url = new URL(urlStr);
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    public static String detectTranslate(String target, String text) throws Exception {

        String urlStr = "https://script.google.com/macros/s/AKfycbxVDgMnrlrWEyxZP0SkMzGGDjGeEqfD7bnPPjfhblUxtRsjs_cS/exec" + "?q=" + text + "&target=" + target + "&source=";
        URL url = new URL(urlStr);
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
