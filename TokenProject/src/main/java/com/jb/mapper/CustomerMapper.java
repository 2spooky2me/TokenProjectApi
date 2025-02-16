package com.jb.mapper;

import com.jb.dto.CustomerDto;
import com.jb.entity.Customer;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = {CouponsMapper.class})
public interface CustomerMapper {
    Customer toEntity(CustomerDto dto);

    CustomerDto toDto(Customer entity);
}
