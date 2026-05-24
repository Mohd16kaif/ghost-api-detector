package com.ghostapidetector.Ghost.API.detector.controller;

import com.ghostapidetector.Ghost.API.detector.model.APIinfo;
import com.ghostapidetector.Ghost.API.detector.model.ObservedApi;
import com.ghostapidetector.Ghost.API.detector.registry.ApiRegistry;
import com.ghostapidetector.Ghost.API.detector.runtime.ApiUsageInterceptor;
import com.ghostapidetector.Ghost.API.detector.service.GhostApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/internal")
public class GhostApiController {

    private final GhostApiService ghostApiService;
    private final ApiRegistry apiRegistry;

    public GhostApiController(GhostApiService ghostApiService, ApiRegistry apiRegistry) {
        this.ghostApiService = ghostApiService;
        this.apiRegistry = apiRegistry;
    }

    @GetMapping("/ghost-apis")
    public GhostApiResponse getGhostApis() {
        List<APIinfo> ghostApis = ghostApiService.findGhostApis();
        int observedCount = ApiUsageInterceptor.OBSERVED_APIS.size();
        int discoveredCount = apiRegistry.getAllApis().size();
        String warning = null;
        if (observedCount == 0) {
            warning = "No traffic observed yet — all endpoints will appear as ghosts until requests are received";
        }
        return new GhostApiResponse(ghostApis, observedCount, discoveredCount, warning);
    }

    @GetMapping("/observed-apis")
    public Set<ObservedApi> getObservedApis() {
        return ApiUsageInterceptor.OBSERVED_APIS;
    }

    public static class GhostApiResponse {
        private final List<APIinfo> ghostApis;
        private final int observedCount;
        private final int discoveredCount;
        private final String warning;

        public GhostApiResponse(List<APIinfo> ghostApis, int observedCount, int discoveredCount, String warning) {
            this.ghostApis = ghostApis;
            this.observedCount = observedCount;
            this.discoveredCount = discoveredCount;
            this.warning = warning;
        }

        public List<APIinfo> getGhostApis() { return ghostApis; }
        public int getObservedCount() { return observedCount; }
        public int getDiscoveredCount() { return discoveredCount; }
        public String getWarning() { return warning; }
    }
}
