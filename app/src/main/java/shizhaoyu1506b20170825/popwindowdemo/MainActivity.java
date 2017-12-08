package shizhaoyu1506b20170825.popwindowdemo;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String[] title = {"查询", "变更", "付款", "领款"};
    private Button btn_popwindow;
    private PopupWindow popupWindow;
    private View popupWindow_view;

    private TabLayout tablayout;
    private ViewPager main_viewpager;
    private View pop_query_view;
    private View pop_change_view;
    private View pop_payment_view;
    private View pop_drawmoney_view;
    private ArrayList<View> view_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //找到控件
        initView();
        //找到Tablayout里面的控件
        iniTabLayoutView();

        initViewPagerView();
        //设置布局
        setTabLayout();
        //设置监听
        setListener();

    }

    private void initViewPagerView() {
        view_list = new ArrayList<>();
        pop_query_view = View.inflate(MainActivity.this, R.layout.pop_query_view, null);
        view_list.add(pop_query_view);
        pop_change_view = View.inflate(MainActivity.this, R.layout.pop_change_view, null);
        view_list.add(pop_change_view);
        pop_payment_view = View.inflate(MainActivity.this, R.layout.pop_payment_view, null);
        view_list.add(pop_payment_view);
        pop_drawmoney_view = View.inflate(MainActivity.this, R.layout.pop_drawmoney_view, null);
        view_list.add(pop_drawmoney_view);

    }

    private void setTabLayout() {
        setViewPagerAdapter();
        tablayout.setTabMode(TabLayout.MODE_FIXED);
        tablayout.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(tablayout,20,20);
            }
        });
        tablayout.setupWithViewPager(main_viewpager);
    }

    private void setIndicator(TabLayout tabs,int leftDip, int rightDip) {

        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());
        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }

    }


    private void setViewPagerAdapter() {

        main_viewpager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return view_list.size() ;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = view_list.get(position);
                container.addView(view);
                return  view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(view_list.get(position));
            }


            @Override
            public CharSequence getPageTitle(int position) {
                //ViewPager的页面和tablayout的tab对应
                return title[position];
            }

        });


    }

    private void iniTabLayoutView() {
        main_viewpager = popupWindow_view.findViewById(R.id.pw_viewpager);
        tablayout = popupWindow_view.findViewById(R.id.tablayout);
    }


    private void setListener() {
        btn_popwindow.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                //弹框操作
                // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
                popupWindow = new PopupWindow(popupWindow_view, 630, 450, true);
                backgroundAlpha(140);
                // 设置动画效果
//                popupWindow.setAnimationStyle(R.style.popup_window_anim);
                //设置可以获取焦点
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corners_pop));


                //设置可以触摸弹出框以外的区域
                popupWindow.setOutsideTouchable(true);
                //放在具体控件下方
//                popupWindow.showAsDropDown(mBtn,Gravity.CENTER,Gravity.RIGHT);
                // 这里是位置显示方式,在屏幕的侧
                popupWindow.showAtLocation(popupWindow_view, Gravity.CENTER, 0, 0);


            }
        });


        // 点击其他地方消失
        popupWindow_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();

                    backgroundAlpha(1.0f);
                    popupWindow = null;
                }
                return false;
            }
        });
    }

    private void initView() {
        popupWindow_view = getLayoutInflater().inflate(R.layout.pop_layout1, null, false);
        btn_popwindow = (Button) findViewById(R.id.btn_popwindow);
    }

    /**
     * 添加遮盖层的方法
     */


    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
}
