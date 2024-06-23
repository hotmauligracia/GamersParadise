package com.example.gamersparadise.admin.home.facility.adapter;

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
import com.example.gamersparadise.admin.home.facility.FacilityViewActivity;
import com.example.gamersparadise.admin.home.facility.FacilityViewFormActivity;
import com.example.gamersparadise.data.Facility;

import java.util.List;
import java.util.Locale;

public class FacilityViewAdapter extends RecyclerView.Adapter<FacilityViewAdapter.FacilityViewHolder> {

    private final Context context;
    private final List<Facility> facilityList;

    public FacilityViewAdapter(Context context, List<Facility> facilityList) {
        this.context = context;
        this.facilityList = facilityList;
    }

    @NonNull
    @Override
    public FacilityViewAdapter.FacilityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_facility_view,
                        parent, false);
        return new FacilityViewAdapter.FacilityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacilityViewAdapter.FacilityViewHolder holder, int position) {
        Facility facility = facilityList.get(position);

        holder.tvFacilityNameView.setText(facility.getName());
        holder.tvFacilityCapacityView.setText(String.format(Locale.US,"%d orang", facility.getCapacity()));
        holder.tvFacilityDetailsView.setText(facility.getDetails());

        Glide.with(context)
                .load(facility.getImageUrl())
                .centerCrop()
                .placeholder(R.drawable.default_image)
                .into(holder.imgFacilityView);

        holder.btnEditFacility.setOnClickListener(v -> {
            Intent editFacility = new Intent(context, FacilityViewFormActivity.class);
            editFacility.putExtra("facility", facility);
            editFacility.putExtra("locationId", facility.getLocationId());
            ((Activity) context).startActivityForResult(editFacility, FacilityViewActivity.REQUEST_CODE_EDIT_FACILITY);
        });

        holder.btnDeleteFacility.setOnClickListener(v -> {
            if (context instanceof FacilityViewActivity) {
                ((FacilityViewActivity) context).showDeletionConfirmationPopup(facility);
            }
        });
    }

    @Override
    public int getItemCount() {
        return facilityList.size();
    }

    public static class FacilityViewHolder extends RecyclerView.ViewHolder {
        TextView tvFacilityNameView, tvFacilityCapacityView, tvFacilityDetailsView;
        ImageView imgFacilityView;
        Button btnEditFacility, btnDeleteFacility;

        public FacilityViewHolder(@NonNull View itemView) {
            super(itemView);

            tvFacilityNameView = itemView.findViewById(R.id.tv_facility_name_view);
            tvFacilityCapacityView = itemView.findViewById(R.id.tv_facility_capacity_view);
            tvFacilityDetailsView = itemView.findViewById(R.id.tv_facility_details_view);
            imgFacilityView = itemView.findViewById(R.id.img_facility_view);
            btnEditFacility = itemView.findViewById(R.id.btn_edit_facility);
            btnDeleteFacility = itemView.findViewById(R.id.btn_delete_facility);
        }
    }
}