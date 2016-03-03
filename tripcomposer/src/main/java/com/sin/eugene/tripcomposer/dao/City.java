package com.sin.eugene.tripcomposer.dao;

import javax.persistence.Embeddable;

@Embeddable
public class City {
	private String cityName;
	
	public City() {
		super();
	}
	
	public City(String cityName) {
		super();
		this.cityName = cityName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ":" + cityName;
	}
	
	
}
