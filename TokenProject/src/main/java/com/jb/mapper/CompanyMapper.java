package com.jb.mapper;

import com.jb.dto.CompanyDto;
import com.jb.entity.Company;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CouponsMapper.class})
public interface CompanyMapper {

    Company toEntity(CompanyDto dto);

    CompanyDto toDto(Company entity);
}
