package com.cretf.backend.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public static String loadContentFromResourceAsStream(String fileName, String extension, String path) {
        String content = null;
        InputStream inputStream = null;
        try {
            // The class loader that loaded the class
            ClassLoader classLoader = FileUtils.class.getClassLoader();
            inputStream = classLoader.getResourceAsStream(path+"/"+fileName+"."+extension); //"sqlScripts/"+fileName+".sql"
            byte[] buffer = inputStream.readAllBytes();
            content = new String(buffer, StandardCharsets.UTF_8);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
        return content;
    }
}
