package com.example.lab3levykin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;

public class Result extends AppCompatActivity {

    ImageView image;
    TextView lab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        image = findViewById(R.id.image_icon);
        lab = findViewById(R.id.textTemp);

        String key = getIntent().getStringExtra("apikey");
        String city = getIntent().getStringExtra("city");

        Thread t = new Thread(() -> {
            try {
                URL url = new URL("https://api.weatherapi.com/v1/current.json?key=" + key + "&q="
                        + city + "&aqi=no");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                InputStream inp = con.getInputStream();
                byte [] buf = new byte [1024];
                String res = "";
                while (true)
                {
                    int len = inp.read(buf, 0, buf.length);
                    if (len < 0) break;
                    res = res + new String(buf, 0, len);
                }
                con.disconnect();
                Log.d("json", res);
                JSONObject doc = new JSONObject(res);
                JSONObject curr = doc.getJSONObject("current");
                //извлекаем данные из jsona
                String time = curr.getString("last_updated"); //дата время
                float temp = (float) curr.getDouble("temp_c"); //температура
                float wind = (float) curr.getDouble("wind_kph"); //ветер км/ч
                float pressure = (float) curr.getDouble("pressure_mb"); //давление милибар
                float precipitation = (float) curr.getDouble("precip_mm"); //осадки милиметрах
                int hum = curr.getInt("humidity"); //влажность %
                int cloud = curr.getInt("cloud"); //облачность %
                //Доп запрос для картинки
                JSONObject cond = curr.getJSONObject("condition");
                String icon = cond.getString("icon");
                URL url1 = new URL("https:" + icon);
                HttpURLConnection con1 = (HttpURLConnection) url1.openConnection();
                InputStream inp1 = con1.getInputStream();
                Bitmap bmp = BitmapFactory.decodeStream(inp1);
                con1.disconnect();

                runOnUiThread(() -> {
                    lab.setText(MessageFormat.format(MessageFormat.format("{0}\nТемпература '{'0'}' °C\n" +
                                    "Ветер '{'1'}' км/ч\nДавление '{'2'}' мбар\nОсадки '{'3'}' мм\n" +
                                    "Влажность '{'4'}' %\nОблака '{'5'}' %\n",
                            time), temp, wind, pressure, precipitation, hum, cloud));
                    image.setImageBitmap(bmp);
                });
                //сохранить в историю
                int nid = StaticDB.database.getMaxID() + 1;
                StaticDB.database.addForecast(nid,time,city,temp,wind,pressure,precipitation,hum,cloud);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        });
        t.start();
    }

    public void onBtnReturnClick(View v)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}