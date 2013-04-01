package com.king.draw.drawtool;

import android.graphics.BlurMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.util.Log;

/**
 * mohu
 * @author simon
 *
 */
public class BlurPen extends AbstractPen {
	private static final String TAG = "KingDraw_BlurPen";
	private MaskFilter mBlur;
 
	public BlurPen(int penSize, int penColor) {
		this(penSize, penColor, Paint.Style.STROKE);
		Log.i(TAG,"BlurPen create ----");
	}

	public BlurPen(int size, int penColor, Paint.Style style) {
		super(size, penColor, style);
		Log.i(TAG,"BlurPen create");
		mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
		mPenPaint.setMaskFilter(mBlur);
	}

	@Override
	public String toString() {
		return "\tplainPen: " + "\tshap: " + mCurrentShape + "\thasDraw: "
				+ hasDraw() + "\tsize: " + mPenSize + "\tstyle:" +mStyle;
	}
}
