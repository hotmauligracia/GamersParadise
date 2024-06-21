package com.example.gamersparadise.admin.home.promotion.adapter;

import static com.example.gamersparadise.data.Promotion.dateTimeFormatter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamersparadise.R;
import com.example.gamersparadise.admin.home.promotion.PromotionViewActivity;
import com.example.gamersparadise.admin.home.promotion.PromotionViewFormActivity;
import com.example.gamersparadise.data.Promotion;

import java.time.LocalDateTime;
import java.util.List;

public class PromotionViewAdapter extends RecyclerView.Adapter<PromotionViewAdapter.PromotionViewHolder> {

    private final Context context;
    private final List<Promotion> promotionList;

    public PromotionViewAdapter(Context context, List<Promotion> promotionList) {
        this.context = context;
        this.promotionList = promotionList;
    }

    @NonNull
    @Override
    public PromotionViewAdapter.PromotionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_promotion_view,
                        parent, false);
        return new PromotionViewAdapter.PromotionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PromotionViewAdapter.PromotionViewHolder holder, int position) {
        Promotion promotion = promotionList.get(position);

        holder.tvPromotionName.setText(promotion.getName());

        LocalDateTime startTime = promotion.getStartTimeAsLocalDateTime();
        LocalDateTime endTime = promotion.getEndTimeAsLocalDateTime();

        holder.tvPromotionStartTime.setText(startTime.format(dateTimeFormatter));
        holder.tvPromotionEndTime.setText(endTime.format(dateTimeFormatter));

        Glide.with(context)
                .load(promotion.getImageUrl())
                .centerCrop()
                .placeholder(R.drawable.default_image)
                .into(holder.imgPromotionView);

        holder.btnEditPromotion.setOnClickListener(v -> {
            Intent editPromotion = new Intent(context, PromotionViewFormActivity.class);
            editPromotion.putExtra("promotion", promotion);
            ((Activity) context).startActivityForResult(editPromotion, PromotionViewActivity.REQUEST_CODE_EDIT_PROMOTION);
        });

        holder.btnDeletePromotion.setOnClickListener(v -> {
            if (context instanceof PromotionViewActivity) {
                ((PromotionViewActivity) context).showDeletionConfirmationPopup(promotion);
            }
        });

        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(startTime) && now.isBefore(endTime)) {
            holder.tvPromotionStatus.setText(R.string.sedang_berjalan);
            holder.tvPromotionStatus.setTextColor(context.getResources().getColor(R.color.orange600));
        } else if (now.isBefore(startTime)) {
            holder.tvPromotionStatus.setText(R.string.akan_berjalan);
            holder.tvPromotionStatus.setTextColor(context.getResources().getColor(R.color.gray_normal));
        } else {
            holder.tvPromotionStatus.setText(R.string.telah_berakhir);
            holder.tvPromotionStatus.setTextColor(context.getResources().getColor(R.color.gray_normal));
        }
    }

    @Override
    public int getItemCount() {
        return promotionList.size();
    }

    public static class PromotionViewHolder extends RecyclerView.ViewHolder {
        TextView tvPromotionName, tvPromotionStartTime, tvPromotionEndTime, tvPromotionStatus;
        ImageView imgPromotionView;
        Button btnEditPromotion, btnDeletePromotion;

        public PromotionViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPromotionName = itemView.findViewById(R.id.tv_promotion_name);
            tvPromotionStartTime = itemView.findViewById(R.id.tv_promotion_start_time);
            tvPromotionEndTime = itemView.findViewById(R.id.tv_promotion_end_time);
            tvPromotionStatus = itemView.findViewById(R.id.tv_promotion_status);
            imgPromotionView = itemView.findViewById(R.id.img_promotion_view);
            btnEditPromotion = itemView.findViewById(R.id.btn_edit_promotion);
            btnDeletePromotion = itemView.findViewById(R.id.btn_delete_promotion);
        }
    }
}
