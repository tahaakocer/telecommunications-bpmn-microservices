package com.tahaakocer.camunda.controller;


import com.tahaakocer.camunda.dto.*;
import com.tahaakocer.camunda.service.ProcessService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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
                "ProcessEntity başarıyla başlatıldı."
        );
        return ResponseEntity.ok(GeneralResponse.<StartProcessResponse>builder()
                        .code(200)
                        .message("ProcessEntity started successfully")
                        .data(response)
                .build()
        );
    }

    @PostMapping("/start")
    public ResponseEntity<GeneralResponse<StartProcessResponse>> processLauncher(@RequestBody OrderRequest orderRequest) {
        ProcessInstance instance = this.processService.launchProcess(orderRequest);
        StartProcessResponse response = new StartProcessResponse(
                instance.getProcessInstanceId(),
                instance.getBusinessKey(),
                instance.getProcessDefinitionId(),
                "ProcessEntity başarıyla başlatıldı."
        );
        return ResponseEntity.ok(GeneralResponse.<StartProcessResponse>builder()
                .code(200)
                .message("ProcessEntity started successfully")
                .data(response)
                .build()
        );
    }


    @GetMapping("/get-tasks-by-process-instance-id")
    public ResponseEntity<GeneralResponse<List<TaskDto>>> getTaskDetailsByProcessInstanceId(
            @RequestParam String processInstanceId,
            @RequestParam(required = false) String businessKey) {
        List<TaskDto> tasks =
                this.processService.getCurrentTaskByProcessInstanceId(processInstanceId,businessKey);
        return ResponseEntity.ok(GeneralResponse.<List<TaskDto>>builder()
                .code(200)
                .message("Task details found successfully")
                .data(tasks)
                .build()
        );
    }
    @PostMapping("/complete-task")
    public ResponseEntity<GeneralResponse<Void>> completeTask(
            @RequestParam String taskId,
            @RequestBody(required = false) Map<String, Object> variables) {
        this.processService.completeTask(taskId,variables);
        return ResponseEntity.ok(GeneralResponse.<Void>builder()
                .code(200)
                .message("Task completed successfully")
                .data(null)
                .build()
        );
    }
    @PostMapping("/send-message")
    public ResponseEntity<GeneralResponse<Void>> sendMessage(
            @RequestParam(required = false) String processInstanceId,
            @RequestParam(required = false) String businessKey,
            @RequestParam String messageName,
            @RequestBody(required = false) Map<String, Object> variables) {
        this.processService.sendMessageToProcess(processInstanceId,businessKey,messageName,variables);
        return ResponseEntity.ok(GeneralResponse.<Void>builder()
                .code(200)
                .message("Message sent successfully")
                .data(null)
                .build()
        );
    }

    @GetMapping("/processes/get-all")
    public ResponseEntity<GeneralResponse<List<ProcessDto>>> getProcessList() {
        List<ProcessDto> processDtos = this.processService.getProcessList();
        return ResponseEntity.ok(GeneralResponse.<List<ProcessDto>>builder()
                .code(200)
                .message("ProcessEntity listesi alındı. ProcessEntity sayısı: " + processDtos.size())
                .data(processDtos)
                .build()
        );
    }
    @GetMapping("/processes/get-by-order-type")
    public ResponseEntity<GeneralResponse<List<ProcessDto>>> getProcessListByOrderType(@RequestParam String orderType) {
        List<ProcessDto> processDtos = this.processService.getProcessListByOrderType(orderType);
        return ResponseEntity.ok(GeneralResponse.<List<ProcessDto>>builder()
                .code(200)
                .message("ProcessEntity listesi alındı. ProcessEntity sayısı: " + processDtos.size())
                .data(processDtos)
                .build()
        );
    }
    @GetMapping("/processes/get-by-id")
    public ResponseEntity<GeneralResponse<ProcessDto>> getProcess(@RequestParam UUID processId) {
        ProcessDto processDto = this.processService.getProcess(processId);
        return ResponseEntity.ok(GeneralResponse.<ProcessDto>builder()
                .code(200)
                .message("ProcessEntity alındı. ProcessId: " + processId)
                .data(processDto)
                .build()
        );
    }
    @GetMapping("/processes/get-by-process-key")
    public ResponseEntity<GeneralResponse<ProcessDto>> getProcess(@RequestParam String processKey) {
        ProcessDto processDto = this.processService.getProcess(processKey);
        return ResponseEntity.ok(GeneralResponse.<ProcessDto>builder()
                .code(200)
                .message("ProcessEntity alındı. ProcessKey: " + processKey)
                .data(processDto)
                .build()
        );
    }
    @PostMapping("/processes/create")
    public ResponseEntity<GeneralResponse<ProcessDto>> createProcess(@RequestBody ProcessDto processDto) {
        ProcessDto process = this.processService.createProcess(processDto);
        return ResponseEntity.ok(GeneralResponse.<ProcessDto>builder()
                .code(200)
                .message("ProcessEntity oluşturuldu. ProcessId: " + process.getId())
                .data(process)
                .build()
        );
    }
    @DeleteMapping("/processes/delete")
    public ResponseEntity<GeneralResponse<Void>> deleteProcess(@RequestParam UUID processId) {
        this.processService.deleteProcess(processId);
        return ResponseEntity.ok(GeneralResponse.<Void>builder()
                .code(200)
                .message("ProcessEntity silindi. ProcessId: " + processId)
                .data(null)
                .build()
        );
    }
}
