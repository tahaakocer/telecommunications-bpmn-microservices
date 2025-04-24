package com.tahaakocer.camunda.listener;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class GlobalTaskListenerPlugin extends AbstractProcessEnginePlugin {

    @Override
    public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        List<BpmnParseListener> parseListeners = processEngineConfiguration.getCustomPostBPMNParseListeners();
        if (parseListeners == null) {
            parseListeners = new ArrayList<>();
            processEngineConfiguration.setCustomPostBPMNParseListeners(parseListeners);
        }

        parseListeners.add(new GlobalTaskParseListener());
    }
}