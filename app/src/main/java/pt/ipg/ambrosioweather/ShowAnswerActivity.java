package pt.ipg.ambrosioweather;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class ShowAnswerActivity extends AppCompatActivity {

    TextToSpeech t1;
    ProgressDialog pd;
    Context context;
    String words;
    Double lat;
    Double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_answer);

        pd.setTitle("A carregar...");
        pd.show();

        context = this;
        if (context != null) {
            pd = new ProgressDialog(context);
        }

        words = getIntent().getExtras().getString("words");
        lat = getIntent().getExtras().getDouble("lat");
        lng = getIntent().getExtras().getDouble("lng");


        URL myURL = null;

        try {
            //Endpoint JSON
            //myURL = new URL("http://api.wunderground.com/api/8af2ea19d61db1d5/conditions/q/Portugal/Guarda.json");
            myURL = new URL("http://api.wunderground.com/api/8af2ea19d61db1d5/conditions/q/" + lat + "," + lng + ".json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        new fetchWeather().execute(myURL);
    }
    private class fetchWeather extends AsyncTask<URL, Void, Weather> {

        private TextView city;
        private TextView temperature;
        private TextView listened;

        @Override
        protected Weather doInBackground(URL... myURL) {
            Helper myHelper = new Helper();
            Weather weathers = myHelper.GetJSONFromURL(myURL);
            return weathers;
        }

        @Override
        protected void onPostExecute(Weather w) {
            //dismiss dialog
            pd.dismiss();

            //set text
            city = (TextView) findViewById(R.id.textView_city);
            temperature = (TextView) findViewById(R.id.textView_temperature);
            city.setText(w.city);
            temperature.setText(Double.toString(w.temperature));
            listened = (TextView) findViewById(R.id.textView_listened);
            listened.setText(words);

            //talk
            t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    //wait
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Helper help = new Helper();
                            //String to_say = "Estão " + temperature.getText() + " graus. Casaco é para meninos.";
                            String to_say = help.WhatToSay(context, temperature.getText().toString());
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                t1.speak(to_say, TextToSpeech.QUEUE_FLUSH, null, null);
                            } else {
                                t1.speak(to_say, TextToSpeech.QUEUE_FLUSH, null);
                            }
                        }
                    }, 100);

                    if (status != TextToSpeech.ERROR) {
                        t1.setLanguage(Locale.getDefault());
                    }
                }
            });

        }
    }
}
