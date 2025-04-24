package com.tahaakocer.externalapiservice.dto.mernis;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ValidMernisResponse {
    private boolean isMernisValid;
}
