package com.king.draw.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.king.draw.utils.FirstCurrentPosition;
import com.king.draw.interfaces.IShapable;
import com.king.draw.interfaces.IShapes;

public class Shape implements IShapes {
	
	protected IShapable paintTool = null;
	protected FirstCurrentPosition firstCurrentPos;
	Path mPath;
	protected float x1 = 0;
	protected float y1 = 0;
	protected float x2 = 0;
	protected float y2 = 0;

	Shape(IShapable paintTool) {
		assert(paintTool!=null);
		this.paintTool = paintTool;
	}
	
	public void draw(Canvas canvas, Paint paint) {
		firstCurrentPos = paintTool.getFirstLastPoint();
		mPath = paintTool.getPath();
		x1 = firstCurrentPos.firstX;
		y1 = firstCurrentPos.firstY;
		x2 = firstCurrentPos.currentX;
		y2 = firstCurrentPos.currentY;
	}

}
