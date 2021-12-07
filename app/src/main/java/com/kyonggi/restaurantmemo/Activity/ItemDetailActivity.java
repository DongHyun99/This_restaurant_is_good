package com.kyonggi.restaurantmemo.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kyonggi.restaurantmemo.Adapter.ItemListAdapter;
import com.kyonggi.restaurantmemo.Data.Item;
import com.kyonggi.restaurantmemo.R;
import com.kyonggi.restaurantmemo.Room.ItemDatabase;
import com.kyonggi.restaurantmemo.databinding.ActivityItemDetailBinding;

import java.text.DecimalFormat;

public class ItemDetailActivity extends AppCompatActivity {

    final static String EDIT = "EDIT";
    final static String DETAIL = "DETAIL";

    ActivityItemDetailBinding binding;
    ItemDatabase itemDatabase;
    Item item;


    // 뒤로와서 다시 불러왔을때를 위해 onStart에 데이터 설정
    @Override
    protected void onStart() {
        super.onStart();
        // 아이템 intent에서 가져오기
        item = (Item) getIntent().getSerializableExtra(DETAIL);
        binding.itemName.setText(item.getName());
        // 화폐 단위 표시
        DecimalFormat priceFormat = new DecimalFormat("#,###");
        String itemPrice = priceFormat.format(item.getPrice());
        binding.itemPrice.setText("$" + itemPrice + "원");
        // 이미지 표시
        setImage();
        // 메모 등록
        if (!item.getDescription().equals("")) {
            binding.itemMemo.setText(item.getDescription());
        }
        // 맛집 평가
        binding.itemRatingBar.setRating(item.getGrade());

    }

    // toolbar 등록
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menu) {
        switch (menu.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_delete:
                delete(item);
                return true;
            case R.id.menu_edit:
                Intent intent = new Intent(getApplicationContext(), AddItemActivity.class);
                // 맛집정보 넘겨줌
                intent.putExtra(EDIT, item);
                startActivity(intent);
                finish();
                return true;

        }
        return super.onOptionsItemSelected(menu);
    }

    // 메뉴 연동하기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_bottom, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        itemDatabase = ItemDatabase.getInstance(getApplicationContext());
        // toolbar 등록
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("");
    }

    private void delete(Item item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("삭제하시겠습니까?");
        builder.setNegativeButton("NO",(dialogInterface, i) -> {});
        builder.setPositiveButton("YES", (dialogInterface, i) -> {
            // 데이터 베이스에 삭제하기
            itemDatabase.itemDAO().delete(item);
            Toast.makeText(this, "삭제되었습니다", Toast.LENGTH_SHORT).show();
            finish();
        });
        builder.create().show();
    }

    // 사진 등록
    private void setImage() {
        if (binding.itemImage.toString().equals("")) {
            Glide.with(this)
                    .load(R.drawable.placeholder)
                    .into(binding.itemImage);
        } else {
            Glide.with(this)
                    .load(item.getImgPath())
                    .into(binding.itemImage);
        }
    }
}