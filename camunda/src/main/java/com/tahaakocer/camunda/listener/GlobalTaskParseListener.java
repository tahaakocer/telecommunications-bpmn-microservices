package com.tahaakocer.camunda.listener;

import com.tahaakocer.camunda.client.OrderRequestServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.core.variable.mapping.IoMapping;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;
import org.camunda.bpm.engine.impl.pvm.process.TransitionImpl;
import org.camunda.bpm.engine.impl.task.TaskDefinition;
import org.camunda.bpm.engine.impl.util.xml.Element;
import org.camunda.bpm.engine.impl.variable.VariableDeclaration;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class GlobalTaskParseListener implements BpmnParseListener {
    private final OrderRequestServiceClient orderRequestServiceClient;

    public GlobalTaskParseListener(OrderRequestServiceClient orderRequestServiceClient) {
        this.orderRequestServiceClient = orderRequestServiceClient;
    }

    @Override
    public void parseUserTask(Element userTaskElement, ScopeImpl scope, ActivityImpl activity) {
        // User Task için dinleyici ekle - doğru yaklaşım
        UserTaskActivityBehavior activityBehavior = (UserTaskActivityBehavior) activity.getActivityBehavior();
        if (activityBehavior != null) {
            TaskDefinition taskDefinition = activityBehavior.getTaskDefinition();
            if (taskDefinition != null) {
                // Task olaylarına listener ekle
                taskDefinition.addTaskListener(TaskListener.EVENTNAME_CREATE, new GlobalUserTaskExecutionListener(orderRequestServiceClient));
//                taskDefinition.addTaskListener(TaskListener.EVENTNAME_ASSIGNMENT, new GlobalUserTaskExecutionListener());
//                taskDefinition.addTaskListener(TaskListener.EVENTNAME_COMPLETE, new GlobalUserTaskExecutionListener());
//                taskDefinition.addTaskListener(TaskListener.EVENTNAME_DELETE, new GlobalUserTaskExecutionListener());

                log.info("GlobalTaskParseListener - Task listener eklendi: {}", activity.getId());
            }
        }
    }

    @Override
    public void parseServiceTask(Element serviceTaskElement, ScopeImpl scope, ActivityImpl activity) {
        // Service Task için execution listener ekle
        activity.addListener(org.camunda.bpm.engine.delegate.ExecutionListener.EVENTNAME_START,
                new GlobalServiceTaskExecutionListener(orderRequestServiceClient));
//        activity.addExecutionListener(org.camunda.bpm.engine.delegate.ExecutionListener.EVENTNAME_END,
//                new GlobalServiceTaskExecutionListener());

        log.info("GlobalTaskParseListener - Service Task listener eklendi: {}", activity.getId());
    }

    @Override public void parseProcess(Element processElement, ProcessDefinitionEntity processDefinition) {}
    @Override public void parseStartEvent(Element startEventElement, ScopeImpl scope, ActivityImpl activity) {}
    @Override public void parseEndEvent(Element endEventElement, ScopeImpl scope, ActivityImpl activity) {}
    @Override public void parseExclusiveGateway(Element exclusiveGwElement, ScopeImpl scope, ActivityImpl activity) {}
    @Override public void parseInclusiveGateway(Element inclusiveGwElement, ScopeImpl scope, ActivityImpl activity) {}
    @Override public void parseParallelGateway(Element parallelGwElement, ScopeImpl scope, ActivityImpl activity) {}
    @Override public void parseScriptTask(Element scriptTaskElement, ScopeImpl scope, ActivityImpl activity) {}
    @Override public void parseBusinessRuleTask(Element businessRuleTaskElement, ScopeImpl scope, ActivityImpl activity) {}
    @Override public void parseTask(Element taskElement, ScopeImpl scope, ActivityImpl activity) {}
    @Override public void parseManualTask(Element manualTaskElement, ScopeImpl scope, ActivityImpl activity) {}
    @Override public void parseReceiveTask(Element receiveTaskElement, ScopeImpl scope, ActivityImpl activity) {}
    @Override public void parseSendTask(Element sendTaskElement, ScopeImpl scope, ActivityImpl activity) {}
    @Override public void parseCallActivity(Element callActivityElement, ScopeImpl scope, ActivityImpl activity) {}
    @Override public void parseProperty(Element element, VariableDeclaration variableDeclaration, ActivityImpl activity) {}
    @Override public void parseSubProcess(Element subProcessElement, ScopeImpl scope, ActivityImpl activity) {}
    @Override public void parseBoundaryTimerEventDefinition(Element timerEventDefinition, boolean interrupting, ActivityImpl timerActivity) {}
    @Override public void parseBoundaryErrorEventDefinition(Element errorEventDefinition, boolean interrupting, ActivityImpl activity, ActivityImpl nestedErrorEventActivity) {}
    @Override public void parseMultiInstanceLoopCharacteristics(Element activityElement, Element multiInstanceLoopCharacteristicsElement, ActivityImpl activity) {}
    @Override public void parseBoundarySignalEventDefinition(Element signalEventDefinition, boolean interrupting, ActivityImpl signalActivity) {}
    @Override public void parseEventBasedGateway(Element element, ScopeImpl scope, ActivityImpl activity) {}
    @Override public void parseTransaction(Element element, ScopeImpl scope, ActivityImpl activity) {}
    @Override public void parseCompensateEventDefinition(Element element, ActivityImpl activity) {}
    @Override public void parseBoundaryMessageEventDefinition(Element messageEventDefinition, boolean interrupting, ActivityImpl messageActivity) {}
    @Override public void parseBoundaryEscalationEventDefinition(Element escalationEventDefinition, boolean interrupting, ActivityImpl boundaryEventActivity) {}
    @Override public void parseBoundaryConditionalEventDefinition(Element conditionalEventDefinition, boolean interrupting, ActivityImpl conditionalActivity) {}
    @Override public void parseIntermediateSignalCatchEventDefinition(Element signalEventDefinition, ActivityImpl signalActivity) {}
    @Override public void parseIntermediateTimerEventDefinition(Element timerEventDefinition, ActivityImpl timerActivity) {}
    @Override public void parseRootElement(Element element, List<ProcessDefinitionEntity> list) {}
    @Override public void parseIntermediateConditionalEventDefinition(Element conditionalEventDefinition, ActivityImpl conditionalActivity) {}
    @Override public void parseConditionalStartEventForEventSubprocess(Element element, ActivityImpl activity, boolean b) {}
    @Override public void parseIoMapping(Element element, ActivityImpl activity, IoMapping ioMapping) {}
    @Override public void parseIntermediateMessageCatchEventDefinition(Element messageEventDefinition, ActivityImpl messageActivity) {}
    @Override public void parseIntermediateThrowEvent(Element intermediateEventElement, ScopeImpl scope, ActivityImpl activity) {}
    @Override public void parseIntermediateCatchEvent(Element intermediateEventElement, ScopeImpl scope, ActivityImpl activity) {}
    @Override public void parseBoundaryEvent(Element element, ScopeImpl scope, ActivityImpl activity) {}
    @Override public void parseSequenceFlow(Element sequenceFlowElement, ScopeImpl scopeElement, TransitionImpl transition) {}
}