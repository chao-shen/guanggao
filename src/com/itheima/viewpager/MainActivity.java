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
			"���������ף��ҾͲ��ܵ���",
			"�����ֻ��������ٳ������ϸ������˴�ϳ�",
			"���ر�����Ӱ�������",
			"������TV�������",
			"��Ѫ��˿�ķ�ɱ",
	};
	
	private View[] dots = new View[imageResIds.length];
	
	private ImageView[] images = new ImageView[imageResIds.length];
	/** ��ǰѡ�еĵ� */
	private View currentDot;
	/** ViewPager��ҳ�� */
	private int pageSize = imageResIds.length * 10000 * 100;
	
	private int halfPageSize = pageSize / 2;
	
	/** �Զ��л�ͼƬ */
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

    /** �Ƴ��л�ͼƬ����Ϣ */
	private void removeSiwtchImageMessage() {
		handler.removeMessages(AUTO_SWITCH_IMAGE);
	}
    
    /** �л�ͼƬ */
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
     * �����л�ͼƬ����Ϣ
     */
	private void sendSwitchImageMessage() {
		handler.sendEmptyMessageDelayed(AUTO_SWITCH_IMAGE, 3000);
	}

	OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {
		
    	// ��ĳһҳ��ѡ���ʱ��
		@Override
		public void onPageSelected(int position) {
			changeDescAndDot(position);
		}
		
		// ��ҳ�滬����ʱ��
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			
		}
		
		// ��ҳ�滬��״̬�����ı��ʱ��
		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE) {
				sendSwitchImageMessage();
			} else {
				// ��ǰ���ǿ���״̬
				removeSiwtchImageMessage();
			}
		}
	};

    /**
     * �ı�ͼƬ�����͵�
     * @param position
     */
    private void changeDescAndDot(int position) {
    	position = position % images.length;	// ��position�� 0 ~ images.length - 1֮��
    	tv_desc.setText(descs[position]);
    	
    	// ���ԭ����ѡ�еĵ㣬��ԭ���ı��δѡ��״̬
    	if (currentDot != null) {
    		currentDot.setSelected(false);
		}
    	
    	// �ѵ�ǰλ�õĵ���ѡ��״̬
    	dots[position].setSelected(true);
    	currentDot = dots[position];
    	
	}

	/** ��ʼ��ViewPager����ĵ� */
	private void initDots(int i) {
		dots[i] = new View(this);
		LinearLayout.LayoutParams params = new LayoutParams(5, 5);
		if (i != 0) {
			// ������ǵ�һ����Ҫ����marginLeft����
			params.leftMargin = 5;
		}
		dots[i].setLayoutParams(params);
		dots[i].setBackgroundResource(R.drawable.selector_dot);
		ll_dots.addView(dots[i]);
	}

    /** ��ʼ��ͼƬ */
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
		 * ����Ҫ��ʾ�Ľ���
		 * �൱��BaseAdapter�е�getView
		 * @param container ViewPager����
		 * @param position Ҫ���ɽ����λ��
		 * 
		 */
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			position = position % images.length;	// ��position�� 0 ~ images.length - 1֮��
			ImageView imageView = images[position];
			container.addView(imageView);
			return imageView;
		};
		
		/**
		 * ����һ������
		 * @param container ViewPager����
		 * @param position Ҫ���ٽ����λ��
		 * @param object instantiateItem�����ķ���ֵ
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
