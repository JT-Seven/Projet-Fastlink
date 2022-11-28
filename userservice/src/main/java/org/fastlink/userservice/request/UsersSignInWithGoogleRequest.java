/*
package org.fastlink.userservice.request;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.fastlink.userservice.service.GoogleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@Getter
public class UsersSignInWithGoogleRequest {
    private String accessToken;
    private String refreshToken;
    private String idToken;

    @Autowired
    private GoogleService googleService;

    public void getTokenFromGoogleApi(String code) throws IOException {
        log.info("Getting token from google api");
        //Request to get the access token
        OkHttpClient httpClient = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://oauth2.googleapis.com/token").newBuilder();
        RequestBody formBody = new FormBody.Builder()
                .addEncoded("client_id", "757933430677-kcirsusfm3mdevesl1b57f582sb5cn4j.apps.googleusercontent.com")
                .addEncoded("client_secret", "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")
                .addEncoded("code", code)
                .addEncoded("grant_type", "authorization_code")
                .addEncoded("redirect_uri", "http://localhost:3000")
                .addEncoded("access_type", "online")
                .build();
        System.out.println();
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        //Response from the request
        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code: " + response);
            // Get response headers
            //log.info("Response Headers: {}", response.headers());
            if (response.body() != null) {
                String tokenResponse = response.body().string();
                // Get response body
                log.info("Response Body from google api: {}", tokenResponse);
                getUserInformation(tokenResponse);
            }else{
                log.error("Response Body from google api: {}", "No response body");
            }
        }
    }

    public void getUserInformation(String tokenResponse) throws IOException {
        log.info("Getting information from google api");
        //parse json response
        org.json.JSONObject jsonObject = new org.json.JSONObject(tokenResponse);
        this.accessToken = jsonObject.getString("access_token");
        this.refreshToken = jsonObject.getString("refresh_token");
        this.idToken = jsonObject.getString("id_token");


        // Get information user
        OkHttpClient httpClient = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://www.googleapis.com/oauth2/v1/userinfo").newBuilder();
        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            ResponseBody body = response.body();

            assert body != null;
            Optional<String> potentialUserDetail = Optional.of(body.string());
            String userDetail = potentialUserDetail
                    .orElseThrow(() -> new IOException("Unexpected code " + response));

            log.info("User information receive by google api : {}", userDetail);
           // googleService.registerUserFromGoogle(userDetail);
        }

    }

    public Boolean FirstVerifyTokenOfGoogle(String idToken) {
        log.info("Verify token of google api");
        OkHttpClient httpClient = new OkHttpClient();
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse("https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken)).newBuilder();
        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {


            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
*/
