package com.example.gamersparadise.customer.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.gamersparadise.Authentication;
import com.example.gamersparadise.R;
import com.example.gamersparadise.admin.home.menu.MenuViewActivity;
import com.example.gamersparadise.customer.home.adapter.FacilityHomeCustomerAdapter;
import com.example.gamersparadise.customer.home.adapter.MenuHomeCustomerAdapter;
import com.example.gamersparadise.customer.home.adapter.PromotionHomeCustomerAdapter;
import com.example.gamersparadise.customer.home.facility.FacilityActivity;
import com.example.gamersparadise.customer.home.menu.MenuActivity;
import com.example.gamersparadise.customer.home.notification.NotificationActivity;
import com.example.gamersparadise.customer.home.promotion.PromotionActivity;
import com.example.gamersparadise.customer.profile.ProfileFragment;
import com.example.gamersparadise.data.Facility;
import com.example.gamersparadise.data.Location;
import com.example.gamersparadise.data.Menu;
import com.example.gamersparadise.data.Promotion;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeCustomerFragment extends Fragment {

    private ViewPager2 vpPromotionHomeCust;
    private PromotionHomeCustomerAdapter promotionAdapter;
    private View illustNoDataFetched, facilityHomeCust, menuHomeCust;
    private FacilityHomeCustomerAdapter facilityAdapter;
    private MenuHomeCustomerAdapter menuAdapter;
    private List<Location> locationList;
    private List<Promotion> promotionList;
    private List<Facility> facilityList;
    private List<Menu> menuList;
    private Authentication auth;
    private ArrayAdapter<String> spinnerAdapter;
    private String locationId;

    public HomeCustomerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_customer, container, false);

        auth = new Authentication();

        TextView tvUsername = view.findViewById(R.id.tv_welcome);
        TextView tvAllFacility = view.findViewById(R.id.tv_all_facility);
        TextView tvAllMenu = view.findViewById(R.id.tv_all_menu);
        Spinner spinnerLokasi = view.findViewById(R.id.spinner_lokasi);
        ImageButton btnProfileHomeCust = view.findViewById(R.id.btn_profile_home_cust);
        ImageButton btnNotificationHomeCust = view.findViewById(R.id.btn_notification_home_cust);
        RecyclerView rvFacilityHomeCust = view.findViewById(R.id.rv_facility_home_cust);
        RecyclerView rvMenuHomeCust = view.findViewById(R.id.rv_menu_home_cust);

        vpPromotionHomeCust = view.findViewById(R.id.vp_promotion_home_cust);
        illustNoDataFetched = view.findViewById(R.id.illust_no_data_fetched);
        facilityHomeCust = view.findViewById(R.id.facility_home_cust);
        menuHomeCust = view.findViewById(R.id.menu_home_cust);

        String username = auth.getCurrentUser().getDisplayName();
        tvUsername.setText(String.format("Hai, %s", username));

        locationList = new ArrayList<>();
        promotionList = new ArrayList<>();
        facilityList = new ArrayList<>();
        menuList = new ArrayList<>();

        btnProfileHomeCust.setOnClickListener(v -> {
            ProfileFragment profileFragment = new ProfileFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_fragment_customer, profileFragment)
                    .addToBackStack(null)
                    .commit();
        });

        btnNotificationHomeCust.setOnClickListener(v -> {
            Intent notificationIntent = new Intent(getContext(), NotificationActivity.class);
            startActivity(notificationIntent);
        });

        promotionAdapter = new PromotionHomeCustomerAdapter(getContext(), promotionList);
        fetchPromotionData();

        facilityAdapter = new FacilityHomeCustomerAdapter(getContext(), facilityList);
        rvFacilityHomeCust.setAdapter(facilityAdapter);

        menuAdapter = new MenuHomeCustomerAdapter(getContext(), menuList);
        rvMenuHomeCust.setAdapter(menuAdapter);

        ArrayList<String> lokasi = new ArrayList<>();
        lokasi.add("Pilih Lokasi");

        spinnerAdapter = new ArrayAdapter<String>(
                requireContext(), android.R.layout.simple_spinner_item, lokasi) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLokasi.setAdapter(spinnerAdapter);
        spinnerLokasi.setSelection(0);

        spinnerLokasi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    illustNoDataFetched.setVisibility(View.VISIBLE);
                    facilityHomeCust.setVisibility(View.GONE);
                    menuHomeCust.setVisibility(View.GONE);
                } else {
                    String selectedLocation = (String) parent.getItemAtPosition(position);
                    locationId = locationList.get(position - 1).getId();
                    fetchFacilityAndMenuData(selectedLocation);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        fetchLocationData();

        tvAllFacility.setOnClickListener(this::onFacilitiesClick);
        tvAllMenu.setOnClickListener(this::onMenuClick);

        return view;
    }

    private void fetchLocationData() {
        auth.fetchCollectionData("locations", new Authentication.FirebaseCollectionCallback() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                locationList.clear();
                for (QueryDocumentSnapshot document : querySnapshot) {
                    Location location = document.toObject(Location.class);
                    location.setId(document.getId());
                    locationList.add(location);
                }
                updateLocationSpinner();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateLocationSpinner() {
        List<String> lokasiNames = new ArrayList<>();
        lokasiNames.add("Pilih Lokasi");
        for (Location location : locationList) {
            lokasiNames.add(location.getName());
        }
        spinnerAdapter.clear();
        spinnerAdapter.addAll(lokasiNames);
        spinnerAdapter.notifyDataSetChanged();
    }

    private void fetchFacilityAndMenuData(String locationName) {
        locationId = null;

        for (Location location : locationList) {
            if (location.getName().equals(locationName)) {
                locationId = location.getId();
                break;
            }
        }

        if (locationId != null) {
            // Fetch facility data
            auth.fetchCollectionData("locations/" + locationId + "/facilities",
                    new Authentication.FirebaseCollectionCallback() {
                @Override
                public void onSuccess(QuerySnapshot querySnapshot) {
                    facilityList.clear();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        Facility facility = document.toObject(Facility.class);
                        facility.setId(document.getId());
                        facilityList.add(facility);
                    }
                    facilityAdapter.notifyDataSetChanged();
                    updateVisibility();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(getContext(), "Gagal mengambil data fasilitas: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });

            // Fetch menu data
            auth.fetchCollectionData("locations/" + locationId + "/menus",
                    new Authentication.FirebaseCollectionCallback() {
                @Override
                public void onSuccess(QuerySnapshot querySnapshot) {
                    menuList.clear();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        Menu menu = document.toObject(Menu.class);
                        menu.setId(document.getId());
                        fetchMenuIsInStock(menu);
                    }
                    menuAdapter.notifyDataSetChanged();
                    updateVisibility();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(getContext(), "Gagal mengambil data menu: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void fetchMenuIsInStock(Menu menu) {
        auth.fetchDocumentData("locations/" + menu.getLocationId() + "/menus/" + menu.getId(), new Authentication.FirebaseDocumentCallback() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Boolean isInStock = documentSnapshot.getBoolean("isInStock");
                if (isInStock != null) {
                    menu.setInStock(isInStock);
                } else {
                    menu.setInStock(false);
                }
                menuList.add(menu);
                menuAdapter.notifyDataSetChanged();
                updateVisibility();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchPromotionData() {
        auth.fetchCollectionData("promotions", new Authentication.FirebaseCollectionCallback() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                promotionList.clear();
                for (QueryDocumentSnapshot document : querySnapshot) {
                    Promotion promotion = document.toObject(Promotion.class);
                    promotion.setId(document.getId());
                    if (promotion.getStatus().equals("Sedang Berjalan"))
                        promotionList.add(promotion);
                }
                setupCarousel(promotionList);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getContext(), "Gagal mengambil data promo: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupCarousel(List<Promotion> promotionList) {
        promotionAdapter = new PromotionHomeCustomerAdapter(getContext(), promotionList);
        vpPromotionHomeCust.setAdapter(promotionAdapter);

        promotionAdapter.setOnItemClickListener(promotion -> {
            Intent promotionIntent = new Intent(getContext(), PromotionActivity.class);
            promotionIntent.putExtra("promotion", promotion);
            startActivity(promotionIntent);
        });
    }

    private void updateVisibility() {
        if (facilityList.isEmpty() && menuList.isEmpty()) {
            illustNoDataFetched.setVisibility(View.VISIBLE);
            facilityHomeCust.setVisibility(View.GONE);
            menuHomeCust.setVisibility(View.GONE);
        } else {
            illustNoDataFetched.setVisibility(View.GONE);
            if (!facilityList.isEmpty()) {
                facilityHomeCust.setVisibility(View.VISIBLE);
            }
            if (!menuList.isEmpty()) {
                menuHomeCust.setVisibility(View.VISIBLE);
            }
        }
    }

    public void onFacilitiesClick(View view) {
        Intent facilityIntent = new Intent(requireContext(), FacilityActivity.class);
        facilityIntent.putExtra("locationId", locationId);
        startActivity(facilityIntent);
    }

    public void onMenuClick(View view) {
        Intent menuIntent = new Intent(requireContext(), MenuActivity.class);
        menuIntent.putExtra("locationId", locationId);
        startActivity(menuIntent);
    }
}
