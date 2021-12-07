package com.kyonggi.restaurantmemo.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.kyonggi.restaurantmemo.Adapter.ItemListAdapter;
import com.kyonggi.restaurantmemo.R;
import com.kyonggi.restaurantmemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private BottomNavigationView bottomNavigationView;
    private Fragment fragment;
    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // 네비게이션 붙여주기
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        // fragment 기본 init
        fragment = new ItemListFragment();
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_frame, new ItemListFragment()).commit();

        // 바텀 네비게이션 클릭시 fragment 전환
        bottomNavigationView.setOnItemSelectedListener(item -> {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.menu_home:
                    fragment = new ItemListFragment();
                    transaction.replace(R.id.main_frame, fragment).commit();
                    return true;
                case R.id.menu_setting:
                    fragment = new SettingsFragment();
                    transaction.replace(R.id.main_frame,fragment).commitNow();
                    return true;
            }
            return false;
        });
    }

}