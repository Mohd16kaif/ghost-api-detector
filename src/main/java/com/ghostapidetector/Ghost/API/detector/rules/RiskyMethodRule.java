package com.ghostapidetector.Ghost.API.detector.rules;

import com.ghostapidetector.Ghost.API.detector.model.APIinfo;
import org.springframework.stereotype.Component;

@Component
public class RiskyMethodRule implements ApiRule{

    @Override
    public void apply(APIinfo api) {
        if (api.getHttpMethod().equals("POST") || api.getHttpMethod().equals("DELETE") ) {
            api.addTag("RISKY");
        }
    }
}
