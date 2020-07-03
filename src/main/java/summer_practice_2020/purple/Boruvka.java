package summer_practice_2020.purple;

import java.util.*;
import java.lang.Math.*;

public class Boruvka {
    //Подсчет числа компонент связнасти +
    //Борувка для одной компоненты      +
    //Сохранять каждый шаг Борувки      -
    private String[] used;
    private Graph g;
    private Map<String, Map<String, Integer>> vertexMap;
    private HashMap<String, String> visitedMap = new HashMap<String, String>();
    private HashMap<String, Integer> componentMap = new HashMap<String, Integer>();

    public Boruvka(Graph g){
        this.g = g;
    }

    static void dfs(String v, Map<String, Map<String, Integer>> vertexMap, HashMap<String, String> visitedMap){
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
    }

    private String min_edge(Map<String, Map<String, Integer>> vertexMap){
        int min = 2000000;
        String ans = "";
        for(Map.Entry<String, Map<String, Integer>> it: vertexMap.entrySet()){
            for(Map.Entry<String, Integer> now: it.getValue().entrySet()){
                if(now.getValue() < min){
                    min = now.getValue();
                    ans = it.getKey() + " " + now.getKey() + " " + now.getValue();
                }
            }
        }
        return ans;
    }

    //Алгоритм Борувки
    public void boruvka(Graph g){
        vertexMap = g.getVertexMap();
        int num = component(g);
        if(num >1){
            System.out.println("The graph disconnected!");
            return;
        }
        Graph graph = new Graph();

        //Получаем множество вершин текушей компоненты
        for(Map.Entry<String, Map<String, Integer>> it: vertexMap.entrySet()){
            graph.addVertex(it.getKey());
            componentMap.put(it.getKey(), 0);
        }

        //Ищем ребро минимального веса
        int edges = 0, mark = 1;
        while(edges != g.getSize()-1){
            //String debug = min_edge(vertexMap);
            String[] ver = min_edge(vertexMap).split(" ");
            //System.out.println("---------------------------\n"+ debug);
            vertexMap.get(ver[0]).put(ver[1], 2000000);
            vertexMap.get(ver[1]).put(ver[0], 2000000);
            //метки одной компоненты и доюавление в graph
            if(componentMap.get(ver[0]).equals(0) && componentMap.get(ver[1]).equals(0)){
                componentMap.put(ver[0], mark);
                componentMap.put(ver[1], mark);
                graph.addEdge(ver[0], ver[1], Integer.parseInt(ver[2]));
                edges++;
                mark++;
                System.out.println("TUT  1");
            }
            else
            {
                if(componentMap.get(ver[0])*componentMap.get(ver[1]) == 0){
                    mark = Math.max(componentMap.get(ver[0]), componentMap.get(ver[1]));
                    componentMap.put(ver[0], mark);
                    componentMap.put(ver[1], mark);
                    graph.addEdge(ver[0], ver[1], Integer.parseInt(ver[2]));
                    edges++;
                    mark++;
                    System.out.println("TUT  2");

                }
                else {
                    if (!componentMap.get(ver[0]).equals(componentMap.get(ver[1]))) {
                        mark = componentMap.get(ver[0]);
                        int two_mark = componentMap.get(ver[1]);
                        for (Map.Entry<String, Integer> it : componentMap.entrySet()) {
                            if (it.getValue() == two_mark) {
                                it.setValue(mark);
                            }
                        }
                        graph.addEdge(ver[0], ver[1], Integer.parseInt(ver[2]));
                        System.out.println("TUT  3");
                        edges++;
                        mark++;
                    }
                }
            }
            //System.out.println(componentMap);
            //System.out.println(vertexMap);
        }
        System.out.println(graph.getVertexMap());
    }
}

