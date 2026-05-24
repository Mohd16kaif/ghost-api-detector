package com.ghostapidetector.Ghost.API.detector.runtime;

import com.ghostapidetector.Ghost.API.detector.model.ObservedApi;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class ApiUsageInterceptor implements HandlerInterceptor {

        public static final Set<ObservedApi> OBSERVED_APIS =
                Collections.synchronizedSet(new HashSet<>());

        @Override
        public boolean preHandle(
                HttpServletRequest request,
                HttpServletResponse response,
                Object handler) {

            String method = request.getMethod();
            String path = request.getRequestURI();
            String contextPath = request.getContextPath();

            if (contextPath != null && path.startsWith(contextPath)) {
                path = path.substring(contextPath.length());
            }

            if (path.isEmpty()) {
                path = "/";
            }

            if (path.startsWith("/internal")) {
                return true;
            }

            OBSERVED_APIS.add(new ObservedApi(method, path));

            return true;
        }
    }
