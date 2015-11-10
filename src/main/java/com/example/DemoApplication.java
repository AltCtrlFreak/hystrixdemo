package com.example;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@EnableCircuitBreaker
@SpringBootApplication
@EnableHystrixDashboard
@Component
@Controller
@ComponentScan
public class DemoApplication {

    @RequestMapping("/")
    public String home() {
        return "forward:/hystrix";
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

@Component
@RestController
class Client {

    private RestTemplate restTemplate = new RestTemplate();


    @HystrixCommand(fallbackMethod = "getCachedWeather",commandProperties = {@HystrixProperty(name="execution.isolation.strategy", value="THREAD")})
    @RequestMapping(method = RequestMethod.GET, value = "/weather")
    public @ResponseBody String getWeather() {
        return restTemplate.getForObject("http://localhost:8000/weather", String.class);
    }

    public String getCachedWeather() {
        return "Sunny weather";
    }

}
