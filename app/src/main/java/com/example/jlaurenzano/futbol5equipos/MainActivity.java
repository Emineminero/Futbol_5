package com.example.jlaurenzano.futbol5equipos;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> players = new ArrayList<String>();
    ArrayList<String> firstTeam = new ArrayList<String>();
    ArrayList<String> secondTeam = new ArrayList<String>();
    ArrayAdapter<String> adapter1;
    ArrayAdapter<String> adapter2;
    Boolean shouldUploadChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //If we don't request focus of the button, then the first thing that will
        //appear in the app will be the huge and annoying Keyboard
        Button button = (Button)findViewById(R.id.armarCuadros);
        button.requestFocus();

    }

    public void buttonOnClick(View v){
        firstTeam = new ArrayList<>();
        secondTeam = new ArrayList<>();
        players = new ArrayList<>();
        EditText j1 = (EditText)findViewById(R.id.jugador1);
        EditText j2 = (EditText)findViewById(R.id.jugador2);
        EditText j3 = (EditText)findViewById(R.id.jugador3);
        EditText j4 = (EditText)findViewById(R.id.jugador4);
        EditText j5 = (EditText)findViewById(R.id.jugador5);
        EditText j6 = (EditText)findViewById(R.id.jugador6);
        EditText j7 = (EditText)findViewById(R.id.jugador7);
        EditText j8 = (EditText)findViewById(R.id.jugador8);
        EditText j9 = (EditText)findViewById(R.id.jugador9);
        EditText j10 = (EditText)findViewById(R.id.jugador10);
        players.add(j1.getText().toString());
        players.add(j2.getText().toString());
        players.add(j3.getText().toString());
        players.add(j4.getText().toString());
        players.add(j5.getText().toString());
        players.add(j6.getText().toString());
        players.add(j7.getText().toString());
        players.add(j8.getText().toString());
        players.add(j9.getText().toString());
        players.add(j10.getText().toString());
        long seed = System.nanoTime();
        Collections.shuffle(players, new Random(seed));
        showTheTeams(players,seed);
    }
    public void showTheTeams(ArrayList players,long color){
        for(int i = 0; i < 5; i++){
            firstTeam.add(players.get(i).toString());
        }

       for(int i = 5; i < players.size(); i++){
           secondTeam.add(players.get(i).toString());
       }
        ListView  team1=(ListView) findViewById(R.id.team1);
        ListView  team2=(ListView) findViewById(R.id.team2);
        adapter1 = null;
        adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, firstTeam);
        team1.setAdapter(adapter1);
        adapter2 = null;
        adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, secondTeam);
        team2.setAdapter(adapter2);
        TextView estado1 = (TextView)findViewById(R.id.estado1);
        TextView estado2 = (TextView)findViewById(R.id.estado2);
        if(color % 2 == 0){
            estado1.setText("Chaleco");
            estado2.setText("Sin Chaleco");
        } else {
            estado2.setText("Chaleco");
            estado1.setText("Sin Chaleco");
        }
        adapter2.notifyDataSetChanged();
        adapter1.notifyDataSetChanged();

        if(shouldUploadChecked) {
            Log.d("Subir a servidor","Subiendo al server");
            new MyTask().execute();
        } else {
            Log.d("Subir a servidor","No subimos al server");
        }
    }

    public void sendToMySql(){
        String equipo1 ="";
        String equipo2 = "";
        for(int i = 0; i < firstTeam.size(); i++){
            equipo1 += "%20"+firstTeam.get(i).replace(" ","");
            equipo2 += "%20"+secondTeam.get(i).replace(" ","-");
        }
        Log.d("Ejecutando","SI");
        sendRequest(equipo1,equipo2);
    }
    public void sendRequest(String e, String e2){
        final String url = "http://altifutbol5.tk/Conextion.php?equipo="+e+"&equipos="+e2;
        Log.d("ahi viene","viene");
        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
               Map<String, String> map = new HashMap<>();
               map.put("equipo", "fabio");
               map.put("equipos", "fabiolo");

                return map;
            }
        };

// add it to the RequestQueue


        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(getRequest);
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            try {
                sendToMySql();
            } catch(Exception e){
                Log.d("Mensaje",e.toString());
            }
            return null;
        }
    }

    public void toggleOnClick(View v){
        ToggleButton shouldUpload = (ToggleButton)findViewById(R.id.uploadToServer);
        if(shouldUpload.isChecked()){
            shouldUploadChecked = true;
        } else {
            shouldUploadChecked = false;
        }

    }
}
