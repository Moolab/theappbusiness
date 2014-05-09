package com.theappbusiness.whoswho.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;

import com.theappbusiness.whoswho.R;
import com.theappbusiness.whoswho.WhosWhoContract;
import com.theappbusiness.whoswho.adapters.EmployeesAdapter;
import com.theappbusiness.whoswho.utils.ImageCache.ImageCacheParams;
import com.theappbusiness.whoswho.utils.ImageFetcher;

/**
 * 
 * @author lucas
 *
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
	
	private static final String IMAGE_CACHE_DIR = "thumbs";
	
	/**
	 * Image Fetcher
	 */
	private ImageFetcher mImageFetcher;
	
	/**
	 * Cache image for use in offline mode
	 */
	private ImageCacheParams cacheParams;
	
	private GridView mEmployeesGrid;
	
	/**
	 * Grid Adapter for employees
	 */
	private EmployeesAdapter mAdapter;	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
		View inflate = inflater.inflate(R.layout.fragment_main, container, false);
        
        mEmployeesGrid = (GridView) inflate.findViewById(R.id.employees_grid);
        
        /*
         * On scrolling pause the image fetcher, to improve the performance.
         */
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
        
		return inflate;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);		

		cacheParams = new ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);
		cacheParams.setMemCacheSizePercent(0.8f);
		
		mImageFetcher = new ImageFetcher(getActivity(), getResources().getDimensionPixelSize(R.dimen.photo_size));
		mImageFetcher.setLoadingImage(null);
		mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(), cacheParams);

		mAdapter = new EmployeesAdapter(getActivity(), mImageFetcher);
        mEmployeesGrid.setAdapter(mAdapter);
		
        /*
         * Starting the cursor loader
         */
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
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
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
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		mAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
	}
}