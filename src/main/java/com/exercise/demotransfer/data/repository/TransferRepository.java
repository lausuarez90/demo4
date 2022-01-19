package com.exercise.demotransfer.data.repository;

import com.exercise.demotransfer.data.entities.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<TransferEntity, Long> {

    TransferEntity save (TransferEntity transferEntity);
}
