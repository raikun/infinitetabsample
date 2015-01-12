/*
 * Copyright (C) 2015 Raikun.jp
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.raikun.library.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 無限スワイプページャーとセットで使用する、ループするタブレイアウトです。
 * @author Raikun.jp
 */
public class InfiniteTabsLayout extends HorizontalScrollView {

    private static final int TITLE_OFFSET_DIPS = 24;
    private static final int TAB_VIEW_PADDING_DIPS = 16;
    private static final int TAB_VIEW_TEXT_SIZE_SP = 12;

    /**
     * 中央に表示されるタブ項目の背景色(デフォルト値)
     */
    private static final int DEFAULT_SELECTED_INDICATOR_COLOR = 0xFF33B5E5;

    private LinearLayout mTabContainer;

    private LoopViewPager mViewPager;

    private LoopViewPager.OnPageChangeListener mViewPagerPageChangeListener;

    private Paint mCenterIndicatorPaint;

    public InfiniteTabsLayout(Context context) {
        this(context, null);
    }

    public InfiniteTabsLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InfiniteTabsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.setFillViewport(true);
        this.setWillNotDraw(false);

        // コンテナビューを作成
        this.mTabContainer = new LinearLayout(context);
        this.mTabContainer.setOrientation(LinearLayout.HORIZONTAL);
//        this.mTabContainer.setWeightSum(3);
        this.addView(this.mTabContainer, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        // 真ん中の背景を塗るためのペイントオブジェクト
        this.mCenterIndicatorPaint = new Paint();
        this.mCenterIndicatorPaint.setColor(DEFAULT_SELECTED_INDICATOR_COLOR);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // サイズ計り直し (再配置)
        double tabWidth = Math.ceil(w / 3.0);
        for( int i=0; i<this.mTabContainer.getChildCount(); i++ ){
            View v = this.mTabContainer.getChildAt(i);
            v.getLayoutParams().width = (int)tabWidth;
        }

    }

    /**
     * ビューページャーのページ切り替えに発生するイベントリスナーを指定します。<br />
     * このタブに関連づけるViewPagerのイベントリスナーは上書きされるため、代わりにこのメソッドを<br />
     * 使用してイベントリスナーを設定してください。
     * @param listener イベントリスナーオブジェクト
     */
    public void setOnPageChangeListener(LoopViewPager.OnPageChangeListener listener) {
        this.mViewPagerPageChangeListener = listener;
    }

    /**
     * このタブレイアウトに関連づけるビューページャーを指定します。
     * @param viewPager ビューページャー
     */
    public void setViewPager(LoopViewPager viewPager) {
        this.mTabContainer.removeAllViews();

        this.mViewPager = viewPager;
        if( viewPager != null ) {
            viewPager.setOnPageChangeListener(new InternalViewPagerListener());
            this.populateTabStrip();
        }
    }

    /**
     * 中央タブ項目のハイライト背景色を指定します。
     * @param color カラー値 (ARGB指定)
     */
    public void setCenterIndicatorColor(int color) {
        this.mCenterIndicatorPaint.setColor(color);
        this.invalidate();
    }

    /**
     * デフォルトタブ項目を作成するメソッド
     * @param context Application context
     * @return created tab view
     */
    protected TextView createDefaultTabView(Context context) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TAB_VIEW_TEXT_SIZE_SP);
        textView.setTypeface(Typeface.DEFAULT_BOLD);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // If we're running on Honeycomb or newer, then we can use the Theme's
            // selectableItemBackground to ensure that the View has a pressed state
            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground,
                    outValue, true);
            textView.setBackgroundResource(outValue.resourceId);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // If we're running on ICS or newer, enable all-caps to match the Action Bar tab style
            textView.setAllCaps(true);
        }

        int padding = (int) (TAB_VIEW_PADDING_DIPS * getResources().getDisplayMetrics().density);
        textView.setPadding(padding, padding, padding, padding);

        return textView;
    }

    /**
     * タブ要素を生成するプライベートメソッド
     */
    private void populateTabStrip() {
        final PagerAdapter adapter = this.mViewPager.getAdapter();
        final OnClickListener tabClickListener = new TabClickListener();

        int count = adapter.getCount();
        double tabWidth = Math.ceil(this.getWidth() / 3.0);

        if( count <= 0 ) {
            return;
        }

        // 左右に表示分でループされたタブ２つ＋スワイプ中に表示される順方向にループされたタブ１つの、３つ余分にタブUIが必要になるため、その分も生成する。
        for( int i = 0; i < (count + 3); i++) {

            View tabView = null;
            TextView tabTitleView = null;

            tabView = this.createDefaultTabView(this.getContext());

            if (tabTitleView == null && TextView.class.isInstance(tabView)) {
                tabTitleView = (TextView) tabView;
            }

            tabTitleView.setText(adapter.getPageTitle(i));
            tabView.setOnClickListener(tabClickListener);

            this.mTabContainer.addView(tabView, (int)tabWidth, LayoutParams.WRAP_CONTENT);

        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if( this.mViewPager != null ) {
            this.scrollToTab(this.mViewPager.getCurrentItem(), 0);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);

        // 最初にアクティブになるときにタブ位置を初期化（レイアウト位置確定後に呼ばれる）
        if( hasWindowFocus && this.mViewPager != null ) {
            this.scrollToTab(this.mViewPager.getCurrentItem(), 0);
        }
    }

    /**
     * タブコンテナをスクロールするプライベートメソッド
     * @param tabIndex 選択タブ
     * @param positionOffset 左スワイプ基準で、正規位置からの移動割合[0-1）
     */
    private void scrollToTab(int tabIndex, float positionOffset) {

        // タブ幅
        double tabWidth = Math.ceil(this.getWidth() / 3.0);

        // スクロール位置計算（左に表示されるタブ基準）

        // タブを中央に寄せるためのオフセット値を計算
        int offset = (this.getWidth() - (int)tabWidth) / 2;

        // 中央表示タブ
        int centerTabIndex = tabIndex + 1;
        View centerTab = this.mTabContainer.getChildAt(centerTabIndex);

        if( centerTab != null ) {

            // スクロール位置計算
            int left = centerTab.getLeft() + (int) (Math.round(tabWidth * positionOffset)) - offset;

            // スクロール
            this.scrollTo(left, 0);

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        final int height = this.getHeight();
        final int width = this.getWidth();

        // タブ幅計算
        double tabWidth = Math.ceil(width / 3.0);

        // タブを中央に寄せるためのオフセット値を計算
        int offset = (width - (int)tabWidth) / 2;

        // 現在のスクロール位置を取得し、オフセットに加算
        offset += this.getScrollX();

        // 中央部分を塗りつぶす
        canvas.drawRect(offset, 0, (float) (offset + tabWidth), height, this.mCenterIndicatorPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false; // スワイプによるスクロール禁止
    }

    /**
     * ビューページャーのページ切り替え時に発生するイベントを受け取るためのプライベートクラスです。
     */
    private class InternalViewPagerListener implements LoopViewPager.OnPageChangeListener {

        private int mScrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            scrollToTab(position, positionOffset);

            if( mViewPagerPageChangeListener != null ) {
                mViewPagerPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {

            if( mViewPagerPageChangeListener != null ) {
                mViewPagerPageChangeListener.onPageSelected(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            this.mScrollState = state;

            if( mViewPagerPageChangeListener != null ) {
                mViewPagerPageChangeListener.onPageScrollStateChanged(state);
            }

        }

    }

    /**
     * 生成したタブ要素がタップされたときに受け取るイベントリスナー
     */
    private class TabClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {

            // 何番目のタブがタップされたかチェック
            for (int i = 0; i < mTabContainer.getChildCount(); i++) {
                if (v == mTabContainer.getChildAt(i)) {
                    mViewPager.setCurrentItem(i - 1); // 項番が１ずれるのでマイナスして、対応した番号のページに飛ぶ
                    return;
                }
            }

        }
    }
}
