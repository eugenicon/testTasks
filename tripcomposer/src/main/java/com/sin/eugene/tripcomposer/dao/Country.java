package com.sin.eugene.tripcomposer.dao;

import java.util.Arrays;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;

@Entity
@Table(name="Country")
public class Country {
	@Id
	@Column(length=3)
	private String countryISOCode;
	
	private String countryName;
	
	@ElementCollection
	@JoinTable(name="City", 
		joinColumns=@JoinColumn(name="countryISOCode")
	)
	private Set<City> cities;
	
	public Country() {
		super();
	}
	
	public Country(String countryName, String countryISOCode) {
		super();
		this.countryName = countryName;
		this.countryISOCode = countryISOCode;
	}
	
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCountryISOCode() {
		return countryISOCode;
	}
	public void setCountryISOCode(String countryISOCode) {
		this.countryISOCode = countryISOCode;
	}
	public Set<City> getCities() {
		return cities;
	}
	public void setCities(Set<City> cities) {
		this.cities = cities;
	}
	public String citiesToString() {
		return Arrays.toString(cities.toArray());
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName()
				+ ":" + countryName
				+ ":" + countryISOCode
				+ (cities == null ? "" : ":" + cities.size() + " cities") ;
	}
	
	public void onCreate() {
		System.out.println("Country created: " + this);
	}
	
	public void onDestroy() {
		System.out.println("Country DESTROYED: " + this);
	}
}
