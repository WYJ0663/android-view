package com.ptwyj.calendar.view;

import android.content.Context;
import android.util.TypedValue;

public class MetricUtil {

	public static int getDip(Context context, float param) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, param,
				context.getResources().getDisplayMetrics());
	}

	// dip转像素
	public static int DipToPixels(Context context, int dip) {
		final float SCALE = context.getResources().getDisplayMetrics().density;
		float valueDips = dip;
		int valuePixels = (int) (valueDips * SCALE + 0.5f);
		return valuePixels;
	}

	// dip转像素
	public static int DipToPixels(Context context, float dip) {
		final float SCALE = context.getResources().getDisplayMetrics().density;
		float valueDips = dip;
		int valuePixels = (int) (valueDips * SCALE + 0.5f);
		return valuePixels;
	}

	// 像素转dip
	public static float PixelsToDip(Context context, int Pixels) {
		final float SCALE = context.getResources().getDisplayMetrics().density;
		float dips = Pixels / SCALE;
		return dips;
	}

}
