package com.example.viewpagerstrip;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.example.viewpagerstrip.PageStripViewPager.State;
import com.nineoldandroids.view.ViewHelper;

public class MainActivity extends FragmentActivity implements OnScrollListener{
	private SlidingTabLayout slidingTab;
	private SparseArrayCompat<TabHolderScrollingContent> tabHolderScrollingContent = new SparseArrayCompat<TabHolderScrollingContent>();
	private PageStripViewPager viewPager;
	private SectionPagerBottomAdapter adapter;
	private ViewGroup header;
	private ImageView imageview;
	private int scrollY;
	private boolean isDragging;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		header = (ViewGroup) findViewById(R.id.header);
		imageview = (ImageView) findViewById(R.id.imageView1);
		slidingTab = (SlidingTabLayout) findViewById(R.id.tabstrip);
		viewPager = (PageStripViewPager)findViewById(R.id.view_pager);
		adapter = new SectionPagerBottomAdapter(getSupportFragmentManager());
		viewPager.setOffscreenPageLimit(adapter.getCount());
		viewPager.setAdapter(adapter);
		slidingTab.setViewPager(viewPager);
		slidingTab.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int state) {
				if ( state == ViewPager.SCROLL_STATE_DRAGGING  ) {
					isDragging = true;
					if(scrollY > slidingTab.getHeight()){
						TabHolderScrollingContent fragmentContent = tabHolderScrollingContent.valueAt(viewPager.getCurrentItem());
						fragmentContent.adjustScroll(scrollY);
					}
				}else{
					isDragging = false;
				}
			}
			@Override
			public void onPageSelected(int position) {
				super.onPageSelected(position);
				TabHolderScrollingContent fragmentContent = null;
				fragmentContent = tabHolderScrollingContent.valueAt(position);
				fragmentContent.adjustScroll(scrollY);
			}
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				super.onPageScrolled(position, positionOffset, positionOffsetPixels);
				if (positionOffsetPixels > 0) {
					TabHolderScrollingContent fragmentContent = null;
					if(viewPager.getState() == State.GOING_RIGHT){
						fragmentContent = tabHolderScrollingContent.valueAt(position + 1);
					}else{
						fragmentContent = tabHolderScrollingContent.valueAt(position);
					}
//					Log.v("DEBUG32","positionOffset :" +positionOffset+",mState :"+viewPager.getState()+", position : " + position + " , currentItem : " + viewPager.getCurrentItem());
					fragmentContent.adjustScroll(scrollY);
				}
			}
		});
	}

	private boolean isSmall(float positionOffset) {
		return Math.abs(positionOffset) < 0.0001;
	}
	public class SectionPagerBottomAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = { "Categories", "Home", "Top Paid", "Top Free", "Top Grossing", "Top New Paid", "Top New Free", "Trending" };

		public SectionPagerBottomAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public Fragment getItem(int position) {
			Bundle args = new Bundle();
			args.putInt("index", position);
			ViewPagerListFragment fragemt = new ViewPagerListFragment();
			fragemt.onScrollListener(MainActivity.this);
			fragemt.setArguments(args);
			tabHolderScrollingContent.put(position, fragemt);
			return fragemt;
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pageIdx) {
		if(pageIdx == viewPager.getCurrentItem() && !isDragging){
			final int maxScrollHeight = header.getHeight() - slidingTab.getHeight();
			if (firstVisibleItem != 0) {
				return;
			}
			final View firstChild = view.getChildAt(firstVisibleItem);
			if (firstChild == null) {
				return;
			}
			int y = header.getHeight() - getScrollY(view);
			View localView = view.getChildAt(0);
			if ( y >= slidingTab.getHeight() ) {
				scrollY = y;
				Log.v("DEBUG32","scrollY11 : " +scrollY);
//				Log.v("DEBUG32","스크롤111 : " +scrollY +", 계산 : " + ( -header.getHeight() + scrollY));
				int i = -firstChild.getTop() / 2;
				ViewHelper.setTranslationY(imageview,i);
				ViewHelper.setTranslationY(header, -header.getHeight() + scrollY);
			}else{
				scrollY = slidingTab.getHeight(); 
				Log.v("DEBUG32","scrollY22 : " +scrollY);
				ViewHelper.setTranslationY(header, -maxScrollHeight);
			}
				
		}
		
	}
	public int getScrollY(AbsListView view) {
		View c = view.getChildAt(0);
		if (c == null) {
			return 0;
		}
		int firstVisiblePosition = view.getFirstVisiblePosition();
		int top = c.getTop();
		Log.v("DEBUG32","top :" + c.getTop());
		int headerHeight = 0;
		if (firstVisiblePosition >= 1) {
			headerHeight = header.getHeight();
		}
		return -top + firstVisiblePosition * c.getHeight() + headerHeight;
	}
}
