package com.vsrmsolutions.igt.idgtask.data;

/**
 * Created by murthyvelamakanni on 23/05/2017.
 */

public interface DataFetcherResultCallback {
	void onResult(String data);
	void onError();
}
