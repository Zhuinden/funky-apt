package com.zhuinden;

import java.io.File;
import java.util.Arrays;

/**
 * Hello world!
 */
@Funk
public class App {
    String hello;
    String world;

    public static void main(String[] args) throws Exception {
        //System.out.println("Hello World!");
        String currentDir = new File("").getAbsolutePath();
        createDirectoryIfDoesNotExist("target");
        createDirectoryIfDoesNotExist("target/generated-sources");
        com.sun.tools.javac.Main.main(Arrays.asList("-proc:only",
                "-processor", "com.zhuinden.MyProcessor",
                currentDir + "/src/main/java/com/zhuinden/App.java", currentDir + "/src/main/java/com/zhuinden/OtherThing.java", "-s", "target/generated-sources").toArray(new String[7]));
    }

    private static void createDirectoryIfDoesNotExist(String pathname) {
        File targetGenSrcDir = new File(pathname);
        if(!targetGenSrcDir.exists()) {
            targetGenSrcDir.mkdir();
        }
    }
}
