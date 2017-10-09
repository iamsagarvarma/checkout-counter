package com.store;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;

import com.store.objects.info.BillUpdateInfo;
import com.store.objects.info.ProductInfoForBill;
import com.store.utils.BillStatus;

@RunWith(SpringRunner.class)
@JsonTest
public class TestBillUpdate {

	@Autowired
	private JacksonTester<BillUpdateInfo> json;

	public BillUpdateInfo createFailTestBean() {
		BillUpdateInfo updateInfo = new BillUpdateInfo();
		updateInfo.setStatus(BillStatus.IN_PROGRESS);
		List<ProductInfoForBill> productsToBeAdded = new ArrayList<ProductInfoForBill>();
		productsToBeAdded.add(new ProductInfoForBill("ABC-abc-123", 20));
		productsToBeAdded.add(new ProductInfoForBill("ABC-abc-224", 30));
		productsToBeAdded.add(new ProductInfoForBill("ABC-abc-334", 10));

		List<ProductInfoForBill> productsToBeRemoved = new ArrayList<ProductInfoForBill>();
		productsToBeRemoved.add(new ProductInfoForBill("ABC-abc-1235", 2));
		productsToBeRemoved.add(new ProductInfoForBill("ABC-abc-1236", 3));
		productsToBeRemoved.add(new ProductInfoForBill("ABC-abc-1237", 1));

		updateInfo.setProductsToBeAdded(productsToBeAdded);
		updateInfo.setProductsToBeRemoved(productsToBeRemoved);
		return updateInfo;
	}

	@Test
	public void testFailSerialize() throws Exception {

		BillUpdateInfo updateInfo = createFailTestBean();

		assertThat(this.json.write(updateInfo)).isNotEqualTo("expected.json");

		System.out.println(this.json.write(updateInfo).toString());
	}
	
	@Test
	public void testPassSerialize() throws Exception {

		BillUpdateInfo updateInfo = createPassTestBean();

		System.out.println(this.json.write(updateInfo).toString());
		assertThat(this.json.write(updateInfo)).isEqualToJson("expected.json");

		System.out.println(this.json.write(updateInfo).toString());
	}
	
	public BillUpdateInfo createPassTestBean() {
		BillUpdateInfo updateInfo = new BillUpdateInfo();
		updateInfo.setStatus(BillStatus.IN_PROGRESS);
		List<ProductInfoForBill> productsToBeAdded = new ArrayList<ProductInfoForBill>();
		productsToBeAdded.add(new ProductInfoForBill("ABC-abc-1234", 20));
		productsToBeAdded.add(new ProductInfoForBill("ABC-abc-2234", 30));
		productsToBeAdded.add(new ProductInfoForBill("ABC-abc-3234", 10));

		List<ProductInfoForBill> productsToBeRemoved = new ArrayList<ProductInfoForBill>();
		productsToBeRemoved.add(new ProductInfoForBill("ABC-abc-1235", 2));
		productsToBeRemoved.add(new ProductInfoForBill("ABC-abc-1236", 3));
		productsToBeRemoved.add(new ProductInfoForBill("ABC-abc-1237", 1));

		updateInfo.setProductsToBeAdded(productsToBeAdded);
		updateInfo.setProductsToBeRemoved(productsToBeRemoved);
		updateInfo.setStatus(BillStatus.RELEASED);
		return updateInfo;
	}

}
