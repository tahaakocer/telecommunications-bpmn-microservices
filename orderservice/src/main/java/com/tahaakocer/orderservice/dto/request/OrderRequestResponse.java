package com.tahaakocer.orderservice.dto.request;

import com.tahaakocer.orderservice.model.mongo.BaseOrder;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequestResponse {
    private UUID id;
    private String code;
    private String channel;
    private BaseOrder baseOrder;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String createdBy;
    private String lastModifiedBy;


}
