package com.store.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.store.objects.domain.Product;
import com.store.objects.info.ProductInfo;
import com.store.services.ProductService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author iamsagarvarma
 * @since 08-oct-2017
 *
 */

@RestController
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@ApiOperation(value = "View list of available products", response = Iterable.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "Bad Credentials") })
	@RequestMapping(value = "/products", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Product>> getAllProducts() {
		return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Create a new Product", response = String.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "id <id> created"),
			@ApiResponse(code = 401, message = "Bad Credentials") })
	@RequestMapping(value = "/products", method = RequestMethod.POST)
	public ResponseEntity<Product> createProduct(
			@ApiParam(value = "Data for the new product", required = true) @Valid @RequestBody ProductInfo productInfo) {
		Product product = productService.createProduct(productInfo);

		return new ResponseEntity<>(product, HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "Delete existing Product", response = String.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "id <id> deleted"),
			@ApiResponse(code = 401, message = "Bad Credentials") })
	@RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
		return new ResponseEntity<>("{\"status\": \"success\"}", HttpStatus.OK);
	}
	
	@ApiOperation(value = "View a specific product", response = Product.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved product details"),
			@ApiResponse(code = 401, message = "Bad Credentials"),
			@ApiResponse(code = 404, message = "Produt does not exist") })
	@RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
	public ResponseEntity<Product> getProductById(
			@ApiParam(value = "id of a particular product", required = true) @PathVariable Long id) {
		return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Update existing Product", response = String.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "id <id> updated"),
			@ApiResponse(code = 401, message = "Bad Credentials") })

	@RequestMapping(value = "/products/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Product> updateProduct(@Valid @RequestBody ProductInfo productInfo, @PathVariable Long id) {
		Product prod = productService.updateProduct(productInfo, id);
		return new ResponseEntity<>(prod, HttpStatus.OK);
	}
}
