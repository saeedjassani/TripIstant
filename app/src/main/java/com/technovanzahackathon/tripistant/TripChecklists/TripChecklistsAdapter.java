package com.technovanzahackathon.tripistant.TripChecklists;

/**
 * Created by Pooja S on 12/24/2016.
 */
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.*;
import android.widget.*;

import java.text.DateFormat;
import java.util.*;

import com.technovanzahackathon.tripistant.DatabaseHelper;
import com.technovanzahackathon.tripistant.R;
import it.feio.android.checklistview.interfaces.Constants;

public class TripChecklistsAdapter extends RecyclerView.Adapter<TripChecklistsAdapter.ViewHolder>{

    private List<TripChecklists> notes;
    Context context;
    private static OnItemClickListener onItemClickListener;
    private static OnLongItemClickListener onLongItemClickListener;
    public DatabaseHelper databaseHelper;
    Activity activity;
    private List<TripChecklists> filteredList;
    private int lastPosition = -1;

    private static final DateFormat DATETIME_FORMAT = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);

    public TripChecklistsAdapter(List<TripChecklists> notes, Context context, Activity activity) {
        this.notes = notes;
        this.context = context;
        this.activity = activity;
    }

    public static void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        TripChecklistsAdapter.onItemClickListener = onItemClickListener;
    }

    public static void setOnLongItemClickListener(OnLongItemClickListener onLongItemClickListener) {
        TripChecklistsAdapter.onLongItemClickListener = onLongItemClickListener;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static Comparator<TripChecklists> newestFirstComparator = new Comparator<TripChecklists>() {
        @Override
        public int compare(TripChecklists lhs, TripChecklists rhs) {
            String lDate = lhs.getUpdated_at();
            String rDate = rhs.getUpdated_at();
            return rDate.compareTo(lDate);
        }
    };


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checklist_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
        int color = context.getResources().getColor(R.color.white);

        TripChecklists note = notes.get(position);
        holder.textRow.setText(note.getTitle());
        holder.textUpdated.setText(note.getUpdated_at());

        if(note.getLock_status() == 1){
            holder.lock.setImageResource(R.drawable.ic_menu_share);
            holder.lock.setVisibility(View.VISIBLE);
        }else if(note.getLock_status() == 0){
            holder.lock.setVisibility(View.INVISIBLE);
        }

        holder.cardView.setCardBackgroundColor(note.getColor());


        String content = note.getContent();
        content = content.replace(Constants.CHECKED_SYM,Constants.CHECKED_ENTITY).replace(Constants.UNCHECKED_SYM,Constants.UNCHECKED_ENTITY).replace(System.getProperty("line.separator"), "<br/>");
        Spanned contentSpanned = Html.fromHtml(content);

        if(note.getLock_status() == 1) {
            holder.textContent.setText("****************** \n******************");
        }else {
            holder.textContent.setText(contentSpanned);
        }
    }

    public static int getContrastColor(int color){
        double y = (299 * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color))/1000;
        return y >=128 ? Color.BLACK : Color.WHITE;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textRow;
        public TextView textContent;
        public TextView textUpdated;
        public ImageView imageView,star,lock;
        public final View view;
        public CardView cardView;

        public ViewHolder(View parent) {
            super(parent);
            view = parent;
            textRow = (TextView) parent.findViewById(R.id.textRow);
            textContent = (TextView) parent.findViewById(R.id.note_content);
            textUpdated = (TextView) parent.findViewById(R.id.note_date);
            imageView = (ImageView) parent.findViewById(R.id.imageView);
            lock = (ImageView) parent.findViewById(R.id.lock);
            cardView = (CardView) parent.findViewById(R.id.cardview);

            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getPosition(), v);

                }
            });
            parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onLongItemClickListener.onLongItemClick(getPosition(), v);
                    return true;
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    public interface OnLongItemClickListener {
        void onLongItemClick(int position, View v);
    }

}