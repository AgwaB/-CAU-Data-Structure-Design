package com.example.leesd.datastructure;


import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Created by leesd on 2018-09-22.
 */

public class KruskalAlgorithm {
    private List<Edge> edges;
    private int numberOfVertices;
    public static final int MAX_VALUE = 999;
    private int visited[];
    private double spanning_tree[][];

    public KruskalAlgorithm(int numberOfVertices)
    {
        this.numberOfVertices = numberOfVertices;
        edges = new LinkedList<Edge>();
        visited = new int[this.numberOfVertices + 1];
        spanning_tree = new double[numberOfVertices + 1][numberOfVertices + 1];
    }

    public List<Edge> kruskalAlgorithm(List<Vertex> vertices)
    {
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
        for( int i = 0 ; i < edges.size() ; i++){
            sumDist += edges.get(i).dist;
            avgPop = edges.get(i).pop;
        }

        for( int i = 0 ; i < edges.size() ; i++){
            edges.get(i).dist = edges.get(i).dist / sumDist; // 비율로 저장
        }

        for( int i = 0 ; i < edges.size() ; i++){
            edges.get(i).pop = edges.get(i).pop / avgPop ; // 비율로 저장
        }

        avgDist = sumDist / edges.size(); // 평균
        avgDist /= sumDist; // 비율로 저장

        for( int i = 0 ; i < edges.size() ; i++){
            if(avgDist > edges.get(i).dist){
                edges.get(i).weight = edges.get(i).dist + edges.get(i).pop;
            } else {
                edges.get(i).weight = 0;
            }
        }


        Collections.sort(edges, new EdgeComparator());
        CheckCycle checkCycle = new CheckCycle();
        for (Edge edge : edges)
        {
            spanning_tree[edge.sourcevertex.num][edge.destinationvertex.num] = edge.weight;
            spanning_tree[edge.destinationvertex.num][edge.sourcevertex.num] = edge.weight;
            if (checkCycle.checkCycle(spanning_tree, edge.sourcevertex.num))
            {
                spanning_tree[edge.sourcevertex.num][edge.destinationvertex.num] = 0;
                spanning_tree[edge.destinationvertex.num][edge.sourcevertex.num] = 0;
                edge.weight = -1;
                continue;
            }
            visited[edge.sourcevertex.num] = 1;
            visited[edge.destinationvertex.num] = 1;
            for (int i = 0; i < visited.length; i++)
            {
                if (visited[i] == 0)
                {
                    finished = false;
                    break;
                } else
                {
                    finished = true;
                }
            }
            if (finished)
                break;
        }
        System.out.println("The spanning tree is ");
        for (int i = 1; i <= numberOfVertices; i++)
            System.out.print("\t" + i);
        System.out.println();
        for (int source = 1; source <= numberOfVertices; source++)
        {
            System.out.print(source + "\t");
            for (int destination = 1; destination <= numberOfVertices; destination++)
            {
                System.out.print(spanning_tree[source][destination] + "\t");
            }
            System.out.println();
        }

        for(int i = 0 ; i < edges.size() ; i++)
            if(edges.get(i).weight == 0) {
                edges.remove(i);
                i--;
            }

        for(int i = 0 ; i < edges.size() ; i++)
            System.out.println(edges.get(i).weight);

        for(int k = 0 ; k < edges.size() ; k++) {
            for (int i = 1; i <= numberOfVertices; i++) {
                for (int j = 1; j <= numberOfVertices; j++) {
                    if(edges.get(k).sourcevertex.num == i && edges.get(k).destinationvertex.num == j && spanning_tree[i][j] == 0){
                        edges.get(k).weight = 0;
                        break;
                    } else {
                        continue;
                    }
                }
            }
        }

        for(int i = 0 ; i < edges.size() ; i++)
            if(edges.get(i).weight == 0)
                edges.remove(i);

        for(int i = 0 ; i < edges.size() ; i++)
            System.out.println(edges.get(i).weight);

        return edges;
    }

    class Edge
    {
        Vertex sourcevertex;
        Vertex destinationvertex;
        double dist;
        double pop;
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

    }




    static class Vertex
    {
        int num;
        String name;
        Double latitude;
        Double longitude;
        Double population;

    }

    class EdgeComparator implements Comparator<Edge>
    {
        @Override
        public int compare(Edge edge1, Edge edge2)
        {
            if (edge1.weight < edge2.weight)
                return -1;
            if (edge1.weight > edge2.weight)
                return 1;
            return 0;
        }
    }

    class CheckCycle
    {
        private Stack<Integer> stack;
        private double adjacencyMatrix[][];

        public CheckCycle()
        {
            stack = new Stack<Integer>();
        }

        public boolean checkCycle(double adjacency_matrix[][], int source)
        {
            boolean cyclepresent = false;
            int number_of_nodes = adjacency_matrix[source].length - 1;

            adjacencyMatrix = new double[number_of_nodes + 1][number_of_nodes + 1];
            for (int sourcevertex = 1; sourcevertex <= number_of_nodes; sourcevertex++)
            {
                for (int destinationvertex = 1; destinationvertex <= number_of_nodes; destinationvertex++)
                {
                    adjacencyMatrix[sourcevertex][destinationvertex] = adjacency_matrix[sourcevertex][destinationvertex];
                }
            }

            int visited[] = new int[number_of_nodes + 1];
            int element = source;
            int i = source;
            visited[source] = 1;
            stack.push(source);

            while (!stack.isEmpty())
            {
                element = stack.peek();
                i = element;
                while (i <= number_of_nodes)
                {
                    if (adjacencyMatrix[element][i] >= 1 && visited[i] == 1)
                    {
                        if (stack.contains(i))
                        {
                            cyclepresent = true;
                            return cyclepresent;
                        }
                    }
                    if (adjacencyMatrix[element][i] >= 1 && visited[i] == 0)
                    {
                        stack.push(i);
                        visited[i] = 1;
                        adjacencyMatrix[element][i] = 0;// mark as labelled;
                        adjacencyMatrix[i][element] = 0;
                        element = i;
                        i = 1;
                        continue;
                    }
                    i++;
                }
                stack.pop();
            }
            return cyclepresent;
        }
    }
}
