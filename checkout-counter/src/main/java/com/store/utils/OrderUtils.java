//package com.store.utils;
//
//import java.util.Iterator;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.store.exceptions.CustomException;
//import com.store.objects.domain.Bill;
//import com.store.objects.domain.LineItem;
//import com.store.objects.domain.Product;
//import com.store.repository.ProductRepository;
//
//public class OrderUtils {
//	
//	@Autowired
//	private ProductRepository productRepo;
//	
//	public static Bill computeTotalValues(Bill bill) {
//
//		int noOfItems = 0;
//		double totalValue = 0;
//		double totalCost = 0;
//
//		if (null != bill.getLineItems()) {
//			List<LineItem> lineItems = bill.getLineItems();
//			Iterator<LineItem> lineItemsIter = lineItems.iterator();
//			while (lineItemsIter.hasNext()) {
//				LineItem li = lineItemsIter.next();
//				double saleValue = computeValueForItem(li.getQuantity(), li.getProduct().getProductCategory(),
//						li.getProduct().getRate());
//				totalValue += saleValue;
//				totalCost += li.getQuantity() * li.getProduct().getRate();
//				noOfItems++;
//			}
//		}
//		bill.setNoOfItems(noOfItems);
//		bill.setTotalValue(totalValue);
//		bill.setTotalCost(totalCost);
//		bill.setTotalTax(totalValue - totalCost);
//		
//		return bill;
////		billRepo.save(bill);
//	}
//	
//	private static double computeValueForItem(long quantity, ProductCategory productCategory, double rate) {
//		double saleValue = 0;
//		if (productCategory.equals(ProductCategory.A)) {
//			saleValue = quantity * rate * 1.1; // 10% levy
//
//		} else if (productCategory.equals(ProductCategory.B)) {
//			saleValue = quantity * rate * 1.2; // 10% levy
//
//		} else if (productCategory.equals(ProductCategory.C)) {
//			saleValue = quantity * rate;
//		}
//		return saleValue;
//	}
//
//	private Bill removeProductFromBill(Long billId, String barCodeId) {
//		Bill o1 = billRepo.findOne(billId);
//		List<LineItem> currentLineItems = o1.getLineItems();
//		// check if the product exists in product master
//		checkIfProductExistsInStore(barCodeId);
//
//		if (currentLineItems != null && !currentLineItems.isEmpty()) {
//			LineItem lineItem = getLineItemWithBarCodeId(barCodeId, currentLineItems);
//			// check if current list of line items have this product.
//			if (null == lineItem) {
//				throw new CustomException(
//						"Problem with input data: Product does not exist in current list of products. Cannot remove product with BarCode ID "
//								+ barCodeId);
//
//			}
//			currentLineItems.remove(lineItem);
//			o1.setLineItems(currentLineItems);
//			// lineItemRepo.delete(lineItem); //delete if it exists
//			
//			return o1;
//		} else {
//			throw new CustomException(
//					"Problem with input data: There are no line items currently in the Bill. Cannot remove product with BarCode ID "
//							+ barCodeId);
//		}
//		return o1;
//	}
//	
//}
