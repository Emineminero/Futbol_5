package com.example.jlaurenzano.futbol5equipos;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EditThings extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String jsonResult;
    private String url = "http://www.altifutbol5.tk/getData.php";
    private ArrayList<String> lista = new ArrayList<>();
    private ArrayList<String> listaEquipos = new ArrayList<>();
    private ArrayList<String> listaGoles = new ArrayList<>();
    private ArrayAdapter adapter1;
    private String gameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_things);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this toolbar
        android.support.v7.app.ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        this.sendRequest();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Intent intent = getIntent();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        adapter1 = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,listaEquipos);
        final ListView lv = (ListView)findViewById(R.id.listView);
        lv.setAdapter(adapter1);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                cargarEquipos(position);
                String[] textSplitted;
                textSplitted = lv.getItemAtPosition(position).toString().split("\\)");
                gameId = textSplitted[0];
                Log.d("mensaje",gameId);
            }

        });


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_things, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.home){
            this.finish();
            return true;
        } else if (id == R.id.reloadList){
            loadDatainListView();
            return true;
        } else if(id == R.id.volver){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void sendRequest() {
        Log.d("ahi viene", "viene");
        listaEquipos = new ArrayList<>();
        this.lista = new ArrayList<>();
        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(this.url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if (response != null) {
                            try {
                                armarArrayList(response);
                            } catch (JSONException e) {
                                Log.d("ERROR","ERror");
                                e.printStackTrace();
                            }
                        }
                        Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("", "");
                map.put("", "");

                return map;
            }
        };


// add it to the RequestQueue


        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(getRequest);
    }

    public void loadDatainListView(){
        ListView listView = (ListView) findViewById(R.id.listView);
        listaEquipos = new ArrayList<>();
        Log.d("Mensaje","Entrando al for+3");
        Log.d("Mensaje",""+lista.size());
        for(int i =0; i < lista.size();i = i+3){
            listaEquipos.add(lista.get(i).replace("{","").replace("id:","")+") "+ lista.get(i+1)+" - "+ lista.get(i+2));
            Log.d("Mensaje",listaEquipos.get(0));
        }

        adapter1 = null;
        adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, this.listaEquipos);
        listView.setAdapter(adapter1);
    }

    public void armarArrayList(JSONArray array) throws JSONException {
        ArrayList<String> list = new ArrayList<>();
        String[] stringActual;
        String[] golesActual;
        for(int i = 0; i < array.length(); i++){
            stringActual = array.get(i).toString().split(",");
            // La id se encuentra en la posicion 0
            Log.d("Mensaje",stringActual[0]);
            lista.add(stringActual[0].replace("\"",""));
            //Equipo 1 se encuentra en la posicion 1 del Array
            lista.add(stringActual[1].replace("\"",""));
            //Equipo 2 se encuentra en la posicion 2 del Array
            lista.add(stringActual[2].replace("\"",""));
        }
        for(int i =0;i<array.length();i++){
            stringActual = array.get(i).toString().split(",");
            golesActual = stringActual[4].split(":");
            listaGoles.add(golesActual[1].replace("\"","").replace("}",""));
            golesActual = stringActual[5].split(":");
            listaGoles.add(golesActual[1].replace("\"","").replace("}",""));
        }

    }



    private class MyTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            try {
               // sendToMySql();
            } catch(Exception e){
                Log.d("Mensaje",e.toString());
            }
            return null;
        }
    }
    public void cargarEquipos(int position){
        position = 2 * position;

        EditText golEq1 = (EditText) findViewById(R.id.golesEquipo1);
        EditText golEq2 = (EditText) findViewById(R.id.golesEquipo2);
        golEq1.setText(listaGoles.get(position));
        golEq2.setText(listaGoles.get(position + 1));

    }

    public void updateGoalsOnClick(View v){
        EditText golesEq1 = (EditText) findViewById(R.id.golesEquipo1);
        EditText golesEq2 = (EditText) findViewById(R.id.golesEquipo2);
        String gol1 = golesEq1.getText().toString();
        String gol2 = golesEq2.getText().toString();
        sendUpdateRequest(gol1,gol2,gameId);
    }


    public void sendUpdateRequest(String gol1, String gol2,String gameId) {
        final String url = "http://altifutbol5.tk/update.php?gol="+gol1+"&goles="+gol2+"&id="+gameId;
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
                map.put("", "");
                map.put("", "");

                return map;
            }
        };

// add it to the RequestQueue


        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(getRequest);
    }
}
