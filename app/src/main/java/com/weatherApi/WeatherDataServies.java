package com.weatherApi;


import android.content.Context;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import com.android.volley.toolbox.JsonObjectRequest;
import com.weatherApi.myapplication.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataServies {
    public static final String QUERY_FOR_CITY_ID = "https://www.metaweather.com/api/location/search/?query=";
    public static final String QUERY_FOR_CITY_WEATHER_BY_ID = "https://www.metaweather.com/api/location/";
    Context context;
    String cityId;

    public WeatherDataServies(Context context) {
        this.context = context;
    }
    public interface VolleyResponseListener{
        void onError(String message);
        void onResponse(String cityId);
    }



    public void getCityId(String cityName, final VolleyResponseListener volleyResponseListener){
        String url = QUERY_FOR_CITY_ID +cityName;
        final JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET,url,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                cityId="";
                try {
                    JSONObject cityInfo= response.getJSONObject(0);
                    cityId=cityInfo.getString("woeid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

              //  Toast.makeText(context, "City Id="+cityId, Toast.LENGTH_SHORT).show();
                volleyResponseListener.onResponse(cityId);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  Toast.makeText(context, "Something is Wrong", Toast.LENGTH_SHORT).show();
                volleyResponseListener.onError("something wrong");

            }
        });

        MySingleton.getInstance(context).addToRequestQueue(request);

    }

    public interface ForeCastByIdResponse{
        void onError(String message);
        void onResponse(List<WeatherReportModel> weatherReportModels);
    }

    public void getCityForeCastById(String cityId , final ForeCastByIdResponse foreCastByIdResponse){
        final List<WeatherReportModel> weatherReportModels=new ArrayList<>();
        String url = QUERY_FOR_CITY_WEATHER_BY_ID +cityId;
        JsonObjectRequest request= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               // Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    JSONArray consolidated_weather_list= response.getJSONArray("consolidated_weather");

                    for (int i=0; i< consolidated_weather_list.length();i++) {
                        WeatherReportModel One_Day_Weater= new WeatherReportModel();
                        JSONObject first_day_api =  consolidated_weather_list.getJSONObject(i);
                        // Same name declear in weather Report Model
                        One_Day_Weater.setId(first_day_api.getInt("id"));
                        One_Day_Weater.setWeather_state_name(first_day_api.getString("weather_state_name"));
                        One_Day_Weater.setWeather_state_abbr(first_day_api.getString("weather_state_abbr"));
                        One_Day_Weater.setWind_direction_compass(first_day_api.getString("wind_direction_compass"));
                        One_Day_Weater.setCreated(first_day_api.getString("created"));
                        One_Day_Weater.setApplicable_date(first_day_api.getString("applicable_date"));
                        One_Day_Weater.setMax_temp(first_day_api.getLong("max_temp"));
                        One_Day_Weater.setMin_temp(first_day_api.getLong("min_temp"));
                        One_Day_Weater.setThe_temp(first_day_api.getLong("the_temp"));
                        One_Day_Weater.setWind_speed(first_day_api.getLong("wind_speed"));
                        One_Day_Weater.setWind_direction(first_day_api.getLong("wind_direction"));
                        One_Day_Weater.setAir_pressure(first_day_api.getLong("air_pressure"));
                        One_Day_Weater.setHumidity(first_day_api.getInt("humidity"));
                        One_Day_Weater.setVisibility(first_day_api.getLong("visibility"));
                        One_Day_Weater.setPredictability(first_day_api.getInt("predictability"));
                         weatherReportModels.add(One_Day_Weater);
                    }
                    foreCastByIdResponse.onResponse((List<WeatherReportModel>) weatherReportModels);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "SomeThing wrong ", Toast.LENGTH_SHORT).show();

            }
        });
        MySingleton.getInstance(context).addToRequestQueue(request);

    }
    public interface GetCityForeCastByNameCallBack{
        void onError(String message);
        void onResponse(List<WeatherReportModel> weatherReportModels);
    }

    public  void getCityForeCastByName(String cityName, final GetCityForeCastByNameCallBack getCityForeCastByNameCallBack) {
        getCityId(cityName, new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(String cityId) {
                getCityForeCastById(cityId, new ForeCastByIdResponse() {
                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModels) {
                        getCityForeCastByNameCallBack.onResponse(weatherReportModels);
                    }
                });
            }
        });
    }
}
