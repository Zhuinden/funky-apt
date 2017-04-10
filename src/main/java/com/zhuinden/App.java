package com.zhuinden;

import java.io.File;
import java.util.Arrays;

/**
 * Hello world!
 */
@Funk
public class App {
    public static void main(String[] args) throws Exception {
        //System.out.println("Hello World!");
        createDirectoryIfDoesNotExist("target");
        createDirectoryIfDoesNotExist("target/generated-sources");
        com.sun.tools.javac.Main.main(Arrays.asList("-proc:only",
                "-processor", "com.zhuinden.MyProcessor",
                "c:/Development/HomeProjects/funky-apt/src/main/java/com/zhuinden/App.java", "-s", "target/generated-sources").toArray(new String[6]));
    }

    private static void createDirectoryIfDoesNotExist(String pathname) {
        File targetGenSrcDir = new File(pathname);
        if(!targetGenSrcDir.exists()) {
            targetGenSrcDir.mkdir();
        }
    }
}
