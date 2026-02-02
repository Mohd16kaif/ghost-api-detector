package com.ghostapidetector.Ghost.API.detector.registry;


import com.ghostapidetector.Ghost.API.detector.model.APIinfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ApiRegistry {

    private final List<APIinfo> apis = new ArrayList<>();

    public void register(APIinfo apiInfo){
        apis.add(apiInfo);
    }

    public List<APIinfo> getAllApis(){
        return apis;
    }

}

