package com.vsrmsolutions.igt.idgtask;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vsrmsolutions.igt.idgtask.model.GameDetail;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * A fragment representing a single Game detail screen.
 * This fragment is either contained in a {@link GameListActivity}
 * in two-pane mode (on tablets) or a {@link GameDetailActivity}
 * on handsets.
 */
public class GameDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";
	public static final String ARG_CURRENCY_ID = "currency_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private GameDetail mItem;
	private String mCurrency;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public GameDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = new Gson().fromJson(getArguments().getString(ARG_ITEM_ID), GameDetail.class);
			mCurrency = getArguments().getString(ARG_CURRENCY_ID);

			Activity activity = this.getActivity();
			/*CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
			if (appBarLayout != null) {
				appBarLayout.setTitle(mItem.getName());
			}*/
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.game_detail, container, false);

		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			((TextView) rootView.findViewById(R.id.game_name)).setText(mItem.getName());
			Currency currency = Currency.getInstance(mCurrency);
			NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
			format.setCurrency(currency);
			((TextView) rootView.findViewById(R.id.game_jackpot)).setText(format.format(mItem.getJackpot() / Math.pow(10, currency.getDefaultFractionDigits())));
//			String date = DateUtils.formatDateTime(getContext(), mItem.getDate().getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_TIME);
//			String time = DateFormat.getTimeFormat(getContext()).format(mItem.getDate());
			String bestDateTimePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "EyMMMdhms");
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(bestDateTimePattern);
			String date = simpleDateFormat.format(mItem.getDate());

			((TextView) rootView.findViewById(R.id.game_date)).setText(date);
		}

		return rootView;
	}
}
