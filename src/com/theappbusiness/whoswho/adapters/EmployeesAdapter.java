package com.theappbusiness.whoswho.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.theappbusiness.whoswho.R;
import com.theappbusiness.whoswho.WhosWhoContract;
import com.theappbusiness.whoswho.utils.Fonts;
import com.theappbusiness.whoswho.utils.ImageFetcher;

public class EmployeesAdapter extends CursorAdapter {

	/*
	 * Context
	 */
	private final Context mContext;

	private LayoutInflater inflater;
	private ImageFetcher mImageFetcher;

	public EmployeesAdapter(Context context, ImageFetcher mImageFetcher) {
		super(context, null, true);
		mContext = context;
		this.mImageFetcher = mImageFetcher;
		inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		int count;
		if (getCursor() == null) {
			count = 0;
		} else {
			count = getCursor().getCount();
		}
		return count;
	}

	@Override
	public Cursor getItem(int position) {
		if (getCursor() != null) {
			getCursor().moveToPosition(position);
		}
		return getCursor();
	}

	@Override
	public long getItemId(int position) {
		Cursor cursor = getItem(position);
		if (cursor == null) {
			return 0L;
		} else {
			return cursor.getLong(cursor.getColumnIndex(WhosWhoContract.Biographies._ID));
		}
	}

	@SuppressLint("DefaultLocale")
	@Override
	public View getView(int position, View convertView, ViewGroup container) {

		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.employee, container, false);

			viewHolder = new ViewHolder();
			viewHolder.photo = (ImageView) convertView.findViewById(R.id.photo);
			
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.title.setTypeface(Fonts.getInstance().getRobotoCondensed(mContext.getAssets()));
			
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.name.setTypeface(Fonts.getInstance().getRobotoBoldCondensed(mContext.getAssets()));
			
			viewHolder.bio = (TextView) convertView.findViewById(R.id.user_description);
			viewHolder.bio.setTypeface(Fonts.getInstance().getRobotoRegular(mContext.getAssets()));

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Cursor item = getItem(position);

		String photo = item.getString(item.getColumnIndex(WhosWhoContract.Biographies.COLUMN_NAME_PHOTO));
		if (photo != null) {
			mImageFetcher.loadImage(photo, viewHolder.photo, true);
		} else {
			viewHolder.photo.setImageBitmap(null);
		}

		viewHolder.name.setText(item.getString(item.getColumnIndex(WhosWhoContract.Biographies.COLUMN_NAME_NAME)));
		viewHolder.title.setText(item.getString(item.getColumnIndex(WhosWhoContract.Biographies.COLUMN_NAME_TITLE)));
		viewHolder.name.setText(item.getString(item.getColumnIndex(WhosWhoContract.Biographies.COLUMN_NAME_NAME)));
		viewHolder.bio.setText(item.getString(item.getColumnIndex(WhosWhoContract.Biographies.COLUMN_NAME_BIO)));

		return convertView;
	}

	@Override
	public int getItemViewType(int arg0) {
		return 1;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return null;
	}

	public static class ViewHolder {
		public ImageView photo;
		public TextView title;
		public TextView name;
		public TextView bio;
	}
}