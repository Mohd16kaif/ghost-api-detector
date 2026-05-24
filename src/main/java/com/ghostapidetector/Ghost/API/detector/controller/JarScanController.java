package com.ghostapidetector.Ghost.API.detector.controller;
import com.ghostapidetector.Ghost.API.detector.jarscanner.JarClassReader;
import com.ghostapidetector.Ghost.API.detector.model.DiscoveredApi;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.List;


@RestController
@RequestMapping("/internal/jar")
public class JarScanController {

    private final JarClassReader jarClassReader = new JarClassReader();

    @PostMapping("/classes")
    public List<DiscoveredApi> scanJarClasses(
            @RequestParam("file") MultipartFile file
    ) throws Exception {

        File tempJar = File.createTempFile("upload-", ".jar");
        file.transferTo(tempJar);

        List<DiscoveredApi> apis =
                jarClassReader.discoverSpringApis(tempJar);

        tempJar.delete();
        return apis;
    }
}
