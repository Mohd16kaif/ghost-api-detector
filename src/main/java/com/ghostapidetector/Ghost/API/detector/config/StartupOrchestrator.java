package com.ghostapidetector.Ghost.API.detector.config;

import com.ghostapidetector.Ghost.API.detector.discovery.ApiDiscovery;
import com.ghostapidetector.Ghost.API.detector.reporting.ApiReporter;
import com.ghostapidetector.Ghost.API.detector.rules.ApiRuleEngine;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupOrchestrator {

    private final ApiDiscovery apiDiscovery;
    private final ApiRuleEngine apiRuleEngine;
    private final ApiReporter apiReporter;

    public StartupOrchestrator(ApiDiscovery apiDiscovery, ApiRuleEngine apiRuleEngine, ApiReporter apiReporter) {
        this.apiDiscovery = apiDiscovery;
        this.apiRuleEngine = apiRuleEngine;
        this.apiReporter = apiReporter;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        apiDiscovery.discoverApis();
        apiRuleEngine.applyRules();
        apiReporter.report();
    }
}
