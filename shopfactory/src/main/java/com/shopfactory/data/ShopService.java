package com.shopfactory.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.shopfactory.domain.Category;
import com.shopfactory.domain.Product;
import com.shopfactory.domain.ProductStatus;
import com.shopfactory.domain.Shop;

public class ShopService {
	
	private ShopDAO shopDAO;
	
	public ShopService(ShopDAO shopDAO) {
		this.shopDAO = shopDAO;
	}
	
	public List<Product> createProducts(List<Category> categories) {
		List<Product> products = new ArrayList<>();
		categories.forEach(category -> {
			for(int i=0; i<4; i++){
				Product product = new Product();
				product.setTitle(String.format("Prod%s%s", category.getId(), i));
				product.setStatus(ProductStatus.AVAILABLE);
				product.setPrice(42);
				product.setIdCategory(category.getId());
				shopDAO.addProduct(product);
				products.add(product);
			}
		});
		return products;
	}
	
	public void setProductsAbsent(List<Product> products) {
		if (!products.isEmpty()) {
			int categoryId = products.get(0).getIdCategory();
			List<Product> collected = products.stream()
				.filter(p -> p.getIdCategory() == categoryId)
				.collect(Collectors.toList());
			shopDAO.setProductStatus(ProductStatus.ABSENT, collected);
		}
	}
	
	public void setProductsExpected(List<Product> products) {
		List<Product> collected = products.stream()
				.filter(p -> p.getStatus() == ProductStatus.AVAILABLE)
				.collect(Collectors.toList());
		shopDAO.setProductStatus(ProductStatus.EXPECTED, collected.subList(0, collected.size()/2));		
	}
	
	public void modifyPrice(List<Product> products, double modifier) {
		List<Product> collected = products.stream()
				.filter(p -> p.getStatus() == ProductStatus.AVAILABLE)
				.collect(Collectors.toList());
		shopDAO.modifyPrice(collected, modifier);
	}

	public List<Shop> getShops() {
		return shopDAO.getShops();
	}

	public List<Category> getCategories(Shop shop) {
		return shopDAO.getCategories(shop);
	}

}
