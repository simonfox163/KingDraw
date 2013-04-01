package com.king.draw.shapes;

import com.king.draw.interfaces.IShapable;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Line extends Shape{
	private static final String TAG = "KingDraw__Line";
	public Line(IShapable paintTool) {
		super(paintTool);
	}

	public void draw(Canvas canvas, Paint paint) {
		super.draw(canvas, paint);
		//Slog.i(TAG,"x1 y1 x2 y2 is: " + x1 + "  " + y1+ "  " + x2 + "   " + y2);
		canvas.drawLine(x1, y1, x2, y2, paint);
	}
}