package com.example.leesd.datastructure;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by leesd on 2018-09-23.
 */

public class Map extends AppCompatActivity implements OnMapReadyCallback {
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

        vertices.add(new KruskalAlgorithm2.Vertex());

        InputStream is = getResources().openRawResource(R.raw.data);
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
    public void onMapReady(final GoogleMap map) {
        Intent intent = getIntent();

        LatLng SEOUL = new LatLng(33.288, 126.37);


        switch (intent.getExtras().getString("KEY")){
            case "basic" :
                for(int i = 1 ; i < vertices.size() ; i++){
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(vertices.get(i).latitude, vertices.get(i).longitude));
                    markerOptions.title(vertices.get(i).name);
                    map.addMarker(markerOptions);
                }
                edges = basic();

                for(int i = 0 ; i < edges.length ; i++){
                    if(edges[i].weight == 0)
                        continue;
                    LatLng source = new LatLng(edges[i].sourcevertex.latitude, edges[i].sourcevertex.longitude);
                    LatLng destination = new LatLng(edges[i].destinationvertex.latitude, edges[i].destinationvertex.longitude);
                    PolylineOptions polylineOptions = new PolylineOptions().add(source).add(destination).width(5);
                    map.addPolyline(polylineOptions);
                }
                break;
            case "winter" :
                winter();
                break;
            case "summer" :
                summer();
                break;
        }
        map.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        map.animateCamera(CameraUpdateFactory.zoomTo(10));
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
}
