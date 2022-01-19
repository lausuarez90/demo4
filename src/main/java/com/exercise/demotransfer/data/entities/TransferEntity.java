package com.exercise.demotransfer.data.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Transfer")
public class TransferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Double amount;
    private String currency;
    private String origin_account;
    private String destination_account;
    private String description;
    private Double tax_collected;
    private Double cad;




}
