package com.vsrmsolutions.igt.idgtask;

import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vsrmsolutions.igt.idgtask.data.DataDownloader;
import com.vsrmsolutions.igt.idgtask.data.DataDownloader.DataDownloaderCallback;
import com.vsrmsolutions.igt.idgtask.data.DataFetcher;
import com.vsrmsolutions.igt.idgtask.data.DataFetcherResultCallback;
import com.vsrmsolutions.igt.idgtask.model.GameData;
import com.vsrmsolutions.igt.idgtask.model.GameDetail;
import com.vsrmsolutions.igt.idgtask.model.PlayerInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboSharedPreferences;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by murthyvelamakanni on 23/05/2017.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class DataFetcherTest {
	@Test
	public void fetchProfile() {
		DataFetcher dataFetcher = new DataFetcher(new DataFetcherResultCallback() {
			@Override
			public void onResult(String data) {
				assertEquals("Failed to fetch data", "{\n" +
						"  \"name\": \"PlayerName\",\n" +
						"  \"balance\": 987654,\n" +
						"  \"avatarLink\": \"https://dl.dropboxusercontent.com/s/8a1j70z1ik3y0q8/user_avatar.png\",\n" +
						"  \"lastLogindate\": \"04/05/2016T16:45\"\n" +
						"}", data);
				Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy'T'HH:mm").create();
				PlayerInfo playerInfo = gson.fromJson(data, PlayerInfo.class);
				PlayerInfo playerInfoExpected = new PlayerInfo();
				playerInfoExpected.setName("PlayerName");
				playerInfoExpected.setBalance(987654);
				playerInfoExpected.setAvatarLink("https://dl.dropboxusercontent.com/s/8a1j70z1ik3y0q8/user_avatar.png");
				//playerInfoExpected.setLastLogindate();
				assertEquals("Wrong name", playerInfoExpected.getName(), playerInfo.getName());
				assertEquals("Wrong balance", playerInfoExpected.getBalance(), playerInfo.getBalance(), 0);
				assertEquals("wrong avatar link", playerInfoExpected.getAvatarLink(), playerInfo.getAvatarLink());
				assertEquals("wrong date", "Wed May 04 16:45:00 BST 2016", playerInfo.getLastLogindate().toString());
			}

			@Override
			public void onError() {
				fail("Error fetching data");
			}
		});
		dataFetcher.execute("https://dl.dropboxusercontent.com/s/5zz3hibrxpspoe5/playerInfo.json");
		Robolectric.flushBackgroundThreadScheduler();
	}

	@Test
	public void fetchGameData() {
		DataFetcher dataFetcher = new DataFetcher(new DataFetcherResultCallback() {
			@Override
			public void onResult(String data) {
				assertEquals("Failed to fetch data", "{\n" +
						"\t\"response\": \"success\",\n" +
						"\t\"currency\" : \"GBP\",\n" +
						"\t\"data\" : [\n" +
						"\t\t{\n" +
						"\t\t\t\"name\": \"Game 1\",\n" +
						"\t\t\t\"jackpot\": 34000000,\n" +
						"\t\t\t\"date\": \"2015-01-25T20:20:30+01:00\"\n" +
						"\t\t},\n" +
						"\t\t{\n" +
						"\t\t\t\"name\": \"Game 2\",\n" +
						"\t\t\t\"jackpot\": 100000000,\n" +
						"\t\t\t\"date\": \"2015-02-16T08:40:30+01:00\"\n" +
						"\t\t},\n" +
						"\t\t{\n" +
						"\t\t\t\"name\": \"Game 3\",\n" +
						"\t\t\t\"jackpot\": 100000,\n" +
						"\t\t\t\"date\": \"2015-11-09T10:25:30+01:00\"\n" +
						"\t\t},\n" +
						"\t\t{\n" +
						"\t\t\t\"name\": \"Game 4\",\n" +
						"\t\t\t\"jackpot\": 45000000,\n" +
						"\t\t\t\"date\": \"2015-03-10T18:55:30+01:00\"\n" +
						"\t\t},\n" +
						"\t\t{\n" +
						"\t\t\t\"name\": \"Game 5\",\n" +
						"\t\t\t\"jackpot\": 60000000,\n" +
						"\t\t\t\"date\": \"2015-07-20T03:45:30+01:00\"\n" +
						"\t\t},\n" +
						"\t\t{\n" +
						"\t\t\t\"name\": \"Game 6\",\n" +
						"\t\t\t\"jackpot\": 95000000,\n" +
						"\t\t\t\"date\": \"2015-06-22T09:40:30+01:00\"\n" +
						"\t\t},\n" +
						"\t\t{\n" +
						"\t\t\t\"name\": \"Game 7\",\n" +
						"\t\t\t\"jackpot\": 100000000,\n" +
						"\t\t\t\"date\": \"2015-10-19T08:30:30+01:00\"\n" +
						"\t\t},\n" +
						"\t\t{\n" +
						"\t\t\t\"name\": \"Game 8\",\n" +
						"\t\t\t\"jackpot\": 12000,\n" +
						"\t\t\t\"date\": \"2015-12-06T07:20:30+01:00\"\n" +
						"\t\t}\n" +
						"\t]\n" +
						"}", data);
				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssX").create();
				GameData gameData = gson.fromJson(data, GameData.class);
				GameData gameDataExpected = new GameData();
				gameDataExpected.setResponse("success");
				gameDataExpected.setCurrency("GBP");
				assertEquals("didnt get success", gameDataExpected.getResponse(), gameData.getResponse());
				assertEquals("didnt get correct currency", gameDataExpected.getCurrency(), gameData.getCurrency());
				assertEquals("wrong number of game details", 8, gameData.getData().size());
				GameDetail gameDetail = new GameDetail();
				gameDetail.setName("Game 2");
				gameDetail.setJackpot(100000000);
				DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
				try {
					Date date = fmt.parse("2015-02-16T08:40:30+01:00");
					gameDetail.setDate(date);
					assertTrue("does not contain expected game details", gameData.getData().contains(gameDetail));
				} catch (ParseException e) {
					e.printStackTrace();
					fail("failed to parse date");
				}
			/*assertEquals("Wrong name", playerInfoExpected.getName(), playerInfo.getName());
			assertEquals("Wrong balance", playerInfoExpected.getBalance(), playerInfo.getBalance(), 0);
			assertEquals("wrong avatar link", playerInfoExpected.getAvatarLink(), playerInfo.getAvatarLink());
			assertEquals("wrong date", "Wed May 04 16:45:00 BST 2016", playerInfo.getLastLogindate().toString());*/
			}

			@Override
			public void onError() {
				fail("Error fetching data");
			}
		});
		dataFetcher.execute("https://dl.dropboxusercontent.com/s/2ewt6r22zo4qwgx/gameData.json");
		Robolectric.flushBackgroundThreadScheduler();
	}

	@Test
	public void downloadData() throws Exception {
		RoboSharedPreferences preferences = (RoboSharedPreferences) RuntimeEnvironment.application.getSharedPreferences("", MODE_PRIVATE);

		new DataDownloader().downloadData(preferences, new DataDownloaderCallback() {
			@Override
			public void onSuccess(PlayerInfo playerInfo, Bitmap playerAvatar, GameData gameData) {
				assertTrue("success", playerInfo != null && playerAvatar != null && gameData != null);
			}

			@Override
			public void onError() {
				fail("failed to download data");
			}
		});
		Robolectric.flushBackgroundThreadScheduler();
	}
}
