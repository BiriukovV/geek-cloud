package com.example.nio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class PathExamples {


    public static void main(String[] args) throws IOException {

        Path path = Path.of("server-files","images","earth.png");
        Path absolutePath = path.toAbsolutePath();
        System.out.println(Files.exists(path));

        Path newDir = Path.of("server-files","texts");

        if(!Files.exists(newDir)){
            Files.createDirectories(newDir);
        }

        Path textFile = newDir.resolve("sample.txt");

//        Files.createFile(textFile);
//        Files.writeString(textFile,"Hello world");
        Files.writeString(textFile,"Hello world\n\r");

    }
}
