package com.cp2196g03gr01.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Entity
@Data
@Table(name = "customer")
public class Customer {
 @Id
 @GeneratedValue
 @Column(name = "customer_id")
 private Long id;
 
 @NotBlank
 @Column(name = "customer_name",length = 100)
 private String name;
 
 @Column(name = "customer_point")
 private int earnedPoint;
 
 @Column(name = "customer_phone"  )
 private String phone;
 
 @Column(name = "customer_address",length = 100)
 private String address;
 
}
