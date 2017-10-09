package com.store.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.store.objects.domain.OrderItem;

public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {
	
	public List<OrderItem> findByProduct_id(long prodId);

}
