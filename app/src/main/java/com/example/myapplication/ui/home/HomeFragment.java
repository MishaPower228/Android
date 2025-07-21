package com.example.myapplication.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

    private final Handler handler = new Handler();
    private final int UPDATE_INTERVAL_MS = 10 * 60 * 1000;
    private Runnable updateTask;

    private final String baseUrl = "http://192.168.0.200/api/sensordata"; // 🔁 Заміни на свою IP
    private final String selectedRoom = "Room1"; // 🔁 Заміни динамічно, якщо потрібно

    private TextView tvRoomName, tvTemperature, tvHumidity, tvGas, tvPressure, tvAltitude, tvRecommendation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        tvRoomName = root.findViewById(R.id.tvRoomName);
        tvTemperature = root.findViewById(R.id.tvTemperature);
        tvHumidity = root.findViewById(R.id.tvHumidity);
        tvGas = root.findViewById(R.id.tvGas);
        tvPressure = root.findViewById(R.id.tvPressure);
        tvAltitude = root.findViewById(R.id.tvAltitude);
        tvRecommendation = root.findViewById(R.id.tvRecommendation);

        updateTask = new Runnable() {
            @Override
            public void run() {
                getSensorData(selectedRoom);
                getRecommendation(selectedRoom);
                handler.postDelayed(this, UPDATE_INTERVAL_MS);
            }
        };
        updateTask.run();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(updateTask);
    }

    private void getSensorData(String room) {
        String url = baseUrl + "/sensordata/latest";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            if (obj.getString("roomName").equalsIgnoreCase(room)) {
                                tvRoomName.setText("Кімната: " + obj.getString("roomName"));
                                tvTemperature.setText("Температура: " + obj.getDouble("temperatureDht") + " °C");
                                tvHumidity.setText("Вологість: " + obj.getDouble("humidityDht") + " %");
                                boolean gasDetected = obj.getBoolean("gasDetected");
                                tvGas.setText("Газ: " + (gasDetected ? "виявлено" : "немає"));
                                tvPressure.setText("Тиск: " + obj.getDouble("pressure") + " hPa");
                                tvAltitude.setText("Висота: " + obj.getDouble("altitude") + " м");
                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> Log.e("API", "Помилка отримання сенсорів: " + error.getMessage())
        );

        Volley.newRequestQueue(requireContext()).add(request);
    }

    private void getRecommendation(String room) {
        String url = baseUrl + "/sensordata/recommendations";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            if (obj.getString("roomName").equalsIgnoreCase(room)) {
                                String recRaw = obj.getString("recommendation");
                                String[] recList = recRaw.split("\\. ");
                                StringBuilder recFormatted = new StringBuilder();
                                for (String rec : recList) {
                                    rec = rec.trim();
                                    if (!rec.isEmpty()) {
                                        recFormatted.append("• ").append(rec).append(".\n");
                                    }
                                }
                                tvRecommendation.setText(recFormatted.toString());
                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> Log.e("API", "Помилка отримання рекомендацій: " + error.getMessage())
        );

        Volley.newRequestQueue(requireContext()).add(request);
    }
}
