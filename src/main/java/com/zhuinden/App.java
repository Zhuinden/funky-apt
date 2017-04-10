package com.zhuinden;

import java.util.Arrays;

/**
 * Hello world!
 */
@Funk
public class App {
    public static void main(String[] args) throws Exception {
        //System.out.println("Hello World!");
        com.sun.tools.javac.Main.main(Arrays.asList("-proc:only",
                "-processor", "com.zhuinden.MyProcessor",
                "c:/Development/HomeProjects/funky-apt/src/main/java/com/zhuinden/App.java", "-s", "target/generated-sources").toArray(new String[6]));
    }
}
