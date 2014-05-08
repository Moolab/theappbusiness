package com.theappbusiness.whoswho.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.GridView;

import com.theappbusiness.whoswho.BuildConfig;
import com.theappbusiness.whoswho.R;
import com.theappbusiness.whoswho.WhosWhoContract;
import com.theappbusiness.whoswho.adapters.EmployeesAdapter;
import com.theappbusiness.whoswho.utils.ImageCache.ImageCacheParams;
import com.theappbusiness.whoswho.utils.ImageFetcher;

public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
	
	private static final String TAG = "ImageGridFragment";
	private static final String IMAGE_CACHE_DIR = "thumbs";

	private int mCardWidth;
	private int mImageThumbSpacing;	
	private ImageFetcher mImageFetcher;
	private ImageCacheParams cacheParams;
	
	private GridView mEmployeesGrid;
	private EmployeesAdapter mAdapter;
	private int mCardHeight;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
		View inflate = inflater.inflate(R.layout.fragment_main, container, false);
        
        mEmployeesGrid = (GridView) inflate.findViewById(R.id.employees_grid);
        mEmployeesGrid.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView absListView, int scrollState) {
				// Pause fetcher to ensure smoother scrolling when flinging
				if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
					mImageFetcher.setPauseWork(true);
				} else {
					mImageFetcher.setPauseWork(false);
				}
			}
			@Override
			public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			}
		});
        
        mEmployeesGrid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (mAdapter.getNumColumns() == 0) {
					final int numColumns = (int) Math.floor(mEmployeesGrid.getWidth() / (mCardWidth + mImageThumbSpacing));
					if (numColumns > 0) {
						final int columnWidth = (mEmployeesGrid.getWidth() / numColumns) - mImageThumbSpacing;						
						mImageFetcher.setImageSize(columnWidth);						
						mAdapter.setNumColumns(numColumns);
						mAdapter.setItemHeight(mCardHeight);
						if (BuildConfig.DEBUG) {
							Log.d(TAG, "onCreateView - numColumns set to " + numColumns);
						}
					}
				}
			}
		});
        
		return inflate;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);

		mCardWidth = getResources().getDimensionPixelSize(R.dimen.employess_card_width);
		mCardHeight = getResources().getDimensionPixelSize(R.dimen.employee_card_height);
		mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.employee_spacing);		

		cacheParams = new ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);
		cacheParams.setMemCacheSizePercent(0.8f);

		mImageFetcher = new ImageFetcher(getActivity(), mCardWidth);
		mImageFetcher.setLoadingImage(null);
		mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(), cacheParams);

		mAdapter = new EmployeesAdapter(getActivity(), mImageFetcher);
        mEmployeesGrid.setAdapter(mAdapter);
		
		getLoaderManager().initLoader(0, null, this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mImageFetcher.setExitTasksEarly(false);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onPause() {
		super.onPause();
		mImageFetcher.setExitTasksEarly(true);
		mImageFetcher.flushCache();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mImageFetcher.closeCache();
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String[] projection = { 
				WhosWhoContract.Biographies._ID,
				WhosWhoContract.Biographies.COLUMN_NAME_BIO,
				WhosWhoContract.Biographies.COLUMN_NAME_NAME,
				WhosWhoContract.Biographies.COLUMN_NAME_PHOTO,
				WhosWhoContract.Biographies.COLUMN_NAME_TITLE
		};
		return new CursorLoader(getActivity(), WhosWhoContract.Biographies.CONTENT_URI, projection, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor data) {
		mAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mAdapter.swapCursor(null);
	}
}