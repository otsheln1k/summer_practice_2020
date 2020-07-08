package summer_practice_2020.purple;

import java.util.*;

public class Boruvka {

    private IGraph g;
    private HashMap<IGraph.Node, Integer> componentMap = new HashMap<>();
    private HashMap<IGraph.Node, String> visitedMap = new HashMap<>();
    private int amountCompanent = 1;
    private Iterable<IGraph.Node> nodes;
    private List<IGraph.Edge> list = new ArrayList<>();
    private Set<IGraph.Edge> SnapShot = new HashSet<>();
    private List<BoruvkaSnapshot> blist = new ArrayList<>();
    private int step = 0;

    public Boruvka(IGraph g) {
        this.g = g;
    }


    public int MyCompare (IGraph.Edge e1, IGraph.Edge e2){
        return Double.compare(e1.getWeight(), e2.getWeight());
    }

    private void dfs(IGraph.Node v){
        visitedMap.put(v, "visited");
        for(IGraph.Edge now: g.getEdgesFrom(v)) {
            if (v.equals(now.firstNode())) {
                if (visitedMap.get(now.secondNode()).equals("not_visited")) {
                    System.out.println("Second = " + now.secondNode().getTitle());
                    System.out.println();
                    dfs(now.secondNode());
                }
            }
            else {
                if (visitedMap.get(now.firstNode()).equals("not_visited")) {
                    System.out.println("First = " + now.firstNode().getTitle());
                    System.out.println();
                    dfs(now.firstNode());
                }
            }
        }
    }

    private int component(){
        int result = 0;
        for(IGraph.Node it: g.getNodes()){
            visitedMap.put(it, "not_visited");
        }

        for(IGraph.Node it:g.getNodes()){
            if(visitedMap.get(it).equals("not_visited")){
                dfs(it);
                result++;
            }
        }
        return result;
    }

    private boolean hasNext_step() {
        return SnapShot.size() < g.nodesCount() - amountCompanent;
    }

    private void next_step(int mark, IGraph.Edge edge) {
        if (componentMap.get(edge.firstNode()).equals(0) && componentMap.get(edge.secondNode()).equals(0)) {
            componentMap.put(edge.firstNode(), mark);
            componentMap.put(edge.secondNode(), mark);
            SnapShot.add(edge);
        } else {
            if (componentMap.get(edge.firstNode()) * componentMap.get(edge.secondNode()) == 0) {
                mark = Math.max(componentMap.get(edge.firstNode()), componentMap.get(edge.secondNode()));
                componentMap.put(edge.firstNode(), mark);
                componentMap.put(edge.secondNode(), mark);
                SnapShot.add(edge);
            } else {
                if (!componentMap.get(edge.firstNode()).equals(componentMap.get(edge.secondNode()))) {
                    mark = Math.min(componentMap.get(edge.firstNode()), componentMap.get(edge.secondNode()));
                    int not_mark = Math.max(componentMap.get(edge.firstNode()), componentMap.get(edge.secondNode()));
                    for(Map.Entry<IGraph.Node, Integer> e: componentMap.entrySet()){
                        if(e.getValue().equals(not_mark)){
                            componentMap.put(e.getKey(), mark);
                        }
                    }
                    componentMap.put(edge.firstNode(), mark);
                    componentMap.put(edge.secondNode(), mark);
                    SnapShot.add(edge);
                }
            }
        }
    }

    public void boruvka() {

        Iterable<IGraph.Node> nodes = g.getNodes();
        Graph snapshot = new Graph();
        g.getEdges().forEach(list::add);
        list.sort(this::MyCompare);

        int mark = 1;
        int num = 0;

        amountCompanent = component();

        for (IGraph.Node n : nodes) {
            IGraph.Node nnode = snapshot.addNode();
            nnode.setTitle(n.getTitle());
            componentMap.put(n, 0);
        }

        IGraph.Edge edge;
        while (hasNext_step()) {
            edge = list.get(num);
            next_step(mark, edge);
            blist.add(BoruvkaSnapshot.fromMapAndSet(componentMap, SnapShot));
            mark++;
            num++;
        }

    }

    public Iterable<IGraph.Edge> getSnapShotSet(){
        return SnapShot;
    }

    public Set<IGraph.Edge> resultEdgeSet(){
        return SnapShot;
    }

    public HashMap<IGraph.Node, Integer> getComponentMap(){
        return componentMap;
    }

    public boolean hasNext() {
        return step < SnapShot.size();
    }

    public void setStep(int st){
        step = st;
    }

    public BoruvkaSnapshot next() {
        step++;
        return blist.get(step-1);
    }
}
