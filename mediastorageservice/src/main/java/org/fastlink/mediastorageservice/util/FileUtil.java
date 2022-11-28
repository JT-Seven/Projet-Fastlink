package org.fastlink.mediastorageservice.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.nio.file.Path;
import java.nio.file.Paths;

@AllArgsConstructor
public class  FileUtil
{
    public final static String folderPath = "./media-storage/";
    public final static Path filePath = Paths.get(folderPath);

}
