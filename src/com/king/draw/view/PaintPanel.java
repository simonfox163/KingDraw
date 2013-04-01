package com.king.draw.view;

import java.util.ArrayList;

import com.king.draw.utils.BitMapUtils;
import com.king.draw.utils.Slog;
import com.king.draw.drawtool.BlurPen;
import com.king.draw.drawtool.EmbossPen;
import com.king.draw.drawtool.Eraser;
import com.king.draw.drawtool.NormalPen;
import com.king.draw.interfaces.IDrawTool;
import com.king.draw.interfaces.IOnDrawCallback;
import com.king.draw.interfaces.IShapable;
import com.king.draw.interfaces.IShapes;
import com.king.draw.shapes.Curve;
import com.king.draw.shapes.Line;
import com.king.draw.shapes.Oval;
import com.king.draw.shapes.Rect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import static com.king.draw.utils.Constants.*;

public class PaintPanel extends View  {
	private static final String TAG = "KingDraw_Panel";
	
	private IDrawTool mCurrentTool;
	private Canvas mCanvas ;
	private Paint mBitmapPaint = null;
	private Bitmap mBitmap = null;
	private Bitmap mOrgBitMap = null;
	private boolean mIsTouchUp = false ;
	private boolean mCanvasIsCreated = false ;
	private int mBitmapWidth ;
	private int mBitmapHeight;
	private PaintPadUndoStack mUndoStack = null;
	private int mStackedSize = UNDO_STACK_SIZE;
	private int mShapeType = IShapes.CURVE;
	private IOnDrawCallback mCallback ;
	private int mBackGroundColor = DEFAULT.BACKGROUND_COLOR;
	private int mToolType = IDrawTool.PEN;
	private int mDrawToolSize = DEFAULT.PENSIZE;
	private int mCurColor = Color.BLACK;
	
	public PaintPanel(Context context) {
		this(context,null);
		Slog.i(TAG,"PaintPanel create 1");
	}
	
	public PaintPanel(Context context, AttributeSet attrs) {
		super(context,attrs);
		init();
		Slog.i(TAG,"PaintPanel create 2");
	} 
	
	public PaintPanel(Context context, AttributeSet attrs, int defStyle) {
		
		super(context, attrs, defStyle);
		init();
		Slog.i(TAG,"PaintPanel create 3");
	}
	
	private void init(){
		mCanvas = new Canvas();
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		createNewDrawTool();
		mUndoStack = new PaintPadUndoStack(this, mStackedSize);
		setShape();
	} 
	
	/**
	 * ��������
	 */
	private void createNewDrawTool() {
		Slog.i(TAG, "createNewDrawTool"+ mToolType+"");
		switch (mToolType) {
		case IDrawTool.EMBOSS:
			mCurrentTool = new EmbossPen(mDrawToolSize, mCurColor);
			break;
		case IDrawTool.BLUR:
			mCurrentTool = new BlurPen(mDrawToolSize, mCurColor);
			break;
		case IDrawTool.ERASER:
			mCurrentTool = new Eraser(DEFAULT.PENSIZE * 10);
			break;
		default:
			mCurrentTool = new NormalPen(mDrawToolSize, mCurColor);
			break;
		}
		setShape();
	}
	
	
	
	protected void onDraw(Canvas canvas){
		//Slog.i(TAG,"onDraw");
		canvas.drawColor(mBackGroundColor);
		canvas.drawBitmap(mBitmap, 0, 0,mBitmapPaint);
		// ���ⲿ���Ƶķ���ֻ��һ�֣���������bitmap�ϻ��ƣ�Ȼ����ص�cv
		if (!mIsTouchUp) {
			// ƽʱ��ֻ��view��cv����ʱ����
			// earaser������cv�ϻ��ƣ���Ҫֱ�ӻ�����bitmap��
			if (mToolType != IDrawTool.ERASER) {
				mCurrentTool.draw(canvas);
			}
		}
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		Slog.i(TAG,"size is: " + w + " " + h + " " + oldw + " " + oldh);
		if (!mCanvasIsCreated) {
			mBitmapWidth = w;
			mBitmapHeight = h;
			creatCanvasBitmap(w, h);
			mCanvasIsCreated = true;
		}
	}
	
	private void creatCanvasBitmap(int w, int h) {
		mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mCanvas.setBitmap(mBitmap);
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		mIsTouchUp = false ;
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				Slog.i(TAG,"down");
				mCanvas.setBitmap(mBitmap);
				createNewDrawTool();
				mCurrentTool.touchDown(x, y);
				mUndoStack.clearRedo();
				mCallback.onTouchDown(); 
				invalidate();
				break;
			case MotionEvent.ACTION_MOVE:
				Slog.i(TAG,"Action_move");
				mCurrentTool.touchMove(x, y);
				if (mToolType == IDrawTool.ERASER) {
					mCurrentTool.draw(mCanvas);
				}
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				Slog.i(TAG,"ACTION_UP");
				if (mCurrentTool.hasDraw()) {
					Slog.i(TAG,"hasDraw");
					mUndoStack.push(mCurrentTool);
					if (mCallback != null) {
						// ����undo\redo����ʵ
						mCallback.onDrawDone();
					}
				}
				mCurrentTool.touchUp(x, y);
				mCurrentTool.draw(mCanvas);
				invalidate();
				mIsTouchUp = true ;
				break;
			 
		}
		return true;
	}

	

	public boolean hasDraw() {
		// TODO Auto-generated method stub
		return false;
	}
 
	
	
	/**
	 * �õ���ǰview�Ľ�ͼ
	 */
	public Bitmap getBitmap() {
		// ��õ�ǰ��view��ͼƬ
		setDrawingCacheEnabled(true);
		buildDrawingCache(true);
		Bitmap bitmap = getDrawingCache(true);
		Bitmap bmp = BitMapUtils.duplicateBitmap(bitmap);
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
		// �����������
		setDrawingCacheEnabled(false);
		return bmp;
	}
	
	/**
	 * ���û��ʴ�С
	 * @param size
	 */
	public void setToolSize(int size){
		mDrawToolSize = size ;
	}
	
	public void setToolType(int type){
		mToolType = type ;
	}
	
	/**
	 * ���û�����״
	 * @param type
	 */
	public void setShapeType(int type){
		this.setToolType(mToolType);
		mShapeType = type;
		this.setShape();
	}
	
	public void setPenSize(int size){
		mDrawToolSize = size;
		mCurrentTool.setPenSize(mDrawToolSize);
	}
	
	public void setDrawColor(int color){
		mCurColor = color;
		mCurrentTool.setColor(color);
	}
	
	private void setShape() {
		// TODO Auto-generated method stub
		IShapes shape  = new Curve((IShapable) mCurrentTool);
		switch( mShapeType){
			case IShapes.CURVE:
				shape = new Curve((IShapable) mCurrentTool);
				break;
			case IShapes.LINE:
				shape = new Line((IShapable) mCurrentTool);
				break;
			case IShapes.RECT:
				shape = new Rect((IShapable) mCurrentTool);
				break;
			case IShapes.OVAL:
				shape = new Oval((IShapable) mCurrentTool);
				break;
		}
		Slog.i(TAG,"setSharp");
		((IShapable)mCurrentTool).setShap(shape);
	}
	
	

	public void undo() {
		if (null != mUndoStack) {
			Slog.i(TAG,"undo");
			mUndoStack.undo();
		}
	}

	public void redo() {
		if (null != mUndoStack) {
			Slog.i(TAG,"redo");
			mUndoStack.redo();
		}
	}

	public boolean canUndo() {
		return mUndoStack.canUndo();
	}

	public boolean canRedo() {
		return mUndoStack.canRedo();
	}
	
	private void recycleMBitmap() {
		if (mBitmap != null && !mBitmap.isRecycled()) {
			mBitmap.recycle();
			mBitmap = null;
		}
	}
	
	protected void setTempForeBitmap(Bitmap tempForeBitmap) {
		if (null != tempForeBitmap) {
			recycleMBitmap();
			mBitmap = BitMapUtils.duplicateBitmap(tempForeBitmap);
			if (null != mBitmap && null != mCanvas) {
				mCanvas.setBitmap(mBitmap);
				invalidate();
			}
		}
	}
	
	public void setForeBitMap(Bitmap bitmap) {
		if (bitmap != null) {
			recycleMBitmap();
			recycleOrgBitmap(); 
		}
		mBitmap = BitMapUtils.duplicateBitmap(bitmap, getWidth(), getHeight());
		mOrgBitMap = BitMapUtils.duplicateBitmap(mBitmap);
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
		invalidate();
	}
	
	 
	public void resetState() {
		 setShapeType(IDrawTool.PEN);
		 setDrawColor(DEFAULT.PEN_COLOR);
		setBackGroundColor(DEFAULT.BACKGROUND_COLOR);
		mUndoStack.clearAll();
	}

	public void setBackGroundColor(int color) {
		mBackGroundColor = color;
		invalidate();
	}

	
	private void recycleOrgBitmap() {
		if (mOrgBitMap != null && !mOrgBitMap.isRecycled()) {
			mOrgBitMap.recycle();
			mOrgBitMap = null;
		}
	}


	
	public void setOnDrawCallback(IOnDrawCallback callback){
		mCallback = callback;
	}
	
	public class PaintPadUndoStack {
		private int m_stackSize = 0;
		private PaintPanel mPaintPanel = null;
		private ArrayList<IDrawTool> mUndoStack = new ArrayList<IDrawTool>();
		private ArrayList<IDrawTool> mRedoStack = new ArrayList<IDrawTool>();
		private ArrayList<IDrawTool> mOldActionStack = new ArrayList<IDrawTool>();

		public PaintPadUndoStack(PaintPanel paintpanel, int stackSize) {
			mPaintPanel = paintpanel;
			m_stackSize = stackSize;
		}

		/**
		 * ��painter����ջ��
		 */
		public void push(IDrawTool penTool) {
			if (null != penTool) {
				// ���undo�Ѿ�����
				if (mUndoStack.size() == m_stackSize && m_stackSize > 0) {
					// �õ���Զ�Ļ���
					IDrawTool removedTool = mUndoStack.get(0);
					// ���еıʼ�����
					mOldActionStack.add(removedTool);
					mUndoStack.remove(0);
				}

				mUndoStack.add(penTool);
			}
		}

		/**
		 * �������
		 */
		public void clearAll() {
			mRedoStack.clear();
			mUndoStack.clear();
			mOldActionStack.clear();
		}

		/**
		 * undo
		 */
		public void undo() {
			if (canUndo() && null != mPaintPanel) {
				IDrawTool removedTool = mUndoStack
						.get(mUndoStack.size() - 1);
				mRedoStack.add(removedTool);
				mUndoStack.remove(mUndoStack.size() - 1);

				if (null != mOrgBitMap) {
					// Set the temporary fore bitmap to canvas.
					// �������ļ�ʱ������һ��,����Ҫ���»��Ƴ���
					mPaintPanel.setTempForeBitmap(mPaintPanel.mOrgBitMap);
				} else {
					// ������������ڣ������´���һ�ݱ���
					mPaintPanel.creatCanvasBitmap(mPaintPanel.mBitmapWidth,
							mPaintPanel.mBitmapHeight);
				}

				Canvas canvas = mPaintPanel.mCanvas;

				// First draw the removed tools from undo stack.
				int i = 0;
				for (IDrawTool paintTool : mOldActionStack) {
					Slog.i(TAG,"mOldActionStack :" + i++);
					paintTool.draw(canvas);
				}
				int m = 0;
				for (IDrawTool paintTool : mUndoStack) {
					Slog.i(TAG,"mUndoStack :" + m++);
					paintTool.draw(canvas);
				}

				mPaintPanel.invalidate();
			}
		}

		/**
		 * redo
		 */
		public void redo() {
			if (canRedo() && null != mPaintPanel) {
				IDrawTool removedTool = mRedoStack
						.get(mRedoStack.size() - 1);
				mUndoStack.add(removedTool);
				mRedoStack.remove(mRedoStack.size() - 1);

				if (null != mOrgBitMap) {
					// Set the temporary fore bitmap to canvas.
					mPaintPanel.setTempForeBitmap(mPaintPanel.mOrgBitMap);
				} else {
					// Create a new bitmap and set to canvas.
					mPaintPanel.creatCanvasBitmap(mPaintPanel.mBitmapWidth,
							mPaintPanel.mBitmapHeight);
				}

				Canvas canvas = mPaintPanel.mCanvas;
				// ������ǰ�ıʼ��������removedStack��
				// First draw the removed tools from undo stack.
				for (IDrawTool sketchPadTool : mOldActionStack) {
					sketchPadTool.draw(canvas);
				}
				// �����������Ǵӳ���������ƣ�����ֻ����ʱ�Ĵ洢
				for (IDrawTool sketchPadTool : mUndoStack) {
					sketchPadTool.draw(canvas);
				}

				mPaintPanel.invalidate();
			}
		}

		public boolean canUndo() {
			return (mUndoStack.size() > 0);
		}

		public boolean canRedo() {
			return (mRedoStack.size() > 0);
		}

		public void clearRedo() {
			mRedoStack.clear();
		}

		@Override
		public String toString() {
			return "canUndo" + canUndo();
		}
	}
	
}
