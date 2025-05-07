package com.tahaakocer.crm.mapper;

import com.tahaakocer.crm.dto.CustomerDto;
import com.tahaakocer.crm.model.Customer;
import lombok.experimental.SuperBuilder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = SuperBuilder.class)
public interface CustomerMapper {
    CustomerDto entityToDto(Customer saved);

}
