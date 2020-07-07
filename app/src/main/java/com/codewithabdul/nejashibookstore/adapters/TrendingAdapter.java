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
import com.codewithabdul.nejashibookstore.activities.BookDetailActivity;
import com.codewithabdul.nejashibookstore.models.BookModel;

import java.util.ArrayList;

import static com.codewithabdul.nejashibookstore.utils.ConstantUtils.BASE_BOOK_IMAGES;

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.ViewHolder> {

    private ArrayList<BookModel> data;
    private Context context;

    public TrendingAdapter(Context context, ArrayList<BookModel> data) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_trending, parent, false);
        return new ViewHolder(listItem);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(BASE_BOOK_IMAGES + data.get(position).getImage())
                .into(holder.img_main);

        holder.tv_title.setText(data.get(position).getTitle());
        holder.tv_by.setText(data.get(position).getAuthor());

        holder.itemView.setOnClickListener(v -> {
            context.startActivity(new Intent(context, BookDetailActivity.class)
                    .putExtra("id", data.get(position).getProduct_id()));
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_main;
        TextView tv_title, tv_by;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_main = itemView.findViewById(R.id.img_main);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_by = itemView.findViewById(R.id.tv_by);
        }
    }
}
