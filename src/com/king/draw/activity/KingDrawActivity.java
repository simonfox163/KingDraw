package com.king.draw.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.widget.LinearLayout.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory; 
import android.graphics.Color; 
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.king.draw.utils.BitMapUtils;
import com.king.draw.utils.Constants.DEFAULT;
import com.king.draw.utils.Slog;
import com.king.draw.R;
import com.king.draw.interfaces.IDrawTool;
import com.king.draw.interfaces.IOnButtonListener;
import com.king.draw.interfaces.IOnDrawCallback;
import com.king.draw.interfaces.IShapes;
import com.king.draw.view.*; 

public class KingDrawActivity extends Activity implements OnClickListener,IOnDrawCallback {
	private static final String TAG = "KingDraw_KingDrawActivity";
	LinearLayout mPaintPanelLayout;
	PaintPanel mPaintPanel;
	
	ImageView mLineImg; 
	ImageView mOvalBu;
	ImageView mCurveeBu; 
	ImageView mRectBu;  
	
	ImageView mUndoImg;
	ImageView mRedoImg;
	
	ImageView mEraserImg ;
	
	ImageView mColorPickImg ;
	ImageView mPenSizeImg ;
	
	ImageView mRedImg ;
	ImageView mBlackImg;
	ImageView mYellowImg;
	ImageView mBlueImg;
	ImageView mGreenImg;
	
	ImageView mShapState ;
	ImageView mColorState;
	ImageView mSizeState;
	ImageView mStylePick;
	
	ImageView mSize1;
	ImageView mSize2;
	ImageView mSize3;
	ImageView mSize4;
	ImageView mSize5;
	
	
	ImageView mOpenImg ;
	ImageView mSaveImg;
	
	TextView mStyleNormal;
	TextView mStyleEmboss;
	TextView mStyleBlur;
	
	
	AlertDialog mReturnDialog ;
	
	private static final int LOAD_ACTIVITY = 1;
	private static final int COLOR_PICKER = 2;
	private boolean mHasDraw = false ;
	
	
	/**
	 *  两个PopWindow,分别用来用来选择颜色和笔刷大小
	 */
	private PopupWindow mColorPickPopupWindow = null;
	private PopupWindow mSizePickPopupWindow = null;
	private PopupWindow mStylePickPopupWindow = null;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Slog.i(TAG,"Activity create");   
		setContentView(R.layout.main);
		initView();
		
	}
	
	protected void onPause(){
		super.onPause();
		Slog.i(TAG,"onPause");
	}
	public void onStop(){
		super.onStop();
		Slog.i(TAG,"onStop");
	}
	
	public void onClick(View v) {
		mColorPickPopupWindow.dismiss();
		switch (v.getId()) {
			case R.id.openImg:
				startLoadActivity();
				break;
			case R.id.saveImg:
				if(saveTask()){
					Toast.makeText(KingDrawActivity.this, R.string.save_success, Toast.LENGTH_SHORT).show();
				};
				break;
			case R.id.ovalImg: 
				mPaintPanel.setShapeType(IShapes.OVAL);
				mPaintPanel.setToolType(IDrawTool.PEN);
				mShapState.setImageDrawable(this.getResources().getDrawable(R.drawable.shap_ovalf_press));
				break; 
			case R.id.lineImg:
				Slog.i(TAG,"lineImg Click");
				mPaintPanel.setShapeType(IShapes.LINE);
				mPaintPanel.setToolType(IDrawTool.PEN);
				mShapState.setImageDrawable(this.getResources().getDrawable(R.drawable.shap_line_press));
				break;
			case R.id.curveImg:
				Slog.i(TAG,"curveImg Click");
				mPaintPanel.setShapeType(IShapes.CURVE);
				mPaintPanel.setToolType(IDrawTool.PEN);
				mShapState.setImageDrawable(this.getResources().getDrawable(R.drawable.shap_curv_press));
				break;
			case R.id.rectImg: 
				Slog.i(TAG,"rectImg Click");
				mPaintPanel.setToolType(IDrawTool.PEN);
				mShapState.setImageDrawable(this.getResources().getDrawable(R.drawable.shap_rect_press));
				mPaintPanel.setShapeType(IShapes.RECT);
				break;
			case R.id.eraImg:
				Slog.i(TAG,"eraImg Click");
				mPaintPanel.setToolType(IDrawTool.ERASER);
				mShapState.setImageDrawable(this.getResources().getDrawable(R.drawable.tool_eraser_press));
				break;
			case R.id.styleImg:
				mStylePickPopupWindow.showAtLocation(findViewById(R.id.opArea),
						Gravity.TOP, 0, 0);
				break;
			case R.id.redoImg:
				mPaintPanel.redo(); 
				break;
			case R.id.undoImg:
				mPaintPanel.undo();
				break;
			case R.id.pensizeImg:
				mSizePickPopupWindow.showAtLocation(findViewById(R.id.opArea),
						Gravity.TOP, 0, 0);
				break;
			case R.id.colorImg:
				Slog.i(TAG,"colorPick clicked...");
				/*mColorPickPopupWindow.showAtLocation(findViewById(R.id.opArea),
						Gravity.TOP, 0, 0);*/
				startColorPickerActivity();
				break;
			case R.id.redImg:
				Slog.i(TAG,"set color.Red");
				mPaintPanel.setDrawColor(Color.RED);
				mColorState.setBackgroundColor(Color.RED);
				mColorPickPopupWindow.dismiss();
				break;
			case R.id.blueImg:
				Slog.i(TAG,"set color.Red");
				mPaintPanel.setDrawColor(Color.BLUE);
				mColorState.setBackgroundColor(Color.BLUE);
				mColorPickPopupWindow.dismiss();
				break;
			case R.id.yellowImg:
				mPaintPanel.setDrawColor(Color.YELLOW);
				mColorState.setBackgroundColor(Color.YELLOW);
				mColorPickPopupWindow.dismiss();
				break;
			case R.id.greenImg:
				mPaintPanel.setDrawColor(Color.GREEN);
				mColorState.setBackgroundColor(Color.GREEN);
				mColorPickPopupWindow.dismiss();
				break;
			case R.id.blackImg:
				mPaintPanel.setDrawColor(Color.BLACK);
				mColorState.setBackgroundColor(Color.BLACK);
				mColorPickPopupWindow.dismiss();
				break;
			case R.id.size1:
				mPaintPanel.setPenSize(DEFAULT.PENSIZE*1);
				mSizeState.setImageDrawable(this.getResources().getDrawable(R.drawable.size_radio_1_focus));
				mSizePickPopupWindow.dismiss();
				break;
			case R.id.size2:
				mPaintPanel.setPenSize(DEFAULT.PENSIZE*2);
				mSizeState.setImageDrawable(this.getResources().getDrawable(R.drawable.size_radio_2_focus));
				mSizePickPopupWindow.dismiss();
			break;
			case R.id.size3:
				mPaintPanel.setPenSize(DEFAULT.PENSIZE*3);
				mSizeState.setImageDrawable(this.getResources().getDrawable(R.drawable.size_radio_3_focus));
				mSizePickPopupWindow.dismiss();
				break;
			case R.id.size4:
				mPaintPanel.setPenSize(DEFAULT.PENSIZE*4);
				mSizeState.setImageDrawable(this.getResources().getDrawable(R.drawable.size_radio_4_focus));
				mSizePickPopupWindow.dismiss();
				break;
			case R.id.size5:
				mPaintPanel.setPenSize(DEFAULT.PENSIZE*5);
				mSizeState.setImageDrawable(this.getResources().getDrawable(R.drawable.size_radio_5_focus));
				mSizePickPopupWindow.dismiss();
				break;
			case R.id.normalTyle:
				mPaintPanel.setToolType(IDrawTool.PEN);
				mStylePickPopupWindow.dismiss();
				break;
			case R.id.embossTyle:
				mPaintPanel.setToolType(IDrawTool.EMBOSS);
				mStylePickPopupWindow.dismiss();
				break;
			case R.id.blurStyle:
				mPaintPanel.setToolType(IDrawTool.BLUR);
				mStylePickPopupWindow.dismiss();
				break;
			
		}
	}
	
	private void initView(){
		mPaintPanelLayout = (LinearLayout) findViewById(R.id.paintArea);
		mPaintPanel = new PaintPanel(this);
		mPaintPanelLayout.addView(mPaintPanel);
		mPaintPanel.setOnDrawCallback(this);
		
		mLineImg = (ImageView) findViewById(R.id.lineImg);
		mOvalBu = (ImageView) findViewById(R.id.ovalImg);
		mCurveeBu = (ImageView) findViewById(R.id.curveImg);
		mRectBu = (ImageView) findViewById(R.id.rectImg);
		mEraserImg = (ImageView) findViewById(R.id.eraImg);
		
		mUndoImg = (ImageView) findViewById(R.id.undoImg);
		mUndoImg.setClickable(false);
		mRedoImg = (ImageView) findViewById(R.id.redoImg);
		mRedoImg.setClickable(false);
		
		mColorPickImg = (ImageView) findViewById(R.id.colorImg);
		mPenSizeImg = (ImageView) findViewById(R.id.pensizeImg);
		mStylePick = (ImageView) findViewById(R.id.styleImg);
		
		mShapState = (ImageView)findViewById(R.id.shapeState);
		mShapState.setImageDrawable(this.getResources().getDrawable(R.drawable.shap_curv_press));
		mColorState = (ImageView)findViewById(R.id.colorState);
		
		mSizeState = (ImageView)findViewById(R.id.sizeState);
		mSizeState.setImageDrawable(this.getResources().getDrawable(R.drawable.size_radio_1_focus));
		
		mOpenImg = (ImageView)findViewById(R.id.openImg);
		mSaveImg = (ImageView)findViewById(R.id.saveImg);
		
		mLineImg.setOnClickListener(this);
		mOvalBu.setOnClickListener(this); 
		mCurveeBu.setOnClickListener(this);
		mRectBu.setOnClickListener(this);
		mEraserImg.setOnClickListener(this);
		
		mUndoImg.setOnClickListener(this);
		mRedoImg.setOnClickListener(this);
		mColorPickImg.setOnClickListener(this);
		mPenSizeImg.setOnClickListener(this);
		mOpenImg.setOnClickListener(this);
		mSaveImg.setOnClickListener(this);
		mStylePick.setOnClickListener(this);
		
		initColorPicker();
		initSizePicker();
		initStylePicker();
	}
	
	private void initColorPicker(){
		View pickLayout = initColorPickPopWindowLayout();
		initColorPickImg(pickLayout);
	}
	private void initSizePicker(){
		View pickLayout = initSizePickPopWindowLayout(); 
		penSizePickImg(pickLayout);
	}
	
	private void initStylePicker(){
		View styleLayout = initStylePickPopWindowLayout(); 
		initStylePickImg(styleLayout);
	}
	
	private void initStylePickImg(View view){
		mStyleNormal = (TextView)view.findViewById(R.id.normalTyle);
		mStyleEmboss = (TextView)view.findViewById(R.id.embossTyle);
		mStyleBlur = (TextView)view.findViewById(R.id.blurStyle);
		
		mStyleNormal.setOnClickListener(this);
		mStyleEmboss.setOnClickListener(this);
		mStyleBlur.setOnClickListener(this);
	}
	
	
	private void initColorPickImg(View view){
		mRedImg = (ImageView)view.findViewById(R.id.redImg);
		mYellowImg = (ImageView)view.findViewById(R.id.yellowImg);
		mGreenImg = (ImageView)view.findViewById(R.id.greenImg);
		mBlackImg = (ImageView)view.findViewById(R.id.blackImg);
		mBlueImg = (ImageView)view.findViewById(R.id.blueImg);
		
		mRedImg.setOnClickListener(this);
		mYellowImg.setOnClickListener(this);
		mGreenImg.setOnClickListener(this);
		mBlackImg.setOnClickListener(this);
		mBlueImg.setOnClickListener(this);
	}
	
	private void penSizePickImg(View view){
		mSize1 = (ImageView)view.findViewById(R.id.size1);
		mSize2 = (ImageView)view.findViewById(R.id.size2);
		mSize3 = (ImageView)view.findViewById(R.id.size3);
		mSize4 = (ImageView)view.findViewById(R.id.size4);
		mSize5 = (ImageView)view.findViewById(R.id.size5);
		
		mSize1.setOnClickListener(this);
		mSize2.setOnClickListener(this);
		mSize3.setOnClickListener(this);
		mSize4.setOnClickListener(this);
		mSize5.setOnClickListener(this);
	}
	
	private View initStylePickPopWindowLayout() {
		LayoutInflater mLayoutInflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View stylePopup = mLayoutInflater.inflate(R.layout.stylepick, null);
		mStylePickPopupWindow = new PopupWindow(stylePopup,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		mStylePickPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		return stylePopup;
	}
	
	private View initColorPickPopWindowLayout() {
		LayoutInflater mLayoutInflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View toolsPopup = mLayoutInflater.inflate(R.layout.colorpick, null);
		mColorPickPopupWindow = new PopupWindow(toolsPopup,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		mColorPickPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		return toolsPopup;
	}
	
	private View initSizePickPopWindowLayout() {
		LayoutInflater mLayoutInflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View sizePopup = mLayoutInflater.inflate(R.layout.sizepick, null);
		mSizePickPopupWindow = new PopupWindow(sizePopup,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		mSizePickPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		return sizePopup;
	}

	
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			Slog.i(TAG, "Menu Clicked");
		}
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(!mHasDraw){
				return super.onKeyUp(keyCode, event);
			}
			String title = this.getResources().getString(R.string.note);
			YesNoDialog returnDialog = new YesNoDialog(this,
					new IOnButtonListener() {
						public void onButtonClick(int which) { 
								if(which == IOnButtonListener.YES){
									if(saveTask()){
										Toast.makeText(KingDrawActivity.this, R.string.save_success, Toast.LENGTH_SHORT).show();
									}
									KingDrawActivity.this.finish();
								}else if(which == IOnButtonListener.NO){
									KingDrawActivity.this.finish();
								}
						} 
					},title);
			
			returnDialog.show();
			returnDialog.setMessage(this.getResources().getString(R.string.save_or_not));
			return true;
		}
		
		return super.onKeyUp(keyCode, event);
	}
	
	private boolean saveTask() {
		mHasDraw = false ; //已经保存过，所以此变量重新赋值
		String sdPath = BitMapUtils.getSDPath();
		if (sdPath == null) {
			Toast.makeText(this, R.string.sdcard_error, Toast.LENGTH_SHORT).show();

		} else {
			File file = new File(sdPath + "/KingDraw");
			Slog.i(TAG, "file dir is : " + file.toString());
			if (!file.exists() || !file.isDirectory()) {
				Slog.i(TAG, "del dir");
				file.delete();
				file.mkdir();
		 	}
			
			SimpleDateFormat formatter;
		    formatter = new SimpleDateFormat ("yyyy-MM-dd-HH_mm_ss");
		    Date time = new Date();
		    String currentTime = formatter.format(time);
			String fullFileName = file.toString() + "/" +currentTime;
			Slog.i(TAG,"FullName is : " + fullFileName);
			Bitmap bitmap = mPaintPanel.getBitmap();
			if (bitmap != null) {
				BitMapUtils.saveToSdCard(fullFileName+".png", bitmap);
			}
		}
		return true;
	}
	
	/**
	 * 启动图片选择窗口
	 */
	private void startLoadActivity() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, LOAD_ACTIVITY);
	}
	
	/**
	 * 启动颜色颜色器
	 */
	private void startColorPickerActivity(){
		Intent intent = new Intent(this,GridViewColorActivity.class);
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, COLOR_PICKER);
	}
	
	/**
	 * 载入之后得到图片路径
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// 载入之后得到图片路径
		case LOAD_ACTIVITY:
			if (data != null) {
				Uri uri = data.getData();
				ContentResolver cr = this.getContentResolver();
				try {
					Bitmap bitmap;
					BitmapFactory.Options op = new BitmapFactory.Options();
					op.inJustDecodeBounds = true;
					BitmapFactory.decodeStream(cr.openInputStream(uri), null,
							op);
					int wRatio = (int) Math.ceil(op.outWidth
							/ (float) mPaintPanel.getWidth());
					int hRatio = (int) Math.ceil(op.outHeight
							/ (float) mPaintPanel.getHeight());
					// 如果超出指定大小，则缩小相应的比例
					if (wRatio > 1 && hRatio > 1) {
						if (wRatio > hRatio) {
							op.inSampleSize = wRatio;
						} else {
							op.inSampleSize = hRatio;
						}
					}
					op.inJustDecodeBounds = false;
					bitmap = BitmapFactory.decodeStream(
							cr.openInputStream(uri), null, op);
					bitmap = BitmapFactory
							.decodeStream(cr.openInputStream(uri));
					mPaintPanel.setForeBitMap(bitmap);
					mPaintPanel.resetState();
					//upDateUndoRedo();
					if (bitmap != null && !bitmap.isRecycled()) {
						bitmap.recycle();
						bitmap = null;
					}
				} catch (Exception e) {
					return;
				}
			}
			break;
			// 设置画笔颜色
		case COLOR_PICKER:
			if(data != null){
				int color = data.getIntExtra("color", Color.BLACK);
				mPaintPanel.setDrawColor(color);
				mColorState.setBackgroundColor(color);
			}
			
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	
	public void onDestroy(){
		Slog.i(TAG,"onDestroy");
		super.onDestroy();
	}
	
	/**
	 * 画板回调函数
	 * 当一次绘制完成后被调用
	 */
	public void onDrawDone() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 画板回调函数，当手指按下画板后被调用
	 */
	public void onTouchDown() {
		mHasDraw = true;
		// TODO Auto-generated method stub
		/*if(mColorPickPopupWindow != null && mColorPickPopupWindow.isShowing()){
			mColorPickPopupWindow.dismiss();
		}*/
		
	}

}
