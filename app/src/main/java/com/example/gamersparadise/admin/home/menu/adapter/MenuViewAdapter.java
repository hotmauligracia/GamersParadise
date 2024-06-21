package com.example.gamersparadise.admin.home.menu.adapter;

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
import com.example.gamersparadise.admin.home.menu.MenuViewActivity;
import com.example.gamersparadise.admin.home.menu.MenuViewFormActivity;
import com.example.gamersparadise.data.Menu;

import java.util.List;

public class MenuViewAdapter extends RecyclerView.Adapter<MenuViewAdapter.MenuViewHolder> {

    private final Context context;
    private final List<Menu> menuList;

    public MenuViewAdapter(Context context, List<Menu> menuList) {
        this.context = context;
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public MenuViewAdapter.MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu_view,
                        parent, false);
        return new MenuViewAdapter.MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewAdapter.MenuViewHolder holder, int position) {
        Menu menu = menuList.get(position);

        holder.tvMenuNameView.setText(menu.getName());
        holder.tvMenuTypeView.setText(menu.getMenuTypeId());
        holder.tvMenuPriceView.setText(String.valueOf(menu.getPrice()));

        Glide.with(context)
                .load(menu.getImageUrl())
                .centerCrop()
                .placeholder(R.drawable.default_image)
                .into(holder.imgMenuView);

        holder.btnEditMenu.setOnClickListener(v -> {
            Intent editMenu = new Intent(context, MenuViewFormActivity.class);
            editMenu.putExtra("menu", menu);
            editMenu.putExtra("locationId", menu.getLocationId());
            ((Activity) context).startActivityForResult(editMenu, MenuViewActivity.REQUEST_CODE_EDIT_MENU);
        });

        holder.btnDeleteMenu.setOnClickListener(v -> {
            if (context instanceof MenuViewActivity) {
                ((MenuViewActivity) context).showDeletionConfirmationPopup(menu);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView tvMenuNameView, tvMenuTypeView, tvMenuPriceView;
        ImageView imgMenuView;
        Button btnEditMenu, btnDeleteMenu;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMenuNameView = itemView.findViewById(R.id.tv_menu_name_view);
            tvMenuTypeView = itemView.findViewById(R.id.tv_menu_type_view);
            tvMenuPriceView = itemView.findViewById(R.id.tv_menu_price_view);
            imgMenuView = itemView.findViewById(R.id.img_menu_view);
            btnEditMenu = itemView.findViewById(R.id.btn_edit_menu);
            btnDeleteMenu = itemView.findViewById(R.id.btn_delete_menu);
        }
    }
}
