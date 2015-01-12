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

package jp.raikun.sample.infinitetabsample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import jp.raikun.library.widget.LoopViewPager;
import jp.raikun.library.widget.InfiniteTabsLayout;


public class MainActivity extends ActionBarActivity {

    InfiniteTabsLayout mInfiniteTab;

    LoopViewPager mViewPager;

    TestPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mPagerAdapter = new TestPagerAdapter(this.getSupportFragmentManager());

        this.mInfiniteTab = (InfiniteTabsLayout)this.findViewById(R.id.sliding_tabs);
        this.mViewPager = (LoopViewPager)this.findViewById(R.id.viewpager);

        this.mViewPager.setAdapter(this.mPagerAdapter);

        this.mInfiniteTab.setViewPager(this.mViewPager);
    }

}
