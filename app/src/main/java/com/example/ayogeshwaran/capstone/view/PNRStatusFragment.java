package com.example.ayogeshwaran.capstone.view;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ayogeshwaran.capstone.R;
import com.example.ayogeshwaran.capstone.Utils;
import com.example.ayogeshwaran.capstone.model.PNRStatus;
import com.example.ayogeshwaran.capstone.networkquery.PNRQuery;
import com.example.ayogeshwaran.capstone.widget.PNRHistoryWidgetManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PNRStatusFragment extends Fragment {

    @BindView(R.id.submit_btn)
    protected Button submitButton;

    @BindView(R.id.edit_text_enter_pnr)
    protected EditText pnrNumberEditText;

    private String enteredPNR;

    private PNRQuery pnrQuery;
    private final String ENTERED_TEXT = "entered_text";

    public PNRStatusFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pnr_status, container, false);
        ButterKnife.bind(this, view);
        setRetainInstance(true);
        if (savedInstanceState != null && savedInstanceState.containsKey(ENTERED_TEXT)) {
            pnrNumberEditText.setText(savedInstanceState.getString(ENTERED_TEXT));
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pnrQuery = new PNRQuery();
        submitButton.setOnClickListener(v -> {
            enteredPNR = String.valueOf(pnrNumberEditText.getText());
            if (Utils.checkForValidPNR(getActivity(), enteredPNR)) {
                if (Utils.isOnline(getContext())) {
                    getPNRStatus(pnrQuery, enteredPNR);
                } else {
                    Toast.makeText(getContext(), R.string.check_network,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ENTERED_TEXT, pnrNumberEditText.getText().toString());
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
                    Log.e("PNRStatusFragment", "onResponse: null");
                } else if (pnrStatus.getPassengers().size() <= 0) {
                    Toast.makeText(getContext(), R.string.empty_response,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Utils.addPNRToDb(getContext(), String.valueOf(pnrStatus.getPnr()));
                    new PNRHistoryWidgetManager().updateWidgetPNR(Utils.getAllPNRFromDb(getContext()));
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
}
