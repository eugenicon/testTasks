package com.shopfactory.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.shopfactory.domain.Category;
import com.shopfactory.domain.Product;
import com.shopfactory.domain.ProductStatus;
import com.shopfactory.domain.Shop;

public class ShopDAO {
	private Connection connection;

	public ShopDAO(Connection connection) {
		this.connection = connection;
	}
	
	private Connection getConnection() {
		return this.connection;
	}

	public List<Shop> getShops() {
		List<Shop> shops = new ArrayList<>();
		String query = "select id, name from shop";
		
		try(Statement statement = getConnection().createStatement();){
			ResultSet resultSet = statement.executeQuery(query);			
			while (resultSet.next()) {
				Shop shop = Shop.getInstance(resultSet.getInt("id"));
				shop.setName(resultSet.getString("name"));
				shops.add(shop);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
				
		return shops;
	}

	public void addProduct(Product product) {
		String query = "insert into product (title, status, id_category, price) values (?, ?, ?, ?)"
				+ " on duplicate key update price=?";

		try(PreparedStatement statement = getConnection().prepareStatement(query);) {
			statement.setString(1, product.getTitle());
			statement.setString(2, product.getStatus().toString());
			statement.setInt(3, product.getIdCategory());
			statement.setDouble(4, product.getPrice());
			statement.setDouble(5, product.getPrice());
			statement.executeUpdate();
			
			try(PreparedStatement updatedStatement = getConnection()
					.prepareStatement("select id, title, price, status, id_category from product where title = ?");) {
				updatedStatement.setString(1, product.getTitle());
				ResultSet resultSet = updatedStatement.executeQuery();
	            if (resultSet.next()) {
	            	readProduct(resultSet, product);
	            }
	        }
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setProductStatus(ProductStatus status, List<Product> products) {
		if (products.isEmpty()) {
			return;
		}
		String query = String.format("update product set status = ? where id in (%s)", getProductsId(products));

		try(PreparedStatement statement = getConnection().prepareStatement(query);) {
			statement.setString(1, status.toString());
			statement.executeUpdate();
			products.forEach(p->p.setStatus(status));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private String getProductsId(List<Product> products){
		StringBuilder builder = new StringBuilder();
		products.forEach(p -> builder.append(",").append(p.getId()));
		return builder.substring(1);
	}
	
	public List<Category> getCategories(Shop shop) {
		List<Category> categories = new ArrayList<>();
		String query = "select id, name from category where id_shop = ?";
		
		try(PreparedStatement statement = getConnection().prepareStatement(query);){
			statement.setInt(1, shop.getId());
			ResultSet resultSet = statement.executeQuery();	
			while (resultSet.next()) {
				categories.add(readCategory(resultSet));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return categories;
	}

	private Category readCategory(ResultSet resultSet) throws SQLException {
		Category cat = new Category();
		cat.setId(resultSet.getInt("id"));
		cat.setName(resultSet.getString("name"));
		return cat;
	}
	
	public List<Product> getProducts(Shop shop) {
		List<Product> products = new ArrayList<>();
		String query = "select id, title, price, status, id_category from product where "
				+ " id_category in (select id from category where id_shop = ?)";
		
		try(PreparedStatement statement = getConnection().prepareStatement(query);){
			statement.setInt(1, shop.getId());
			ResultSet resultSet = statement.executeQuery();	
			while (resultSet.next()) {
				products.add(readProduct(resultSet));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return products;
	}
	
	public List<Product> getProducts(Shop shop, ProductStatus status) {
		List<Product> products = new ArrayList<>();
		String query = "select id, title, price, status, id_category from product where status = ? "
				+ "and id_category in (select id from category where id_shop = ?)";
		
		try(PreparedStatement statement = getConnection().prepareStatement(query);){
			statement.setString(1, status.toString());
			statement.setInt(2, shop.getId());
			ResultSet resultSet = statement.executeQuery();	
			while (resultSet.next()) {
				products.add(readProduct(resultSet));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return products;
	}	

	private Product readProduct(ResultSet resultSet) throws SQLException {
		Product prod = new Product();
		readProduct(resultSet, prod);
		return prod;
	}

	private void readProduct(ResultSet resultSet, Product prod) throws SQLException {
		prod.setId(resultSet.getInt("id"));
		prod.setTitle(resultSet.getString("title"));
		prod.setStatus(ProductStatus.valueOf(resultSet.getString("status")));
		prod.setPrice(resultSet.getDouble("price"));
	}
	
	public void modifyPrice(List<Product> products, double modifier) {
		if (products.isEmpty()) {
			return;
		}
		String query = String.format("update product set price = price*? where id in (%s)", getProductsId(products));
		
		try(PreparedStatement statement = getConnection().prepareStatement(query);){
			statement.setDouble(1, modifier);
			statement.executeUpdate();
			products.forEach(p->p.setPrice(p.getPrice()*modifier));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
