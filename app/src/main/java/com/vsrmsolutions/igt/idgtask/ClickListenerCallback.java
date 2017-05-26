package com.vsrmsolutions.igt.idgtask;

import android.graphics.Bitmap;
import android.support.v4.app.FragmentManager;

import com.vsrmsolutions.igt.idgtask.model.GameDetail;
import com.vsrmsolutions.igt.idgtask.model.PlayerInfo;

/**
 * Created by murthyvelamakanni on 25/05/2017.
 */
public interface ClickListenerCallback {
	void handleClick(GameDetail gameDetail);
}
