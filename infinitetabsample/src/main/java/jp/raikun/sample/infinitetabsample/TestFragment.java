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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class TestFragment extends Fragment {

    private static final String ARG_PAGE_NO = "ARG_PAGE_NO";

    private int mPageNo;

    public static TestFragment newInstance(int page) {

        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PAGE_NO, page);

        TestFragment fragment = new TestFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    public TestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if( bundle != null ) {
            this.mPageNo = bundle.getInt(ARG_PAGE_NO);
        }
        else {
            this.mPageNo = 0;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tv = (TextView)view.findViewById(R.id.textViewPage);
        tv.setText( this.getResources().getString(R.string.fragment_page_title, this.mPageNo + 1) );

    }
}
