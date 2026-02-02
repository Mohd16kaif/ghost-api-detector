package com.ghostapidetector.Ghost.API.detector.rules;

import com.ghostapidetector.Ghost.API.detector.registry.ApiRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApiRuleEngine {

    private final ApiRegistry apiRegistry;

    private final List<ApiRule> rules;

    public ApiRuleEngine(ApiRegistry apiRegistry, List<ApiRule> rules){
        this.apiRegistry = apiRegistry;
        this.rules = rules;
    }

    @PostConstruct
    public void applyRules(){
        apiRegistry.getAllApis()
                .forEach(api ->
                        rules.forEach(rule -> rule.apply(api))
                );
    }


}
