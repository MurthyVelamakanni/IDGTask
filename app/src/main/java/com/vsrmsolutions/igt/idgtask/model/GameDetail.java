package com.vsrmsolutions.igt.idgtask.model;

import java.util.Date;

/**
 * Created by murthyvelamakanni on 23/05/2017.
 */

public class GameDetail {
	private String name;
	private long jackpot;
	private Date date;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getJackpot() {
		return jackpot;
	}

	public void setJackpot(long jackpot) {
		this.jackpot = jackpot;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj.getClass() == this.getClass()){
			GameDetail detail = (GameDetail) obj;
			return detail.date.getTime() == date.getTime() && detail.jackpot == jackpot && detail.name.equals(name);
		}
		return false;
	}
}
