package com.ghostapidetector.Ghost.API.detector.service;

import com.ghostapidetector.Ghost.API.detector.model.APIinfo;
import com.ghostapidetector.Ghost.API.detector.model.ObservedApi;
import com.ghostapidetector.Ghost.API.detector.registry.ApiRegistry;
import com.ghostapidetector.Ghost.API.detector.runtime.ApiUsageInterceptor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GhostApiService {

    private final ApiRegistry apiRegistry;

    public GhostApiService(ApiRegistry apiRegistry) {
        this.apiRegistry = apiRegistry;
    }

    public List<APIinfo> findGhostApis() {
        Set<ObservedApi> observed = ApiUsageInterceptor.OBSERVED_APIS;

        return apiRegistry.getAllApis().stream()
                .filter(api -> !observed.contains(new ObservedApi(api.getHttpMethod(), api.getPath())))
                .collect(Collectors.toList());
    }
}
