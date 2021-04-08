package com.ncedu.scooter.product.info.service;

import com.ncedu.scooter.product.info.entity.StockStatus;
import com.ncedu.scooter.product.info.exception.ProductNotFound;
import com.ncedu.scooter.product.info.repository.StockStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.ncedu.scooter.product.info.exception.ExceptionMessage.PRODUCT_NOT_FOUND;

@Service
public class StockStatusService {
    @Autowired
    StockStatusRepository stockStatusRepository;

    public StockStatus findById(int id) throws ProductNotFound {
        StockStatus stockStatus = stockStatusRepository.findById(id);
        if (stockStatus != null) {
            return stockStatus;
        } else {
            throw new ProductNotFound(PRODUCT_NOT_FOUND);
        }
    }

    public ArrayList<StockStatus> getAllStockStatus() {
        return stockStatusRepository.findAll();
    }

    public boolean updateStockStatus(StockStatus stockStatus) {
        StockStatus status = new StockStatus(stockStatus.getId(), stockStatus.getCount());
        stockStatusRepository.save(status);
        return true;
    }


}
