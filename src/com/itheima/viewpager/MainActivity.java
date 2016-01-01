package com.itheima.viewpager;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.itheima.viewpager77.R;

public class MainActivity extends Activity {
	
	private int[] imageResIds = {
			R.drawable.a,
			R.drawable.b,
			R.drawable.c,
			R.drawable.d,
			R.drawable.e,
	};

	private String[] descs = {
			"巩俐不低俗，我就不能低俗",
			"扑树又回来啦！再唱经典老歌引万人大合唱",
			"揭秘北京电影如何升级",
			"乐视网TV版大派送",
			"热血屌丝的反杀",
	};
	
	private View[] dots = new View[imageResIds.length];
	
	private ImageView[] images = new ImageView[imageResIds.length];
	/** 当前选中的点 */
	private View currentDot;
	/** ViewPager的页数 */
	private int pageSize = imageResIds.length * 10000 * 100;
	
	private int halfPageSize = pageSize / 2;
	
	/** 自动切换图片 */
	private static final int AUTO_SWITCH_IMAGE = 0;
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AUTO_SWITCH_IMAGE:
				swithImage();
				break;

			default:
				break;
			}
		};
	};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        ll_dots = (LinearLayout) findViewById(R.id.ll_dots);
        
        for (int i = 0; i < imageResIds.length; i++) {
			initImages(i);
			initDots(i);
		}
        
		view_pager.setOnPageChangeListener(mOnPageChangeListener);
        
        changeDescAndDot(0);
        
        view_pager.setAdapter(adapter);
        view_pager.setCurrentItem(halfPageSize);
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    	sendSwitchImageMessage();
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	removeSiwtchImageMessage();
    }

    /** 移除切换图片的消息 */
	private void removeSiwtchImageMessage() {
		handler.removeMessages(AUTO_SWITCH_IMAGE);
	}
    
    /** 切换图片 */
    protected void swithImage() {
    	int currentItem = view_pager.getCurrentItem();
    	if (currentItem == view_pager.getAdapter().getCount() - 1) {
    		currentItem = 0;
		} else {
			currentItem++;
		}
    	view_pager.setCurrentItem(currentItem);
    	sendSwitchImageMessage();
	}

    /**
     * 发送切换图片的消息
     */
	private void sendSwitchImageMessage() {
		handler.sendEmptyMessageDelayed(AUTO_SWITCH_IMAGE, 3000);
	}

	OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {
		
    	// 当某一页被选择的时候
		@Override
		public void onPageSelected(int position) {
			changeDescAndDot(position);
		}
		
		// 当页面滑动的时候
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			
		}
		
		// 当页面滑动状态发生改变的时候
		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE) {
				sendSwitchImageMessage();
			} else {
				// 当前不是空闲状态
				removeSiwtchImageMessage();
			}
		}
	};

    /**
     * 改变图片描述和点
     * @param position
     */
    private void changeDescAndDot(int position) {
    	position = position % images.length;	// 让position在 0 ~ images.length - 1之间
    	tv_desc.setText(descs[position]);
    	
    	// 如果原来有选中的点，把原来的变成未选择状态
    	if (currentDot != null) {
    		currentDot.setSelected(false);
		}
    	
    	// 把当前位置的点变成选择状态
    	dots[position].setSelected(true);
    	currentDot = dots[position];
    	
	}

	/** 初始化ViewPager里面的点 */
	private void initDots(int i) {
		dots[i] = new View(this);
		LinearLayout.LayoutParams params = new LayoutParams(5, 5);
		if (i != 0) {
			// 如果不是第一个点要设置marginLeft属性
			params.leftMargin = 5;
		}
		dots[i].setLayoutParams(params);
		dots[i].setBackgroundResource(R.drawable.selector_dot);
		ll_dots.addView(dots[i]);
	}

    /** 初始化图片 */
	private void initImages(int i) {
		images[i] = new ImageView(this);
		images[i].setBackgroundResource(imageResIds[i]);
	}
    
    PagerAdapter adapter = new PagerAdapter() {

		@Override
		public int getCount() {
			return pageSize;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		
		/**
		 * 生成要显示的界面
		 * 相当于BaseAdapter中的getView
		 * @param container ViewPager对象
		 * @param position 要生成界面的位置
		 * 
		 */
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			position = position % images.length;	// 让position在 0 ~ images.length - 1之间
			ImageView imageView = images[position];
			container.addView(imageView);
			return imageView;
		};
		
		/**
		 * 销毁一个界面
		 * @param container ViewPager对象
		 * @param position 要销毁界面的位置
		 * @param object instantiateItem方法的返回值
		 */
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		};
		
		

	};

	private TextView tv_desc;

	private ViewPager view_pager;

	private LinearLayout ll_dots;

}
