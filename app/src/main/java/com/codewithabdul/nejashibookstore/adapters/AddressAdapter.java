package com.codewithabdul.nejashibookstore.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codewithabdul.nejashibookstore.R;
import com.codewithabdul.nejashibookstore.activities.AddEditAddressActivity;
import com.codewithabdul.nejashibookstore.interfaces.AddressSelectionInterface;
import com.codewithabdul.nejashibookstore.models.AddressModel;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter {

    private ArrayList<AddressModel> data;
    private Context context;
    private final int TYPE_ADDRESS = 0;
    private final int TYPE_ADD_ADDRESS = 1;
    private AddressSelectionInterface iAddressSelection;

    public AddressAdapter(Context context, ArrayList<AddressModel> data, AddressSelectionInterface iAddressSelection) {
        this.data = data;
        this.context = context;
        this.iAddressSelection = iAddressSelection;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case TYPE_ADDRESS:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_address, parent, false);
                return new ViewHolder(view);
            case TYPE_ADD_ADDRESS:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_add_address, parent, false);
                return new AddAddressViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == data.size()) {
            ((AddAddressViewHolder) holder).rl_address.setOnClickListener(v -> {
                context.startActivity(new Intent(context, AddEditAddressActivity.class));
            });
        } else {
            ((ViewHolder) holder).tv_name.setText(data.get(position).getTitle());
            ((ViewHolder) holder).tv_contact.setText(data.get(position).getContact());
            ((ViewHolder) holder).tv_desc.setText(data.get(position).getDescription());
            ((ViewHolder) holder).ll_address.setOnClickListener(v -> {
                setSelection(position);
            });
            ((ViewHolder) holder).ll_address.setBackgroundColor(context.getResources().getColor(data.get(position).isSelected() ? R.color.green_light : R.color.white));
            ((ViewHolder) holder).iv_edit.setOnClickListener(v -> {
                context.startActivity(new Intent(context, AddEditAddressActivity.class)
                        .putExtra("address", data.get(position)));
            });
            ((ViewHolder) holder).iv_delete.setOnClickListener(v -> {
                Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == data.size()) {
            return TYPE_ADD_ADDRESS;
        }
        return TYPE_ADDRESS;
    }

    private void setSelection(int position) {
        for (int i = 0; i < data.size(); i++) {
            data.get(i).setSelected(position == i);
        }
        iAddressSelection.onAddressSelected(data.get(position));
        notifyDataSetChanged();
    }

    public void setSelectionById(String id) {
        for (int i = 0; i < data.size(); i++) {
            data.get(i).setSelected(id.equals(data.get(i).getAdd_id()));
            if(id.equals(data.get(i).getAdd_id())) {
                iAddressSelection.onAddressSelected(data.get(i));
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ll_address;
        TextView tv_name, tv_contact, tv_desc;
        ImageView iv_edit, iv_delete;
        RelativeLayout rl_address;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_address = itemView.findViewById(R.id.ll_address);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_contact = itemView.findViewById(R.id.tv_contact);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            iv_edit = itemView.findViewById(R.id.iv_edit);
            iv_delete = itemView.findViewById(R.id.iv_delete);
            rl_address = itemView.findViewById(R.id.rl_address);
        }
    }

    static class AddAddressViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rl_address;

        AddAddressViewHolder(@NonNull View itemView) {
            super(itemView);
            rl_address = itemView.findViewById(R.id.rl_address);
        }
    }
}
