package com.example.gamersparadise.customer.home.menu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamersparadise.R;
import com.example.gamersparadise.admin.home.menu.adapter.MenuViewAdapter;
import com.example.gamersparadise.data.Menu;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private final Context context;
    private final List<Menu> menuList;

    public MenuAdapter(Context context, List<Menu> menuList) {
        this.context = context;
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public MenuAdapter.MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.MenuViewHolder holder, int position) {
        Menu menu = menuList.get(position);

        holder.name.setText(menu.getName());
        holder.description.setText(menu.getDescription());
        holder.price.setText(menu.getFormattedPrice());

        Glide.with(context)
                .load(menu.getImageUrl())
                .centerCrop()
                .placeholder(R.drawable.default_image)
                .into(holder.image);

//        if (holder.btnAddMenu.getExplicitStyle() == R.style.secondary_button){
//            holder.btnAddMenu.setOnClickListener(v -> {
//
//            });
//        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, price;
        ImageView image;
        Button btnAddMenu;
        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_menu_name);
            description = itemView.findViewById(R.id.tv_menu_desc);
            price = itemView.findViewById(R.id.tv_menu_price);
            image = itemView.findViewById(R.id.img_menu);
            btnAddMenu = itemView.findViewById(R.id.btn_add_menu);
        }
    }
}
