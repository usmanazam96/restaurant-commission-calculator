package com.vd.restaurant.commission.calculator.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vd.restaurant.commission.calculator.entities.MenuItem;
import com.vd.restaurant.commission.calculator.exceptions.InvalidStockException;
import com.vd.restaurant.commission.calculator.exceptions.MenuItemNotFoundException;
import com.vd.restaurant.commission.calculator.repositories.MenuItemRepository;

@Service
public class MenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    public MenuItem addMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    public Optional<MenuItem> getMenuItemById(Long id) {
        return menuItemRepository.findById(id);
    }
    
    public boolean existsById(Long id) {
		return menuItemRepository.existsById(id);
	}

    public void deleteMenuItem(Long id) {
        if (!existsById(id)) {
            throw new MenuItemNotFoundException("Menu item with id " + id + " not found");
        }
        menuItemRepository.deleteById(id);
    }
    
    public MenuItem updateMenuItemStock(Long id, int quantity) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new MenuItemNotFoundException("Menu item with id " + id + " not found"));
        if (menuItem.getStock() + quantity < 0) {
            throw new InvalidStockException("Insufficient stock for menu item with id " + id);
        }
        menuItem.setStock(menuItem.getStock() + quantity);
        return menuItemRepository.save(menuItem);
    }
}