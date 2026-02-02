package com.ghostapidetector.Ghost.API.detector.model;

import java.util.HashSet;
import java.util.Set;

public class APIinfo {

    private String path;
    private String httpMethod;

    private String controllerName;
    private String controllerPackage;
    private String handlerMethodName;

    private Set<String> tags = new HashSet<>();

    //cons
    public APIinfo(String path, String httpMethod, String controllerName, String controllerPackage, String handlerMethodName){

        this.path = path;
        this.httpMethod = httpMethod;
        this.controllerName = controllerName;
        this.controllerPackage = controllerPackage;
        this.handlerMethodName = handlerMethodName;
    }

    public String getPath() {
        return path;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getControllerName() {
        return controllerName;
    }

    public String getControllerPackage() {
        return controllerPackage;
    }

    public String getHandlerMethodName() {
        return handlerMethodName;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void addTag(String tag) {
        this.tags.add(tag);
    }
}
