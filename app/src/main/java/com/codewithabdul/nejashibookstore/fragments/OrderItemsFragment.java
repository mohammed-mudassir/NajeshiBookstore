package com.codewithabdul.nejashibookstore.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.codewithabdul.nejashibookstore.R;
import com.codewithabdul.nejashibookstore.adapters.BooksAdapter;
import com.codewithabdul.nejashibookstore.interfaces.CustomOnClickInterface;
import com.codewithabdul.nejashibookstore.models.BookModel;

import java.util.ArrayList;

public class OrderItemsFragment extends Fragment implements CustomOnClickInterface {

    ArrayList<BookModel> books;
    Context context;
    BooksAdapter booksAdapter;

    public OrderItemsFragment(Context context, ArrayList<BookModel> books) {
        this.context = context;
        this.books = books;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_items, container, false);
        RecyclerView rv_items = view.findViewById(R.id.rv_items);
        booksAdapter = new BooksAdapter(context, books, this, true);
        rv_items.setAdapter(booksAdapter);
        return view;
    }

    @Override
    public void onClick(String id) {

    }
}