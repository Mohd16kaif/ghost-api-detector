package com.ghostapidetector.Ghost.API.detector.reporting;

import com.ghostapidetector.Ghost.API.detector.registry.ApiRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

@Component
public class ApiReporter {

    private final ApiRegistry apiRegistry;

    public ApiReporter(ApiRegistry apiRegistry){
        this.apiRegistry = apiRegistry;
    }

    @PostConstruct
    public void report(){
        System.out.println("\\n==== API REPORT ====");
        apiRegistry.getAllApis().forEach(api ->

                System.out.println(

                        api.getHttpMethod() + " " +
                                api.getPath() + " -> " +
                                api.getTags()
                )

                );
    }

}
