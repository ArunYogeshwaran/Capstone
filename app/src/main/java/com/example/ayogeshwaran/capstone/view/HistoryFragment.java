package com.example.ayogeshwaran.capstone.view;

import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayogeshwaran.capstone.R;
import com.example.ayogeshwaran.capstone.Utils;
import com.example.ayogeshwaran.capstone.adapter.PNRHistoryRecyclerViewAdapter;
import com.example.ayogeshwaran.capstone.db.PNRContract;
import com.example.ayogeshwaran.capstone.model.PNRStatus;
import com.example.ayogeshwaran.capstone.networkquery.PNRQuery;
import com.example.ayogeshwaran.capstone.widget.PNRHistoryWidgetManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment implements
        PNRHistoryRecyclerViewAdapter.IPNRNumberClickedListener, LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.history_rv)
    protected RecyclerView historyRecyclerView;

    @BindView(R.id.no_result)
    protected TextView noResultTv;

    private Parcelable listState;

    // To persist the recycler view scroll position
    private final String LIST_STATE = "list_state";

    private PNRHistoryRecyclerViewAdapter pnrHistoryRecyclerViewAdapter;

    private List<String> pnrs = new ArrayList<>();

    private static final int LOADER_ID = 123;

    private PNRQuery pnrQuery;

    public HistoryFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, view);
        setRetainInstance(true);
        if (savedInstanceState != null && savedInstanceState.containsKey(LIST_STATE)) {
            listState = savedInstanceState.getParcelable("ListState");
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchPnrsFromDb();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pnrQuery = new PNRQuery();
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        pnrHistoryRecyclerViewAdapter = new PNRHistoryRecyclerViewAdapter(pnrs, this);
        pnrHistoryRecyclerViewAdapter.updateList(pnrs);
        if (listState != null) {
            historyRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
        historyRecyclerView.setAdapter(pnrHistoryRecyclerViewAdapter);
        fetchPnrsFromDb();
    }

    @Override
    public void onClick(String pnrNumber) {
        if (Utils.checkForValidPNR(getActivity(), pnrNumber)) {
            if (Utils.isOnline(getContext())) {
                getPNRStatus(pnrQuery, pnrNumber);
            } else {
                Toast.makeText(getContext(), R.string.check_network,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fetchPnrsFromDb() {
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClear(String pnrNumber) {
        Utils.removePNRFromDB(getActivity(), pnrNumber);
        pnrs = Utils.getAllPNRFromDb(getContext());
        pnrHistoryRecyclerViewAdapter.updateList(pnrs);
    }

    private void getPNRStatus(PNRQuery pnrQuery, String pnrNO) {
        Call<PNRStatus> resultCall = pnrQuery.getPNRResult(pnrNO);
        resultCall.enqueue(new Callback<PNRStatus>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NonNull Call<PNRStatus> call,
                                   @NonNull Response<PNRStatus> response) {
                PNRStatus pnrStatus = response.body();
                if (pnrStatus == null) {
                    Log.e("HistoryFragment", "onResponse: null");
                } else if (pnrStatus.getPassengers().size() <= 0) {
                    Toast.makeText(getContext(), R.string.empty_response,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getContext(), ShowPNRStatusActivity.class);
                    intent.putExtra(ShowPNRStatusActivity.PNR_STATUS_OBJECT, pnrStatus);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        startActivity(intent,
                                ActivityOptions.makeCustomAnimation(getActivity(),
                                        R.anim.zoom_enter, R.anim.zoom_exit).toBundle());
                    } else {
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<PNRStatus> call, @NonNull Throwable t) {
                if (!call.isCanceled()) {
                    Toast.makeText(getContext(), R.string.error_in_retrieving,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LIST_STATE,
                historyRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                PNRContract.PNREntry.CONTENT_URI,
                null,
                null,
                null, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        pnrs.clear();
        if (data != null && data.moveToFirst()) {
            do {
                String pnrNo = data.getString(data.getColumnIndex(PNRContract.PNREntry.PNR));
                pnrs.add(pnrNo);
            } while (data.moveToNext());
            new PNRHistoryWidgetManager().updateWidgetPNR(Utils.getAllPNRFromDb(getContext()));
            pnrHistoryRecyclerViewAdapter.updateList(pnrs);
            if (listState != null) {
                historyRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
            }
            showDataView();
        }
        if (data == null || !data.moveToFirst()) {
            pnrHistoryRecyclerViewAdapter.updateList(pnrs);
            showNoDataView();
        }
    }

    private void showDataView() {
        noResultTv.setVisibility(View.GONE);
        historyRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showNoDataView() {
        noResultTv.setVisibility(View.VISIBLE);
        historyRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
