package com.example.ayogeshwaran.capstone.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ayogeshwaran.capstone.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PNRHistoryRecyclerViewAdapter extends
        RecyclerView.Adapter<PNRHistoryRecyclerViewAdapter.PNRHistoryViewHolder>  {

    private List<String> pnrNumbers;

    private final IPNRNumberClickedListener ipnrNumberClickedListener;

    public PNRHistoryRecyclerViewAdapter(List<String> pnrNumbers,
                                         IPNRNumberClickedListener ipnrNumberClickedListener) {
        if (pnrNumbers != null) {
            this.pnrNumbers = pnrNumbers;
        }
        this.ipnrNumberClickedListener = ipnrNumberClickedListener;
    }

    public void updateList(List<String> newList) {
        pnrNumbers = newList;
        notifyDataSetChanged();
    }

    @Override
    public PNRHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.history_item, parent, false);
        return new PNRHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PNRHistoryViewHolder holder, int position) {
        if (holder != null) {
            String pnrNumber = pnrNumbers.get(position);
            holder.pnrNumberTv.setText(pnrNumber);
        }
    }

    @Override
    public int getItemCount() {
        if (pnrNumbers != null) {
            return pnrNumbers.size();
        }
        return 0;
    }

    public class PNRHistoryViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.pnr_number_textview)
        TextView pnrNumberTv;

        @BindView(R.id.pnr_number_clear)
        ImageButton pnrNumberClearBtn;

        PNRHistoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            pnrNumberClearBtn.setOnClickListener(v -> {
                String pnrNumber = pnrNumbers.get(getAdapterPosition());
                ipnrNumberClickedListener.onClear(pnrNumber);
            });
        }


        @Override
        public void onClick(View v) {
            String pnrNumber = pnrNumbers.get(getAdapterPosition());
            ipnrNumberClickedListener.onClick(pnrNumber);
        }
    }

    public interface IPNRNumberClickedListener {
        void onClick(String pnrNumber);

        void onClear(String pnrNumber);
    }
}
