package com.king.draw.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.king.draw.R;

import com.king.draw.interfaces.IOnButtonListener;
import com.king.draw.utils.Slog;

/**
 * 自定义对话框
 * @author simon
 *
 */
public class YesNoDialog extends Dialog implements
		android.view.View.OnClickListener {
	private static final String TAG = "KingDraw_Dialog";
	private IOnButtonListener mListener;
	private ImageView mImageView;
	private ImageButton mOkButton;
	private ImageButton mCancelButton;
	private Context mContext;
	private TextView mTextView;
	private String mTiltle;

	public YesNoDialog(Context context, IOnButtonListener listener, String title) {
		super(context);
		Slog.i(TAG, "Dialog create");
		mListener = listener;
		mContext = context;
		mTiltle = title;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Slog.i(TAG, "onCreate");
		setContentView(R.layout.okcancledialog);
		Window window = this.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.alpha = 0.9f;
		window.setAttributes(lp);

		mOkButton = (ImageButton) findViewById(R.id.imageButtonOkCancleDialogOk);
		mCancelButton = (ImageButton) findViewById(R.id.imageButtonOkCancleDialogCancle);
		mImageView = (ImageView) findViewById(R.id.imageViewOkCancleDialog);
		mTextView = (TextView) findViewById(R.id.textViewOkCanleDialogMsg);

		mImageView.setImageResource(R.drawable.caution);
		mTextView.setTextSize(17);
		mOkButton.setBackgroundDrawable(mContext.getResources().getDrawable(
				R.drawable.finished_nor));
		mOkButton.setOnClickListener(this);
		mCancelButton.setBackgroundDrawable(mContext.getResources()
				.getDrawable(R.drawable.cancel_nor));
		mCancelButton.setOnClickListener(this);
		this.setTitle(mTiltle);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageButtonOkCancleDialogOk:
			mListener.onButtonClick(IOnButtonListener.YES);
			dismiss();
			break;
		case R.id.imageButtonOkCancleDialogCancle:
			mListener.onButtonClick(IOnButtonListener.NO);
			dismiss();
			break;
		default:
			break;
		}
	}

	public void setMessage(String msg) {
		Slog.i(TAG, "setMessage");
		mTextView.setText(msg);
	}
}
