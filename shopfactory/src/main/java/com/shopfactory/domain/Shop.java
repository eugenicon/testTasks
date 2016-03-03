package com.shopfactory.domain;

import java.util.Hashtable;
import java.util.Map;

public class Shop {
	private int id;
	private String name;
	private static Map<Integer, Shop> instances = new Hashtable<>();
	
	private Shop(int id) {
		this.id = id;
	}
	
	public static Shop getInstance(int id) {
		Shop shop = instances.get(id);
		if (shop == null) {
			shop = new Shop(id);
			instances.put(id, shop);
		}
		return shop;
	}
	
	public static void clearInstances() {
		instances.clear();
	}	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Shop other = (Shop) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("Shop [id=%s, name=%s]", id, name);
	}

}
