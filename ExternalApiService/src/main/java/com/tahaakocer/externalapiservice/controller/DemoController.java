package com.tahaakocer.externalapiservice.controller;

import com.tahaakocer.externalapiservice.dto.bbk.FullAddressResponse;
import com.tahaakocer.externalapiservice.dto.infrastructure.TTInfrastructureDetailDto;
import com.tahaakocer.externalapiservice.service.BbkService;
import com.tahaakocer.externalapiservice.service.InfrastructureService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {
    private final BbkService bbkService;
    private final InfrastructureService infrastructureService;

    public DemoController(BbkService bbkService, InfrastructureService infrastructureService) {
        this.bbkService = bbkService;
        this.infrastructureService = infrastructureService;
    }

    @GetMapping("/bbk")
    public FullAddressResponse bbk(@RequestParam Integer flat) {
        return this.bbkService.getFullAddress(String.valueOf(flat));

    }

//    @GetMapping("/altyapi")
//    public TTInfrastructureDetailDto altyapi(@RequestParam Integer bbk) {
//        return this.infrastructureService.getTTInfrastructureDetail(bbk);
//    }
}
