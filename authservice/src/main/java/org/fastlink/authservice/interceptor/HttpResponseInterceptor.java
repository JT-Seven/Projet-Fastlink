package org.fastlink.authservice.interceptor;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

@Slf4j
public class HttpResponseInterceptor implements Interceptor
{

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException
    {
        log.info("Enter intercept - HttpResponseInterceptor");

        Request request = chain.request();

        Response response = chain.proceed(request);

        return response.newBuilder()
                .body(ResponseBody.create(Objects.requireNonNull(response.body()).string(), Objects.requireNonNull(response.body()).contentType()))
                .build();

    }
}
