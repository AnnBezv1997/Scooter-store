package com.ncedu.scooter.product.info.service;

import com.ncedu.scooter.product.info.entity.StockStatus;
import com.ncedu.scooter.product.info.repository.StockStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockStatusService {
    @Autowired
    StockStatusRepository stockStatusRepository;

    public boolean saveStockStatus(StockStatus stockStatus) {

            StockStatus newStockStatus = new StockStatus();
            newStockStatus.setCount(stockStatus.getCount());
            stockStatusRepository.save(newStockStatus);
            return true;

    }
    public boolean updateStockStatus(StockStatus stockStatus) {
        StockStatus status = new StockStatus(stockStatus.getId(), stockStatus.getCount());
        stockStatusRepository.save(status);
        return true;
    }

}
