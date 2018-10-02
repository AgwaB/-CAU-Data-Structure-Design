package com.example.leesd.datastructure;

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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by leesd on 2018-09-23.
 */

public class Map extends AppCompatActivity implements OnMapReadyCallback {
    int number_of_vertices;
    List<KruskalAlgorithm.Edge> edges;
    List<KruskalAlgorithm.Vertex> vertices = new ArrayList<KruskalAlgorithm.Vertex>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        vertices.add(new KruskalAlgorithm.Vertex());

        InputStream is = getResources().openRawResource(R.raw.data);
        Scanner scan = new Scanner(is);

        number_of_vertices = scan.nextInt();


        for ( int i = 0 ; i < number_of_vertices ; i++){
            KruskalAlgorithm.Vertex vertex = new KruskalAlgorithm.Vertex();
            vertex.num = scan.nextInt();
            vertex.name = scan.next();
            vertex.latitude = Double.parseDouble(scan.next());
            vertex.longitude = Double.parseDouble(scan.next());
            vertex.population = Double.parseDouble(scan.next());
            vertices.add((vertex));
        }


        scan.close();
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

                for(int i = 0 ; i < edges.size() ; i++){
                    if(edges.get(i).weight == 0)
                        continue;
                    LatLng source = new LatLng(edges.get(i).sourcevertex.latitude, edges.get(i).sourcevertex.longitude);
                    LatLng destination = new LatLng(edges.get(i).destinationvertex.latitude, edges.get(i).destinationvertex.longitude);
                    PolylineOptions polylineOptions = new PolylineOptions().add(source).add(destination).width(15);
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

    public List<KruskalAlgorithm.Edge> basic(){
        KruskalAlgorithm kruskalAlgorithm = new KruskalAlgorithm(number_of_vertices);
        return kruskalAlgorithm.kruskalAlgorithm(vertices);
    }

    public void winter(){
        KruskalAlgorithm kruskalAlgorithm = new KruskalAlgorithm(number_of_vertices);
        kruskalAlgorithm.kruskalAlgorithm(vertices);
    }

    public void summer(){

    }
}
