package com.ghostapidetector.Ghost.API.detector.discovery;


import com.ghostapidetector.Ghost.API.detector.model.APIinfo;
import com.ghostapidetector.Ghost.API.detector.registry.ApiRegistry;
import org.apache.coyote.Request;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
public class ApiDiscovery {

    private final RequestMappingHandlerMapping handlerMapping;
    private final ApiRegistry apiRegistry;

    public ApiDiscovery(RequestMappingHandlerMapping handlerMapping, ApiRegistry apiRegistry){
        this.handlerMapping = handlerMapping;
        this.apiRegistry = apiRegistry;
    }

    public void discoverApis(){

        handlerMapping.getHandlerMethods()
                .forEach((RequestMappingInfo mappingInfo, HandlerMethod handlerMethod) -> {

                    String path = mappingInfo.getPathPatternsCondition()
                            .getPatterns()
                            .iterator()
                            .next()
                            .toString();

                    String httpMethod = mappingInfo.getMethodsCondition()
                            .getMethods()
                            .stream()
                            .findFirst()
                            .map(Enum::name)
                            .orElse("ALL");

                    String controllerName =
                            handlerMethod.getBeanType().getSimpleName();

                    String controllerPackage =
                            handlerMethod.getBeanType().getPackageName();

                    String methodName =
                            handlerMethod.getMethod().getName();

                    APIinfo apiInfo = new APIinfo(
                            path,
                            httpMethod,
                            controllerName,
                            controllerPackage,
                            methodName
                    );

                    apiRegistry.register(apiInfo);

                });

    }
}
