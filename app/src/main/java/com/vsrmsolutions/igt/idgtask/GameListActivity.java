package com.vsrmsolutions.igt.idgtask;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vsrmsolutions.igt.idgtask.data.DataDownloader;
import com.vsrmsolutions.igt.idgtask.model.GameData;
import com.vsrmsolutions.igt.idgtask.model.GameDetail;
import com.vsrmsolutions.igt.idgtask.model.PlayerInfo;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * An activity representing a list of Games. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link GameDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class GameListActivity extends AppCompatActivity {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	private GameData gameData;
	private PlayerInfo playerInfo;
	private Bitmap playerAvatar;

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_list);

		final View progressBar = findViewById(R.id.progress_bar);
		progressBar.setVisibility(View.VISIBLE);

		DataDownloader dataDownloader = new DataDownloader();
		dataDownloader.downloadData(getPreferences(MODE_PRIVATE), new DataDownloader.DataDownloaderCallback() {
			@Override
			public void onSuccess(final PlayerInfo playerInfo, final Bitmap playerAvatar, final GameData gameData) {
				GameListActivity.this.playerInfo = playerInfo;
				GameListActivity.this.playerAvatar = playerAvatar;
				GameListActivity.this.gameData = gameData;
				runOnUiThread(new Runnable() {
					@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
					@Override
					public void run() {
						View recyclerView = findViewById(R.id.game_list);
						assert recyclerView != null;
						setupRecyclerView((RecyclerView) recyclerView);

						TextView name = (TextView) findViewById(R.id.name);
						name.setText(playerInfo.getName());

						TextView loggedIn = (TextView) findViewById(R.id.loggedIn);
//				        String date = DateFormat.getDateFormat(getBaseContext()).format(playerInfo.getLastLogindate());
						String bestDateTimePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "EyMMMd");
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(bestDateTimePattern, Locale.getDefault());
						String date = simpleDateFormat.format(playerInfo.getLastLogindate());
						loggedIn.setText(date);

						TextView balance = (TextView) findViewById(R.id.balance);
						NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
						String currencyStr = gameData.getCurrency();
						Currency currency = Currency.getInstance(currencyStr);
						format.setCurrency(currency);
						balance.setText(format.format(playerInfo.getBalance() / Math.pow(10, currency.getDefaultFractionDigits())));

						((ImageView) findViewById(R.id.image)).setImageBitmap(playerAvatar);

						setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

						findViewById(R.id.app_bar).setVisibility(View.VISIBLE);
						findViewById(R.id.frameLayout).setVisibility(View.VISIBLE);
						progressBar.setVisibility(View.GONE);
					}
				});
			}

			@Override
			public void onError() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						((TextView) findViewById(R.id.progressText)).setText(R.string.failed_to_fetch_data);
					}
				});
			}
		});

		/*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});*/
		if (findViewById(R.id.game_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-w900dp).
			// If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;
		}
	}

	private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
		recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(gameData.getData(), new ClickListenerCallback() {

			@Override
			public void handleClick(GameDetail gameDetail) {
				if (mTwoPane) {
					Bundle arguments = new Bundle();
					Gson gson = new Gson();
					String json = gson.toJson(gameDetail);
					arguments.putString(GameDetailFragment.ARG_ITEM_ID, json);
					arguments.putString(GameDetailFragment.ARG_CURRENCY_ID, gameData.getCurrency());
					GameDetailFragment fragment = new GameDetailFragment();
					fragment.setArguments(arguments);
					getSupportFragmentManager().beginTransaction()
							.replace(R.id.game_detail_container, fragment)
							.commit();
				} else {
					Intent intent = new Intent(getBaseContext(), GameDetailActivity.class);
					Gson gson = new Gson();
					String json = gson.toJson(gameDetail);
					intent.putExtra(GameDetailFragment.ARG_ITEM_ID, json);
					intent.putExtra(GameDetailFragment.ARG_CURRENCY_ID, gameData.getCurrency());
					Gson profileGson = new Gson();
					String profileJson = profileGson.toJson(playerInfo);
					intent.putExtra(GameDetailActivity.ARG_PROFILE, profileJson);
					intent.putExtra(GameDetailActivity.ARG_PROFILE_IMAGE, playerAvatar);

					startActivity(intent);
				}
			}
		}));
	}

}
