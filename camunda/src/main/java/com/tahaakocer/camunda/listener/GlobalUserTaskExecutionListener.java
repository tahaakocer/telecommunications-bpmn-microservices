package com.tahaakocer.camunda.listener;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GlobalUserTaskExecutionListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        String taskId = delegateTask.getId();
        String taskName = delegateTask.getName();
        String processInstanceId = delegateTask.getProcessInstanceId();
        String executionId = delegateTask.getExecutionId();
        String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
        String eventName = delegateTask.getEventName();

        log.info("GlobalUserTaskExecutionListener - taskId: {}" +
                        "\ntaskName: {}" +
                        "\nprocessInstanceId: {}" +
                        "\nexecutionId: {}" +
                        "\ntaskDefinitionKey: {}" +
                        "\neventName: {}",
                taskId, taskName, processInstanceId, executionId, taskDefinitionKey, eventName);
        
    }
}