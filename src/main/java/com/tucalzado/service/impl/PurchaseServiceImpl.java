package com.tucalzado.service.impl;


import com.tucalzado.models.entity.*;
import com.tucalzado.repository.IItemRepository;
import com.tucalzado.repository.IPurchaseRepository;
import com.tucalzado.repository.IShoeStockRepository;
import com.tucalzado.service.IPurchaseService;
import com.tucalzado.service.IShoeService;
import com.tucalzado.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PurchaseServiceImpl implements IPurchaseService {
    private final IUserService userService;
    private final IPurchaseRepository purchaseRepository;
    private final IItemRepository itemRepository;
    private final IShoeService shoeService;
    private final IShoeStockRepository shoeStockRepository;

    public PurchaseServiceImpl(IItemRepository itemRepository, IUserService userService, IPurchaseRepository purchaseRepository, IShoeService shoeService, IShoeStockRepository shoeStockRepository) {
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.purchaseRepository = purchaseRepository;
        this.shoeService = shoeService;
        this.shoeStockRepository = shoeStockRepository;
    }

    @Override
    @Transactional
    public boolean savePurchase(Purchase purchase, String username) {
        if (verifyExistsShoeStock(purchase.getItems())) {
            Optional<User> user = userService.getUserByUsername(username);
            if (user.isPresent()) {
                purchase.setUser(user.get());
                purchase.setDate(purchase.getDate());
                purchase.setTotalPurchase(purchase.getTotalPurchase());
                Purchase savedPurchase = purchaseRepository.save(purchase);
                if (saveItems(savedPurchase)) {
                    return true;
                }
            } else {
                throw new RuntimeException("User with username " + username + " not found");
            }
        }
        return false;
    }

    private boolean verifyExistsShoeStock(List<Item> items) {
        for (Item item : items) {
            Optional<Shoe> optionalShoe = shoeService.findById(item.getShoe().getId());
            if (optionalShoe.isPresent()) {
                Shoe shoe = optionalShoe.get();
                for (ShoeStock stock : shoe.getShoeStocks()) {
                    if (stock.getSize().getShoeSize().getSizeAsInt() == item.getSize()) {
                        if (stock.getStock() < item.getQuantity()) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean saveItems(Purchase savedPurchase) {
        for (Item item : savedPurchase.getItems()) {
            Optional<Shoe> optionalShoe = shoeService.findById(item.getShoe().getId());
            if (optionalShoe.isPresent()) {
                Shoe shoe = optionalShoe.get();
                updateShoeStock(item, shoe);
                item.setPurchase(savedPurchase);
                itemRepository.save(item);
            } else {
                throw new RuntimeException("Shoe with ID " + item.getShoe().getId() + " not found");
            }
        }
        return true;
    }

    private void updateShoeStock(Item item, Shoe shoe) {
        for (ShoeStock stock : shoe.getShoeStocks()) {
            if (stock.getSize().getShoeSize().getSizeAsInt() == item.getSize()) {
                int newStock = stock.getStock() - item.getQuantity();
                stock.setStock(newStock); // Asegurar que el stock no sea negativo
                shoeStockRepository.save(stock);
            }
        }
    }




    @Override
    public List<Purchase> getPurchaseByUser(User user) {
        return purchaseRepository.findAllByUser(user);
    }

    @Override
    public Purchase getPurchaseById(Long id) {
        return purchaseRepository.findById(id).orElse(null);
    }

}
