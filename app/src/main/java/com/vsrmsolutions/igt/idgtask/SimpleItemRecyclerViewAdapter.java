package com.vsrmsolutions.igt.idgtask;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vsrmsolutions.igt.idgtask.model.GameDetail;

import java.util.List;

/**
 * Created by murthyvelamakanni on 25/05/2017.
 */
public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

	private final ClickListenerCallback clickListenerCallback;
	private List<GameDetail> mValues;

	public SimpleItemRecyclerViewAdapter(List<GameDetail> items, ClickListenerCallback clickListenerCallback) {
		mValues = items;
		this.clickListenerCallback = clickListenerCallback;
	}

	public void setValues(List<GameDetail> mValues) {
		this.mValues = mValues;
	}

	public List<GameDetail> getValues() {
		return mValues;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.game_list_content, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		holder.setItem(mValues.get(position));
		holder.getIdView().setText(mValues.get(position).getName());
//			holder.mContentView.setText("" + mValues.get(position).getJackpot());

		holder.getView().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				clickListenerCallback.handleClick(mValues.get(position));
			};
		});
	}

	@Override
	public int getItemCount() {
		return mValues.size();
	}

}
