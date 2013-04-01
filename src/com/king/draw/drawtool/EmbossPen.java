package com.king.draw.drawtool;

import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.util.Log;


/**
 *  fudiao 
 * @author simon
 *
 */
public class EmbossPen extends AbstractPen {
	private MaskFilter mEmboss;
	private static final String TAG = "EmbossPen";
	public EmbossPen(int penSize, int penColor) {
		this(penSize, penColor, Paint.Style.STROKE);
	}

	public EmbossPen(int size, int penColor, Paint.Style style) {
		super(size, penColor, style);
		Log.i(TAG,"EmbossPen create");
		mEmboss = new EmbossMaskFilter(new float[] { 1, 1, 1 }, 0.4f, 6, 3.5f);
		mPenPaint.setMaskFilter(mEmboss);
	}
	
	@Override
	public String toString() {
		return "\tplainPen: " + "\tshap: " + mCurrentShape + "\thasDraw: "
				+ hasDraw() + "\tsize: " + mPenSize + "\tstyle:" +mStyle;
	}
}
