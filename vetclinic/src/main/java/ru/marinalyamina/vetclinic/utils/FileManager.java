package ru.marinalyamina.vetclinic.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {
    public static String rootDir = "C:\\TEMP";

    public static String getFilePath(String path) throws IOException {
        Path filePath = Paths.get(rootDir, path);
        if (Files.exists(filePath)) {
            return filePath.toAbsolutePath().toString(); // абсолютный путь
        } else {
            throw new IOException("Файл не найден: " + filePath.toString());
        }
    }
}
