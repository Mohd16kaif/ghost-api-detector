package com.ghostapidetector.Ghost.API.detector.jarscanner;

import com.ghostapidetector.Ghost.API.detector.discovery.SpringMvcApiDiscovery;
import com.ghostapidetector.Ghost.API.detector.model.DiscoveredApi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarClassReader {

    private static final String BOOT_INF_CLASSES = "BOOT-INF/classes/";

    public List<String> readClassName(File jarFile) throws IOException{

        List<String> classNames = new ArrayList<>();

        try(JarFile jar = new JarFile(jarFile)){

            Enumeration<JarEntry> entries = jar.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();

                if (name.endsWith(".class")) {

                    String className = name
                            .replace(".class", "")
                            .replace("/", ".");

                    classNames.add(className);
                }


                if (name.startsWith(BOOT_INF_CLASSES) && name.endsWith(".class")) {
                    String className = name
                            .replace(BOOT_INF_CLASSES, "")
                            .replace(".class", "")
                            .replace("/", ".");

                    classNames.add(className);
                }
            }

        }
        return classNames;
    }

    public List<DiscoveredApi> discoverSpringApis(File jarFile) throws Exception {

        System.out.println(">>> ENTERED discoverSpringApis()");

        List<DiscoveredApi> apis = new ArrayList<>();

        try (JarFile jar = new JarFile(jarFile)) {
            Enumeration<JarEntry> entries = jar.entries();

            SpringMvcApiDiscovery discovery = new SpringMvcApiDiscovery();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();

                if (entry.getName().endsWith(".class")) {

                    System.out.println(">>> scanning class with ASM : " + entry.getName());

                    try (InputStream is = jar.getInputStream(entry)) {
                        apis.addAll(discovery.discover(is));
                    }
                }

            }
        }
        return apis;
    }

}
