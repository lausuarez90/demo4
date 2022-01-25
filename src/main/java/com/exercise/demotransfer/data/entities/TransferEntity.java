package com.exercise.demotransfer.data.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Transfer")
public class TransferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private BigDecimal amount;
    private String currency;
    private String originAccount;
    private String destinationAccount;
    private String description;
    private Double taxCollected;
    private BigDecimal cad;
    private Integer numberTransfer;

    @Temporal(TemporalType.DATE)
    private Date dateTransfer;




}
