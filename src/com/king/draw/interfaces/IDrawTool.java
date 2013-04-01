package com.king.draw.interfaces;

import android.graphics.Canvas;

public interface IDrawTool {
	public static final int PEN = 1;
	public static final int ERASER = 2;
	public static final int EMBOSS = 3;
	public static final int BLUR = 4;
	
	
	public void draw(Canvas canvas);
	public void touchDown(float x, float y);
	public void touchMove(float x, float y);
	public void touchUp(float x, float y);
	public boolean hasDraw();
	public void setColor(int color);
	public void setPenSize(int color);
}
