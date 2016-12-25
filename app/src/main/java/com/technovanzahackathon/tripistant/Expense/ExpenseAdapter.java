package com.technovanzahackathon.tripistant.Expense;

/**
 * Created by Pooja S on 10/1/2016.
 */
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import android.view.*;
import android.widget.*;
import com.technovanzahackathon.tripistant.DatabaseHelper;
import com.technovanzahackathon.tripistant.R;

import java.text.DateFormat;
import java.util.*;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder>{

    private List<Expense> notes;

    Context context;
    public DatabaseHelper databaseHelper;
    Activity activity;
    private int which;
    private int lastPosition = -1;
    View view;
    String drawableText;
    private static final String EXTRA_NOTE = "EXTRA_NOTE";
    String STAGGER_CONTENT = "EXTRA CONTENT";

    private static final DateFormat DATETIME_FORMAT = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);

    public ExpenseAdapter(List<Expense> notes, Context context, Activity activity) {
        this.notes = notes;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public long getItemId(int position) {
        return notes.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }


    public static Comparator<Expense> newestFirstComparator = new Comparator<Expense>() {
        @Override
        public int compare(Expense lhs, Expense rhs) {
            String lDate = lhs.getDate();
            String rDate = rhs.getDate();
            return rDate.compareTo(lDate);
        }
    };

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_rows, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Expense note = notes.get(position);
        holder.textRow.setText(note.getTitle());
        holder.textUpdated.setText(note.getDate());
        holder.textContent.setText(note.getNote());
        holder.textContent.setMaxLines(7);

        String check = note.getTitle();
        if(check.equalsIgnoreCase("Clothing") || check.equalsIgnoreCase("E-Commerce") || check.equalsIgnoreCase("Extras") || check.equalsIgnoreCase("Food") || check.equalsIgnoreCase("Movies") || check.equalsIgnoreCase("Stocks")){
            holder.star.setTextColor(Color.RED);
            holder.star.setText(String.valueOf(note.getAmount()));
        }else if(check.equalsIgnoreCase("Bank") || check.equalsIgnoreCase("Deposit") || check.equalsIgnoreCase("Extras (Savings)") || check.equalsIgnoreCase("Gift")){
            holder.star.setTextColor(Color.GREEN);
            holder.star.setText(String.valueOf(note.getAmount()));
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textRow;
        public TextView textContent;
        public TextView textUpdated;
        public TextView star;
        public final View view;
        public CardView cardView;
        public RelativeLayout relativeLayout;

        public ViewHolder(View parent) {
            super(parent);
            view = parent;
            textRow = (TextView) parent.findViewById(R.id.textRow);
            textContent = (TextView) parent.findViewById(R.id.note_content);
            textUpdated = (TextView) parent.findViewById(R.id.note_date);
            star = (TextView) parent.findViewById(R.id.star);
            cardView = (CardView) parent.findViewById(R.id.cardview);
            relativeLayout = (RelativeLayout) parent.findViewById(R.id.relativeLayout);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    public interface OnLongItemClickListener {
        void onLongItemClick(int position, View v);
    }
}