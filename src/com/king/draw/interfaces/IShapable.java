package com.king.draw.interfaces;

import com.king.draw.utils.FirstCurrentPosition;

import android.graphics.Path;

public interface IShapable {
	
	public Path getPath();

	public FirstCurrentPosition getFirstLastPoint();

	void setShap(IShapes shape);
}
