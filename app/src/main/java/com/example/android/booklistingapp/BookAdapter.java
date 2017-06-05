package com.example.android.booklistingapp;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BookAdapter extends ArrayAdapter {
    public BookAdapter(@NonNull Context context, List<Book> Books) {
        super(context, 0, Books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
        }

        // Find the Book at the given position in the list of Books
        Book currentBook = (Book) getItem(position);

        String title = currentBook.getTitle();
        String author = currentBook.getAuthor();
        String publisher = currentBook.getPublisher();

        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author);
        authorTextView.setText(author);

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title);
        titleTextView.setText(title);

        TextView publisherTextView = (TextView) listItemView.findViewById(R.id.publisher);
        publisherTextView.setText(publisher);

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

}
