package com.store.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.exceptions.CustomException;
import com.store.objects.domain.Bill;
import com.store.objects.domain.OrderItem;
import com.store.objects.domain.Product;
import com.store.objects.info.BillUpdateInfo;
import com.store.objects.info.ProductInfoForBill;
import com.store.repository.BillRepository;
import com.store.repository.OrderItemRepository;
import com.store.repository.ProductRepository;
import com.store.utils.ProductCategory;

/**
 * @author iamsagarvarma
 * @since 08-oct-2017
 *
 */

@Service
public class BillingService {

	@Autowired
	private BillRepository billRepo;

	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private ProductRepository productRepo;
	
	public Bill createBill(Bill bill) {
		Bill bill1 = billRepo.save(bill);
		return bill1;

	}
	
	public Bill updateBill(BillUpdateInfo billUpdateInfo, Long billId) {

		if (null == billUpdateInfo) {
			throw new CustomException("There is no information to be updated for id " + billId);
		}
		verifyBillExists(billId);

		if (null != billUpdateInfo.getProductsToBeAdded()) {
			List<ProductInfoForBill> prodToBeAdded = billUpdateInfo.getProductsToBeAdded();
			Iterator<ProductInfoForBill> prodToBeAddedIter = prodToBeAdded.iterator();
			while (prodToBeAddedIter.hasNext()) {
				ProductInfoForBill pInfo = prodToBeAddedIter.next();
				addProductToBill(billId, pInfo.getBarCodeId(), pInfo.getQuantity());
			}
		}

		if (null != billUpdateInfo.getProductsToBeRemoved()) {
			List<ProductInfoForBill> prodToBeRemoved = billUpdateInfo.getProductsToBeRemoved();
			Iterator<ProductInfoForBill> prodToBeRemovedIter = prodToBeRemoved.iterator();
			while (prodToBeRemovedIter.hasNext()) {
				ProductInfoForBill pInfo = prodToBeRemovedIter.next();
				removeProductFromBill(billId, pInfo.getBarCodeId());
			}
		}

		Bill bill = billRepo.findOne(billId);
		bill.setBillStatus(billUpdateInfo.getStatus());
		computeTotalValues(bill);
		return bill;
	}
	
	private Bill removeProductFromBill(Long billId, String barCodeId) {
		Bill o1 = billRepo.findOne(billId);
		List<OrderItem> currentLineItems = o1.getOrderItems();
		// check if the product exists in product master
		verifyIfProductExists(barCodeId);

		if (currentLineItems != null && !currentLineItems.isEmpty()) {
			OrderItem lineItem = getOrderItemWithBarCodeId(barCodeId, currentLineItems);
			// check if current list of line items have this product.
			if (null == lineItem) {
				throw new CustomException(
						"Problem with input data: Product does not exist in current list of products. Cannot remove product with BarCode ID "
								+ barCodeId);

			}
			currentLineItems.remove(lineItem);
			o1.setOrderItems(currentLineItems);
			billRepo.save(o1);
			// lineItemRepo.delete(lineItem); //delete if it exists
		} else {
			throw new CustomException(
					"Problem with input data: There are no line items currently in the Bill. Cannot remove product with BarCode ID "
							+ barCodeId);
		}
		billRepo.save(o1);
		return o1;
	}
	
	public Bill getBillById(Long id) {
		verifyBillExists(id);
		return billRepo.findOne(id);
	}
	
	public void deleteBill(Long id) {
		verifyBillExists(id);
		billRepo.delete(id);
	}

	public Iterable<Bill> getAllBills() {
		Iterable<Bill> bill = billRepo.findAll();
		return bill;
	}
	
	/**
	 * This method adds product to the bill
	 * 
	 * @param billId
	 * @param barCodeId
	 * @param quantity
	 * @return
	 */
	private Bill addProductToBill(Long billId, String barCodeId, int quantity) {
		Bill o1 = billRepo.findOne(billId);
		Product selectedProduct1 = checkIfProductExistsInStore(barCodeId);

		// create line item for a product
		OrderItem l1 = new OrderItem(selectedProduct1, quantity);
		orderItemRepository.save(l1);

		// add lineitem to bill.
		List<OrderItem> currentLineItems = o1.getOrderItems();
		if (currentLineItems != null) { // There are lineItems in the bill
										// already.
			OrderItem existingLi = getOrderItemWithBarCodeId(barCodeId, currentLineItems);
			if (existingLi == null) {

				o1.getOrderItems().add(l1); // there is no line item with
											// existing product
			} else {
				long newQty = existingLi.getQuantity() + quantity;

				existingLi.setQuantity(newQty); // increment the quantity of the
												// product if it already exists
												// in the Bill.
			}

		} else {
			currentLineItems = new ArrayList<>();
			currentLineItems.add(l1);
			o1.setOrderItems(currentLineItems);
		}
		billRepo.save(o1);
		return o1;
	}
	
		/**
		 * Get the product from order items list
		 * 
		 * @param barCodeId
		 * @param currentOrderItems
		 * @return
		 */
		private OrderItem getOrderItemWithBarCodeId(String barCodeId, List<OrderItem> currentOrderItems) {
			for (int i = 0; i < currentOrderItems.size(); i++) {
				OrderItem li = currentOrderItems.get(i);
				if (barCodeId.equals(li.getProduct().getBarCodeId())) {
					// assumes there will only be one item per product. Save method
					// to ensure that there are no duplicates.
					return li;
				}
			}
			return null;
		}
		
		/**
		 * Method to check if product is present in the store catalog
		 * 
		 * @param barCodeId
		 * @return
		 */
		private Product checkIfProductExistsInStore(String barCodeId) {
			List<Product> productsByBarCodeID = productRepo.findByBarCodeId(barCodeId);
			if (null == productsByBarCodeID || productsByBarCodeID.isEmpty()) {
				throw new CustomException(
						"Problem with input data: BarCode ID " + barCodeId + " does not exist in Product Master");
			}
			return productsByBarCodeID.get(0);
		}
		
		private double computeValueForItem(long quantity, ProductCategory productCategory, double rate) {
			double saleValue = 0;
			if (productCategory.equals(ProductCategory.A)) {
				saleValue = quantity * rate * 1.1; // 10% levy

			} else if (productCategory.equals(ProductCategory.B)) {
				saleValue = quantity * rate * 1.2; // 10% levy

			} else if (productCategory.equals(ProductCategory.C)) {
				saleValue = quantity * rate;
			}
			return saleValue;
		}
		
		private void computeTotalValues(Bill bill) {

			int noOfItems = 0;
			double totalValue = 0;
			double totalCost = 0;

			if (null != bill.getOrderItems()) {
				List<OrderItem> lineItems = bill.getOrderItems();
				Iterator<OrderItem> lineItemsIter = lineItems.iterator();
				while (lineItemsIter.hasNext()) {
					OrderItem li = lineItemsIter.next();
					double saleValue = computeValueForItem(li.getQuantity(), li.getProduct().getProductCategory(),
							li.getProduct().getRate());
					totalValue += saleValue;
					totalCost += li.getQuantity() * li.getProduct().getRate();
					noOfItems++;
				}
			}
			bill.setNoOfItems(noOfItems);
			bill.setTotalValue(totalValue);
			bill.setTotalCost(totalCost);
			bill.setTotalTax(totalValue - totalCost);
			
//			return bill;
			billRepo.save(bill);
		}
		
		private void verifyBillExists(Long id) {
			Bill bill = billRepo.findOne(id);
			if (bill == null) {
				throw new CustomException("Bill with id " + id + " not found");
			}
		}
		
		private Product verifyIfProductExists(String barCodeId) {
			List<Product> productsByBarCodeID = productRepo.findByBarCodeId(barCodeId);
			if (null == productsByBarCodeID || productsByBarCodeID.isEmpty()) {
				throw new CustomException(
						"Problem with input data: BarCode ID " + barCodeId + " does not exist in Product Master");
			} 
			return productsByBarCodeID.get(0);
		}
		
}
