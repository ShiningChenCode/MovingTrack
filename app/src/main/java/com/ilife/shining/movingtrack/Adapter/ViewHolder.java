package com.ilife.shining.movingtrack.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ilife.shining.movingtrack.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * ViewHolder 类
 * 
 * @author Administrator
 * 
 */
public class ViewHolder {
	private SparseArray<View> mViews;
	private int mPosition;
	protected View mConvertView;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	private DisplayImageOptions options;

	public ViewHolder(Context context, ViewGroup parent, int layoutId,
			int postion) {
		this.mPosition = postion;
		this.mConvertView = LayoutInflater.from(context).inflate(layoutId,
				parent, false);
		this.mViews = new SparseArray<View>();
		mConvertView.setTag(this);

		options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.build();
	}




	/**
	 *为了不用每次使用都New一个ViewHolder对象，采用复用机制
	 * 
	 * @param context
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 * @param postion
	 * @return
	 */
	public static ViewHolder getViewHolder(Context context, View convertView,
			ViewGroup parent, int layoutId, int postion) {
		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId, postion);
		} else {
			ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.mPosition = postion;
			return viewHolder;
		}

	}

	/**
	 * 根据ViewID获取控件
	 * 
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	/**
	 * 
	 * @return 返回处理后的View
	 */
	public View getConvertView() {
		return mConvertView;

	}
	
	/**
	 * 设置TextView的文本内容
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setText(int viewId, String text) {
		TextView tv = (TextView) getView(viewId) ;
		tv.setText(text);
		return this;
	}

	/**
	 * 设置TextView的文本颜色
	 * @param viewId
	 * @param colorId
	 * @return
	 */
	public ViewHolder setTextColor(int viewId, int colorId) {
		TextView tv = (TextView) getView(viewId) ;
		tv.setTextColor(colorId);
		return this;
	}
	
	/**
	 * 设置ImageView的图片形式
	 * @param viewId
	 * @param resId
	 * @return
	 */
	public ViewHolder setImageResource(int viewId, int resId) {
		ImageView iv = (ImageView) getView(viewId) ;
		iv.setImageResource(resId);
		return this;

	}
	
	public ViewHolder setImageUrl(int viewId,String url) {
		ImageView iv = (ImageView) getView(viewId) ;


		ImageLoader.getInstance().displayImage(url,iv, options, animateFirstListener);
		return this;
	}



	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
	

}
