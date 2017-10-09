package com.store.repository;

import org.springframework.data.repository.CrudRepository;

import com.store.objects.domain.Bill;

public interface BillRepository extends CrudRepository<Bill, Long> {

}
