package com.technovanzahackathon.tripistant;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.technovanzahackathon.tripistant.TripChecklists.ColorGenerator;
import com.technovanzahackathon.tripistant.TripChecklists.TripChecklists;
import com.technovanzahackathon.tripistant.TripChecklists.Trips;
import it.feio.android.checklistview.interfaces.Constants;

import java.text.DateFormat;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Pooja S on 12/24/2016.
 */
public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.ViewHolder>{

    private List<Trips> trips;
    Context context;
    private static TripsAdapter.OnItemClickListener onItemClickListener;
    private static TripsAdapter.OnLongItemClickListener onLongItemClickListener;
    public DatabaseHelper databaseHelper;
    Activity activity;
    private List<TripChecklists> filteredList;
    private int lastPosition = -1;

    private static final DateFormat DATETIME_FORMAT = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);

    public TripsAdapter(List<Trips> trips, Context context, Activity activity) {
        this.trips = trips;
        this.context = context;
        this.activity = activity;
    }

    public static void setOnItemClickListener(TripsAdapter.OnItemClickListener onItemClickListener) {
        TripsAdapter.onItemClickListener = onItemClickListener;
    }

    public static void setOnLongItemClickListener(TripsAdapter.OnLongItemClickListener onLongItemClickListener) {
        TripsAdapter.onLongItemClickListener = onLongItemClickListener;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public static Comparator<Trips> newestFirstComparator = new Comparator<Trips>() {
        @Override
        public int compare(Trips lhs, Trips rhs) {
            String lDate = lhs.getTripdate();
            String rDate = rhs.getTripdate();
            return rDate.compareTo(lDate);
        }
    };


    @Override
    public TripsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trips_adapter, parent, false);
        return new TripsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TripsAdapter.ViewHolder holder, int position) {

        ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;

        Trips trip = trips.get(position);

        holder.textname.setText(trip.getTripname());
        holder.textsrc.setText(trip.getTripsrc());
        holder.textdest.setText(trip.getTripdest());
        holder.textdate.setText(trip.getTripdate()+" "+ trip.getTriptime());
        holder.cardView.setCardBackgroundColor(mColorGenerator.getRandomColor());

    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textname, textsrc, textdest, textdate;
        public final View view;
        public CardView cardView;

        public ViewHolder(View parent) {
            super(parent);
            view = parent;
            textname = (TextView) parent.findViewById(R.id.tripname);
            textsrc = (TextView) parent.findViewById(R.id.src);
            textdest = (TextView) parent.findViewById(R.id.dest);
            textdate = (TextView) parent.findViewById(R.id.datetime);
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