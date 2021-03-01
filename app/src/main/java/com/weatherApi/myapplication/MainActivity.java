package com.weatherApi.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.weatherApi.WeatherDataServies;
import com.weatherApi.WeatherReportModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btn_Get_city, btn_GetWeatherByCityID, btn_GetWeatherByCityName;
    EditText et_data_input;
    ListView lv_WeatherApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_Get_city = findViewById(R.id.btn_Get_city);
        btn_GetWeatherByCityID = findViewById(R.id.btn_GetWeatherByCityID);
        btn_GetWeatherByCityName = findViewById(R.id.btn_GetWeatherByCityName);
        et_data_input = findViewById(R.id.et_data_input);
        lv_WeatherApp = findViewById(R.id.lv_WeatherApp);
        final WeatherDataServies weatherDataServies = new WeatherDataServies(MainActivity.this);
        btn_Get_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                weatherDataServies.getCityId(et_data_input.getText().toString(), new WeatherDataServies.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String cityId) {
                        Toast.makeText(MainActivity.this, "Response Of City Id=" + cityId, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        btn_GetWeatherByCityID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weatherDataServies.getCityForeCastById(et_data_input.getText().toString(), new WeatherDataServies.ForeCastByIdResponse() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModels) {
                       // Toast.makeText(MainActivity.this, weatherReportModel.toString(), Toast.LENGTH_SHORT).show();
                        ArrayAdapter arrayAdapter=new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,weatherReportModels);
                        lv_WeatherApp.setAdapter(arrayAdapter);


                    }
                });
            }
        });
        btn_GetWeatherByCityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weatherDataServies.getCityForeCastByName(et_data_input.getText().toString(), new WeatherDataServies.GetCityForeCastByNameCallBack() {
                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModels) {
                        ArrayAdapter arrayAdapter=new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,weatherReportModels);
                        lv_WeatherApp.setAdapter(arrayAdapter);
                    }
                });
            }
        });
    }
}