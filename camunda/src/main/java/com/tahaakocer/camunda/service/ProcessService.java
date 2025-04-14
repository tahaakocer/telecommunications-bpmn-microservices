package com.tahaakocer.camunda.service;

import com.tahaakocer.camunda.config.ProcessMappingConfig;
import com.tahaakocer.camunda.dto.MessageResponse;
import com.tahaakocer.camunda.dto.TaskDto;
import com.tahaakocer.camunda.exception.GeneralException;
import com.tahaakocer.camunda.exception.StartProcessException;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ProcessService {
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final Keycloak keycloakClient;
    private final ProcessMappingConfig processMapping;

    public ProcessService(RuntimeService runtimeService, TaskService taskService,
                          Keycloak keycloakClient, ProcessMappingConfig processMapping) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.keycloakClient = keycloakClient;
        this.processMapping = processMapping;
    }
    public ProcessInstance startProcess(String orderType, String channel, Map<String, Object> variables) {
        try {

            String token = obtainAccessToken();

            String processKey = determineProcessKey(orderType, channel);
            if (processKey == null) {
                throw new StartProcessException("Belirtilen orderType ve channel için uygun process bulunamadı: " + orderType + ", " + channel);
            }

            Map<String, Object> processVariables = new HashMap<>(variables);
            processVariables.put("orderType", orderType);
            processVariables.put("channel", channel);

            String businessKey = generateBusinessKey(orderType, channel);
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                    processKey,
                    businessKey,
                    processVariables
            );

            log.info("Process başlatıldı. ProcessInstanceId: {}, BusinessKey: {}, OrderType: {}, Channel: {}",
                    processInstance.getId(), businessKey, orderType, channel);

            return processInstance;
        } catch (Exception e) {
            log.error("Process başlatılırken hata oluştu. OrderType: {}, Channel: {}", orderType, channel, e);
            throw new StartProcessException("Process başlatılırken hata oluştu: " + e.getMessage(), e);
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
    private String determineProcessKey(String orderType, String channel) {
        Map<String, String> channelMap = processMapping.getMapping().get(orderType.toUpperCase());
        if (channelMap == null) {
            log.warn("Konfigürasyonda orderType '{}' için mapping bulunamadı.", orderType);
            return null;
        }

        String processKey = channelMap.get(channel.toUpperCase());
        if (processKey == null) {
            log.warn("Konfigürasyonda orderType '{}' ve channel '{}' için mapping bulunamadı.", orderType, channel);
        }

        return processKey;
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
            throw new GeneralException("Process instance veya business key eksik");
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

}