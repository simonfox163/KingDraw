package com.king.draw.drawtool;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;

import com.king.draw.utils.FirstCurrentPosition;
import com.king.draw.utils.Slog;
import com.king.draw.interfaces.IDrawTool;
import com.king.draw.interfaces.IShapable;
import com.king.draw.interfaces.IShapes;

public  class AbstractPen implements IDrawTool,IShapable {
	
	private static final String TAG = "kingDraw_AbstractPen";
	private Path mPath = null;
	protected Paint mPenPaint = null;
	private boolean mHasDraw = false;
	protected IShapes mCurrentShape = null;
	protected int mPenSize;
	protected Style mStyle;
	private float mCurrentX = 0.0f;
	private float mCurrentY = 0.0f;
	private static final float TOUCH_TOLERANCE = 4.0f;
	private FirstCurrentPosition mFirstCurrentPosition;

	public AbstractPen(){
		this(0, 0);
	}
	
	public AbstractPen(int penSize, int penColor){
		this(penSize, penColor, Paint.Style.STROKE);
	}
	public AbstractPen(int penSize, int penColor, Paint.Style style){
		initPaint(penSize,penColor,style);
	} 
	
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		//canvas.drawPath(mPath, mPenPaint);
		if(canvas != null){
			Slog.i(TAG,"shape is not null");
			mFirstCurrentPosition.currentX = mCurrentX;
			mFirstCurrentPosition.currentY = mCurrentY;
			mCurrentShape.draw(canvas, mPenPaint);
		}
	}
 
	public void touchDown(float x, float y) {
		// TODO Auto-generated method stub
		saveDownPoint(x,y);
		mPath.reset();
		mPath.moveTo(x, y);
		savePoint(x, y);
	}

	public void touchMove(float x, float y) {
		// TODO Auto-generated method stub
		if (isMoved(x, y)) {
			// 贝赛尔曲线
			drawBeziercurve(x, y);
			savePoint(x, y);
			mHasDraw = true;
		}
	}
 
	public void touchUp(float x, float y) {
		// TODO Auto-generated method stub
		mPath.lineTo(x, y);
	}

	public boolean hasDraw() {
		// TODO Auto-generated method stub
		return mHasDraw;
	}

	protected void initPaint(int penSize, int penColor, Paint.Style style) {
		mPath = new Path();
		
		mFirstCurrentPosition = new FirstCurrentPosition();
		mPenPaint = new Paint();
		mPenPaint.setStrokeWidth(penSize);
		mPenPaint.setColor(penColor);
		mPenSize = penSize;
		mStyle = style;
		mPenPaint.setDither(true);
		mPenPaint.setAntiAlias(true);
		mPenPaint.setStyle(style);
		mPenPaint.setStrokeJoin(Paint.Join.ROUND);
		mPenPaint.setStrokeCap(Paint.Cap.ROUND);
	} 

	
	public void setColor(int color){
		mPenPaint.setColor(color);
	}
	
	public void setPenSize(int size){
		this.mPenSize = size;
	}
	
	public Path getPath(){
		return mPath;
	}
	
	private void drawBeziercurve(float x, float y) {
		mPath.quadTo(mCurrentX, mCurrentY, (x + mCurrentX)/2,(y + mCurrentY)/2);
	}
	
	private void savePoint(float x, float y) {
		mCurrentX = x;
		mCurrentY = y;
	}
	
	/**
	 * 保存起始位置
	 */
	private void saveDownPoint(float x, float y) {
		mFirstCurrentPosition.firstX = x;
		mFirstCurrentPosition.firstY = y;
	}
	
	private boolean isMoved(float x, float y) {
		float dx = Math.abs(x - mCurrentX);
		float dy = Math.abs(y - mCurrentY);
		boolean isMoved = dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE;
		return isMoved;
	}
	
	
	public FirstCurrentPosition getFirstLastPoint() {
		// TODO Auto-generated method stub
		return mFirstCurrentPosition;
	}

	public void setShap(IShapes shape) {
		// TODO Auto-generated method stub
		mCurrentShape = shape;
	}

	
}
