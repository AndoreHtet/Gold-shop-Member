package com.example.securitycontextdemo.controller;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello(Principal principal){
//        SecurityContext context = SecurityContextHolder.getContext();
//        Authentication authentication = context.getAuthentication();
        return "Hello" + principal.getName();
    }
    @GetMapping("/bye")
    @Async
    public void bye(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        System.out.println("Bye Bye!" + authentication.getName());
    }
    @GetMapping("/hola")
    public String hello()throws Exception{
        Callable<String> task = () -> {
            SecurityContext context = SecurityContextHolder
                    .getContext();
            return context.getAuthentication().getName();
        };
        ExecutorService e = Executors.newCachedThreadPool();
        e = new DelegatingSecurityContextExecutorService(e);
        try{
            return "Hola, " + e.submit(task).get() + " !";
        }finally {
            e.shutdown();
        }
    }
    @GetMapping("/greet")
    public String greet() throws Exception{
        Callable<String> task = ()->{
          SecurityContext context = SecurityContextHolder.getContext();
          return context.getAuthentication().getName();
        };
        ExecutorService e = Executors.newCachedThreadPool();
        try {
            return "Hi ," + e.submit(task).get() + " !";
        }finally {
            e.shutdown();
        }
    }
}
