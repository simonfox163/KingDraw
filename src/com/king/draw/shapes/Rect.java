package com.king.draw.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.king.draw.interfaces.IShapable;

public class Rect extends Shape{
	public Rect(IShapable paintTool) {
		super(paintTool);
		// TODO Auto-generated constructor stub
	}
	
	public void draw(Canvas canvas, Paint paint) {
		super.draw(canvas, paint);
		canvas.drawRect(x1, y1, x2, y2, paint);
	}
}
