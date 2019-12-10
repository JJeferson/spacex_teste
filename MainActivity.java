package com.spacex_teste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    //-------------------------------------------------------//
   
    /*

    In this exercise I will access the API twice.
    One to list another to fetch the objects and feed the memory
    with them so you can access them on another screen.


    Neste exercício irei acessar duas vezes a API.
    Uma para listar outra para buscar os objetos e
    alimentar a memória com eles para poder acessa-los em outra tela.

    */

    //------------------------------------------------------//
    // Declaring the list component
    private ListView listView_ID;

    private ArrayAdapter<String> itensAdapter;
    private ArrayList<String> itens;

    private TextView errorMessage_ID;

    public static final String Reference_File = "ReferenceFile";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final SharedPreferences sharedPreferences = getSharedPreferences(Reference_File, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();




        errorMessage_ID =      (TextView) findViewById(R.id.errorMessage_ID);
        listView_ID = (ListView) findViewById(R.id.listView_ID);

         //Clear
        errorMessage_ID.setText("");
        // Load a List
        List();
        //Onclick Event
        listView_ID.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    OkHttpClient client = new OkHttpClient();

                    HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.spacexdata.com/v3/missions").newBuilder();


                    String url = urlBuilder.build().toString();

                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, final Response response) throws IOException {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    try {


                                        try {
                                            String data = response.body().string();

                                            JSONArray jsonArray = new JSONArray(data);
                                            JSONObject jsonObject;

                                            //get item by position
                                            jsonObject = jsonArray.getJSONObject(position);
                                            //Save in memory itens
                                            editor.putString("manufacturers",jsonObject.getString("manufacturers"));
                                            editor.putString("description",jsonObject.getString("description"));
                                            editor.commit();
                                            //go to activity description
                                            startActivity(new Intent(MainActivity.this, description.class));


                                        } catch (JSONException e) {
                                            errorMessage_ID.setText(e.getMessage());
                                        }


                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                        }

                        ;
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }







            }
        });  //End of Onclick Event






    }// End of OnCreate



    public void List() {


        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.spacexdata.com/v3/missions").newBuilder();

            String url = urlBuilder.build().toString();


            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                try {

                                    //Catch body of Json
                                    String data = response.body().string();

                                    //Catch Json String and convert him to Array
                                    JSONArray jsonArray = new JSONArray(data);
                                    JSONObject jsonObject;


                                    //Create Adapter
                                    itens = new ArrayList<String>();


                                    itensAdapter = new ArrayAdapter<String>(getApplicationContext(),
                                            android.R.layout.simple_list_item_2,
                                            android.R.id.text2, itens);
                                    listView_ID.setAdapter(itensAdapter);

                                    // Build a List

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        jsonObject = jsonArray.getJSONObject(i);
                                        itens.add("Mission_id:" + jsonObject.getString("mission_id") +
                                        "|"+ "Mission_name: " + jsonObject.getString("mission_name"));



                                    }


                                } catch (JSONException e) {

                                    // Case Error
                                    errorMessage_ID.setText(e.getMessage());
                                }


                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }

                ;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }// End of Function List



}//End of Java Class
