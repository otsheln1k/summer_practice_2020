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
    public boolean hasNext(Graph current_snapshot){
        if(SnapShot.indexOf(current_snapshot) + 1 < SnapShot.size())
            return true;
        else
            return false;
    }

    // Если нет следующеговозвращается тот же самый snashot
    public Graph next(Graph current_snapshot){
        if(hasNext(current_snapshot)){
            return SnapShot.get(SnapShot.indexOf(current_snapshot)+1);
        }
        else{
            return  current_snapshot;
        }
    }

    private boolean hasNext_step(Graph snapshot){
        if(snapshot.edgesCount() < g.nodesCount() - amountCompanent){
            return true;
        }
        else {
            return false;
        }
    }

    private Graph next_step(Graph snapshot, int mark){
        //Выбрать мин ребро
        //System.out.println("ШАГ");
        Iterable<IGraph.Edge> edges = g.getEdges();
        double min = 2000000;
        boolean flag_add_edge = false;
        IGraph.Edge edge = null;
        for(IGraph.Edge e: edges){
            if(e.getWeight() < min){
                min = e.getWeight();
                edge = e;
            }
        }

        //System.out.println("Найденое ребро " + edge.firstNode().getTitle() + " " + edge.secondNode().getTitle() + " " + edge.getWeight());
        if(componentMap.get(edge.firstNode()).equals(0) && componentMap.get(edge.secondNode()).equals(0)) {
            componentMap.put(edge.firstNode(), mark);
            componentMap.put(edge.secondNode(), mark);
            //Добавить в snapshot
            snapshot.addEdge(edge.firstNode(), edge.secondNode()).setWeight(edge.getWeight());
            flag_add_edge = true;
            //System.out.println("Добавили ребро: " + edge.firstNode().getTitle() + " " + edge.secondNode().getTitle() + " " + edge.getWeight());
            edge.setWeight(2000000);
            //System.out.println("Обе вершины ребра имели метки 0");
            //System.out.println("Метки теперь: " + componentMap.get(edge.firstNode()) + " " + componentMap.get(edge.secondNode()));
        }
        else {
            //System.out.println("Первый else");
            //System.out.println(componentMap.get(edge.firstNode()) + " " + componentMap.get(edge.secondNode()));
            //System.out.println(componentMap.get(edge.firstNode()) * componentMap.get(edge.secondNode()));
            if(componentMap.get(edge.firstNode()) * componentMap.get(edge.secondNode()) == 0){
                mark = Math.max(componentMap.get(edge.firstNode()), componentMap.get(edge.secondNode()));
                //System.out.println("Метки вершин: " + componentMap.get(edge.firstNode()) + " " + componentMap.get(edge.secondNode()));
                //System.out.println("Ставим метку " + mark);
                componentMap.put(edge.firstNode(), mark);
                componentMap.put(edge.secondNode(), mark);
                //Добавить в snapshot
                snapshot.addEdge(edge.firstNode(), edge.secondNode()).setWeight(edge.getWeight());
                flag_add_edge = true;
                //System.out.println("Добавили ребро: " + edge.firstNode().getTitle() + " " + edge.secondNode().getTitle() + " " + edge.getWeight());
                edge.setWeight(2000000);
                //System.out.println("Одна из вершин ребра имела метку 0");
                //System.out.println("Метки теперь: " + componentMap.get(edge.firstNode()) + " " + componentMap.get(edge.secondNode()));
            }
            else {
                //System.out.println("Второй else");
                //System.out.println(componentMap.get(edge.firstNode()).equals(componentMap.get(edge.secondNode())));
                if(componentMap.get(edge.firstNode()) != componentMap.get(edge.secondNode())) {
                    mark = Math.min(componentMap.get(edge.firstNode()), componentMap.get(edge.secondNode()));
                    //System.out.println("Метки вершин: " + componentMap.get(edge.firstNode()) + " " + componentMap.get(edge.secondNode()));
                    //System.out.println("Ставим метку " + mark);
                    componentMap.put(edge.firstNode(), mark);
                    componentMap.put(edge.secondNode(), mark);
                    //Добавить в snapshot
                    snapshot.addEdge(edge.firstNode(), edge.secondNode()).setWeight(edge.getWeight());
                    flag_add_edge = true;
                    //System.out.println("Добавили ребро: " + edge.firstNode().getTitle() + " " + edge.secondNode().getTitle() + " " + edge.getWeight());
                    edge.setWeight(2000000);
                    //System.out.println("Ни одна из вершин ребра имела метку 0");
                    //System.out.println("Метки теперь: " + componentMap.get(edge.firstNode()) + " " + componentMap.get(edge.secondNode()));
                }
            }
        }
        ///////
        if(flag_add_edge) {
            return snapshot;
        }
        else{
            edge.setWeight(2000000);
            return snapshot;
        }
    }

    public void boruvka(){

        Iterable<IGraph.Node> nodes = g.getNodes();
        Graph snapshot = new Graph();
        int mark = 1;

        for(IGraph.Node n: nodes){
            IGraph.Node nnode = snapshot.addNode();
            nnode.setTitle(n.getTitle());
            componentMap.put(n, 0);
        }

        while (hasNext_step(snapshot)){
            snapshot = next_step(snapshot, mark);
            mark++;
        }
        SnapShot.add(snapshot);
    }

    public Iterable<Graph> getSnapShotList(){
        return SnapShot;
    }

}
