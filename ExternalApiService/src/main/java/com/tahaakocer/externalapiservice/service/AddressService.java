package com.tahaakocer.externalapiservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class AddressService {
    private final BbkService bbkService;
    private final RestTemplate restTemplate;

    public AddressService(BbkService bbkService, RestTemplate restTemplate) {
        this.bbkService = bbkService;
        this.restTemplate = restTemplate;
    }
    //https://www.netspeed.com.tr/Home/GetAddress
}
