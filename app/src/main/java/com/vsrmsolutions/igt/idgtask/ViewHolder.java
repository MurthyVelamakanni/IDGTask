package com.vsrmsolutions.igt.idgtask;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.vsrmsolutions.igt.idgtask.model.GameDetail;

/**
 * Created by murthyvelamakanni on 25/05/2017.
 */
public class ViewHolder extends RecyclerView.ViewHolder {

	private final View mView;
	private final TextView mIdView;
	//			public final TextView mContentView;
	private GameDetail mItem;

	public View getView() {
		return mView;
	}

	public TextView getIdView() {
		return mIdView;
	}

	public GameDetail getItem() {
		return mItem;
	}

	public ViewHolder(View view) {
		super(view);
		mView = view;
		mIdView = (TextView) view.findViewById(R.id.id);
//				mContentView = (TextView) view.findViewById(R.id.content);
	}

	@Override
	public String toString() {
		return super.toString()/* + " '" + mContentView.getText() + "'"*/;
	}

	public void setItem(GameDetail gameDetail) {
		mItem = gameDetail;
	}
}
