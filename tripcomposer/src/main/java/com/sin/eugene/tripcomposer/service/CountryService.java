package com.sin.eugene.tripcomposer.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sin.eugene.tripcomposer.dao.Country;
import com.sin.eugene.tripcomposer.dao.CountryDAO;

public class CountryService {

	private CountryDAO countryDao;
	private String serviceURL;
	private String serviceKey;
	private String serviceEcho;

	public String getServiceKey() {
		return serviceKey;
	}

	public void setServiceKey(String serviceKey) {
		this.serviceKey = serviceKey;
	}

	public String getServiceEcho() {
		return serviceEcho;
	}

	public void setServiceEcho(String serviceEcho) {
		this.serviceEcho = serviceEcho;
	}

	public CountryDAO getCountryDao() {
		return countryDao;
	}

	public void setCountryDao(CountryDAO countryDao) {
		this.countryDao = countryDao;
	}

	public String getServiceURL() {
		return serviceURL;
	}

	public void setServiceURL(String serviceURL) {
		this.serviceURL = serviceURL;
	}

	public void add(Country country) {
		getCountryDao().insert(country);
	}

	public void addAll(List<Country> country) {
		getCountryDao().insertAll(country);
	}

	public List<Country> selectAllCountries() {
		return getCountryDao().selectAll();
	}

	private String requestJsonCountriesFromRemote() throws IOException{

		StringBuilder result = new StringBuilder();

		JsonObject jsonData = new JsonObject();
		jsonData.addProperty("key", serviceKey);
		jsonData.addProperty("echo", serviceEcho);

		HttpPost post = new HttpPost(serviceURL);
		post.setHeader("Content-Type", "application/json");

		post.setEntity(new StringEntity(jsonData.toString()));
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = client.execute(post);

		if (response.getStatusLine().getStatusCode() == 200) {

			@SuppressWarnings("resource")
			Scanner in = new Scanner(response.getEntity().getContent());
			while (in.hasNext()) {
				result.append(in.next());
			}

		} else {
			System.out.println(response);
		}

		return result.toString();

	}

	private List<Country> parseCountriesFromJson(String jsonCountries) {

		List<Country> countries = new ArrayList<>();

		JsonObject jResponse = new JsonParser().parse(jsonCountries).getAsJsonObject();
		Gson g = new Gson();

		for (JsonElement countryData : jResponse.get("countries").getAsJsonArray()) {
			countries.add(g.fromJson(countryData.toString(), Country.class));
		}

		return countries;
	}

	public void addAllFromRemote() {
		String jsonResponse;
		try {
			jsonResponse = requestJsonCountriesFromRemote();
			List<Country> countries = parseCountriesFromJson(jsonResponse);
			addAll(countries);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
