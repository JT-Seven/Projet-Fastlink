package org.fastlink.mediastorageservice.config;


import org.fastlink.mediastorageservice.util.FileDownloadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Optional;

@Configuration
public class EndPointConfig
{
    @Autowired
    private FileDownloadUtil fileDownloadUtil;

    @Bean
    public RouterFunction<ServerResponse> router() throws IOException
    {
        RouterFunctions.Builder route = RouterFunctions.route();
        route.GET("/download/video/{mediaCode}", request -> {
            try
            {
                return videoHandler(request);
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        });
        return route
                .build();
    }

    private Mono<ServerResponse> videoHandler(ServerRequest request) throws IOException
    {
        String mediaCode = request.pathVariable("mediaCode");
        Optional<String> userId = request.queryParam("userId");
        if (userId.isEmpty()) {
            return ServerResponse.badRequest().build();
        }

        String userIdStr = userId.get();

        try
        {
            return org.springframework.web.reactive.function.server.ServerResponse.ok()
                    .contentType(MediaType.valueOf("video/mp4"))
                    .body(this.fileDownloadUtil.getVideoAsResource(mediaCode, userIdStr), Resource.class);
        } catch (IOException e)
        {
            throw new IOException("File not found ...");
        }
    }


}
