package com.example.gamersparadise.customer.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamersparadise.R;
import com.example.gamersparadise.data.Facility;

import java.util.List;

public class FacilityHomeCustomerAdapter extends RecyclerView.Adapter<FacilityHomeCustomerAdapter.FacilityHomeCustomerViewHolder> {

    private final Context context;
    private final List<Facility> facilityList;

    public FacilityHomeCustomerAdapter(Context context, List<Facility> facilityList) {
        this.context = context;
        this.facilityList = facilityList;
    }

    @NonNull
    @Override
    public FacilityHomeCustomerAdapter.FacilityHomeCustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_facility_cardview, parent, false);
        return new FacilityHomeCustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacilityHomeCustomerAdapter.FacilityHomeCustomerViewHolder holder, int position) {
        Facility facility = facilityList.get(position);

        Glide.with(context)
                .load(facility.getImageUrl())
                .centerCrop()
                .placeholder(R.drawable.default_image)
                .into(holder.imgFacilityCardview);

        holder.tvFacilityCapacityCardview.setText(String.valueOf(facility.getCapacity()));
        holder.tvFacilityNameCardview.setText(facility.getName());
        holder.tvFacilityPriceCardview.setText(String.format("%s/jam", facility.getFormattedPrice()));
    }

    @Override
    public int getItemCount() {
        return facilityList.size();
    }

    public static class FacilityHomeCustomerViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFacilityCardview;
        TextView tvFacilityCapacityCardview;
        TextView tvFacilityNameCardview;
        TextView tvFacilityPriceCardview;

        public FacilityHomeCustomerViewHolder(@NonNull View itemView) {
            super(itemView);

            imgFacilityCardview = itemView.findViewById(R.id.img_facility_cardview);
            tvFacilityCapacityCardview = itemView.findViewById(R.id.tv_facility_capacity_cardview);
            tvFacilityNameCardview = itemView.findViewById(R.id.tv_facility_name_cardview);
            tvFacilityPriceCardview = itemView.findViewById(R.id.tv_facility_price_cardview);
        }
    }
}
