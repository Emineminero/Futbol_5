package com.example.jlaurenzano.futbol5equipos;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> players = new ArrayList<String>();
    ArrayList<String> firstTeam = new ArrayList<String>();
    ArrayList<String> secondTeam = new ArrayList<String>();
    ArrayAdapter<String> adapter1;
    ArrayAdapter<String> adapter2;
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

    }
}
