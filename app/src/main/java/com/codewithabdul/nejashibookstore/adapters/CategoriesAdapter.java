package com.codewithabdul.nejashibookstore.adapters;

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
import com.codewithabdul.nejashibookstore.R;
import com.codewithabdul.nejashibookstore.activities.BookListActivity;
import com.codewithabdul.nejashibookstore.models.CategoryModel;

import java.util.ArrayList;

import static com.codewithabdul.nejashibookstore.utils.ConstantUtils.BASE_CATEGORY_IMAGES;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private ArrayList<CategoryModel> listdata;
    private Context context;

    public CategoriesAdapter(Context context, ArrayList<CategoryModel> listdata) {
        this.listdata = listdata;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_category, parent, false);
        return new ViewHolder(listItem);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(BASE_CATEGORY_IMAGES + listdata.get(position).getCat_image())
                .into(holder.img_main);

        holder.tv_title.setText(listdata.get(position).getName());

        holder.itemView.setOnClickListener(v -> {
            context.startActivity(new Intent(context, BookListActivity.class)
                    .putExtra("category_id", listdata.get(position).getCategory_id()));
        });

    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_main;
        TextView tv_title;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_main = itemView.findViewById(R.id.img_main);

            tv_title = itemView.findViewById(R.id.tv_title);
        }
    }
}
