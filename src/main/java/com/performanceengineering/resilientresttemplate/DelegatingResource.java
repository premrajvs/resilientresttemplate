package com.performanceengineering.resilientresttemplate;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping(value = "/delegate")
@CrossOrigin(origins = "http://localhost:8900")
public class DelegatingResource {

    @Autowired
    private RestTemplate restTemplate;
    private int attempt = 1;

    @RequestMapping(path = "/demo", method = RequestMethod.GET)
    @CircuitBreaker(name = "AccountDetailsCircuitBreaker", fallbackMethod = "getAccountTransactionsFromCache")
    public String getDemoDelegate() {// "http://localhost:8080/demo"
                                     // //http://vmiksa699b1tjr:8090/internal/customer-management/customers/v1/account
                                     // //http://localhost:8901/performanceengineering/serviceList
        System.out.println("retry method called " + attempt++ + " times " + " at " + new Date());
        return this.restTemplate.getForObject("http://localhost:8093/accounttransactions/obj/1",
                String.class);
    }

    public String getAccountTransactionsFromCache(Exception e) {
        String s = "{\"listofaccounttransactions\":[{\"accId\":\"1\",\"transactionId\":\"tr-001-000-111\",\"transactionamout\":52,\"transactiondate\":\"11-13-2021\"},{\"accId\":\"1\",\"transactionId\":\"tr-002-000-112\",\"transactionamout\":27,\"transactiondate\":\"11-13-2021\"},{\"accId\":\"1\",\"transactionId\":\"tr-003-000-113\",\"transactionamout\":12,\"transactiondate\":\"11-13-2021\"},{\"accId\":\"2\",\"transactionId\":\"tr-001-000-111\",\"transactionamout\":52,\"transactiondate\":\"02-02-2021\"},{\"accId\":\"2\",\"transactionId\":\"tr-002-000-112\",\"transactionamout\":27,\"transactiondate\":\"02-02-2021\"},{\"accId\":\"2\",\"transactionId\":\"tr-003-000-113\",\"transactionamout\":12,\"transactiondate\":\"02-02-2021\"}]}";
        return s;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}