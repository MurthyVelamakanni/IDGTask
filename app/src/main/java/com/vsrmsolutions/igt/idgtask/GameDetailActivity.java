package com.vsrmsolutions.igt.idgtask;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vsrmsolutions.igt.idgtask.model.PlayerInfo;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * An activity representing a single Game detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link GameListActivity}.
 */
public class GameDetailActivity extends AppCompatActivity {

	public static final String ARG_PROFILE = "PROFILE";
	public static final String ARG_PROFILE_IMAGE = "PROFILE_IMAGE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_detail);

		((ImageView) findViewById(R.id.image)).setImageBitmap((Bitmap) getIntent().getParcelableExtra(ARG_PROFILE_IMAGE));
		PlayerInfo playerInfo = new Gson().fromJson(getIntent().getStringExtra(ARG_PROFILE), PlayerInfo.class);
		TextView name = (TextView) findViewById(R.id.name);
		name.setText(playerInfo.getName());
		TextView loggedIn = (TextView) findViewById(R.id.loggedIn);
		String date = DateUtils.formatDateTime(this, playerInfo.getLastLogindate().getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_TIME);
		loggedIn.setText(date);
		TextView balance = (TextView) findViewById(R.id.balance);
		NumberFormat format = NumberFormat.getCurrencyInstance();
		balance.setText(format.format(playerInfo.getBalance() / 100));
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		/*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});*/

		// Show the Up button in the action bar.
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(GameDetailFragment.ARG_ITEM_ID, getIntent().getStringExtra(GameDetailFragment.ARG_ITEM_ID));
			arguments.putString(GameDetailFragment.ARG_CURRENCY_ID, getIntent().getStringExtra(GameDetailFragment.ARG_CURRENCY_ID));
			GameDetailFragment fragment = new GameDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.frameLayout, fragment)
					.commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			//navigateUpTo(new Intent(this, GameListActivity.class));
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
