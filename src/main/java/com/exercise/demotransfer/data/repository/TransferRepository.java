package com.exercise.demotransfer.data.repository;

import com.exercise.demotransfer.data.entities.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TransferRepository extends JpaRepository<TransferEntity, Long> {

    TransferEntity save (TransferEntity transferEntity);

    List<Optional<TransferEntity>> findByOriginAccount(String originAccount);

    @Query("SELECT MAX(numberTransfer) FROM TransferEntity where origin_account = :originAccount and date_transfer =:dateTransfer")
    Long lastTransfer(@Param("originAccount") String originAccount, @Param("dateTransfer") Date dateTransfer);
}
