package com.tahaakocer.camunda.listener;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GlobalServiceTaskExecutionListener implements ExecutionListener {


    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String activityId = execution.getCurrentActivityId();
        String activityName = execution.getCurrentActivityName();
        String processInstanceId = execution.getProcessInstanceId();
        String executionId = execution.getId();
        String eventName = execution.getEventName();
        String processDefinitionId = execution.getProcessDefinitionId();

        log.info("GlobalServiceTaskExecutionListener - activityId: {}" +
                        "\nactivityName: {}" +
                        "\nprocessInstanceId: {}" +
                        "\nexecutionId: {}" +
                        "\neventName: {}" +
                        "\nprocessDefinitionId: {}",
                activityId, activityName, processInstanceId, executionId, eventName, processDefinitionId);


    }
}