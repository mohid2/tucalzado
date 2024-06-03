package com.tucalzado.service;



import com.tucalzado.entity.Purchase;
import com.tucalzado.entity.User;

import java.util.List;

public interface IPurchaseService {
    boolean savePurchase(Purchase purchase, String username);

    List<Purchase> getPurchaseByUser(User user);

    Purchase getPurchaseById(Long id);
}
