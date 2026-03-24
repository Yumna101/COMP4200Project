package com.example.volunteertracker;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

//fragment
import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


public class choose_org_activity extends Fragment {
    private RecyclerView rvOrganizations;
    private OrganizationAdapter adapter;
    private List<Organization> organizationList;
    private List<Organization> filteredList;
    private SearchView searchView;
    private LinearLayout layoutEmpty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // FIXED: corrected layout name to match existing XML file
        View view = inflater.inflate(R.layout.activity_organizations, container, false);

        // ADDED: initialize views using view.findViewById
        rvOrganizations = view.findViewById(R.id.rvOrganizations);
        searchView = view.findViewById(R.id.searchView);
        layoutEmpty = view.findViewById(R.id.layoutEmpty);

        rvOrganizations.setLayoutManager(new LinearLayoutManager(getContext())); // FIXED: use getContext()

        // Create initial data
        organizationList = new ArrayList<>();
        organizationList.add(new Organization("YMS", R.drawable.ym_logo_main, "#800080"));
        organizationList.add(new Organization("AI Club", R.drawable.ai_club, "#0000FF"));

        filteredList = new ArrayList<>(organizationList);

        adapter = new OrganizationAdapter(filteredList, org -> {
            // FIXED: use getContext() inside fragment
            SharedPreferences sp = getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("organization", org.getName());
            editor.putString("theme_color", org.getThemeColor());
            editor.apply();

            // FIXED: close fragment properly instead of finish()
            //changed
            //requireActivity().getSupportFragmentManager().popBackStack();
        });

        rvOrganizations.setAdapter(adapter);

        // Search logic
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        // ADDED: handle Save Organization button inside fragment
        Button btnSave = view.findViewById(R.id.btn_saveOrg);
        //changed
        btnSave.setOnClickListener(v -> {
            SharedPreferences spCheck = getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
            String orgCheck = spCheck.getString("organization", "");

            if(orgCheck.isEmpty()){
                Toast.makeText(getContext(), "Please select an organization", Toast.LENGTH_SHORT).show();
                return;
            }

            ((create_account_activity) getActivity()).saveOrganizationAndProceed();

            // NOW close fragment AFTER saving
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view; // IMPORTANT for fragments
    }

    private void filter(String text) {
        filteredList.clear();
        for (Organization org : organizationList) {
            if (org.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(org);
            }
        }

        if (filteredList.isEmpty()) {
            layoutEmpty.setVisibility(View.VISIBLE);
            rvOrganizations.setVisibility(View.GONE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
            rvOrganizations.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }

    // Organization model class
    public static class Organization {
        private String name;
        private int iconRes;
        private String themeColor;

        public Organization(String name, int iconRes, String themeColor) {
            this.name = name;
            this.iconRes = iconRes;
            this.themeColor = themeColor;
        }

        public String getName() { return name; }
        public int getIconRes() { return iconRes; }
        public String getThemeColor() { return themeColor; }
    }

    // Adapter for RecyclerView
    private static class OrganizationAdapter extends RecyclerView.Adapter<OrganizationAdapter.OrgViewHolder> {
        private List<Organization> list;
        private OnOrgClickListener listener;
        private int selectedPosition = -1;

        public interface OnOrgClickListener {
            void onOrgClick(Organization org);
        }

        public OrganizationAdapter(List<Organization> list, OnOrgClickListener listener) {
            this.list = list;
            this.listener = listener;
        }

        @NonNull
        @Override
        public OrgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // FIXED: use correct item layout for each organization row
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_choose, parent, false);
            return new OrgViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull OrgViewHolder holder, int position) {
            Organization org = list.get(position);
            holder.tvName.setText(org.getName());
            holder.ivIcon.setImageResource(org.getIconRes());
            holder.viewColor.setBackgroundColor(Color.parseColor(org.getThemeColor()));
            if(position == selectedPosition){
                holder.itemView.setBackgroundColor(Color.parseColor("#8972B4")); // purple
            } else {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            }
            holder.itemView.setOnClickListener(v -> {
                selectedPosition = position;
                notifyDataSetChanged();
                listener.onOrgClick(org);
            });
        }

        @Override
        public int getItemCount() { return list.size(); }

        static class OrgViewHolder extends RecyclerView.ViewHolder {
            TextView tvName;
            ImageView ivIcon;
            View viewColor;

            public OrgViewHolder(@NonNull View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvOrgName);
                ivIcon = itemView.findViewById(R.id.ivOrgIcon);
                viewColor = itemView.findViewById(R.id.viewColorPreview);
            }
        }
    }
}

