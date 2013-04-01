package com.king.draw.drawtool;

import android.graphics.Paint;

public class NormalPen extends AbstractPen{

	public NormalPen(int size, int penColor){
		this(size,penColor,Paint.Style.STROKE);
	}
	
	public NormalPen(int size, int penColor,Paint.Style style ) {
		super(size, penColor,style);
	}
	
	public String toString(){
		return "\tplainPen: " + "\tshap: " + mCurrentShape + "\thasDraw: "
				+ hasDraw() + "\tsize: " + mPenSize + "\tstyle:" +mStyle;
	}
}
	
 