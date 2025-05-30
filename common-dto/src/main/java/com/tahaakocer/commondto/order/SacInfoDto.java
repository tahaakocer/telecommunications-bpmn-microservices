package com.tahaakocer.commondto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tahaakocer.commondto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder

public class SacInfoDto extends BaseDto {
    private Integer maxSpeed;
    private String SVUID;
    private boolean adslPortState;
    private Integer adslDistance;
    private boolean vdslPortState;
    private Integer vdslDistance;
    private boolean fiberPortState;
    private Integer fiberDistance;
}
