package com.codewithabdul.nejashibookstore.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codewithabdul.nejashibookstore.R;
import com.codewithabdul.nejashibookstore.activities.BookDetailActivity;
import com.codewithabdul.nejashibookstore.database.OrderTable;
import com.codewithabdul.nejashibookstore.interfaces.CustomOnClickInterface;
import com.codewithabdul.nejashibookstore.models.BookModel;
import com.codewithabdul.nejashibookstore.utils.ConstantUtils;

import java.util.ArrayList;

import static com.codewithabdul.nejashibookstore.utils.ConstantUtils.BASE_BOOK_IMAGES;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {

    private ArrayList<BookModel> data;
    private Context context;
    private OrderTable orderTable;
    private CustomOnClickInterface iCustomClick;
    private boolean isHistory;

    public BooksAdapter(Context context, ArrayList<BookModel> data, CustomOnClickInterface iCustomClick, boolean isHistory) {
        this.data = data;
        this.context = context;
        this.iCustomClick = iCustomClick;
        this.orderTable = new OrderTable(context);
        this.isHistory = isHistory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_book, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(BASE_BOOK_IMAGES + data.get(position).getImage())
                .into(holder.img_main);

        holder.tv_title.setText(data.get(position).getTitle());
        holder.tv_author.setText(data.get(position).getAuthor());

        holder.ll_counter.setVisibility(isHistory ? View.GONE : View.VISIBLE);

        holder.img_main.setOnClickListener(v -> {
            context.startActivity(new Intent(context, BookDetailActivity.class)
                    .putExtra("id", data.get(position).getProduct_id()));
        });

        holder.ll_details.setOnClickListener(v -> {
            context.startActivity(new Intent(context, BookDetailActivity.class)
                    .putExtra("id", data.get(position).getProduct_id()));
        });

        data.get(position).setCount(String.valueOf(orderTable.getCartItemQty(data.get(position).getProduct_id())));

        holder.tv_price.setText(data.get(position).getPrice() + " " + ConstantUtils.CURRENCY_SYMBOL);
        holder.tv_price.setVisibility(isHistory ? View.GONE : View.VISIBLE);

        holder.tv_count.setText(data.get(position).getCount());
        if (Integer.parseInt(data.get(position).getCount()) > 0) {
            holder.iv_minus.setVisibility(View.VISIBLE);
            holder.tv_count.setVisibility(View.VISIBLE);
            holder.tv_add.setVisibility(View.GONE);
        } else {
            holder.iv_minus.setVisibility(View.GONE);
            holder.tv_count.setVisibility(View.GONE);
            holder.tv_add.setVisibility(View.VISIBLE);
        }
        holder.iv_plus.setOnClickListener(v -> {
            String count = String.valueOf(Integer.parseInt(data.get(position).getCount()) + 1);
            holder.tv_count.setText(count);
            data.get(position).setCount(count);
            if (Integer.parseInt(count) > 0) {
                holder.iv_minus.setVisibility(View.VISIBLE);
                holder.tv_count.setVisibility(View.VISIBLE);
                holder.tv_add.setVisibility(View.GONE);
            } else {
                holder.iv_minus.setVisibility(View.GONE);
                holder.tv_count.setVisibility(View.GONE);
                holder.tv_add.setVisibility(View.VISIBLE);
            }
            orderTable.addToCart(data.get(position), orderTable.getCartItemQty(data.get(position).getProduct_id()) + 1);
            iCustomClick.onClick("event");
        });
        holder.iv_minus.setOnClickListener(v -> {
            String count = String.valueOf(Integer.parseInt(data.get(position).getCount()) - 1);
            holder.tv_count.setText(count);
            data.get(position).setCount(count);
            if (Integer.parseInt(count) > 0) {
                holder.iv_minus.setVisibility(View.VISIBLE);
                holder.tv_count.setVisibility(View.VISIBLE);
                holder.tv_add.setVisibility(View.GONE);
            } else {
                holder.iv_minus.setVisibility(View.GONE);
                holder.tv_count.setVisibility(View.GONE);
                holder.tv_add.setVisibility(View.VISIBLE);
            }
            orderTable.addToCart(data.get(position), orderTable.getCartItemQty(data.get(position).getProduct_id()) - 1);
            iCustomClick.onClick("event");
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_main, iv_minus, iv_plus;
        TextView tv_title, tv_author, tv_count, tv_add, tv_price;
        LinearLayout ll_details, ll_counter;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_main = itemView.findViewById(R.id.img_main);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_count = itemView.findViewById(R.id.tv_count);
            iv_minus = itemView.findViewById(R.id.iv_minus);
            tv_add = itemView.findViewById(R.id.tv_add);
            iv_plus = itemView.findViewById(R.id.iv_plus);
            tv_author = itemView.findViewById(R.id.tv_author);
            tv_price = itemView.findViewById(R.id.tv_price);
            ll_details = itemView.findViewById(R.id.ll_details);
            ll_counter = itemView.findViewById(R.id.ll_counter);
        }
    }
}
