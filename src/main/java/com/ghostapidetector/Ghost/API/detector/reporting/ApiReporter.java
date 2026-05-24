package com.ghostapidetector.Ghost.API.detector.reporting;

import com.ghostapidetector.Ghost.API.detector.registry.ApiRegistry;
import org.springframework.stereotype.Component;

@Component
public class ApiReporter {

    private final ApiRegistry apiRegistry;

    public ApiReporter(ApiRegistry apiRegistry){
        this.apiRegistry = apiRegistry;
    }

    public void report(){
        System.out.println("\n==== API REPORT ====");
        apiRegistry.getAllApis().forEach(api ->

                System.out.println(

                        api.getHttpMethod() + " " +
                                api.getPath() + " -> " +
                                api.getTags()
                )

                );
    }

}
