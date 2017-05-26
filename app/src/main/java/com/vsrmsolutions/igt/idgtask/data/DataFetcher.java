package com.vsrmsolutions.igt.idgtask.data;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by murthyvelamakanni on 23/05/2017.
 */

public class DataFetcher extends AsyncTask<String, Void, String> {
	DataFetcherResultCallback dataFetcherResultCallback;
	public DataFetcher(DataFetcherResultCallback dataFetcherResultCallback){
		this.dataFetcherResultCallback = dataFetcherResultCallback;
	}

	@Override
	protected String doInBackground(String... strings) {
		String urlString = strings[0];
		BufferedReader reader = null;
		try {
			URL url = new URL(urlString);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuffer buffer = new StringBuffer();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1) {
				buffer.append(chars, 0, read);
			}

			return buffer.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			dataFetcherResultCallback.onError();
		} catch (IOException e) {
			e.printStackTrace();
			dataFetcherResultCallback.onError();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return null;
	}

	@Override
	protected void onPostExecute(String data) {
		dataFetcherResultCallback.onResult(data);
	}
}
