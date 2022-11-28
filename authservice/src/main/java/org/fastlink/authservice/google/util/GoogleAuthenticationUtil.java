package org.fastlink.authservice.google.util;

import com.google.gson.Gson;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.fastlink.authservice.config.GoogleClientConfig;
import org.fastlink.authservice.dto.UserDto;
import org.fastlink.authservice.helper.UserServiceRequestHelper;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import xin.altitude.cms.common.util.SpringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component @Getter @Setter
public class GoogleAuthenticationUtil
{
//4/0AX4XfWi4oNTuJuzlCCf0xGTKgrRopY7xZVrfJGQIir0ZeQaIr5N_E0tqJDWthu1kLT_uKQ
    private final GoogleClientConfig config = SpringUtils.getBean(GoogleClientConfig.class);

    /* Google Auth */
    private String clientId = config.getId();

    private String clientSecret = config.getSecret();

    private final Gson gson;
    private final OkHttpClient.Builder builder;
    private final OkHttpClient httpClient;

    private final RestTemplate restTemplate;

    private final RestTemplate anotherRestTemplate = new RestTemplate();

    public GoogleAuthenticationUtil(Gson gson, OkHttpClient.Builder builder, RestTemplate restTemplate)
    {
        this.gson = gson;
        this.builder = builder;
        this.httpClient = builder.build();
        this.restTemplate = restTemplate;
    }

    private String accessToken;
    private String refreshToken;
    private String idToken;

    public UserDto getGoogleApiToken(String code, HttpServletResponse servletResponse) throws IOException
    {
        log.info("Enter getGoogleApiToken - GoogleAuthenticationUtil");

        HttpUrl.Builder urlBuilder = Objects.requireNonNull(
                HttpUrl.parse("https://oauth2.googleapis.com/token")
        ).newBuilder();

        RequestBody requestBody = new FormBody.Builder()
                .addEncoded("client_id", clientId)
                .addEncoded("client_secret", clientSecret)
                .addEncoded("code", code)
                .addEncoded("grant_type", "authorization_code")
                .addEncoded("redirect_uri", "http://localhost:3000")
                .addEncoded("access_type", "online")
                .build();

        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .post(requestBody)
                .build();


        try (Response response = httpClient.newCall(request).execute())
        {

            if ( ! response.isSuccessful() )
            {
                throw new IOException(String.format("Unexpected code: %s", response));
            }

            if (response.body() == null)
            {
                log.error("Response body missing: {}", (Object) null);
            }
            assert response.body() != null;

            Optional<String> responseBody = Optional.of(Objects.requireNonNull(response.body()).string());
            String body = responseBody.get();

            JSONObject jsonObject = new JSONObject(body);
            this.accessToken = jsonObject.getString("access_token");
            this.refreshToken = jsonObject.getString("refresh_token");
            this.idToken = jsonObject.getString("id_token");

            log.info("accessToken: {}", this.accessToken);
            log.info("refreshToken: {}", this.refreshToken);
            log.info("idToken: {}", this.idToken);
            log.info("Response body from googleAPI: {}", body);

/*
            servletResponse.sendRedirect("http://localhost:8082/api/v1/auth/google/userinfo?token=" + this.accessToken);
            response.close();
*/
            String userDetails = getUserDetails();

            return sendRegistrationRequest(userDetails);
        }
    }

    private String getUserDetails() throws IOException
    {
        log.info("Enter getUserDetails - GoogleAuthenticationUtil");

/*        JSONObject jsonObject = gson.fromJson(responseBody, JSONObject.class);
        this.accessToken = jsonObject.getString("access_token");
        this.refreshToken = jsonObject.getString("refresh_token");
        this.idToken = jsonObject.getString("id_token");*/

        HttpUrl.Builder urlBuilder = Objects.requireNonNull(
                HttpUrl.parse("https://www.googleapis.com/oauth2/v3/userinfo")
        ).newBuilder();

        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .addHeader(AUTHORIZATION, "Bearer " + this.accessToken)
                .build();

        try (Response response = httpClient.newCall(request).execute())
        {
            if ( ! response.isSuccessful() )
            {
                throw new IOException(String.format("Unexpected code: %s", response));
            }

            if (response.body() == null)
            {
                log.error("Response body missing: {}", (Object) null);
            }
            assert response.body() != null;

            Optional<String> thisResponseBody = Optional.of(Objects.requireNonNull(response.body()).string());

            //<String> optionalUserDetails = Optional.of(response.body().string());

            return thisResponseBody.get();
        }

    }
    public Boolean validateGoogleToken(String idToken)
    {
        log.info("Enter validateGoogleToken - GoogleAuthenticationUtil");

        HttpUrl.Builder urlBuilder = Objects.requireNonNull(
                HttpUrl.parse("https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken)
        ).newBuilder();

        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .addHeader(AUTHORIZATION, "Bearer " + this.accessToken)
                .build();

        try (Response response = httpClient.newCall(request).execute())
        {
            assertResponse(response);

            log.info("Google token validation respone body:\n{}", response.body().string());

            return true;
        } catch (IOException e)
        {
            log.error("Unable to get response - Invalid token");
            log.error("Exception message: {}", e.getMessage());
        }
        return false;
    }


    private UserDto sendRegistrationRequest(String userDetails) throws IOException
    {
        JSONObject jsonObject = new JSONObject(userDetails);

/*        RequestBody requestBody = new FormBody.Builder()
                .add("firstName", jsonObject.getString("given_name"))
                .add("lastName", jsonObject.getString("family_name"))
                .add("email", jsonObject.getString("email"))
                .add("username", jsonObject.getString("name"))
                .add("profilePicture", jsonObject.getString("picture"))
                .build();

        HttpUrl.Builder urlBuilder = Objects.requireNonNull(
                HttpUrl.parse(UserServiceRequestHelper.GOOGLE_REGISTRATION_URL)
        ).newBuilder();

        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .addHeader("Content-Type", "application/json")
                .post(requestBody)
                .build();

        try (Response response = httpClient.newCall(request).execute())
        {
            assertResponse(response);

            log.info("User registered successfully ...");
            log.info("User info:\n{}", response.body().string());
        }*/
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject requestOptions = new JSONObject();
        requestOptions.put("firstName", jsonObject.getString("given_name"));
        requestOptions.put("lastName", jsonObject.getString("family_name"));
        requestOptions.put("email", jsonObject.getString("email"));
        requestOptions.put("username", jsonObject.getString("name"));
        requestOptions.put("profilePicture", jsonObject.getString("picture"));

        HttpEntity<String> requestEntity = new HttpEntity<>(requestOptions.toString(), headers);

        return restTemplate.postForObject(UserServiceRequestHelper.GOOGLE_REGISTRATION_URL, requestEntity, UserDto.class);
    }

    private void assertResponse(Response response) throws IOException
    {
        if ( ! response.isSuccessful() )
        {
            throw new IOException(String.format("Unexpected code: %s", response));
        }

        if (response.body() == null)
        {
            log.error("Response body missing: {}", (Object) null);
        }
        assert response.body() != null;
    }

}
