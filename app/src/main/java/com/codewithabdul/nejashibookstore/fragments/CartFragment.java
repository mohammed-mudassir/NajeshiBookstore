package com.codewithabdul.nejashibookstore.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.codewithabdul.nejashibookstore.R;
import com.codewithabdul.nejashibookstore.adapters.CartItemsAdapter;
import com.codewithabdul.nejashibookstore.database.OrderTable;
import com.codewithabdul.nejashibookstore.interfaces.CustomOnClickInterface;
import com.codewithabdul.nejashibookstore.models.BookModel;
import com.codewithabdul.nejashibookstore.models.CartModel;

import java.util.ArrayList;

public class CartFragment extends Fragment implements CustomOnClickInterface {

    LinearLayout ll_empty_cart;

    ArrayList<BookModel> cartItems = new ArrayList<>();
    OrderTable orderTable;
    CartItemsAdapter cartItemsAdapter;
    CustomOnClickInterface iCustomClick;

    public CartFragment(CustomOnClickInterface iCustomClick) {
        this.iCustomClick = iCustomClick;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ll_empty_cart = view.findViewById(R.id.ll_empty_cart);

        orderTable = new OrderTable(getContext());

        RecyclerView rv_books = view.findViewById(R.id.rv_books);
        cartItemsAdapter = new CartItemsAdapter(getContext(), cartItems, this);
        rv_books.setAdapter(cartItemsAdapter);

        getCartData();

        return view;
    }

    private void getCartData() {
        CartModel cartModel = orderTable.cartData();
        cartItems.clear();
        cartItems.addAll(cartModel.getBookModels());
        cartItemsAdapter.notifyDataSetChanged();
        ll_empty_cart.setVisibility(cartItems.size() > 0 ? View.GONE : View.VISIBLE);
        iCustomClick.onClick(cartModel.getTotal());
    }

    @Override
    public void onClick(String item_id) {
        if (item_id.equals("event")) {
            getCartData();
        } else {
            orderTable.deleteItemFromCart(item_id);
            getCartData();
        }
        iCustomClick.onClick(item_id);
    }
}