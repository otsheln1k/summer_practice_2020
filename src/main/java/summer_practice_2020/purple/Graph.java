package summer_practice_2020.purple;

import java.util.*;

public class Graph {

    private HashMap<String, Map<String, Integer>> vertexMap = new HashMap<String, Map<String, Integer>>();

    public void addVertex(String vertexName) {
        if (!hasVertex(vertexName)) {
            vertexMap.put(vertexName, new HashMap<String, Integer>());
        }
    }

    public boolean hasVertex(String vertexName) {
        return vertexMap.containsKey(vertexName);
    }

    public boolean hasEdge(String vertexName1, String vertexName2) {
        if (!hasVertex(vertexName1)) return false;
        Map<String, Integer> edges = vertexMap.get(vertexName1);
        return edges.containsKey(vertexName2);
    }

    public void addEdge(String vertexName1, String vertexName2, Integer cost) {
        if (!hasVertex(vertexName1)) addVertex(vertexName1);
        if (!hasVertex(vertexName2)) addVertex(vertexName2);
        Map<String, Integer> edges1= vertexMap.get(vertexName1);
        Map<String, Integer> edges2= vertexMap.get(vertexName2);
        edges1.put(vertexName2, cost);
        edges2.put(vertexName1, cost);
    }

    public Map<String, Map<String, Integer>> getVertexMap() {
        return vertexMap;
    }

    public Integer getSize(){
        return vertexMap.size();
    }
}