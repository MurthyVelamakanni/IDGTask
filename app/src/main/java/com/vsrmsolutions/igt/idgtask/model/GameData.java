package com.vsrmsolutions.igt.idgtask.model;

import java.util.List;

/**
 * Created by murthyvelamakanni on 23/05/2017.
 */

public class GameData {
	private String response;
	private String currency;
	private List<GameDetail> data;

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public List<GameDetail> getData() {
		return data;
	}

	public void setData(List<GameDetail> data) {
		this.data = data;
	}
}
