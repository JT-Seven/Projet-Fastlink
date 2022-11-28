package org.fastlink.mediastorageservice.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.fastlink.mediastorageservice.helper.FileExtensionValidator;
import org.fastlink.mediastorageservice.helper.MediaHelper;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
public class FileUploadUtil
{
    public static String saveFile(String fileName, MultipartFile multipartFile, String userId) throws IOException, IOException
    {
        createDirIfNotExists(userId);
        //Path uploadPath = Paths.get(FileUtil.folderPath + userId + "/" );

        final String uploadPath = "./media-storage/" + userId + "/";

        String extension = MediaHelper.getFileExtension(fileName);

        if (! FileExtensionValidator.validateFileExtension(fileName)) {
            throw new IOException("Invalid file extension: " + fileName);
        }

        final String mediaCode = RandomStringUtils.randomAlphanumeric(8);

        try (FileOutputStream output = new FileOutputStream(uploadPath + mediaCode + "-" + fileName))
        {
            output.write(multipartFile.getBytes());
        } catch (IOException ioe)
        {
            log.error("Error while saving file: " + ioe.getMessage());
            throw new IOException("Could not save media file: " + fileName, ioe);
        }

        return mediaCode;
    }

    private static void createDirIfNotExists(String userId)
    {
        File directory = new File(FileUtil.folderPath + userId + "/");
        if (!directory.exists())
        {
            directory.mkdirs();
        }
    }
}
