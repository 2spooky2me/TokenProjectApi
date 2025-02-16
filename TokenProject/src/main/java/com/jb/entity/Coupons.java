package com.jb.entity;

import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.CharJdbcType;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Coupons {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JdbcType(CharJdbcType.class)
    @Column(unique = true, updatable = false, nullable = false)
    private UUID uuid;
    private String category;
    private String title;
    private Date start_data;
    private Date end;
    private int amount;
    private String description;
    private double price;
    private String image;

    @ManyToMany(mappedBy = "coupons" , cascade = CascadeType.REMOVE)
    private Set<Customer> customers = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}
