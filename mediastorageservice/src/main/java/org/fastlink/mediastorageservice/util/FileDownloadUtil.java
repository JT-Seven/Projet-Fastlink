package org.fastlink.mediastorageservice.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileDownloadUtil
{
    private Path foundFile;

    public byte[] getFileAsResource(String mediaCode, String userId) throws IOException
    {
        Path uploadDirectory = Paths.get(FileUtil.folderPath + userId + "/");

        Files.list(uploadDirectory).forEach(file -> {
            if (file.getFileName().toString().contains(mediaCode))
            {
                foundFile = file;
            }
        });

        if (foundFile == null)
        {
            throw new IOException("File not found ...");
        }

        return Files.readAllBytes(foundFile);
    }


    public Mono<Resource> getVideoAsResource(String mediaCode, String userId) throws IOException
    {
        Path uploadDirectory = Paths.get(FileUtil.folderPath + userId + "/");

        Files.list(uploadDirectory).forEach(file -> {
            if (file.getFileName().toString().contains(mediaCode))
            {
                foundFile = file;
            }
        });

        if (foundFile == null)
        {
            throw new IOException("File not found ...");
        }

        byte[] resource = Files.readAllBytes(foundFile);

        return Mono.fromSupplier(() -> new ByteArrayResource(resource));
    }
}
/*    public Path getFoundFile(String mediaCode, String userId) throws IOException
    {

    }*/

