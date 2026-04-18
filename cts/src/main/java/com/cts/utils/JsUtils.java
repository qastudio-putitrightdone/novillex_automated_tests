package com.cts.utils;

import com.microsoft.playwright.Page;
import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.qameta.allure.internal.shadowed.jackson.databind.JsonNode;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;

public class JsUtils {

    private static String token;
    private static boolean signedIn;

    public static String fetchLoginAccessKey(Page playwrightPage) {
        JsonNode jsonNode = null;
        while (!signedIn) {
            String seesionVal = playwrightPage.evaluate("() => sessionStorage.getItem('admin')").toString();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                jsonNode = objectMapper.readTree(seesionVal);
            } catch (JsonProcessingException e) {
                System.out.println("Error while parsing token");
            }
            try {
                JsonNode authNode = objectMapper.readTree(jsonNode.get("auth").asText());
                token = authNode.get("session").get("token").asText();
                signedIn = authNode.get("session").get("signedIn").asBoolean();
                System.out.println(signedIn);
            } catch (JsonProcessingException e) {
                System.out.println("Error while parsing auth token");
            }
        }

        return token;
    }
}