package com.tahaakocer.account.client;

import com.tahaakocer.account.config.FeignClientConfig;
import com.tahaakocer.commondto.crm.AccountRefDto;
import com.tahaakocer.commondto.crm.PartyRoleDto;
import com.tahaakocer.commondto.request.GeneralOrderRequest;
import com.tahaakocer.commondto.response.GeneralResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "crm",
        configuration = FeignClientConfig.class
)
public interface PartyRoleServiceClient {
    @GetMapping("/api/crm/party-role/{partyRoleId}")
    ResponseEntity<GeneralResponse<PartyRoleDto>> getPartyRole(
            @PathVariable String partyRoleId
    );

    @GetMapping("/api/crm/get-party-role-by-order-request-id")
    ResponseEntity<GeneralResponse<PartyRoleDto>> getPartyRoleByOrderRequestId(
            @RequestBody GeneralOrderRequest generalOrderRequest
    );

    @PostMapping("/api/crm/party-role/create-account-ref")
     ResponseEntity<GeneralResponse<AccountRefDto>> createAccountRef(
             @RequestBody GeneralOrderRequest generalOrderRequest
    );
}
