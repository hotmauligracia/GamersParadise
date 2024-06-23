package com.example.gamersparadise.customer.history;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gamersparadise.R;
import com.example.gamersparadise.customer.history.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HistoryFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);

        setupViewPager(viewPager);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Dijadwalkan");
                            break;
                        case 1:
                            tab.setText("Selesai");
                            break;
                        case 2:
                            tab.setText("Dibatalkan");
                            break;
                    }
                }).attach();

        return view;
    }

    private void setupViewPager(ViewPager2 viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        adapter.addFragment(new HistoryScheduledFragment());
        adapter.addFragment(new HistoryFinishedFragment());
        adapter.addFragment(new HistoryCancelledFragment());
        viewPager.setAdapter(adapter);
    }
}