package com.exercise.demotransfer.data.entities;

import lombok.AllArgsConstructor;
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
    private String originAccount;
    private String destinationAccount;
    private String description;
    private Double taxCollected;
    private Double cad;
    private Integer numberTransfer;




}
