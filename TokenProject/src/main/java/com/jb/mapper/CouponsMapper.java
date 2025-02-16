package com.jb.mapper;

import com.jb.dto.CouponsDto;
import com.jb.entity.Coupons;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CouponsMapper {
    Coupons toEntity(CouponsDto dto);

    CouponsDto toDto(Coupons entity);

    List<CouponsDto> toDtoList(List<Coupons> couponsList);

}
