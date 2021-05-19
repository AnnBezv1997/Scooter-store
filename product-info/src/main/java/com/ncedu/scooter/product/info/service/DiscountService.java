package com.ncedu.scooter.product.info.service;

import com.ncedu.scooter.product.info.entity.Discount;
import com.ncedu.scooter.product.info.entity.Product;
import com.ncedu.scooter.product.info.exception.ExceptionNotFound;
import com.ncedu.scooter.product.info.repository.DiscountRepository;
import com.ncedu.scooter.product.info.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.ncedu.scooter.product.info.exception.ExceptionMessage.DISCONT_NOT_FOUND;

@Service
public class DiscountService {
    @Autowired
    DiscountRepository discountRepository;
    @Autowired
    ProductRepository productRepository;

    public ArrayList<Discount> getAllDiscount() {
        return discountRepository.findAll();
    }

    public boolean saveDiscount(Discount discount){
        Discount newDiscount = new Discount();
        newDiscount.setName(discount.getName());
        newDiscount.setDescription(discount.getDescription());
        newDiscount.setDiscountType(discount.getDiscountType());
        newDiscount.setValue(discount.getValue());
        discountRepository.save(newDiscount);
        return true;
    }

    public boolean updateDiscount(Discount discount) throws ExceptionNotFound {
        Discount disc = discountRepository.findById(discount.getId().intValue());
        if(disc == null){
            throw new ExceptionNotFound(DISCONT_NOT_FOUND);
        }else {
            discountRepository.save(discount);
            return true;
        }
    }

    public boolean deleteDiscount(int id) throws ExceptionNotFound {
        Discount d = discountRepository.findById(id);
        if (d == null) {
            throw new ExceptionNotFound(DISCONT_NOT_FOUND);

        } else {
            List<Product> productList = productRepository.findByDiscountId(id);
            for(Product p : productList){
                p.setDiscount(null);
                productRepository.save(p);
            }
            discountRepository.delete(d);
            return true;
        }
    }

}
