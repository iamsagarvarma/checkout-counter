package com.store.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.store.objects.domain.Bill;
import com.store.objects.info.BillUpdateInfo;
import com.store.services.BillingService;
import com.store.utils.BillStatus;

//import com.store.objects.domain.Bill;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author iamsagarvarma
 * @since 08-oct-2017
 *
 */
@Api(value = "onlinestore", description="Billing related operations")	
@RestController
public class BillingController {

	@Autowired
	private BillingService billService;
	
	@ApiOperation(produces = "application/json", value = "fetches all bills from the database")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved list") })
	@RequestMapping(value = "/bills", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Bill>> getAllBills() {
		return new ResponseEntity<>(billService.getAllBills(), HttpStatus.OK);
	}
	
	@ApiOperation(produces = "application/json", value = "Creates an Bill and returns an id.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Bill details") })
	@RequestMapping(value = "/bills", method = RequestMethod.POST)
	public ResponseEntity<Bill> createBill() {
		Bill bill = billService.createBill(new Bill(0.0, 0, BillStatus.IN_PROGRESS));
		return new ResponseEntity<>(bill, HttpStatus.CREATED);
	}
	
	@ApiOperation(produces = "application/json", value = "Deletes Bill")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Status of request"),
			@ApiResponse(code = 404, message = "Bill does not exist") })
	@RequestMapping(value = "/bills/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteBill(@PathVariable Long id) {
		billService.deleteBill(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@ApiOperation(produces = "application/json", value = "fetches a particular bill details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved Bill details"),
			@ApiResponse(code = 404, message = "Bill Not Found") })
	@RequestMapping(value = "/bills/{id}", method = RequestMethod.GET)
	public ResponseEntity<Bill> getBillById(@PathVariable Long id) {
		return new ResponseEntity<>(billService.getBillById(id), HttpStatus.OK);
	}
	
	@ApiOperation(produces = "application/json", value = "Add or Remove products from the Bill")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Bill details"),
			@ApiResponse(code = 404, message = "Data validation error") })
	@RequestMapping(value = "/bills/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Bill> updateBill(@RequestBody BillUpdateInfo billUpdateInfo, @PathVariable Long id) {
		Bill updated = billService.updateBill(billUpdateInfo, id);
		return new ResponseEntity<>(updated, HttpStatus.OK);
	}
	
}
