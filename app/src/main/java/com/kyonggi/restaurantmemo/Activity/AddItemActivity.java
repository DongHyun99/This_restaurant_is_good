package com.kyonggi.restaurantmemo.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.kyonggi.restaurantmemo.Adapter.ItemListAdapter;
import com.kyonggi.restaurantmemo.Data.Item;
import com.kyonggi.restaurantmemo.R;
import com.kyonggi.restaurantmemo.Room.ItemDatabase;
import com.kyonggi.restaurantmemo.databinding.ActivityAddItemBinding;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.Random;

public class AddItemActivity extends AppCompatActivity {

    final static String EDIT = "EDIT";

    ActivityAddItemBinding binding;
    Uri itemImage = Uri.parse("");
    TextInputEditText editTextItemName;
    TextInputEditText editTextItemPrice;
    TextInputEditText editTextItemMemo;
    RatingBar ratingBar;

    // 데이터베이스 Access
    ItemDatabase itemDatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        editTextItemName = binding.editTextItemName;
        editTextItemPrice = binding.editTextItemPrice;
        editTextItemMemo = binding.editTextItemMemo;
        ratingBar = binding.ratingItemPriority;
        itemDatabase = ItemDatabase.getInstance(this);
        // Init Action
        onClickBack();
        // Init Add Item Action
        initAddItemAction();

        // 수정으로 들어옴
        if (getIntent() != null && getIntent().hasExtra(EDIT)) {
            editItemView((Item) getIntent().getSerializableExtra(EDIT));
            binding.completeButton.setText("수정");
        }

    }

    // 맛집을 추가하기 위해 입력값들을 넣어준다
    private void initAddItemAction() {
        applyTextFormat();
        binding.addImageButton.setOnClickListener(view -> {
            // ImageView 를 눌렀을 때 이미지 추가 액티비티로 이동
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setActivityTitle("이미지 추가")
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setCropMenuCropButtonTitle("완료")
                    .setRequestedSize(1280, 900)
                    .start(this);
        });
        binding.imageViewItemImageLayout.setOnClickListener(view -> {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setActivityTitle("이미지 추가")
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setCropMenuCropButtonTitle("완료")
                    .setRequestedSize(1280, 900)
                    .start(this);
        });
        binding.imageViewItemImage.setOnClickListener(view ->{
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setActivityTitle("이미지 추가")
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setCropMenuCropButtonTitle("완료")
                    .setRequestedSize(1280, 900)
                    .start(this);
        });

        // 작성 완료 액션

        binding.completeButton.setOnClickListener(view -> {
            if (editTextItemName.getText().toString().equals("") || editTextItemPrice.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "아직 필수 사항이 남아있습니다", Toast.LENGTH_SHORT).show();
            } else {
                addItem();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = result.getUri();
                // Use the uri to load the image
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    itemImage = bitmapToFile(bitmap);
                    binding.addImageButton.setVisibility(View.GONE);
                    binding.imageViewItemImageLayout.setVisibility(View.VISIBLE);
                    binding.imageViewItemImage.setImageURI(itemImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "이미지 선택 및 편집 오류", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void applyTextFormat() {
        // 화폐의 단위를 자동으로 입력해주는 TextWatcher
        DecimalFormat decimalFormat = new DecimalFormat("#,###");

        new TextWatcher() {
            String result = "";
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) { }

            // 3자리마다 ,로 화폐단위 표시
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence.toString()) && !charSequence.toString().equals(result)){
                    result = decimalFormat.format(Double.parseDouble(charSequence.toString().replaceAll(",","")));
                    editTextItemPrice.setText(result);
                    editTextItemPrice.setSelection(result.length());
                }
            }
        };
    }

    // 수정을 위하여 데이터 기본값 복원
    private void editItemView(Item item) {
        itemImage = Uri.parse(item.getImgPath());
        if (item.getImgPath().equals("")){
            binding.addImageButton.setVisibility(View.VISIBLE);
            binding.imageViewItemImageLayout.setVisibility(View.GONE);
            Glide.with(this)
                    .load(R.drawable.placeholder)
                    .into(binding.imageViewItemImage);
        } else {
            binding.addImageButton.setVisibility(View.GONE);
            binding.imageViewItemImageLayout.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(item.getImgPath())
                    .into(binding.imageViewItemImage);
        }

        // Title 변경
        binding.textViewTitle.setText("수정하기");
        // 변경전의 값 세팅
        editTextItemName.setText(item.getName());
        editTextItemPrice.setText(item.getPrice().toString());
        editTextItemMemo.setText(item.getDescription());
        ratingBar.setRating(item.getGrade());
    }

    private Uri bitmapToFile(Bitmap bitmap) {
        ContextWrapper wrapper = new ContextWrapper((Context)this);
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        String randomNumber = String.valueOf(random.nextInt(1000000000));
        File file = wrapper.getDir("Images", 0);
        file = new File(file, "item_" + randomNumber + ".jpg");

        try {
            OutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Uri.parse(file.getAbsolutePath());
    }

    // 작성 완료시 Action
    private void addItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (getIntent() != null && getIntent().hasExtra(EDIT)) {
            builder.setMessage(getResources().getText(R.string.editDialog));
        } else {
            builder.setMessage(getResources().getText(R.string.completeDialog));
        }
        builder.setNegativeButton("NO",(dialogInterface, i) -> {});
        builder.setPositiveButton("YES", (dialogInterface, i) -> {
            // 데이터 베이스에 저장하기
            if (getIntent() != null && getIntent().hasExtra(EDIT)) {
                Toast.makeText(this, "수정 되었습니다", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "등록 되었습니다", Toast.LENGTH_SHORT).show();
            }
            addToDatabase();
            finish();
        });
        builder.create().show();
    }

    // 데이터베이스에 저장
    private void addToDatabase() {
        Item item;
        // 수정이라면 기본값에서 시작
        if (getIntent() != null && getIntent().hasExtra(EDIT)) {
            item = (Item) getIntent().getSerializableExtra(EDIT);
        } else {
            item = new Item();
        }
        item.setGrade(ratingBar.getRating());
        item.setName(editTextItemName.getText().toString());
        if (editTextItemMemo.getText().toString().equals("")) {
            editTextItemMemo.setText("메모가 없습니다.");
        }
        item.setDescription(editTextItemMemo.getText().toString());
        if (itemImage.toString().equals("")) {
            item.setImgPath("");
        } else {
            item.setImgPath(itemImage.toString());
        }
        item.setPrice(Long.parseLong(Objects.requireNonNull(editTextItemPrice.getText()).toString().replace(",","")));

        // 수정이 아니라면 데이터 추가
        if (getIntent().hasExtra(EDIT)) {
            itemDatabase.itemDAO().update(item);
        } else {
            addItem(item);
        }

    }

    // 데이터 추가
    private void addItem(Item item) {
        itemDatabase.itemDAO().insert(item);
    }

    private void onClickBack() {
        binding.backButton.setOnClickListener(view -> {
            finish();
        });
    }
}