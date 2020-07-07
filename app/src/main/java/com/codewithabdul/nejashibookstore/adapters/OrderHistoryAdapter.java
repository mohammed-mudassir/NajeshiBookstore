package com.codewithabdul.nejashibookstore.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codewithabdul.nejashibookstore.R;
import com.codewithabdul.nejashibookstore.activities.OrderDetailsActivity;
import com.codewithabdul.nejashibookstore.interfaces.CancelOrderInterface;
import com.codewithabdul.nejashibookstore.models.OrderModel;
import com.codewithabdul.nejashibookstore.utils.CommonUtils;
import com.codewithabdul.nejashibookstore.utils.ConstantUtils;

import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyHolder> {

    private ArrayList<OrderModel> orders;
    Context context;
    private CancelOrderInterface iCancelOrder;

    public OrderHistoryAdapter(Context context, ArrayList<OrderModel> orders, CancelOrderInterface iCancelOrder) {
        this.orders = orders;
        this.context = context;
        this.iCancelOrder = iCancelOrder;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_order_history, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.tv_id.setText("#" + orders.get(position).getTransaction_id());
//        holder.tv_count.setText(orders.get(position).getTotal_items());
        holder.tv_timestamp.setText(CommonUtils.changeDateFormat(orders.get(position).getTransaction_date()));
        holder.tv_price.setText(orders.get(position).getTransaction_amount() + " " + ConstantUtils.CURRENCY_SYMBOL);

        holder.tv_view.setOnClickListener(v -> {
            context.startActivity(new Intent(context, OrderDetailsActivity.class).putExtra("order", orders.get(position)));
        });

        holder.tv_cancel.setOnClickListener(v -> {
            Toast.makeText(context, "Coming soon...", Toast.LENGTH_SHORT).show();
//            AlertDialog.Builder builder = new AlertDialog.Builder(
//                    context);
//            builder.setTitle("Cancel the order?");
//            builder.setMessage("Do you really want to cancel this order?");
//            builder.setPositiveButton("Yes",
//                    (dialog, which) -> {
//                        // 4: cancel order
//                        iCancelOrder.onCancel(orders.get(position).getTransaction_id());
//                    });
//            builder.setNegativeButton("No",
//                    (dialog, which) -> {
//                    });
//            builder.show();
        });

        boolean canCancel = orders.get(position).getOrder_status().equals(ConstantUtils.ORDER_STATUS_IN_PROGRESS) ||
                orders.get(position).getOrder_status().equals(ConstantUtils.ORDER_STATUS_PENDING_PAYMENT) ||
                orders.get(position).getOrder_status().equals(ConstantUtils.ORDER_STATUS_CONFIRMED);

        holder.tv_cancel.setVisibility(canCancel ? View.VISIBLE : View.GONE);

        switch (orders.get(position).getOrder_status()) {
            case ConstantUtils.ORDER_STATUS_IN_PROGRESS:
                holder.tv_order_status.setText(context.getResources().getString(R.string.in_progress));
                holder.tv_order_status.setTextColor(context.getResources().getColor(R.color.app_color));
                holder.rl_parent.setBackground(context.getResources().getDrawable(R.drawable.pending_rounded_rect_blue));
                break;
            case ConstantUtils.ORDER_STATUS_PENDING_PAYMENT:
                holder.tv_order_status.setText(context.getResources().getString(R.string.pending_payment));
                holder.tv_order_status.setTextColor(context.getResources().getColor(R.color.app_color));
                holder.rl_parent.setBackground(context.getResources().getDrawable(R.drawable.pending_rounded_rect_blue));
                break;
            case ConstantUtils.ORDER_STATUS_CONFIRMED:
                holder.tv_order_status.setText(context.getResources().getString(R.string.confirmed));
                holder.tv_order_status.setTextColor(context.getResources().getColor(R.color.app_color));
                holder.rl_parent.setBackground(context.getResources().getDrawable(R.drawable.pending_rounded_rect_blue));
                break;
            case ConstantUtils.ORDER_STATUS_OUT_FOR_DEL:
                holder.tv_order_status.setText(context.getResources().getString(R.string.out_for_del));
                holder.tv_order_status.setTextColor(context.getResources().getColor(R.color.app_color));
                holder.rl_parent.setBackground(context.getResources().getDrawable(R.drawable.pending_rounded_rect_blue));
                break;
            case ConstantUtils.ORDER_STATUS_DELIVERED:
                holder.tv_order_status.setText(context.getResources().getString(R.string.delivered));
                holder.tv_order_status.setTextColor(context.getResources().getColor(R.color.green));
                holder.rl_parent.setBackground(context.getResources().getDrawable(R.drawable.delivered_rounded_rect_green));
                break;
            case ConstantUtils.ORDER_STATUS_CANCELLED:
                holder.tv_order_status.setText(context.getResources().getString(R.string.cancelled));
                holder.tv_order_status.setTextColor(context.getResources().getColor(R.color.red));
                holder.rl_parent.setBackground(context.getResources().getDrawable(R.drawable.cancelled_rounded_rect_red));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {

        TextView tv_id, tv_count, tv_timestamp, tv_price, tv_order_status, tv_view, tv_cancel;
        RelativeLayout rl_parent;

        MyHolder(@NonNull View itemView) {
            super(itemView);
            tv_id = itemView.findViewById(R.id.tv_id);
            tv_view = itemView.findViewById(R.id.tv_view);
            tv_count = itemView.findViewById(R.id.tv_count);
            tv_timestamp = itemView.findViewById(R.id.tv_timestamp);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_order_status = itemView.findViewById(R.id.tv_order_status);
            rl_parent = itemView.findViewById(R.id.rl_parent);
            tv_cancel = itemView.findViewById(R.id.tv_cancel);
        }
    }
}
