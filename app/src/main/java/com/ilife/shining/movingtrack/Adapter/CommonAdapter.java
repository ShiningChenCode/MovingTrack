package com.ilife.shining.movingtrack.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 通用的Adapter类
 * @author Administrator
 *
 * @param <T>
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
	protected Context mContext;
	protected List<T> mDatas;
	protected LayoutInflater mInflater;
	protected int mLayoutId;

	public CommonAdapter(Context context, List datas,int layoutId) {
		this.mContext = context;
		this.mDatas = datas;
		this.mLayoutId = layoutId;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mDatas.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder viewHolder = ViewHolder.getViewHolder(mContext, arg1, arg2,mLayoutId, arg0);
		convert(viewHolder, mDatas.get(arg0),arg0);
		return viewHolder.getConvertView();
	}

	/**
	 * 子类须要实现的方法
	 * @param viewHolder
	 * @param t
	 */
	public abstract void convert(ViewHolder viewHolder, T t,int position);
}
