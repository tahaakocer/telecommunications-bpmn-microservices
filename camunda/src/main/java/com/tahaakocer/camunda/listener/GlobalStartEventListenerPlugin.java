package com.tahaakocer.camunda.listener;

import com.tahaakocer.camunda.client.OrderRequestServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class GlobalStartEventListenerPlugin extends AbstractProcessEnginePlugin {

    private final OrderRequestServiceClient orderRequestServiceClient;
    public GlobalStartEventListenerPlugin(OrderRequestServiceClient orderRequestServiceClient) {

        this.orderRequestServiceClient = orderRequestServiceClient;
    }

    @Override
    public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {

        List<BpmnParseListener> parseListeners = processEngineConfiguration.getCustomPostBPMNParseListeners();
        if (parseListeners == null) {
            parseListeners = new ArrayList<>();
            processEngineConfiguration.setCustomPostBPMNParseListeners(parseListeners);
        }

        parseListeners.add(new GlobalStartEventParseListener(orderRequestServiceClient));
    }
}