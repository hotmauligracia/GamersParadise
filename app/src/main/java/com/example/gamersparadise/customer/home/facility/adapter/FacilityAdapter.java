package com.example.gamersparadise.customer.home.facility.adapter;

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
import java.util.Locale;

public class FacilityAdapter extends RecyclerView.Adapter<FacilityAdapter.FacilityViewHolder> {
    private final List<Facility> facilities;
    private final Context context;

    public FacilityAdapter(Context context, List<Facility> facilities) {
        this.context = context;
        this.facilities = facilities;
    }

    @NonNull
    @Override
    public FacilityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_facility, parent, false);
        return new FacilityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacilityAdapter.FacilityViewHolder holder, int position) {
        Facility facility = facilities.get(position);
        holder.name.setText(facility.getName());
        holder.capacity.setText(String.valueOf(facility.getCapacity()));
        holder.details.setText(facility.getDetails());
        holder.price.setText(String.format(Locale.US,"$%.2f", facility.getPrice()));

        if (facility.getImageUrl() != null && !facility.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(facility.getImageUrl())
                    .into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.default_image);
        }
    }

    @Override
    public int getItemCount() {
        return facilities.size();
    }

    static class FacilityViewHolder extends RecyclerView.ViewHolder {
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
