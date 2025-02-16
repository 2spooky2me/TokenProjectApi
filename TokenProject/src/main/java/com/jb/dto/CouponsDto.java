package com.jb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponsDto {
    private int id;
    private UUID uuid;
    private int company_id;
    private String category;
    private String title;
    private Date start_date;
    private Date end;
    private int amount;
    private String description;
    private double price;
    private String image;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<CustomerDto> customers;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<CompanyDto> companies;
}
