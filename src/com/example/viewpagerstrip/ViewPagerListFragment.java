package com.example.viewpagerstrip;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;


public class ViewPagerListFragment extends Fragment implements TabHolderScrollingContent{
	private ArrayList<String> mItems = new ArrayList<String>();
	private ListView list;
	private OnScrollListener mListener;
	private StickyListHeaderView stickyListHeaderView;
	private View headerView;
	private int index;
	private boolean isTouch_Scroll;
	private ListAdapter adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		index = getArguments().getInt("index");
		mItems.add("역지사지");
		mItems.add("연목구어");
		mItems.add("연하고질");
		mItems.add("염량세태");
		mItems.add("우후죽순");
		mItems.add("화사첨족");
		mItems.add("천태만상");
		mItems.add("어부지리");
		mItems.add("일거일동");
		mItems.add("이구동성");
		mItems.add("삼라만상");
		mItems.add("일거일득");
		mItems.add("일문일답");
		mItems.add("일목십항");
		mItems.add("일사일생");
		mItems.add("사면팔방");
		mItems.add("십중팔구");
		mItems.add("호가호위");
		mItems.add("천근만근");
		mItems.add("천차만별");
		mItems.add("가가호호");
		mItems.add("가인박명");
		mItems.add("간난신고");

	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final Activity a = getActivity();
		View view =  getView();
		list = (ListView)view.findViewById(R.id.listView1);
		headerView = (View) LayoutInflater.from(a).inflate(R.layout.view_fake_header, list, false);
		list.addHeaderView(headerView);
		final int index = getArguments().getInt("index");
		adapter = new ListAdapter(getActivity(), mItems);
		list.setAdapter(adapter);
		list.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				Log.v("DEBUG32","리스트뷰스크롤");
				if(isTouch_Scroll)
					mListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount,index);
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if(scrollState != SCROLL_STATE_IDLE){
					isTouch_Scroll = true;
				}else{
					isTouch_Scroll = false;
				}
				Log.v("DEBUG32","리스트뷰스크롤상태:"+scrollState);
			}
		});
	}
	@Override
	public void adjustScroll(int tabBarTop) {
		// TODO Auto-generated method stub
		if (tabBarTop == 0 && list.getFirstVisiblePosition() >= 1) {
			return;
		}
		list.setSelectionFromTop(1, tabBarTop);
	}
	public void onScrollListener(OnScrollListener listener){
		this.mListener = listener;
	}
	private class StickyListHeaderView extends LinearLayout implements AbsListView.OnScrollListener {

		private boolean mAlreadyInflated = false;

		private View imageView1;
		private View placeholderView;

		public StickyListHeaderView(Context context) {
			super(context);
		}

		private void afterSetContent() {
			imageView1 = findViewById(R.id.imageView1);
			placeholderView = findViewById(R.id.placeholderView);
		}

		@Override
		public void onFinishInflate() {
			if (!mAlreadyInflated) {
				mAlreadyInflated = true;
				inflate(getContext(), R.layout.view_sticky_list_header, this);
				afterSetContent();
			}
			super.onFinishInflate();
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			View localView = view.getChildAt(0);
			if (localView == this) {
				int i = -localView.getTop() / 2;
				this.imageView1.setTranslationY(i);
			}

			stickHeader(view);
		}

		private void stickHeader(AbsListView view) {
			int scrollY = getScrollY(view);
			Log.d("DEBUG32", "스크롤: " + Math.max(0, placeholderView.getTop() - scrollY));
		}

		public int getScrollY(AbsListView view) {
			View c = view.getChildAt(0);
			if (c == null) {
				return 0;
			}

			int firstVisiblePosition = view.getFirstVisiblePosition();
			int top = c.getTop();

			int headerHeight = 0;
			if (firstVisiblePosition >= 1) {
				headerHeight = stickyListHeaderView.getHeight();
			}

			return -top + firstVisiblePosition * c.getHeight() + headerHeight;
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}

	}

	public StickyListHeaderView buildStickyListHeaderView(Context context) {
		StickyListHeaderView instance = new StickyListHeaderView(context);
		instance.onFinishInflate();
		return instance;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}
}
