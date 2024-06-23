package com.example.gamersparadise.customer.home.adapter;

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
import com.example.gamersparadise.customer.home.menu.ViewMenuActivity;
import com.example.gamersparadise.data.Menu;

import java.util.List;

public class MenuHomeCustomerAdapter extends RecyclerView.Adapter<MenuHomeCustomerAdapter.MenuHomeCustomerHolder> {
    private final Context context;
    private final List<Menu> menuList;

    public MenuHomeCustomerAdapter(Context context, List<Menu> menuList) {
        this.context = context;
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public MenuHomeCustomerAdapter.MenuHomeCustomerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu_cardview, parent, false);
        return new MenuHomeCustomerAdapter.MenuHomeCustomerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuHomeCustomerAdapter.MenuHomeCustomerHolder holder, int position) {
        Menu menu = menuList.get(position);

        Glide.with(context)
                .load(menu.getImageUrl())
                .centerCrop()
                .placeholder(R.drawable.default_image)
                .into(holder.imgMenuCardview);

        holder.tvMenuNameCardview.setText(menu.getName());
        holder.tvMenuPriceCardview.setText(menu.getFormattedPrice());

        holder.itemView.setOnClickListener(v -> {
            Intent menuIntent = new Intent(context, ViewMenuActivity.class);
            menuIntent.putExtra("menu", menu);
            context.startActivity(menuIntent);
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public static class MenuHomeCustomerHolder extends RecyclerView.ViewHolder {
        ImageView imgMenuCardview;
        TextView tvMenuNameCardview, tvMenuPriceCardview;

        public MenuHomeCustomerHolder(@NonNull View itemView) {
            super(itemView);
            imgMenuCardview = itemView.findViewById(R.id.img_menu_cardview);
            tvMenuNameCardview = itemView.findViewById(R.id.tv_menu_name_cardview);
            tvMenuPriceCardview = itemView.findViewById(R.id.tv_menu_price_cardview);
        }
    }
}
