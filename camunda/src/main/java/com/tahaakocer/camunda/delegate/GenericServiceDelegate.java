package com.tahaakocer.camunda.delegate;

import camundajar.impl.com.google.gson.Gson;
import com.tahaakocer.camunda.dto.LogDto;
import com.tahaakocer.camunda.exception.GeneralException;
import com.tahaakocer.camunda.service.LogService;
import com.tahaakocer.camunda.utils.VariableUtils;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GenericServiceDelegate implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(GenericServiceDelegate.class);

    private final RestTemplate restTemplate;
    private final RedisTemplate<String, String> redisTemplate;
    private final Gson gson;
    private final LogService logService;
    private final Keycloak keycloakClient;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.username}")
    private String username;

    @Value("${keycloak.password}")
    private String password;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.token.url}")
    private String tokenUrl;
    private Expression serviceName;
    private Expression parameters;
    private Expression results;
    private String serviceUrl;
    private Map<String, Object> requestBody;
    private Map fullResponse;
    private List<String> parameterKeys;
    private List<String> resultFields;

    public GenericServiceDelegate(RestTemplate restTemplate, RedisTemplate<String, String> redisTemplate, Gson gson, LogService logService, Keycloak keycloakClient) {
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
        this.gson = gson;
        this.logService = logService;
        this.keycloakClient = keycloakClient;
    }

    @Override
    public void execute(DelegateExecution execution) {

            initializeVariables(execution);
            buildRequestBody(execution);

            String processInstanceId = execution.getProcessInstanceId();
            String accessTokenKey = "accessToken:" + processInstanceId;

            makeServiceCallWithToken(execution, accessTokenKey);

            processResponse(execution);
            logExecution(execution);
    }

    private void initializeVariables(DelegateExecution execution) {
        String parametersVal;
        String resultsVal;
        try {
            serviceUrl = (String) serviceName.getValue(execution);
            parametersVal = (String) parameters.getValue(execution);
            resultsVal = (String) results.getValue(execution);
        } catch (Exception e) {
            log.error("GenericServiceDelegate - Hata oluştu: {}", e.getMessage(), e);
            execution.setVariable("errorMessage", e.getMessage());
            throw new BpmnError("SERVICE_ERROR", e.getMessage());
        }
        log.info("GenericServiceDelegate - serviceUrl: {}", serviceUrl);
        log.info("GenericServiceDelegate - parameters: {}", parametersVal);
        log.info("GenericServiceDelegate - results: {}", resultsVal);

        parameterKeys = VariableUtils.StringSplit(parametersVal);
        resultFields = VariableUtils.StringSplit(resultsVal);

        requestBody = new HashMap<>();
        fullResponse = new HashMap<>();
    }

    private void buildRequestBody(DelegateExecution execution) {
        try {
            for (String key : parameterKeys) {
                key = key.trim();
                Object value = getParameterValue(execution, key);
                if (value != null) {
                    String requestKey = key;
                    if (key.contains("Results")) {
                        String[] parts = key.split("\\.");
                        requestKey = parts[parts.length - 1]; // Son kısmı alıyoz (örneğin keycloakUserId)
                    }
                    requestBody.put(requestKey, value);
                    log.info("Request body param '{}' set edildi: {}", requestKey, value);
                } else {
                    log.warn("Parametre '{}' process variable'larda ya da result map'lerinde bulunamadı.", key);
                    throw new GeneralException("Parametre '" + key + "' process variable'larda ya da result map'lerinde bulunamadı.");
                }
            }
            log.info("GenericServiceDelegate - Oluşturulan Request Body: {}", requestBody);

        } catch (Exception e) {
            log.error("GenericServiceDelegate - Hata oluştu: {}", e.getMessage(), e);
            execution.setVariable("errorMessage", e.getMessage());
            throw new BpmnError("SERVICE_ERROR", e.getMessage());
        }

    }

    private void makeServiceCallWithToken(DelegateExecution execution, String accessTokenKey) {
        String accessToken;
        try {
            accessToken = redisTemplate.opsForValue().get(accessTokenKey);
            if (accessToken == null || accessToken.isEmpty()) {
                log.info("Redis'te access token bulunamadı, yeni token alınıyor.");
                accessToken = obtainNewAccessToken();
                redisTemplate.opsForValue().set(accessTokenKey, accessToken);
            }
            ResponseEntity<Map> responseEntity = sendRequest(serviceUrl, requestBody, accessToken);
            fullResponse = responseEntity.getBody();
            log.info("GenericServiceDelegate - Full Response: {}", fullResponse);
            if (fullResponse == null) {
                String errorMsg = "Mikroservisten boş response alındı.";
                log.error(errorMsg);
                execution.setVariable("errorMessage", errorMsg);
                //noinspection unchecked
                fullResponse.put("errorMessage", errorMsg);
                throw new BpmnError("SERVICE_ERROR", errorMsg);
            }
        }
        catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                log.warn("Access token geçersiz, yeni token alınıyor.");
                try {
                    accessToken = obtainNewAccessToken();
                    redisTemplate.opsForValue().set(accessTokenKey, accessToken);
                    ResponseEntity<Map> responseEntity = sendRequest(serviceUrl, requestBody, accessToken);
                    fullResponse = responseEntity.getBody();
                    log.info("GenericServiceDelegate - Full Response (after token refresh): {}", fullResponse);
                } catch (Exception ex) {
                    String errorMsg = "Yeni token alımı sırasında hata: " + ex.getMessage();
                    log.error(errorMsg);
                    //noinspection unchecked
                    fullResponse.put("errorMessage", errorMsg);
                    execution.setVariable("errorMessage", errorMsg);
                    throw new BpmnError("TOKEN_ERROR", errorMsg);
                }
            } else {
                log.error("GenericServiceDelegate - Mikroservis isteği hatası: {}", e.getMessage());
                String errorMsg = "GenericServiceDelegate - Mikroservis isteği hatası: " + e.getMessage();
                execution.setVariable("errorMessage", errorMsg);
                //noinspection unchecked
                fullResponse.put("errorMessage", errorMsg);
                throw new BpmnError("SERVICE_ERROR", errorMsg);
            }
        }
        catch (Exception e) {
            log.error("GenericServiceDelegate - Hata oluştu: {}", e.getMessage(), e);
            execution.setVariable("errorMessage", e.getMessage());
            //noinspection unchecked
            fullResponse.put("errorMessage", e.getMessage());
            throw new BpmnError("SERVICE_ERROR", e.getMessage());
        }
        finally {
            logExecution(execution);
        }

    }

    private void processResponse(DelegateExecution execution) {
        if (fullResponse.containsKey("code")) {
            int code;
            try {
                code = Integer.parseInt(fullResponse.get("code").toString());
            } catch (NumberFormatException nfe) {
                String errorMsg = "Response 'code' değeri sayı formatında değil: " + fullResponse.get("code");
                log.error(errorMsg);
                execution.setVariable("errorMessage", errorMsg);
                throw new BpmnError("SERVICE_ERROR", errorMsg);
            }

            HttpStatus status = HttpStatus.resolve(code);
            if (status == null || status.isError()) {
                String message = fullResponse.containsKey("message") ? fullResponse.get("message").toString() : "Hata oluştu.";
                String errorMsg = "Mikroservis hata kodu: " + code + ", Mesaj: " + message;
                log.error(errorMsg);
                execution.setVariable("errorMessage", errorMsg);
                throw new BpmnError("SERVICE_ERROR", errorMsg);
            }
        }

        Map<String, Object> resultMap = new HashMap<>();
        for (String field : resultFields) {
            field = field.trim();
            if (fullResponse.containsKey(field)) {
                resultMap.put(field, fullResponse.get(field));
                log.info("Result field '{}' set edildi: {}", field, fullResponse.get(field));
            } else {
                log.warn("Response içerisinde '{}' alanı bulunamadı.", field);
            }
        }

        String currentResultKey = execution.getCurrentActivityId() + "_Results";
        execution.setVariable(currentResultKey, resultMap);
        log.info("GenericServiceDelegate - Result map '{}' anahtarıyla set edildi.", currentResultKey);

        @SuppressWarnings("unchecked")
        List<String> resultKeys = (List<String>) execution.getVariable("resultKeys");
        if (resultKeys == null) {
            resultKeys = new ArrayList<>();
        }
        resultKeys.add(currentResultKey);
        execution.setVariable("resultKeys", resultKeys);
    }

    private Object getParameterValue(DelegateExecution execution, String param) {

        Object value = execution.getVariable(param);
        if (value != null) {
            log.info("Parametre '{}' process variables'dan bulundu: {}", param, value);
            return value;
        }

        @SuppressWarnings("unchecked")
        List<String> resultKeys = (List<String>) execution.getVariable("resultKeys");
        if (resultKeys == null || resultKeys.isEmpty()) {
            log.warn("Hiçbir Results variable'ı bulunamadı.");
            return null;
        }

        if (param.contains("Results")) {
            String[] parts = param.split("\\.");
            String resultKey = parts[0]; /// ST_DemoService_Results gibi gibi
            if (execution.hasVariable(resultKey)) {
                @SuppressWarnings("unchecked")
                Map<String, Object> resultMap = (Map<String, Object>) execution.getVariable(resultKey);
                if (resultMap != null) {
                    Object result = extractNestedValue(resultMap, parts, 1);
                    if (result != null) {
                        log.info("Parametre '{}' Results '{}' içinden bulundu: {}", param, resultKey, result);
                        return result;
                    }
                }
            }
            log.warn("Results '{}' bulunamadı veya değer yok.", resultKey);
            return null;
        }

        for (int i = resultKeys.size() - 1; i >= 0; i--) {
            String key = resultKeys.get(i);
            @SuppressWarnings("unchecked")
            Map<String, Object> resultMap = (Map<String, Object>) execution.getVariable(key);
            if (resultMap != null && resultMap.containsKey(param)) {
                log.info("Parametre '{}' Results '{}' içinden bulundu: {}", param, key, resultMap.get(param));
                return resultMap.get(param);
            }
        }

        for (int i = resultKeys.size() - 1; i >= 0; i--) {
            String key = resultKeys.get(i);
            @SuppressWarnings("unchecked")
            Map<String, Object> resultMap = (Map<String, Object>) execution.getVariable(key);
            if (resultMap != null) {
                Object result = searchInMap(resultMap, param);
                if (result != null) {
                    log.info("Parametre '{}' Results '{}' içinde derinlemesine bulundu: {}", param, key, result);
                    return result;
                }
            }
        }

        log.warn("Parametre '{}' hiçbir yerde bulunamadı.", param);
        return null;
    }

    private Object extractNestedValue(Map<String, Object> map, String[] parts, int index) {
        if (index >= parts.length) {
            return null;
        }
        Object value = map.get(parts[index]);
        if (value == null) {
            return null;
        }
        if (index == parts.length - 1) {
            return value;
        }
        if (value instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> nestedMap = (Map<String, Object>) value;
            return extractNestedValue(nestedMap, parts, index + 1);
        }
        return null;
    }

    private Object searchInMap(Map<String, Object> map, String param) {
        if (map.containsKey(param)) {
            return map.get(param);
        }
        for (Object value : map.values()) {
            if (value instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> nestedMap = (Map<String, Object>) value;
                Object result = searchInMap(nestedMap, param);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    private ResponseEntity<Map> sendRequest(String serviceUrl, Map<String, Object> requestBody, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (token != null && !token.isEmpty()) {
            headers.setBearerAuth(token);
        }
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        return restTemplate.postForEntity(serviceUrl, entity, Map.class);
    }

    private String obtainNewAccessToken() {
        try {
            AccessTokenResponse tokenResponse = this.keycloakClient.tokenManager().getAccessToken();
            String newAccessToken = tokenResponse.getToken();

            log.info("Yeni access token başarıyla alındı.");
            return newAccessToken;
        } catch (Exception e) {
            log.error("Keycloak'tan token alınırken hata oluştu: {}", e.getMessage());
            throw new GeneralException("Yeni access token alınamadı.", e);
        }
    }

    private void logExecution(DelegateExecution execution) {
        try {
            LogDto log = LogDto.builder()
                    .activityId(execution.getCurrentActivityId())
                    .processInstanceId(execution.getProcessInstanceId())
                    .stepName(execution.getCurrentActivityName())
                    .serviceName(serviceUrl)
                    .requestBody(gson.toJson(requestBody))
                    .responseBody(fullResponse != null ? gson.toJson(fullResponse) : null)
                    .timestamp(LocalDateTime.now())
                    .build();
            this.logService.save(log);
        } catch (Exception e) {
            log.error("Log kaydederken hata oluştu: {}", e.getMessage(), e);
        }
    }
}