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

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import jp.raikun.library.widget.LoopViewPager;

public class TestPagerAdapter extends FragmentPagerAdapter {

    private static final int PAGE_MAX = 5;

    public TestPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        i = LoopViewPager.toRealPosition(i, this.getCount());
        return TestFragment.newInstance(i);
    }

    @Override
    public int getCount() {
        return PAGE_MAX;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        position = LoopViewPager.toRealPosition(position, this.getCount());

        if( position < 0 || position >= PAGE_MAX ) {
            throw new IllegalArgumentException("ページ数が異常です！");
        }

        return "Page " + (position + 1);

    }
}
