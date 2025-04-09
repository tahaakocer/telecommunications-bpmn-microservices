package com.tahaakocer.orderservice.model.mongo;

import com.tahaakocer.orderservice.utils.Util;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@EqualsAndHashCode(callSuper = true)
@Document("orderRequest")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest extends BaseModel {

    @Field("code")
    private String code;
    @Field("channel")
    private String channel;
    @Field("baseOrder")
    private BaseOrder baseOrder;

    @PrePersist
    public void prePersist() {
        this.code = Util.generateRandomCode("ORDRQ");
    }
}
