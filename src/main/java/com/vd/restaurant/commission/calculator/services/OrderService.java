package com.vd.restaurant.commission.calculator.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vd.restaurant.commission.calculator.entities.Order;
import com.vd.restaurant.commission.calculator.entities.OrderItem;
import com.vd.restaurant.commission.calculator.exceptions.OrderNotFoundException;
import com.vd.restaurant.commission.calculator.repositories.OrderRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private MenuItemService menuItemService;

	@Transactional
	public Order createOrder(Order order) {
		for (OrderItem item : order.getItems()) {
			menuItemService.updateMenuItemStock(item.getMenuItem().getId(), -item.getQuantity());
		}
		return orderRepository.save(order);
	}

	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	public Optional<Order> getOrderById(Long id) {
		return orderRepository.findById(id);
	}

	@Transactional
	public void deleteOrder(Long id) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException("Order with id " + id + " not found"));
		for (OrderItem item : order.getItems()) {
			menuItemService.updateMenuItemStock(item.getMenuItem().getId(), item.getQuantity());
		}
		orderRepository.deleteById(id);
	}

	@Transactional
	public Order addOrderItem(Long orderId, OrderItem orderItem) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException("Order with id " + orderId + " not found"));
		menuItemService.updateMenuItemStock(orderItem.getMenuItem().getId(), -orderItem.getQuantity());
		order.getItems().add(orderItem);
		return orderRepository.save(order);
	}

	@Transactional
	public Order removeOrderItem(Long orderId, Long orderItemId) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException("Order with id " + orderId + " not found"));
		OrderItem orderItem = order.getItems().stream().filter(item -> item.getId().equals(orderItemId)).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Order item with id " + orderItemId + " not found"));
		menuItemService.updateMenuItemStock(orderItem.getMenuItem().getId(), orderItem.getQuantity());
		order.getItems().remove(orderItem);
		return orderRepository.save(order);
	}

}
