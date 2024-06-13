package com.example.gamersparadise.admin.home.location.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamersparadise.Authentication;
import com.example.gamersparadise.R;
import com.example.gamersparadise.admin.home.location.LocationViewActivity;
import com.example.gamersparadise.admin.home.location.LocationViewFormActivity;
import com.example.gamersparadise.data.Location;

import java.util.List;

public class LocationViewAdapter extends RecyclerView.Adapter<LocationViewAdapter.LocationViewHolder> {

    private final Context context;
    private final List<Location> locationList;
    private final Authentication auth;

    public LocationViewAdapter(Context context, List<Location> locationList, Authentication auth) {
        this.context = context;
        this.locationList = locationList;
        this.auth = auth;
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
            Intent intent = new Intent(context, LocationViewFormActivity.class);
            intent.putExtra("location", location);
            ((Activity) context).startActivityForResult(intent, LocationViewActivity.REQUEST_CODE_EDIT_LOCATION);
        });

        holder.btnDeleteLocation.setOnClickListener(v -> auth.deleteDocumentData("locations", location.getId(), new Authentication.FirebaseDocumentDeleteCallback() {
            @Override
            public void onSuccess() {
                locationList.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
                Toast.makeText(context, "Data lokasi terhapus", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(context, "Gagal menghapus data lokasi: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        }));
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
