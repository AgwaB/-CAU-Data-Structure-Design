package com.example.leesd.datastructure;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leesd on 2018-09-23.
 */

public class Map extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mGooglemap;
    String number_of_vertices;
    KruskalAlgorithm2.Edge[] edges;
    List<KruskalAlgorithm2.Vertex> vertices = new ArrayList<KruskalAlgorithm2.Vertex>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Spinner yearSpinner = (Spinner)findViewById(R.id.spinner_time);
        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(this,
                R.array.date_time, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = getIntent();
                String season = intent.getExtras().getString("KEY");

                if (season.equals("summer")) {
                    switch (position) {
                        case 0:
                            ioProcess2("data_summer_07_11");
                            onResume();
                            onMapReady(mGooglemap);
                            break;
                        case 1:
                            ioProcess2("data_summer_11_15");
                            onResume();
                            onMapReady(mGooglemap);
                            break;
                        case 2:
                            ioProcess2("data_summer_15_19");
                            onResume();
                            onMapReady(mGooglemap);
                            break;
                        case 3:
                            ioProcess2("data_summer_19_24");
                            onResume();
                            onMapReady(mGooglemap);
                            break;
                    }
                } else if (season.equals("winter")){
                    switch (position) {
                        case 0:
                            ioProcess2("data_winter_07_11");
                            onResume();
                            onMapReady(mGooglemap);
                            break;
                        case 1:
                            ioProcess2("data_winter_11_15");
                            onResume();
                            onMapReady(mGooglemap);
                            break;
                        case 2:
                            ioProcess2("data_winter_15_19");
                            onResume();
                            onMapReady(mGooglemap);
                            break;
                        case 3:
                            ioProcess2("data_winter_19_24");
                            onResume();
                            onMapReady(mGooglemap);
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ioProcess1();

    }

    public void ioProcess1(){
        vertices = new ArrayList<KruskalAlgorithm2.Vertex>();
        vertices.add(new KruskalAlgorithm2.Vertex());

        Intent intent = getIntent();

        InputStream is = null;
        switch(intent.getExtras().getString("KEY")){
            case "basic" :
                is = getResources().openRawResource(R.raw.data_winter);
                break;
            case "summer" :
                is = getResources().openRawResource(R.raw.data_summer);
                break;
            case "winter" :
                is = getResources().openRawResource(R.raw.data_winter);
                break;
        }

        InputStreamReader inputreader = new InputStreamReader(is);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;

        try {
            number_of_vertices = buffreader.readLine();
            while((line = buffreader.readLine()) != null){
                KruskalAlgorithm2.Vertex vertex = new KruskalAlgorithm2.Vertex();
                vertex.num = Integer.parseInt(line.split(" ")[0]);
                vertex.name = line.split(" ")[1];
                vertex.latitude = Double.parseDouble(line.split(" ")[2]);
                vertex.longitude = Double.parseDouble(line.split(" ")[3]);
                vertex.population = Double.parseDouble(line.split(" ")[4]);
                vertices.add((vertex));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void ioProcess2(String filename){
        vertices = new ArrayList<KruskalAlgorithm2.Vertex>();
        vertices.add(new KruskalAlgorithm2.Vertex());

        InputStream is = null;
        switch(filename){
            case "data_summer_07_11" :
                is = getResources().openRawResource(R.raw.data_summer_07_11);
                break;
            case "data_summer_11_15" :
                is = getResources().openRawResource(R.raw.data_summer_11_15);
                break;
            case "data_summer_15_19" :
                is = getResources().openRawResource(R.raw.data_summer_15_19);
                break;
            case "data_summer_19_24" :
                is = getResources().openRawResource(R.raw.data_summer_19_24);
                break;
            case "data_winter_07_11" :
                is = getResources().openRawResource(R.raw.data_winter_07_11);
                break;
            case "data_winter_11_15" :
                is = getResources().openRawResource(R.raw.data_winter_11_15);
                break;
            case "data_winter_15_19" :
                is = getResources().openRawResource(R.raw.data_winter_15_19);
                break;
            case "data_winter_19_24" :
                is = getResources().openRawResource(R.raw.data_winter_19_24);
                break;
        }

        InputStreamReader inputreader = new InputStreamReader(is);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;

        try {
            number_of_vertices = buffreader.readLine();
            while((line = buffreader.readLine()) != null){
                KruskalAlgorithm2.Vertex vertex = new KruskalAlgorithm2.Vertex();
                vertex.num = Integer.parseInt(line.split(" ")[0]);
                vertex.name = line.split(" ")[1];
                vertex.latitude = Double.parseDouble(line.split(" ")[2]);
                vertex.longitude = Double.parseDouble(line.split(" ")[3]);
                vertex.population = Double.parseDouble(line.split(" ")[4]);
                vertices.add((vertex));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGooglemap = googleMap;
        Intent intent = getIntent();

        LatLng JEJU = new LatLng(33.385759, 126.550023);

        KruskalAlgorithm2 kruskalAlgorithm = new KruskalAlgorithm2(vertices, Integer.parseInt(number_of_vertices));
        edges = null;
        edges = kruskalAlgorithm.KruskalMST();
                for(int i = 1 ; i < vertices.size() ; i++){
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(vertices.get(i).latitude, vertices.get(i).longitude));
                    markerOptions.title(vertices.get(i).name);
                    mGooglemap.addMarker(markerOptions);
                }

                for(int i = 0 ; i < edges.length ; i++){
                    if(edges[i].weight == 0)
                        continue;
                    LatLng source = new LatLng(edges[i].sourcevertex.latitude, edges[i].sourcevertex.longitude);
                    LatLng destination = new LatLng(edges[i].destinationvertex.latitude, edges[i].destinationvertex.longitude);
                    PolylineOptions polylineOptions = new PolylineOptions().add(source).add(destination).width(5);
                    mGooglemap.addPolyline(polylineOptions);
                }

        mGooglemap.moveCamera(CameraUpdateFactory.newLatLng(JEJU));
        mGooglemap.animateCamera(CameraUpdateFactory.zoomTo(10));
    }

    public KruskalAlgorithm2.Edge[] basic(){
        KruskalAlgorithm2 kruskalAlgorithm = new KruskalAlgorithm2(vertices, Integer.parseInt(number_of_vertices));
        return kruskalAlgorithm.KruskalMST();
    }

    public void winter(){
//        KruskalAlgorithm kruskalAlgorithm = new KruskalAlgorithm(Integer.parseInt(number_of_vertices));
//        kruskalAlgorithm.kruskalAlgorithm(vertices);
    }

    public void summer(){

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mGooglemap != null)
            mGooglemap.clear();
    }


}
