package com.ncedu.scooter.product.info.service;

import com.ncedu.scooter.product.info.entity.Discount;
import com.ncedu.scooter.product.info.exception.DiscountNotFound;
import com.ncedu.scooter.product.info.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.ncedu.scooter.product.info.exception.ExceptionMessage.DISCONT_NOT_FOUND;

@Service
public class DiscountService {
    @Autowired
    DiscountRepository discountRepository;

    public Discount findById(int id) throws DiscountNotFound {
        Discount discount = discountRepository.findById(id);
        if (discount != null) {
            return discount;
        } else {
            throw new DiscountNotFound(DISCONT_NOT_FOUND);
        }
    }

    public ArrayList<Discount> getAllDiscount() {
        return discountRepository.findAllDiscount();
    }

    public boolean saveDiscount(Discount discount) {
        Discount d = discountRepository.findById((int) discount.getId());
        if (d != null) {
            return false;
        } else {
            discountRepository.save(d);
            return true;
        }
    }

    public boolean updateDiscount(Discount discount) {
        Discount d = new Discount(discount.getId(), discount.getName(), discount.getDescription(),
                                    discount.getDiscountType(), discount.getValue());
        discountRepository.save(d);
        return true;
    }

    public boolean deleteDiscount(int id) {
        Discount d =  discountRepository.findById(id);
        if (d != null) {
            return false;
        } else {
            discountRepository.delete(d);
            return true;
        }
    }

}
