package com.example.gamersparadise.customer.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamersparadise.R;
import com.example.gamersparadise.data.Promotion;

import java.util.List;

public class PromotionHomeCustomerAdapter extends RecyclerView.Adapter<PromotionHomeCustomerAdapter.PromotionHomeCustomerViewHolder> {

    private final Context context;
    private final List<Promotion> promotionList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Promotion promotion);
    }

    public PromotionHomeCustomerAdapter(Context context, List<Promotion> promotionList) {
        this.context = context;
        this.promotionList = promotionList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public PromotionHomeCustomerAdapter.PromotionHomeCustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promotion_cardview, parent, false);
        return new PromotionHomeCustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PromotionHomeCustomerAdapter.PromotionHomeCustomerViewHolder holder, int position) {
        Promotion promotion = promotionList.get(position);

        Glide.with(context)
                .load(promotion.getImageUrl())
                .fitCenter()
                .placeholder(R.drawable.default_image)
                .into(holder.imgPromotionHomeCust);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(promotion);
            }
        });
    }

    @Override
    public int getItemCount() {
        return promotionList.size();
    }

    public static class PromotionHomeCustomerViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPromotionHomeCust;
        public PromotionHomeCustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPromotionHomeCust = itemView.findViewById(R.id.img_promotion_home_cust);
        }
    }
}
