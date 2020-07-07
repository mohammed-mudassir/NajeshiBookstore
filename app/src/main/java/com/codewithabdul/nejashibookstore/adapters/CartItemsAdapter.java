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
import androidx.appcompat.app.AlertDialog;
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
import static com.codewithabdul.nejashibookstore.utils.ConstantUtils.CURRENCY_SYMBOL;

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.MyHolder> {

    private ArrayList<BookModel> items;
    private Context context;
    private CustomOnClickInterface iCustomClick;
    private OrderTable orderTable;

    public CartItemsAdapter(Context context, ArrayList<BookModel> items, CustomOnClickInterface iCustomClick) {
        this.context = context;
        this.items = items;
        this.iCustomClick = iCustomClick;
        this.orderTable = new OrderTable(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cart, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Glide.with(context)
                .load(BASE_BOOK_IMAGES + items.get(position).getImage())
                .into(holder.img_main);

        holder.img_main.setOnClickListener(v -> {
            context.startActivity(new Intent(context, BookDetailActivity.class)
                    .putExtra("id", items.get(position).getProduct_id()));
        });

        holder.ll_details.setOnClickListener(v -> {
            context.startActivity(new Intent(context, BookDetailActivity.class)
                    .putExtra("id", items.get(position).getProduct_id()));
        });

        holder.tv_title.setText(items.get(position).getTitle());
        holder.tv_price.setText(items.get(position).getTotal_price() + " " + ConstantUtils.CURRENCY_SYMBOL);
        items.get(position).setCount(String.valueOf(orderTable.getCartItemQty(items.get(position).getProduct_id())));
        holder.tv_count.setText(items.get(position).getCount());
        if (Integer.parseInt(items.get(position).getCount()) > 0) {
            holder.iv_minus.setVisibility(View.VISIBLE);
            holder.tv_count.setVisibility(View.VISIBLE);
            holder.tv_add.setVisibility(View.GONE);
        } else {
            holder.iv_minus.setVisibility(View.GONE);
            holder.tv_count.setVisibility(View.GONE);
            holder.tv_add.setVisibility(View.VISIBLE);
        }
        holder.iv_plus.setOnClickListener(v -> {
            items.get(position).setCount(String.valueOf(Integer.parseInt(items.get(position).getCount()) + 1));
            holder.tv_count.setText(items.get(position).getCount());
            if (Integer.parseInt(items.get(position).getCount()) > 0) {
                holder.iv_minus.setVisibility(View.VISIBLE);
                holder.tv_count.setVisibility(View.VISIBLE);
                holder.tv_add.setVisibility(View.GONE);
            } else {
                holder.iv_minus.setVisibility(View.GONE);
                holder.tv_count.setVisibility(View.GONE);
                holder.tv_add.setVisibility(View.VISIBLE);
            }
            orderTable.addToCart(items.get(position), orderTable.getCartItemQty(items.get(position).getProduct_id()) + 1);
            iCustomClick.onClick("event");
        });
        holder.iv_minus.setOnClickListener(v -> {
            items.get(position).setCount(String.valueOf(Integer.parseInt(items.get(position).getCount()) - 1));
            holder.tv_count.setText(items.get(position).getCount());
            if (Integer.parseInt(items.get(position).getCount()) > 0) {
                holder.iv_minus.setVisibility(View.VISIBLE);
                holder.tv_count.setVisibility(View.VISIBLE);
                holder.tv_add.setVisibility(View.GONE);
            } else {
                holder.iv_minus.setVisibility(View.GONE);
                holder.tv_count.setVisibility(View.GONE);
                holder.tv_add.setVisibility(View.VISIBLE);
            }
            orderTable.addToCart(items.get(position), orderTable.getCartItemQty(items.get(position).getProduct_id()) - 1);
            iCustomClick.onClick("event");
        });
        holder.iv_del.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    context);
            builder.setTitle("Remove from cart?");
            builder.setMessage("Do you really want to remove this item from cart?");
            builder.setPositiveButton("Yes",
                    (dialog, which) -> iCustomClick.onClick(items.get(position).getProduct_id()));
            builder.setNegativeButton("No",
                    (dialog, which) -> {
                    });
            builder.show();
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {

        ImageView img_main, iv_minus, iv_plus, iv_del;
        TextView tv_title, tv_price, tv_add, tv_count;
        LinearLayout ll_details;

        MyHolder(@NonNull View itemView) {
            super(itemView);
            img_main = itemView.findViewById(R.id.img_main);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_add = itemView.findViewById(R.id.tv_add);
            iv_minus = itemView.findViewById(R.id.iv_minus);
            tv_count = itemView.findViewById(R.id.tv_count);
            iv_plus = itemView.findViewById(R.id.iv_plus);
            iv_del = itemView.findViewById(R.id.iv_del);
            ll_details = itemView.findViewById(R.id.ll_details);
        }
    }
}
