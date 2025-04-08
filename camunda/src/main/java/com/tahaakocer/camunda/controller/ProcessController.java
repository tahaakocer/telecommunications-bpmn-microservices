package com.tahaakocer.camunda.controller;


import com.tahaakocer.camunda.dto.GeneralResponse;
import com.tahaakocer.camunda.dto.StartProcessResponse;
import com.tahaakocer.camunda.service.ProcessService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/starter-process")
public class ProcessController {
    private final ProcessService processService;

    public ProcessController(ProcessService processService) {
        this.processService = processService;
    }

    @GetMapping("/viewer")
    public String showBpmnViewer() {
        return "redirect:/viewer/index.html";
    }

    @PostMapping("/start/{orderType}/{channel}")
    public ResponseEntity<GeneralResponse<StartProcessResponse>> startProcess(@PathVariable String orderType,
                                                                              @PathVariable String channel,
                                                                              @RequestBody Map<String, Object> variables) {
        ProcessInstance instance = this.processService.startProcess(orderType, channel, variables);
        StartProcessResponse response = new StartProcessResponse(
                instance.getProcessInstanceId(),
                instance.getBusinessKey(),
                instance.getProcessDefinitionId(),
                "Process başarıyla başlatıldı."
        );
        return ResponseEntity.ok(GeneralResponse.<StartProcessResponse>builder()
                        .code(200)
                        .message("Process started successfully")
                        .data(response)
                .build()
        );
    }

}
