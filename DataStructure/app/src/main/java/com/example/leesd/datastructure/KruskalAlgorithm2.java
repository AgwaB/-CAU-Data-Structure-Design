package com.example.leesd.datastructure;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by leesd on 2018-10-03.
 */

public class KruskalAlgorithm2 {

    private List<Edge> edges;
    private int numberOfVertices;
    public static final int MAX_VALUE = 999;
    private int visited[];
    private double spanning_tree[][];


    int V, E;    // V-> no. of vertices & E->no.of edges
    Edge edge[]; // collection of all edges


    Edge[] getEdges(){
        return edge;
    }

    static class Vertex
    {
        int num;
        String name;
        Double latitude;
        Double longitude;
        Double population;

    }

    // A class to represent a graph edge
    class Edge implements Comparable<Edge>
    {
        Vertex sourcevertex;
        Vertex destinationvertex;
        double dist; // 거리
        double pop; // 유동인구 차이
        double weight;

        public void calDist(){
            dist = distance(sourcevertex.latitude, sourcevertex.longitude, destinationvertex.latitude, destinationvertex.longitude, "kilometer");
        }
        public void calPop(){
            pop = Math.abs(sourcevertex.population - destinationvertex.population);
        }

        private double distance(double lat1, double lon1, double lat2, double lon2, String unit) {

            double theta = lon1 - lon2;
            double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515;

            if (unit == "kilometer") {
                dist = dist * 1.609344;
            } else if(unit == "meter"){
                dist = dist * 1609.344;
            }

            return (dist);
        }


        // This function converts decimal degrees to radians
        private double deg2rad(double deg) {
            return (deg * Math.PI / 180.0);
        }

        // This function converts radians to decimal degrees
        private double rad2deg(double rad) {
            return (rad * 180 / Math.PI);
        }

        // Comparator function used for sorting edges
        // based on their weight
        public int compareTo(Edge compareEdge)
        {
            if (this.weight < compareEdge.weight)
                return -1;
            if (this.weight > compareEdge.weight)
                return 1;
            return 0;
        }
    };

    // A class to represent a subset for union-find
    class subset
    {
        int parent, rank;
    };


    // Creates a graph with V vertices and E edges
    KruskalAlgorithm2(List<Vertex> vertices, int numberOfVertices)
    {
        this.numberOfVertices = numberOfVertices;
        edges = new LinkedList<Edge>();
        visited = new int[this.numberOfVertices + 1];
        spanning_tree = new double[numberOfVertices + 1][numberOfVertices + 1];

        int[][] checkArray = new int[vertices.size()][vertices.size()];
        boolean finished = false;
        for (int source = 1; source <= numberOfVertices; source++)
        {
            for (int destination = 1; destination <= numberOfVertices; destination++)
            {
                if (checkArray[source][destination] == 0 && source != destination)
                {
                    Edge edge = new Edge();
                    edge.sourcevertex = vertices.get(source);
                    edge.destinationvertex = vertices.get(destination);
                    edge.calDist();
                    edge.calPop();
                    checkArray[destination][source] = 1;
                    edges.add(edge);
                }
            }
        }

        double avgDist = 0.0;
        double sumDist = 0.0;
        double avgPop = 0.0;
        double sumPop = 0.0;

        for( int i = 0 ; i < edges.size() ; i++){
            sumDist += edges.get(i).dist;
            sumPop += edges.get(i).pop;
        }

        for( int i = 0 ; i < edges.size() ; i++){
            edges.get(i).dist = edges.get(i).dist / sumDist; // 비율로 저장
        }

        for( int i = 0 ; i < edges.size() ; i++){
            edges.get(i).pop = edges.get(i).pop / sumPop ; // 비율로 저장
        }

        avgDist = sumDist / edges.size(); // 평균
        avgDist /= sumDist;
        //avgDist /= sumDist; // 비율로 저장
        avgPop = sumPop / edges.size(); // 평균
        //avgPop /= sumPop ; // 비율로 저장

        for( int i = 0 ; i < edges.size() ; i++){
            if(avgDist > edges.get(i).dist ){
                edges.get(i).weight = edges.get(i).dist + edges.get(i).pop;
            } else {
                edges.remove(i);
                i--;
                //edges.get(i).weight = 0;
            }
        }



        V = numberOfVertices;
        E = edges.size();
        edge = new Edge[E];
        for (int i=0; i< E; ++i)
            edge[i] = edges.get(i);
    }

    // A utility function to find set of an element i
    // (uses path compression technique)
    int find(subset subsets[], int i)
    {
        // find root and make root as parent of i (path compression)
        if (subsets[i].parent != i)
            subsets[i].parent = find(subsets, subsets[i].parent);

        return subsets[i].parent;
    }

    // A function that does union of two sets of x and y
    // (uses union by rank)
    void Union(subset subsets[], int x, int y)
    {
        int xroot = find(subsets, x);
        int yroot = find(subsets, y);

        // Attach smaller rank tree under root of high rank tree
        // (Union by Rank)
        if (subsets[xroot].rank < subsets[yroot].rank)
            subsets[xroot].parent = yroot;
        else if (subsets[xroot].rank > subsets[yroot].rank)
            subsets[yroot].parent = xroot;

            // If ranks are same, then make one as root and increment
            // its rank by one
        else
        {
            subsets[yroot].parent = xroot;
            subsets[xroot].rank++;
        }
    }

    // The main function to construct MST using Kruskal's algorithm
    Edge[] KruskalMST()
    {
        Edge result[] = new Edge[V+1];  // Tnis will store the resultant MST
        int e = 0;  // An index variable, used for result[]
        int i = 0;  // An index variable, used for sorted edges
        for (i=0; i<=V; ++i)
            result[i] = new Edge();

        // Step 1:  Sort all the edges in non-decreasing order of their
        // weight.  If we are not allowed to change the given graph, we
        // can create a copy of array of edges
        Arrays.sort(edge);

        // Allocate memory for creating V ssubsets
        subset subsets[] = new subset[V+1];
        for(i=0; i<=V; ++i)
            subsets[i]=new subset();

        // Create V subsets with single elements
        for (int v = 0; v <= V; ++v)
        {
            subsets[v].parent = v;
            subsets[v].rank = 0;
        }

        i = 0;  // Index used to pick next edge

        // Number of edges to be taken is equal to V-1
        while (e < V - 1)
        {
            // Step 2: Pick the smallest edge. And increment
            // the index for next iteration
            Edge next_edge = new Edge();
            next_edge = edge[i++];

            int x = find(subsets, next_edge.sourcevertex.num);
            int y = find(subsets, next_edge.destinationvertex.num);

            // If including this edge does't cause cycle,
            // include it in result and increment the index
            // of result for next edge
            if (x != y)
            {
                result[e++] = next_edge;
                Union(subsets, x, y);
            }
            // Else discard the next_edge
        }

        // print the contents of result[] to display
        // the built MST
        System.out.println("Following are the edges in " +
                "the constructed MST");
        for (i = 0; i < e; ++i)
            System.out.println(result[i].sourcevertex.num+" -- " +
                    result[i].destinationvertex.num+" == " + result[i].weight);

        return result;
    }
}

