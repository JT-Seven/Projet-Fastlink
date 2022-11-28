package org.fastlink.mediastorageservice.controller;


import lombok.AllArgsConstructor;
import org.fastlink.mediastorageservice.helper.MediaHelper;
import org.fastlink.mediastorageservice.res.UploadMediaResponse;
import org.fastlink.mediastorageservice.util.FileUploadUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/file")
@AllArgsConstructor
public class FileUploadController
{
    private final HttpServletRequest request;

    @PostMapping(value = "/upload", consumes = {"*/*"})
    public ResponseEntity<UploadMediaResponse> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("userId") String userID) throws IOException
    {
        String id = userID;

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        long fileSize = file.getSize();

        String mediaCode = FileUploadUtil.saveFile(fileName, file, id);

        String mediaType = MediaHelper.determineMediaType(MediaHelper.getFileExtension(fileName));

        UploadMediaResponse response = new UploadMediaResponse();
        response.setFileName(fileName);
        response.setSize(fileSize);
        response.setFileType(file.getContentType());
        response.setMediaCode(mediaCode);
        response.setMediaType(mediaType);
        response.setFileDownloadUri("http://localhost:9090/api/v1/file/download/" + mediaType + "/"  + mediaCode);

        return new ResponseEntity<>(response, OK);
    }

}
