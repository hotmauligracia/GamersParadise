package com.example.gamersparadise.admin.home.menu.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamersparadise.Authentication;
import com.example.gamersparadise.R;
import com.example.gamersparadise.admin.home.menu.MenuViewActivity;
import com.example.gamersparadise.admin.home.menu.MenuViewFormActivity;
import com.example.gamersparadise.data.Menu;
import com.example.gamersparadise.data.MenuType;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuViewAdapter extends RecyclerView.Adapter<MenuViewAdapter.MenuViewHolder> {

    private final Context context;
    private final List<Menu> menuList;
    private List<MenuType> menuTypeList;
    private final Authentication auth = new Authentication();

    public MenuViewAdapter(Context context, List<Menu> menuList) {
        this.context = context;
        this.menuList = menuList;
        this.menuTypeList = new ArrayList<>();
    }

    public void setMenuTypeList(List<MenuType> menuTypeList) {
        this.menuTypeList = menuTypeList;
    }

    @NonNull
    @Override
    public MenuViewAdapter.MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu_view, parent, false);
        return new MenuViewAdapter.MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewAdapter.MenuViewHolder holder, int position) {
        Menu menu = menuList.get(position);

        holder.tvMenuNameView.setText(menu.getName());
        holder.tvMenuPriceView.setText(String.format("Rp%s", menu.getPrice()));

        for (MenuType menuType : menuTypeList) {
            if (menuType.getId() == menu.getMenuTypeId()) {
                holder.tvMenuTypeView.setText(menuType.getName());
                break;
            }
        }

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

        updateStockButtonsVisibility(holder, menu.isInStock());

        holder.btnStockEmpty.setOnClickListener(v ->
                updateStockStatus(menu, false, holder));

        holder.btnStockReady.setOnClickListener(v ->
                updateStockStatus(menu, true, holder));
    }

    private void updateStockButtonsVisibility(MenuViewHolder holder, boolean isInStock) {
        Log.d("MenuViewAdapter", "Updating stock button visibility: isInStock = " + isInStock);
        holder.btnStockEmpty.setVisibility(isInStock ? View.VISIBLE : View.GONE);
        holder.btnStockReady.setVisibility(isInStock ? View.GONE : View.VISIBLE);
    }

    private void updateStockStatus(Menu menu, boolean isInStock, MenuViewHolder holder) {
        Log.d("MenuViewAdapter", "Updating stock status: " + isInStock);
        Map<String, Object> data = new HashMap<>();
        data.put("isInStock", isInStock);

        auth.updateDocumentData("locations/" + menu.getLocationId() + "/menus", menu.getId(), data, new Authentication.FirebaseDocumentCallback() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Toast.makeText(context, "Status stok berhasil diperbarui", Toast.LENGTH_SHORT).show();
                menu.setInStock(isInStock);
                updateStockButtonsVisibility(holder, isInStock);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(context, "Gagal memperbarui status stok: " + errorMessage, Toast.LENGTH_SHORT).show();
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
        Button btnEditMenu, btnDeleteMenu, btnStockEmpty, btnStockReady;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMenuNameView = itemView.findViewById(R.id.tv_menu_name_view);
            tvMenuTypeView = itemView.findViewById(R.id.tv_menu_type_view);
            tvMenuPriceView = itemView.findViewById(R.id.tv_menu_price_view);
            imgMenuView = itemView.findViewById(R.id.img_menu_view);
            btnEditMenu = itemView.findViewById(R.id.btn_edit_menu);
            btnDeleteMenu = itemView.findViewById(R.id.btn_delete_menu);
            btnStockEmpty = itemView.findViewById(R.id.btn_stock_empty);
            btnStockReady = itemView.findViewById(R.id.btn_stock_ready);
        }
    }
}
