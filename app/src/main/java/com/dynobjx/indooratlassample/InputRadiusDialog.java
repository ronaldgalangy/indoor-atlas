package com.dynobjx.indooratlassample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 12/2/16.
 */

public class InputRadiusDialog extends DialogFragment implements View.OnClickListener {


    @BindView(R.id.et_radius)
    EditText etRadius;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    public InputRadiusDialog() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static InputRadiusDialog newInstance() {
        return new InputRadiusDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_settings, container, false);
        ButterKnife.bind(this, view);
        btnSubmit.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etRadius.setText(PrefsHelper.getFloat(view.getContext(), App.RADIUS_KEY) + "");
    }

    @Override
    public void onClick(View view) {
        final float radius = Float.valueOf(etRadius.getText().toString());
        PrefsHelper.setFloat(view.getContext(), App.RADIUS_KEY, radius > 0 ? radius : 1);
        this.dismiss();
    }
}
