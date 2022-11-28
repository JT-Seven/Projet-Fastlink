package org.fastlink.mediastorageservice.helper;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FileExtensionValidator
{
    private static final Pattern fileExtnPattern = Pattern.compile("(\\S+(\\.(?i)(png|jpg|jpeg|mp4|gif))$)");

    public static boolean validateFileExtension(String fileName)
    {
        Matcher matcher = fileExtnPattern.matcher(fileName);

        return matcher.matches();
    }
}
