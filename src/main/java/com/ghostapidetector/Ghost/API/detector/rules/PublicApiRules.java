package com.ghostapidetector.Ghost.API.detector.rules;

import com.ghostapidetector.Ghost.API.detector.model.APIinfo;
import org.springframework.stereotype.Component;

@Component
public class PublicApiRules implements ApiRule{

    @Override
    public void apply(APIinfo api) {
        if (api.getPath().startsWith("/api")){
            api.addTag("PUBLIC");
        }
    }
}
