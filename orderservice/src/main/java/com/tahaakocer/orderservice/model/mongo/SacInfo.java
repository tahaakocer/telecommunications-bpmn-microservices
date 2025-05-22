package com.tahaakocer.orderservice.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SacInfo extends BaseModel{
    private Integer maxSpeed;
    private String SVUID;
    private boolean adslPortState;
    private Integer adslDistance;
    private boolean vdslPortState;
    private Integer vdslDistance;
    private boolean fiberPortState;
    private Integer fiberDistance;

}
