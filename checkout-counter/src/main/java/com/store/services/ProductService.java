package com.store.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.exceptions.CustomException;
import com.store.objects.domain.OrderItem;
import com.store.objects.domain.Product;
import com.store.objects.info.ProductInfo;
import com.store.repository.OrderItemRepository;
import com.store.repository.ProductRepository;

/**
 * @author iamsagarvarma
 * @since 08-oct-2017
 *
 */

@Service
public class ProductService {

	/**
	 * product repository for product operations;
	 */
	@Autowired
	private ProductRepository productRepo;
	
	/**
	 * Order items repository for order items
	 */
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	/**
	 * @param productInfo
	 * @return
	 */
	public Product createProduct(ProductInfo productInfo) {
		verifyIfProductExists(productInfo.getBarCodeId());
		Product product = new Product();
		product.setBarCodeId(productInfo.getBarCodeId());
		product.setName(productInfo.getName());
		product.setProductCategory(productInfo.getProductCategory());
		product.setRate(productInfo.getRate());

		product = productRepo.save(product);
		
		return product;

	}
	
	/**
	 * @param id
	 */
	public void deleteProduct(Long id) {
		verifyProductExists(id);
		verifyLineItemExists(id);
		productRepo.delete(id);
	}

	/**
	 * @param id
	 */
	private void verifyLineItemExists(Long id) {
		List<OrderItem> orderItems = orderItemRepository.findByProduct_id(id);
		if (null!= orderItems && !orderItems.isEmpty()) {
			throw new CustomException("Product -  " + id + " is associated with bills. Cannot be deleted.");
		}
	}
	
	/**
	 * @return
	 */
	public Iterable<Product> getAllProducts() {
		Iterable<Product> products = productRepo.findAll();
		return products;
	}

	/**
	 * @param id
	 * @return
	 */
	public Product getProductById(Long id) {
		verifyProductExists(id);
		return productRepo.findOne(id);
	}

	/**
	 * @param productInfo
	 * @param id
	 * @return
	 */
	public Product updateProduct(ProductInfo productInfo, Long id) {
		verifyProductExists(id);
		Product product = productRepo.findOne(id);
		product.setBarCodeId(productInfo.getBarCodeId());
		product.setName(productInfo.getName());
		product.setProductCategory(productInfo.getProductCategory());
		product.setRate(productInfo.getRate());
		Product p = productRepo.save(product);
		
		return p;
	}

	
	/**
	 * @param barCodeId
	 */
	private void verifyIfProductExists(String barCodeId) {
		List<Product> productsByBarCodeID = productRepo.findByBarCodeId(barCodeId);
		if (null != productsByBarCodeID && !productsByBarCodeID.isEmpty()) {
			throw new CustomException(
					"Problem with input data: BarCode ID  " + barCodeId + " already exists in Product Master");
		}
	}

	/**
	 * @param id
	 */
	private void verifyProductExists(Long id) {
		Product product = productRepo.findOne(id);
		if (product == null) {
			throw new CustomException("Product with id " + id + " not found");
		}
	}
}
