package com.example.ayogeshwaran.capstone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ayogeshwaran.capstone.R;
import com.example.ayogeshwaran.capstone.model.Passenger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PassengerListRecyclerViewAdapter extends
        RecyclerView.Adapter<PassengerListRecyclerViewAdapter.PassengerListViewHolder> {

    private List<Passenger> passengers;

    private final Context context;

    public PassengerListRecyclerViewAdapter(List<Passenger> passengers, Context context) {
        if (passengers != null) {
            this.passengers = passengers;
        }
        this.context = context;
    }

    public void updateList(List<Passenger> newList) {
        passengers = newList;
        notifyDataSetChanged();
    }

    @Override
    public PassengerListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.passenger_item, parent, false);
        return new PassengerListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PassengerListViewHolder holder, int position) {
        if (holder != null) {
            Passenger passenger = passengers.get(position);

            int index = position + 1;
            holder.passengerIndexTv.setText(String.format(context.getResources().getString(
                    R.string.passenger_index), String.valueOf(index)));
            holder.bookingStatusTv.setText(passenger.getBookingStatus());
            if (passenger.getBookingStatus().startsWith("CNF") &&
                    !passenger.getCurrentStatus().startsWith("CAN")) {
                holder.currentStatusTv.setText(passenger.getBookingStatus());
            } else {
                holder.currentStatusTv.setText(passenger.getCurrentStatus());
            }

        }
    }

    @Override
    public int getItemCount() {
        if (passengers != null) {
            return passengers.size();
        }
        return 0;
    }

    public class PassengerListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.passenger_index_tv)
        TextView passengerIndexTv;

        @BindView(R.id.current_status_value_tv)
        TextView currentStatusTv;

        @BindView(R.id.booking_status_value_tv)
        TextView bookingStatusTv;

        PassengerListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
