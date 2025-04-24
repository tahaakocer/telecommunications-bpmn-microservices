package com.tahaakocer.camunda.service;

import com.tahaakocer.camunda.client.OrderRequestServiceClient;
import com.tahaakocer.camunda.config.ProcessMappingConfig;
import com.tahaakocer.camunda.dto.MessageResponse;
import com.tahaakocer.camunda.dto.OrderRequest;
import com.tahaakocer.camunda.dto.ProcessDto;
import com.tahaakocer.camunda.dto.TaskDto;
import com.tahaakocer.camunda.dto.orderRequestDto.OrderRequestDto;
import com.tahaakocer.camunda.exception.GeneralException;
import com.tahaakocer.camunda.exception.StartProcessException;
import com.tahaakocer.camunda.mapper.ProcessMapper;
import com.tahaakocer.camunda.model.ProcessEntity;
import com.tahaakocer.camunda.repository.ProcessRepository;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.MessageCorrelationResult;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProcessService {
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final Keycloak keycloakClient;
    private final ProcessMappingConfig processMapping;
    private final OrderRequestServiceClient orderRequestServiceClient;
    private final ProcessRepository processRepository;
    private final ProcessMapper processMapper;

    public ProcessService(RuntimeService runtimeService,
                          TaskService taskService,
                          Keycloak keycloakClient,
                          ProcessMappingConfig processMapping,
                          OrderRequestServiceClient orderRequestServiceClient,
                          ProcessRepository processRepository,
                          ProcessMapper processMapper) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.keycloakClient = keycloakClient;
        this.processMapping = processMapping;
        this.orderRequestServiceClient = orderRequestServiceClient;
        this.processRepository = processRepository;
        this.processMapper = processMapper;
    }
        public ProcessInstance startProcess(String orderType, String channel, Map<String, Object> variables) {
        try {
            Map<String, Object> processVariables = new HashMap<>(variables);
            processVariables.put("orderType", orderType);
            processVariables.put("channel", channel);

            String processKey;
            if(processVariables.get("processInstanceId") == null) {
                processKey = determineProcessKey(orderType,true);
            } else {
                processKey = determineProcessKey(orderType,false);
            }
            if (processKey == null) {
                throw new StartProcessException("Belirtilen orderType ve channel için uygun process bulunamadı: " + orderType + ", " + channel);
            }



            String businessKey = generateBusinessKey(orderType, channel);
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                    processKey,
                    businessKey,
                    processVariables
            );

            log.info("ProcessEntity başlatıldı. ProcessInstanceId: {}, BusinessKey: {}, OrderType: {}, Channel: {}",
                    processInstance.getId(), businessKey, orderType, channel);

            return processInstance;
        } catch (Exception e) {
            log.error("ProcessEntity başlatılırken hata oluştu. OrderType: {}, Channel: {}", orderType, channel, e);
            throw new StartProcessException("ProcessEntity başlatılırken hata oluştu: " + e.getMessage(), e);
        }
    }
    private String obtainAccessToken() {
        try {
            AccessTokenResponse tokenResponse = this.keycloakClient.tokenManager().getAccessToken();
            String accessToken = tokenResponse.getToken();

            log.info("Yeni access token başarıyla alındı.");
            return accessToken;
        } catch (Exception e) {
            log.error("Keycloak'tan token alınırken hata oluştu: {}", e.getMessage());
            throw new GeneralException("Yeni access token alınamadı.", e);
        }
    }
    private String determineProcessKey(String orderType, boolean isStarter) {
        List<ProcessDto> processes = this.getProcessListByOrderType(orderType);
        if (processes == null) {
            log.warn("orderType '{}' ile ilgili process bulunamadı.", orderType);
            return null;
        }
        ProcessDto processFiltered = processes.stream()
                .filter(process -> process.isStarter() == isStarter && process.getOrderType().equals(orderType))
                .findFirst()
                .orElseThrow(() -> new GeneralException("Belirtilen orderType ve channel için uygun process bulunamadı: " + orderType));

        return processFiltered.getProcessKey();
    }

    //gpt verdi. ne ise yaradıgını sonra arastir
    private String generateBusinessKey(String orderType, String channel) {
        return String.format("%s-%s-%d",
                orderType.toUpperCase(),
                channel.toUpperCase(),
                System.currentTimeMillis());
    }
    public List<TaskDto> getCurrentTaskByProcessInstanceId(String processInstanceId, String businessKey) {
        try {
            List<Task> tasks;

            tasks = taskService.createTaskQuery()
                    .processInstanceId(processInstanceId)
                    .processInstanceBusinessKey(businessKey)
                    .initializeFormKeys()
                    .active()
                    .list();

            List<TaskDto> taskDtos = tasks.stream().map(
                    task -> TaskDto.builder()
                            .id(task.getId())
                            .processInstanceId(task.getProcessInstanceId())
                            .processDefinitionId(task.getProcessDefinitionId())
                            .name(task.getName())
                            .taskDefinitionKey(task.getTaskDefinitionKey())
                            .taskState(task.getTaskState())
                            .executionId(task.getExecutionId())
                            .createTime(task.getCreateTime())
                            .build(
                            )).toList();
            log.info(
                    "ProcessInstanceId '{}' ile ilgili task listesi alındı. Task sayısı: {}",
                    processInstanceId,
                    taskDtos.size()
            );

            if (tasks.isEmpty()) {
                log.warn("ProcessInstanceId '{}' ile ilgili task bulunamadı.", processInstanceId);
                throw new GeneralException("ProcessInstanceId '" + processInstanceId + "' ile ilgili task bulunamadı.");
            }

            return taskDtos;
        } catch (Exception e) {
            log.error("Task listesi alınırken hata oluştu: {}", e.getMessage());
            throw new GeneralException("Task listesi alınırken hata oluştu: " + e.getMessage(), e);
        }
    }
    public void completeTask(String taskId, Map<String, Object> variables) {
        try {
            taskService.complete(taskId, variables);
            log.info("Task '{}' tamamlanıyor.", taskId);
        } catch (Exception e) {
            log.error("Task tamamlanırken hata oluştu: {}", e.getMessage());
            throw new GeneralException("Task tamamlanırken hata oluştu: " + e.getMessage(), e);
        }
    }
    public MessageResponse sendMessageToProcess(String processInstanceId, String businessKey, String messageName, Map<String, Object> variables) {
        if(processInstanceId != null)
            return sendMessageToProcessByInstanceKey(processInstanceId, messageName, variables);
        else if(businessKey != null)
            return sendMessageByBusinessKey(businessKey, messageName, variables);
        else
            throw new GeneralException("ProcessEntity instance veya business key eksik");
    }
    private MessageResponse sendMessageToProcessByInstanceKey(String processInstanceId, String messageName, Map<String, Object> variables) {
        try {

            Execution execution = runtimeService.createExecutionQuery()
                    .processInstanceId(processInstanceId)
                    .messageEventSubscriptionName(messageName)
                    .singleResult();

            if (execution == null) {
                log.warn("No execution found waiting for message '{}' in process instance: {}",
                        messageName, processInstanceId);
                List<Execution> executions = runtimeService.createExecutionQuery()
                        .processInstanceId(processInstanceId)
                        .list();

                if (executions.isEmpty()) {
                    log.warn("No active executions found for process instance: {}", processInstanceId);
                    throw new GeneralException("No active executions found for process instance: " + processInstanceId);
                }
                log.info("Trying to correlate message '{}' to process instance: {}",
                        messageName, processInstanceId);
            }

            MessageCorrelationResult correlationResult;

            if (variables != null && !variables.isEmpty()) {
                correlationResult = runtimeService.createMessageCorrelation(messageName)
                        .processInstanceId(processInstanceId)
                        .setVariables(variables)
                        .correlateWithResult();
            } else {
                correlationResult = runtimeService.createMessageCorrelation(messageName)
                        .processInstanceId(processInstanceId)
                        .correlateWithResult();
            }
            log.info("Message '{}' successfully sent to process instance: {}",
                    messageName, processInstanceId);

            return MessageResponse.builder()
                    .processInstanceId(correlationResult.getProcessInstance().getBusinessKey())
                    .businessKey(correlationResult.getProcessInstance().getBusinessKey())
                    .messageName(messageName)
                    .executionId(correlationResult.getExecution().getId())
                    .build();

        } catch (Exception e) {
            log.error("Error sending message '{}' to process instance: {} - {}",
                    messageName, processInstanceId, e.getMessage(), e);
            throw new GeneralException("Error sending message to process instance: " + e.getMessage(), e);
        }
    }
    private MessageResponse sendMessageByBusinessKey(String businessKey, String messageName, Map<String, Object> variables) {
        try {

            MessageCorrelationResult correlationResult;

            if (variables != null && !variables.isEmpty()) {
                correlationResult = runtimeService.createMessageCorrelation(messageName)
                        .processInstanceBusinessKey(businessKey)
                        .setVariables(variables)
                        .correlateWithResult();
            } else {
                correlationResult = runtimeService.createMessageCorrelation(messageName)
                        .processInstanceBusinessKey(businessKey)
                        .correlateWithResult();
            }

            log.info("Message '{}' successfully sent to process with business key: {}",
                    messageName, businessKey);

          return MessageResponse.builder()
                    .processInstanceId(correlationResult.getProcessInstance().getBusinessKey())
                    .businessKey(correlationResult.getProcessInstance().getBusinessKey())
                    .messageName(messageName)
                    .executionId(correlationResult.getExecution().getId())
                    .build();

        } catch (Exception e) {
            log.error("Error sending message '{}' to process with business key: {} - {}",
                    messageName, businessKey, e.getMessage(), e);
            throw new GeneralException("Error sending message to process with business key: " + e.getMessage(), e);
        }
    }

    public ProcessInstance launchProcess(OrderRequest orderRequest) {
        OrderRequestDto orderRequestDto = Objects.requireNonNull(this.orderRequestServiceClient.getOrderRequest(
                UUID.fromString(orderRequest.getOrderRequestId())).getBody()).getData();
        log.info("OrderRequestDto: {}", orderRequestDto.getCode());
        Map<String, Object> vars = new HashMap<>();
        vars.put("orderRequestId", orderRequest.getOrderRequestId());
        vars.put("channel", orderRequestDto.getChannel());
        vars.put("orderType", orderRequestDto.getBaseOrder().getOrderType());
        vars.put("createdBy", orderRequestDto.getBaseOrder().getCreatedBy());
        vars.put("processInstanceId", orderRequestDto.getBaseOrder().getBpmnFlowRef().getProcessInstanceId());
        ProcessInstance processInstance = this.startProcess(
                orderRequestDto.getBaseOrder().getOrderType(), orderRequestDto.getChannel(), vars);
        log.info("ProcessEntity instance: {}", processInstance);
        return processInstance;
    }

    public List<ProcessDto> getProcessList() {
        List<ProcessEntity> processes = this.processRepository.findAll();
        List<ProcessDto> processDtos = processes.stream().map(this.processMapper::entityToDto).toList();
        log.info("ProcessEntity listesi alındı. ProcessEntity sayısı: {}", processDtos.size());
        return processDtos;
    }

    public ProcessDto getProcess(UUID processId) {
        ProcessEntity process = this.processRepository.findById(processId).orElseThrow(
                () -> new GeneralException("ProcessEntity bulunamadı: " + processId));
        log.info("ProcessEntity alındı. ProcessId: {}", processId);
        return this.processMapper.entityToDto(process);
    }
    public void deleteProcess(UUID processId) {
        ProcessEntity process = this.processRepository.findById(processId).orElseThrow(
                () -> new GeneralException("ProcessEntity bulunamadı: " + processId));
        this.processRepository.delete(process);
        log.info("ProcessEntity silindi. ProcessId: {}", processId);
    }
    public ProcessDto getProcess(String processKey) {
        ProcessEntity process = this.processRepository.findByProcessKey(processKey).orElseThrow(
                () -> new GeneralException("ProcessEntity bulunamadı: " + processKey));
        log.info("ProcessEntity alındı. ProcessKey: {}", processKey);
        return this.processMapper.entityToDto(process);
    }
    public ProcessDto createProcess(ProcessDto processDto) {
        ProcessEntity process = this.processRepository.save(this.processMapper.dtoToEntity(processDto));
        process.setId(UUID.randomUUID());
        log.info("ProcessEntity oluşturuldu. ProcessId: {}", process.getId());
        return this.processMapper.entityToDto(process);
    }
    public List<ProcessDto> getProcessListByOrderType(String orderType) {
        List<ProcessEntity> processes = this.processRepository.findByOrderType(orderType);
        List<ProcessDto> processDtos = processes.stream().map(this.processMapper::entityToDto).toList();
        log.info("ProcessEntity listesi alındı. ProcessEntity sayısı: {}", processDtos.size());
        return processDtos;
    }
}