package com.example.myapplication.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.viewmodel.SensorViewModel;
import com.example.myapplication.model.SensorDataDto;

public class MainFragment extends Fragment {

    private SensorViewModel viewModel;
    private TextView textRoom, textReco;
    private TextView valueTemp, valueHumidity, valuePressure, valueAltitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Прив'язка до елементів
        textRoom = view.findViewById(R.id.textRoom);
        textReco = view.findViewById(R.id.textRecommendation);
        valueTemp = view.findViewById(R.id.valueTemp);
        valueHumidity = view.findViewById(R.id.valueHumidity);
        valuePressure = view.findViewById(R.id.valuePressure);
        valueAltitude = view.findViewById(R.id.valueAltitude);

        // ViewModel
        viewModel = new ViewModelProvider(this).get(SensorViewModel.class);

        // Спостерігачі
        viewModel.getSensorData().observe(getViewLifecycleOwner(), data -> {
            if (data != null) {
                textRoom.setText("Кімната: " + safe(data.getRoomName()));
                valueTemp.setText(format(data.getTemperatureDht(), "°C"));
                valueHumidity.setText(format(data.getHumidityDht(), "%"));
                valuePressure.setText(format(data.getPressure(), "hPa"));
                valueAltitude.setText(format(data.getAltitude(), "м"));
            }
        });

        viewModel.getRecommendation().observe(getViewLifecycleOwner(), reco -> {
            textReco.setText("📋 Рекомендація: " + safe(reco));
        });

        viewModel.getError().observe(getViewLifecycleOwner(),
                msg -> Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show());

        // Завантажити дані
        viewModel.loadData("Kitchen"); // або замінити на вибрану кімнату

        return view;
    }

    private String format(Double val, String unit) {
        return val == null ? "-" : String.format("%.1f %s", val, unit);
    }

    private String safe(String val) {
        return (val == null || val.isEmpty()) ? "-" : val;
    }
}
