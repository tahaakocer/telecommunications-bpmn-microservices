package com.tahaakocer.camunda.controller;


import com.tahaakocer.camunda.dto.GeneralResponse;
import com.tahaakocer.camunda.dto.StartProcessResponse;
import com.tahaakocer.camunda.dto.TaskDto;
import com.tahaakocer.camunda.service.ProcessService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
}
