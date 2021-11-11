package com.performanceengineering.resilientresttemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "/delegate")
@CrossOrigin(origins = "http://localhost:8900")
public class DelegatingResource {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(path = "/demo", method = RequestMethod.GET)
    public String getDemoDelegate() {// "http://localhost:8080/demo"
                                     // //http://vmiksa699b1tjr:8090/internal/customer-management/customers/v1/account
                                     // //http://localhost:8901/performanceengineering/serviceList
        return this.restTemplate.getForObject("http://vmiksa699b1tjr:8090/core/v1/customers/accounts/transactions",
                String.class);
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}