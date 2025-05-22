/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.services;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CaptchaService {
    private static final String Secret = "6LcbK0UrAAAAAP5UmuJgSh6nvcXcfhjA3INJ17Mf";
    private static final String Verify_Url = "https://www.google.com/recaptcha/api/siteverify";

    public boolean isCaptchaValid(String response) {
        String url = Verify_Url + "?secret=" + Secret + "&response=" + response;
        RestTemplate restTemplate = new RestTemplate();
        Map body = restTemplate.postForObject(url, null, Map.class);
        return body != null && Boolean.TRUE.equals(body.get("success"));
    }

}
