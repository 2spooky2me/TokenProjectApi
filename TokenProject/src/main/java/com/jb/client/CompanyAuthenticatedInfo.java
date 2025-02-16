package com.jb.client;

import com.jb.dto.CompanyDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public final class CompanyAuthenticatedInfo {
    private final CompanyDto companyDto;
    private final ClientType clientType;

}
