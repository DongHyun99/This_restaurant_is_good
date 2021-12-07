package com.kyonggi.restaurantmemo.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kyonggi.restaurantmemo.Adapter.ItemListAdapter;
import com.kyonggi.restaurantmemo.Data.Item;
import com.kyonggi.restaurantmemo.R;
import com.kyonggi.restaurantmemo.Room.ItemDatabase;
import com.kyonggi.restaurantmemo.databinding.FragmentItemListBinding;

import java.util.ArrayList;
import java.util.List;


public class ItemListFragment extends Fragment {

    FragmentItemListBinding binding;
    RecyclerView itemListRecyclerView;
    ItemListAdapter itemListAdapter;
    SearchView searchView;
    Menu menu;
    List<Item> itemList;
    ItemDatabase itemDatabase;

    // favorite 저장
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    // fragment로 다시 돌아옴
    @Override
    public void onResume() {
        checkFavorite();
        refreshFragment();
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentItemListBinding.inflate(inflater,container,false);
        // 메뉴 등록
        menu = binding.mainToolbar.toolbar.getMenu();
        // SharedPreference 등록
        initSharedPreferences();
        initItemList();
        // 맛집 리스트 가져오기
        itemDatabase = ItemDatabase.getInstance(getContext());
        // toolbar favorite 클릭되어있다면


        itemList = itemDatabase.itemDAO().getAll();
        itemListAdapter.setData(itemList);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Init Action
        onClickFab();
        checkList();
        // toolbar 사용
        initToolbar();
        initSearch();
    }

    // search를 위한 Init
    private void initSearch() {
        searchView = (SearchView) menu.findItem(R.id.navi_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        // 검색 버튼 클릭시 searchView 힌트
        searchView.setQueryHint("맛집이름으로 검색합니다.");
        // 검색처리가 가능하기 위한 설정
        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String name) {
                itemList = itemDatabase.itemDAO().getItemByName(name);
                itemListAdapter.setData(itemList);
                itemListAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String name) {
                if (name.equals("")) {
                    checkFavorite();
                }
                return true;
            }
        });
    }
    // 툴바 사용을 위한 Init
    private void initToolbar() {
        // 툴바 title
        binding.mainToolbar.toolbar.setTitle("맛집 모음!");
        // 툴바 클릭 Action
        binding.mainToolbar.toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.navi_favorite:
                    onClickFavorite();
                    checkFavorite();
                    return true;
                default:
                    return false;
            }
        });
    }

    // 리스트의 여부 확인
    public void checkList() {
        if (itemListAdapter.getItemCount() == 0) {
            binding.noticeEmptyList.setVisibility(View.VISIBLE);
        } else {
            binding.noticeEmptyList.setVisibility(View.GONE);
        }
    }

    // 맛집 추가 버튼
    private void onClickFab() {
        binding.addButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), AddItemActivity.class);
            startActivity(intent);
        });
    }

    public void refreshFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
        checkList();
    }

    /*
    리사이클러뷰로 보여줌 -> 없으면 없다고 보여줌
     */
    private void initItemList() {
        itemListRecyclerView = binding.itemListRecyclerView;
        itemListAdapter = new ItemListAdapter(getContext());

        // adapter 설정
        itemListRecyclerView.setAdapter(itemListAdapter);
        // LayoutManager 설정
        itemListRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        itemListRecyclerView.setHasFixedSize(true);
    }

    // favorite 상태저장
    private void initSharedPreferences() {
        prefs = getContext().getSharedPreferences("favorite", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    // favorite
    private void checkFavorite() {
        // 현재 상태
        boolean state = prefs.getBoolean("State", false);
        // favorite
        if (state) {
            binding.mainToolbar.toolbar.getMenu().findItem(R.id.navi_favorite).setIcon(R.drawable.ic_baseline_favorite_24);
            // 데이터 변경
            itemList = itemDatabase.itemDAO().getAllByGrade();
            itemListAdapter.setData(itemList);
            itemListAdapter.notifyDataSetChanged();
        } else { // Not favorite
            binding.mainToolbar.toolbar.getMenu().findItem(R.id.navi_favorite).setIcon(R.drawable.ic_baseline_favorite_border_24);
            // 데이터 변경
            itemList = itemDatabase.itemDAO().getAll();
            itemListAdapter.setData(itemList);
            itemListAdapter.notifyDataSetChanged();
        }
    }

    private void onClickFavorite() {
        // 현재 상태
        boolean state = prefs.getBoolean("State", false);
        // favorite
        if (state) {
            editor.putBoolean("State", false);
            Toast.makeText(getContext(), "순서 대로 보기", Toast.LENGTH_SHORT).show();
        } else { // Not favorite
            editor.putBoolean("State", true);
            Toast.makeText(getContext(), "별점 높은순으로 보기", Toast.LENGTH_SHORT).show();
        }
        editor.commit();
    }

}