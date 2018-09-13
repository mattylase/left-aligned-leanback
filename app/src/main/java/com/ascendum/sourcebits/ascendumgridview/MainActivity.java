/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ascendum.sourcebits.ascendumgridview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v17.leanback.widget.ListRowHoverCardView;
import android.support.v17.leanback.widget.ListRowView;
import android.support.v17.leanback.widget.VerticalGridView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {

    List<List<String>> dataSet;
    VerticalGridView view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_custom);

        prepareData();

        view = findViewById(R.id.vertical_grid_view);
        view.setAdapter(new VerticalAdapter(dataSet));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_D) {
            RowViewHolder holder = (RowViewHolder) view.findViewHolderForLayoutPosition(0);
            int index = holder.rowAdapter.incrementSelection();
            ((CustomLayoutManager)holder.listRowView.getGridView().getLayoutManager())
                    .scrollToPositionWithOffset(index, 0);
        }
        if (keyCode == KeyEvent.KEYCODE_A) {
            RowViewHolder holder = (RowViewHolder) view.findViewHolderForLayoutPosition(0);
            int index = holder.rowAdapter.decrementSelection();
            ((CustomLayoutManager)holder.listRowView.getGridView().getLayoutManager())
                    .scrollToPositionWithOffset(index, 0);
        }
        return false;
    }

    class RowViewHolder extends RecyclerView.ViewHolder {
        public List<String> data;
        public ListRowView listRowView;
        RowAdapter rowAdapter;

        public RowViewHolder(View itemView) {
            super(itemView);
            listRowView = itemView.findViewById(R.id.list_row_view);
            listRowView.getGridView().setLayoutManager(new CustomLayoutManager(itemView.getContext()));
            rowAdapter = new RowAdapter(data);

            listRowView.getGridView().setAdapter(rowAdapter);
            listRowView.getGridView().setSelectedPosition(0);
        }

        public void bindData(List<String> data) {
            rowAdapter.data = data;
            rowAdapter.notifyDataSetChanged();
        }
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ListRowHoverCardView cardView;
        public CardViewHolder(View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.card_text);
            this.cardView = itemView.findViewById(R.id.card_view);
        }
    }

    class VerticalAdapter extends RecyclerView.Adapter<RowViewHolder> {
        List<List<String>> gridData;
        VerticalAdapter(List<List<String>> data) {
            this.gridData = data;
        }

        @NonNull
        @Override
        public RowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RowViewHolder holder = new RowViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, null));
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RowViewHolder holder, int position) {
            holder.bindData(gridData.get(position));
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }
    }

    class RowAdapter extends  RecyclerView.Adapter<CardViewHolder> {
        List<String> data;
        int selectedIndex;
        RowAdapter(List<String> rowData) {
            this.data = rowData;
            selectedIndex = 0;
        }

        public int incrementSelection() {
            if (selectedIndex < data.size()) {
                selectedIndex += 1;
                notifyDataSetChanged();
            }
            return selectedIndex;
        }

        public int decrementSelection() {
            if (selectedIndex > 0) {
                selectedIndex -= 1;
                notifyDataSetChanged();
            }
            return selectedIndex;
        }

        @NonNull
        @Override
        public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            CardViewHolder holder = new CardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null));
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
            holder.textView.setText(data.get(position));
            if (selectedIndex == position) {
                holder.cardView.setBackgroundColor(Color.RED);
            } else {
                holder.cardView.setBackgroundColor(Color.GREEN);
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    class CustomLayoutManager extends LinearLayoutManager {
        public CustomLayoutManager(Context context) {
            super(context);
            init();
        }

        public CustomLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
            init();
        }

        public CustomLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            init();
        }

        void init() {
            this.setOrientation(LinearLayoutManager.HORIZONTAL);
        }
    }

    void prepareData() {
        List<String> list1 = Arrays.asList("Red", "Orange", "Yellow", "Green", "Blue", "Indigo", "Violet");
        List<String> list2 = Arrays.asList("Dog", "Cat", "Fish", "Bird", "Elephant");
        List<String> list3 = Arrays.asList("Spaghetti", "Pizza", "Ham Sam", "Taco", "Ice Cream", "Hamburger", "Hotdog");
        List<String> list4 = Arrays.asList("Bud", "Miller", "Coors", "Milbest", "CBC IPA", "Hop Stoopid", "Thirsty Dog", "Avery", "Seventh Son");
        dataSet = Arrays.asList(list1, list2, list3, list4);
    }
}
