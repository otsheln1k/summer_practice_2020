package summer_practice_2020.purple;

import java.util.*;
import java.lang.Math.*;

public class Boruvka {

    private Graph g;
    private HashMap<IGraph.Node, Integer> componentMap = new HashMap<IGraph.Node, Integer>();
    private List<Graph> SnapShot = new ArrayList<Graph>();
    private int amountCompanent = 1;
    private Iterable<IGraph.Node> nodes;

    public Boruvka(Graph g){
        this.g = g;
    }

    /*static void dfs(String v, Map<String, Map<String, Integer>> vertexMap, HashMap<String, String> visitedMap){
        visitedMap.put(v, "visited");
        for(Map.Entry<String, Integer> now: vertexMap.get(v).entrySet() ){
            if(now.getValue() > 0 && visitedMap.get(now.getKey()).equals("not_visited")) {
                dfs(now.getKey(), vertexMap, visitedMap);
            }
        }
    }

    private int component(Graph g){
        int result = 0;
        vertexMap = g.getVertexMap();
        for(Map.Entry<String, Map<String, Integer>> it: vertexMap.entrySet()){
            visitedMap.put(it.getKey(), "not_visited");
        }

        for(Map.Entry<String, Map<String, Integer>> it: vertexMap.entrySet()){
            if(visitedMap.get(it.getKey()).equals("not_visited")){
                dfs(it.getKey(), vertexMap, visitedMap);
                result++;
            }
        }
        return result;
    }*/
    private boolean hasNext(){
        if(SnapShot.size() < g.nodesCount() - amountCompanent){
            return true;
        }
        else {
            return false;
        }
    }

    private Graph next(Graph snapshot, int mark){
        //Выбрать мин ребро
        Iterable<IGraph.Edge> edges = g.getEdges();
        double min = 2000000;
        IGraph.Edge edge = null;
        for(IGraph.Edge e: edges){
            if(e.getWeight() < min){
                min = e.getWeight();
                edge = e;
            }
        }
        //Проверить на группу
        if(componentMap.get(edge.firstNode()) == 0 && componentMap.get(edge.firstNode()) == 0) {
            edge.setWeight(2000000);
            componentMap.put(edge.firstNode(), mark);
            componentMap.put(edge.secondNode(), mark);
            //Добавить в snapshot
            snapshot.addEdge(edge.firstNode(), edge.secondNode());
        }
        else {
            if(componentMap.get(edge.firstNode()) * componentMap.get(edge.firstNode()) == 0){
                mark = Math.max(componentMap.get(edge.firstNode()), componentMap.get(edge.firstNode()));
                edge.setWeight(2000000);
                componentMap.put(edge.firstNode(), mark);
                componentMap.put(edge.secondNode(), mark);
                //Добавить в snapshot
                snapshot.addEdge(edge.firstNode(), edge.secondNode());
            }
            else {
                if(!componentMap.get(edge.firstNode()).equals(componentMap.get(edge.firstNode()))) {
                    mark = Math.min(componentMap.get(edge.firstNode()), componentMap.get(edge.firstNode()));
                    edge.setWeight(2000000);
                    componentMap.put(edge.firstNode(), mark);
                    componentMap.put(edge.secondNode(), mark);
                    //Добавить в snapshot
                    snapshot.addEdge(edge.firstNode(), edge.secondNode());
                }
            }
        }
        return snapshot;
    }

    public void boruvka(){

        Graph snapshot = new Graph();
        int mark = 0;
        Iterable<IGraph.Node> nodes = g.getNodes();

        for(int i = 0; i < g.getSize(); i++){
            snapshot.addNode();
        }

        while (hasNext()){
            snapshot = next(snapshot, mark);
            SnapShot.add(snapshot);
            mark++;
        }
    }


}
