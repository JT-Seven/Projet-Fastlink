package org.fastlink.mediastorageservice.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastlink.mediastorageservice.model.Media;
import org.fastlink.mediastorageservice.service.MediaService;
import org.fastlink.mediastorageservice.util.FileDownloadUtil;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/v1/file")
@Slf4j @AllArgsConstructor
public class FileDownloadController
{

    private final FileDownloadUtil fileDownloadUtil;

    private final MediaService mediaService;

    @GetMapping("/download/photo/{mediaCode}")
    public ResponseEntity<?> downloadFile(@PathVariable("mediaCode") String mediaCode, @RequestParam("userId") String userId) throws IOException
    {

        log.info("Downloading photo with mediaCode: {}", mediaCode);

        byte[] resource;
        try
        {
            resource = fileDownloadUtil.getFileAsResource(mediaCode, userId);
        } catch (IOException e)
        {
            return ResponseEntity.internalServerError().build();
        }

        if (resource == null)
        {
            return new ResponseEntity<>("File not found ...", NOT_FOUND);
        }

        Media media = mediaService.findByMediaCode(mediaCode);

        Resource fileResource = new ByteArrayResource(resource);
        //String contentType = "application/octet-stream";


        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(media.getMimeType()))
                //.header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .contentLength(resource.length)
                .body(fileResource);
    }


    @GetMapping(value = "/download/video/{mediaCode}", produces = "video/mp4")
    public Mono<Resource> downloadVideoFile(@PathVariable("mediaCode") String mediaCode, @RequestParam("userId") String userId) throws IOException
    {
        log.info("Downloading video with mediaCode: {}", mediaCode);

        return fileDownloadUtil.getVideoAsResource(mediaCode, userId);
    }

}
