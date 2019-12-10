package com.spacex_teste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class description extends AppCompatActivity {


    private TextView description_ID;
    private Button   back_ID;


    public static final String Reference_File = "ReferenceFile";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description);


        description_ID    = (TextView) findViewById(R.id.description_ID);
        back_ID           = (Button)   findViewById(R.id.back_ID);

        final SharedPreferences sharedPreferences = getSharedPreferences(Reference_File, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();


        //Beggin test of color
        String colorTest01 = sharedPreferences.getString("manufacturers","");
        String colorTest02=colorTest01.replace("\"", "");//remove " from string to can compare
        if (colorTest02.contentEquals("[SpaceX]"))
        {
          description_ID.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        }



            //write a description
        description_ID.setText(sharedPreferences.getString("description",""));

        back_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 //clear memory
                editor.putString("manufacturers","");
                editor.putString("description","");
                editor.commit();
                startActivity(new Intent(description.this, MainActivity.class));
            }////end
        });//end



    }//end of oncreate




}//end of javaclass
