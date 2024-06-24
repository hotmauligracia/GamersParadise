package com.example.gamersparadise.customer.home.facility.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamersparadise.R;
import com.example.gamersparadise.customer.home.facility.ViewFacilityActivity;
import com.example.gamersparadise.data.Facility;

import java.util.List;

public class FacilityAdapter extends RecyclerView.Adapter<FacilityAdapter.FacilityViewHolder> {
    private final Context context;
    private final List<Facility> facilities;

    public FacilityAdapter(Context context, List<Facility> facilities) {
        this.context = context;
        this.facilities = facilities;
    }

    @NonNull
    @Override
    public FacilityAdapter.FacilityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_facility, parent, false);
        return new FacilityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacilityAdapter.FacilityViewHolder holder, int position) {
        Facility facility = facilities.get(position);

        holder.name.setText(facility.getName());
        holder.capacity.setText(String.format("%s orang", facility.getCapacity()));
        holder.details.setText(facility.getDetails());
        holder.price.setText(facility.getFormattedPrice());

        if (facility.getImageUrl() != null && !facility.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(facility.getImageUrl())
                    .into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.default_image);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent facilityIntent = new Intent(context, ViewFacilityActivity.class);
            facilityIntent.putExtra("facility", facility);
            context.startActivity(facilityIntent);
        });
    }

    @Override
    public int getItemCount() {
        return facilities.size();
    }

    public static class FacilityViewHolder extends RecyclerView.ViewHolder {
        TextView name, capacity, details, price;
        ImageView image;

        public FacilityViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_facility_name);
            capacity = itemView.findViewById(R.id.tv_facility_capacity);
            details = itemView.findViewById(R.id.tv_facility_details);
            price = itemView.findViewById(R.id.tv_facility_price);
            image = itemView.findViewById(R.id.facility_img);
        }
    }
}
