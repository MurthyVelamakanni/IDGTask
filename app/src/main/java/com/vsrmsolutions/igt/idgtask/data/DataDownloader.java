package com.vsrmsolutions.igt.idgtask.data;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vsrmsolutions.igt.idgtask.model.GameData;
import com.vsrmsolutions.igt.idgtask.model.PlayerInfo;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by murthyvelamakanni on 25/05/2017.
 */

public class DataDownloader {
	public static final String PLAYER_INFO = "PlayerInfo";
	public static final String GAME_DATA = "GameData";
	public static final String PLAYER_AVATAR = "PlayerAvatar";
	public static final String TIME_STORED = "TimeStored";
	private GameData gameData;
	private PlayerInfo playerInfo;
	private Bitmap playerAvatar;

	public interface DataDownloaderCallback {
		void onSuccess(PlayerInfo playerInfo, Bitmap playerAvatar, GameData gameData);

		void onError();
	}

	public void downloadData(final SharedPreferences sharedPref, final DataDownloaderCallback dataDownloaderCallback) {
		long timeStored = sharedPref.getLong(TIME_STORED, 0);
		if(System.currentTimeMillis() > timeStored + 3600000) {

			final CountDownLatch latch = new CountDownLatch(2);
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						latch.await();
						if (playerInfo != null && playerAvatar != null && gameData != null) {
							dataDownloaderCallback.onSuccess(playerInfo, playerAvatar, gameData);
							SharedPreferences.Editor editor = sharedPref.edit();
							editor.putLong(TIME_STORED, System.currentTimeMillis());
							editor.putString(PLAYER_INFO, new Gson().toJson(playerInfo));
							editor.putString(GAME_DATA, new Gson().toJson(gameData));
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							playerAvatar.compress(Bitmap.CompressFormat.PNG, 100, baos);
							byte[] b = baos.toByteArray();
							String encoded = Base64.encodeToString(b, Base64.DEFAULT);
							editor.putString(PLAYER_AVATAR, encoded);
							editor.commit();
						} else {
							dataDownloaderCallback.onError();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();

			DataFetcher dataFetcher = new DataFetcher(new DataFetcherResultCallback() {
				private String cleanDateFormat(String json) { // takes in a string of JSON
					Pattern regex = Pattern.compile("\\d\\d:\\d\\d:\\d\\d[-\\+]\\d\\d:\\d\\d");
					Matcher regexMatcher = regex.matcher(json);
					StringBuffer buff = new StringBuffer();
					while (regexMatcher.find()) {
						regexMatcher.appendReplacement(buff, getSubOfMatch(regexMatcher));
					}
					regexMatcher.appendTail(buff);
					return buff.toString();
				}

				//then pull out the colon.
				private String getSubOfMatch(Matcher matcher) {
					StringBuilder sb = new StringBuilder(matcher.group(0));
					sb.deleteCharAt(sb.length() - 3);
					return sb.toString();
				}

				@Override
				public void onResult(String data) {
					data = cleanDateFormat(data);
					Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
					gameData = gson.fromJson(data, GameData.class);
					latch.countDown();
				}

				@Override
				public void onError() {
					latch.countDown();
				}
			});
			dataFetcher.execute("https://dl.dropboxusercontent.com/s/2ewt6r22zo4qwgx/gameData.json");

			DataFetcher profileFetcher = new DataFetcher(new DataFetcherResultCallback() {
				@Override
				public void onResult(String data) {
					Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy'T'HH:mm").create();
					playerInfo = gson.fromJson(data, PlayerInfo.class);
					new AsyncTask<Void, Void, Bitmap>() {
						@Override
						protected Bitmap doInBackground(Void... voids) {
							try {
								URL urlConnection = new URL(playerInfo.getAvatarLink());
								HttpURLConnection connection = (HttpURLConnection) urlConnection
										.openConnection();
								connection.setDoInput(true);
								connection.connect();
								InputStream input = connection.getInputStream();
								Bitmap bitmap = BitmapFactory.decodeStream(input);
								return bitmap;
							} catch (Exception e) {
								e.printStackTrace();
							}
							return null;
						}

						@Override
						protected void onPostExecute(Bitmap bitmap) {
							super.onPostExecute(bitmap);
							playerAvatar = bitmap;
							latch.countDown();
						}
					}.execute();
				}

				@Override
				public void onError() {
					latch.countDown();
				}
			});
			profileFetcher.execute("https://dl.dropboxusercontent.com/s/5zz3hibrxpspoe5/playerInfo.json");
		} else {
			playerInfo = new Gson().fromJson(sharedPref.getString(PLAYER_INFO, ""), PlayerInfo.class);
			gameData = new Gson().fromJson(sharedPref.getString(GAME_DATA, ""), GameData.class);
			String encoded = sharedPref.getString(PLAYER_AVATAR, "");
			byte[] imageAsBytes = Base64.decode(encoded.getBytes(), Base64.DEFAULT);
			playerAvatar = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
			dataDownloaderCallback.onSuccess(playerInfo, playerAvatar, gameData);
		}
	}
}

