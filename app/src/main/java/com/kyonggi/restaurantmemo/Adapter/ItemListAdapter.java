package com.kyonggi.restaurantmemo.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kyonggi.restaurantmemo.Activity.ItemDetailActivity;
import com.kyonggi.restaurantmemo.Activity.ItemListFragment;
import com.kyonggi.restaurantmemo.Activity.MainActivity;
import com.kyonggi.restaurantmemo.Data.Item;
import com.kyonggi.restaurantmemo.R;
import com.kyonggi.restaurantmemo.Room.ItemDatabase;
import com.kyonggi.restaurantmemo.databinding.FragmentItemListBinding;
import com.kyonggi.restaurantmemo.databinding.ListItemBinding;

import java.text.DecimalFormat;
import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.CustomHolder> {

    private List<Item> itemList;
    private Context context;
    final static String DETAIL = "DETAIL";

    public ItemListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ItemListAdapter.CustomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemBinding binding = ListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CustomHolder(binding, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomHolder holder, int position) {
        holder.onBind(itemList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // 데이터 삭제
    public void deleteItem(int adapterPosition) {
        itemList.remove(adapterPosition);
        notifyDataSetChanged();
    }


    // 데이터 세팅
    public void setData(List<Item> itemList) {
        this.itemList = itemList;
    }

    /**
     * 커스텀 뷰홀더
     */
    public class CustomHolder extends RecyclerView.ViewHolder {
        private final ListItemBinding binding;
        private final Context context;

        public CustomHolder(ListItemBinding binding, Context context) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;
        }

        // 맛집 뷰를 바인드 해준다
        public void onBind(Item item, int position) {
            // 설정된 이미지가 없다면
            binding.itemImageView.setVisibility(View.VISIBLE);
            if (item.getImgPath().equals("")) {
                Glide.with(itemView)
                        .load(R.drawable.placeholder)
                        .into(binding.itemImageView);
            } else { // 이미지가 있다면
                Glide.with(itemView)
                        .load(item.getImgPath())
                        .into(binding.itemImageView);
            }

            // 화폐 단위 표시
            DecimalFormat priceFormat = new DecimalFormat("#,###");
            String itemPrice = priceFormat.format(item.getPrice());
            binding.itemPrice.setText("$" + itemPrice + "원");
            binding.itemName.setText(item.getName());

            // 별점 표시
            binding.ratingItemPriority.setRating(item.getGrade());

            // 맛집 상세정보로 이동
            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, ItemDetailActivity.class);
                // 클릭된 맛집정보 넘겨줌
                intent.putExtra(DETAIL, item);
                intent.putExtra("POS", position);
                context.startActivity(intent);
            });

            // 길게 클릭시 dialog
            itemView.setOnLongClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                builder.setMessage("삭제하시겠습니까?");
                builder.setNegativeButton("NO",(dialogInterface, i) -> {});
                builder.setPositiveButton("YES", (dialogInterface, i) -> {
                    // 삭제
                    deleteItem(position);
                    ItemDatabase.getInstance(context).itemDAO().delete(item);
                    Toast.makeText(itemView.getContext(), "삭제되었습니다", Toast.LENGTH_SHORT).show();
                });
                builder.create().show();
                return true;
            });
        }
    }
}
