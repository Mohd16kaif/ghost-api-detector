package com.ghostapidetector.Ghost.API.detector.component;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
public class inspector {

    private final RequestMappingHandlerMapping handlerMapping;

    public inspector(RequestMappingHandlerMapping handlerMapping){
        this.handlerMapping = handlerMapping;
    }

    //using post cons becoz we want it to run after the beans are created and DI is done
    //this method shows  all REST apis registered in this Spring Boot app, and which Java method handles each one
    @PostConstruct
    public void inspectApis(){

        int totalapis = handlerMapping.getHandlerMethods().size();

        System.out.println("Total APIs registered : " + totalapis);

        handlerMapping.getHandlerMethods()
                //this returns a map, of request mapping info as key, and handler method as value
                .forEach((requestMappingInfo, handlerMethod) -> {
                    System.out.println(
                            //request mapping info is this "{GET [/users]}"
                            //and handler method gives this ""@GetMapping("/users")
                            //public List<User> getUsers() { ... }"
                            requestMappingInfo + " -> " + handlerMethod.getMethod().getName()
                    );
                        }
                        );

    }

    //what the inspect mathod actually prints : example
    // {GET [/users]} -> getUsers
    //{POST [/users]} -> createUser
    //{GET [/orders/{id}]} -> getOrderById
}
