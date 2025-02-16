package com.jb.client;

import com.jb.dto.CustomerDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public final class CustomerAuthenticatedInfo {
    private final CustomerDto customerDto;
    private final ClientType clientType;

}