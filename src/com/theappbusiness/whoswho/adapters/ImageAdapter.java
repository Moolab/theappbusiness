package com.theappbusiness.whoswho.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.theappbusiness.whoswho.R;
import com.theappbusiness.whoswho.WhosWhoContract;
import com.theappbusiness.whoswho.utils.Fonts;
import com.theappbusiness.whoswho.utils.ImageFetcher;

public class ImageAdapter extends CursorAdapter {

	private final Context mContext;
	private int mItemHeight = 0;
	private int mNumColumns = 0;
	private GridView.LayoutParams mImageViewLayoutParams;

	private LayoutInflater inflater;
	private ImageFetcher mImageFetcher;

	public ImageAdapter(Context context, ImageFetcher mImageFetcher) {
		super(context, null, true);
		mContext = context;
		this.mImageFetcher = mImageFetcher;
		mImageViewLayoutParams = new GridView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
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
			convertView.setLayoutParams(mImageViewLayoutParams);

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

		// Check the height matches our calculated column width
		if (convertView.getLayoutParams().height != mItemHeight) {
			convertView.setLayoutParams(mImageViewLayoutParams);
		}

		Cursor item = getItem(position);

		String photo = item.getString(item.getColumnIndex(WhosWhoContract.Biographies.COLUMN_NAME_PHOTO));
		if (photo != null) {
			mImageFetcher.loadImage(photo, viewHolder.photo, true);
		} else {
			viewHolder.photo.setImageResource(R.drawable.empty_photo);
		}

		viewHolder.name.setText(item.getString(item.getColumnIndex(WhosWhoContract.Biographies.COLUMN_NAME_NAME)));
		viewHolder.title.setText(item.getString(item.getColumnIndex(WhosWhoContract.Biographies.COLUMN_NAME_TITLE)));
		viewHolder.name.setText(item.getString(item.getColumnIndex(WhosWhoContract.Biographies.COLUMN_NAME_NAME)));
		viewHolder.bio.setText(item.getString(item.getColumnIndex(WhosWhoContract.Biographies.COLUMN_NAME_BIO)));

		return convertView;
	}

	/**
	 * Sets the item height. Useful for when we know the column width so the
	 * height can be set to match.
	 * 
	 * @param height
	 */
	public void setItemHeight(int height) {
		if (height == mItemHeight) {
			return;
		}
		mItemHeight = height;
		mImageViewLayoutParams = new GridView.LayoutParams(LayoutParams.MATCH_PARENT, mItemHeight);
		notifyDataSetChanged();
	}

	public void setNumColumns(int numColumns) {
		mNumColumns = numColumns;
	}

	public int getNumColumns() {
		return mNumColumns;
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