package com.king.draw.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.king.draw.interfaces.IShapable;

public class Oval extends Shape{
	RectF mRectF;
	public Oval(IShapable paintTool) {
		super(paintTool);
		// TODO Auto-generated constructor stub
		mRectF = new RectF();
	}
	
	public void draw(Canvas canvas, Paint paint) {
		super.draw(canvas, paint);
		mRectF.set(x1, y1, x2, y2);
		canvas.drawOval(mRectF, paint);
	}
}
