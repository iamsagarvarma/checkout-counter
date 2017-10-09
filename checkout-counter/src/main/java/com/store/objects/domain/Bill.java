package com.store.objects.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.store.utils.BillStatus;

/**
 * @author iamsagarvarma
 * @since 08-oct-2017
 *
 */

@Entity
@Table(name = "BILLS")

public class Bill {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private int noOfItems;
	private double totalCost;

	private double totalTax;

	private double totalValue;

	@NotNull
	@Enumerated(EnumType.STRING)
	private BillStatus billStatus;

	@OneToMany(cascade=CascadeType.REMOVE,fetch = FetchType.EAGER)
	private List<OrderItem> orderItems;

	public Bill() {
		super();
	}

	public Bill(double totalValue, int noOfItems, BillStatus billStatus) {
		super();
		this.totalValue = totalValue;
		this.noOfItems = noOfItems;
		this.billStatus = billStatus;
	}

	public Bill(List<OrderItem> orderItems) {
		super();
		this.orderItems = orderItems;
	}

	public BillStatus getBillStatus() {
		return billStatus;
	}

	public long getId() {
		return id;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public int getNoOfItems() {
		return noOfItems;
	}

	public double getTotalCost() {
		return totalCost;
	}

	public double getTotalTax() {
		return totalTax;
	}

	public double getTotalValue() {
		return totalValue;
	}

	public void setBillStatus(BillStatus billStatus) {
		this.billStatus = billStatus;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setOrderItems(List<OrderItem> lineItems) {
		this.orderItems = lineItems;
	}

	public void setNoOfItems(int noOfItems) {
		this.noOfItems = noOfItems;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public void setTotalTax(double totalTax) {
		this.totalTax = totalTax;
	}

	public void setTotalValue(double totalValue) {
		this.totalValue = totalValue;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
