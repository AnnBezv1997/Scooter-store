package com.ncedu.scooter.product.info.repository;

import com.ncedu.scooter.product.info.entity.StockStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface StockStatusRepository extends CrudRepository<StockStatus, Integer> {
    StockStatus findById(int id);
    ArrayList<StockStatus> findAllStockStatus();
}
