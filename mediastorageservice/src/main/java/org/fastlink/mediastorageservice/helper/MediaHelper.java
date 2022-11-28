package org.fastlink.mediastorageservice.helper;

import org.apache.commons.io.FilenameUtils;

public class MediaHelper
{
     public static String getFileExtension(String fileName)
    {
        return FilenameUtils.getExtension(fileName);
    }

    public static String determineMediaType(String ext)
    {

        switch (ext)
        {
            case "jpeg":
            case "jpg":
            case "png":
                return "photo";
            case "mp4":
                return "video";
            default:
                return null;
        }
    }
}
