package com.example.gamersparadise.admin.home.location.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamersparadise.R;
import com.example.gamersparadise.admin.home.location.LocationViewActivity;
import com.example.gamersparadise.admin.home.location.LocationViewFormActivity;
import com.example.gamersparadise.data.Location;

import java.util.List;

public class LocationViewAdapter extends RecyclerView.Adapter<LocationViewAdapter.LocationViewHolder> {

    private final Context context;
    private final List<Location> locationList;

    public LocationViewAdapter(Context context, List<Location> locationList) {
        this.context = context;
        this.locationList = locationList;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location_view,
                        parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        Location location = locationList.get(position);
        holder.tvLocationName.setText(location.getName());
        holder.tvLocationAddress.setText(
                String.format("%s, %s, %s, %s %s",
                        location.getStreet(),
                        location.getSubdistrict(),
                        location.getCity(),
                        location.getProvince(),
                        location.getZipCode()
                )
        );

        holder.btnEditLocation.setOnClickListener(v -> {
            Intent editLocationIntent = new Intent(context, LocationViewFormActivity.class);
            editLocationIntent.putExtra("location", location);
            ((Activity) context).startActivityForResult(editLocationIntent, LocationViewActivity.REQUEST_CODE_EDIT_LOCATION);
        });

        holder.btnDeleteLocation.setOnClickListener(v -> {
            if (context instanceof LocationViewActivity) {
                ((LocationViewActivity) context).showDeletionConfirmationPopup(location);
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView tvLocationName, tvLocationAddress;
        Button btnEditLocation, btnDeleteLocation;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);

            tvLocationName = itemView.findViewById(R.id.tv_location_name);
            tvLocationAddress = itemView.findViewById(R.id.tv_location_address);
            btnEditLocation = itemView.findViewById(R.id.btn_edit_location);
            btnDeleteLocation = itemView.findViewById(R.id.btn_delete_location);
        }
    }
}
