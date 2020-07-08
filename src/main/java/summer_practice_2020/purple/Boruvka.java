package summer_practice_2020.purple;
import java.util.*;

public class Boruvka{

    private IGraph g;
    private HashMap<IGraph.Node, Integer> componentMap = new HashMap<IGraph.Node, Integer>();
    private HashMap<IGraph.Node, String> visitedMap = new HashMap<IGraph.Node, String>();
    private int amountCompanent = 1;
    private Iterable<IGraph.Node> nodes;
    private List<Group> all_group = new ArrayList<Group>();
    private Set<IGraph.Edge> blockedEdges = new HashSet<IGraph.Edge>();
    private List<IGraph.Edge> list = new ArrayList<IGraph.Edge>();
    private Set<IGraph.Edge> SnapShot = new HashSet<IGraph.Edge>();
    private List<BoruvkaSnapshot> blist = new ArrayList<BoruvkaSnapshot>();
    private Group nullGroup = new Group();
    private int step = 0;

    public Boruvka(IGraph g) {
        this.g = g;
    }

    public int MyCompare (Group e1, Group e2){
        return Integer.compare(e1.getNodesGroup().size(), e2.getNodesGroup().size());
    }

    private void dfs(IGraph.Node v){
        visitedMap.put(v, "visited");
        //System.out.println("Vertex now = " + v.getTitle());
        /*for(IGraph.Edge now: g.getEdgesFrom(v)){
            System.out.println(now.firstNode().getTitle() + " " + now.secondNode().getTitle() + " " + now.getWeight());
        }*/
        for(IGraph.Edge now: g.getEdgesFrom(v)) {
            //System.out.println(now.firstNode().getTitle() + " " + now.secondNode().getTitle() + " " + now.getWeight());
            if (v.equals(now.firstNode())) {
                //System.out.println("v == firstNone");
                if (visitedMap.get(now.secondNode()).equals("not_visited")) {
                    //System.out.println("Second = " + now.secondNode().getTitle());
                    //System.out.println();
                    dfs(now.secondNode());
                }
            }
            else {
                //System.out.println("v == secondNone");
                if (visitedMap.get(now.firstNode()).equals("not_visited")) {
                    //System.out.println("First = " + now.firstNode().getTitle());
                    //System.out.println();
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
                /*for(Map.Entry<IGraph.Node, String> vv: visitedMap.entrySet()){
                    System.out.printf(vv.getKey().getTitle() + " " + vv.getValue() + " / ");
                }*/
                System.out.println();
                result++;
            }
        }
        return result;
    }

    private boolean hasNext_step() {
        if (SnapShot.size() < g.nodesCount() - amountCompanent) {
            return true;
        } else {
            return false;
        }
    }

    private void next_st(int mark){
        //Берем очередную группу и достаем ее вершины
        list.clear();
        Group nowGroup = all_group.get(0);
        Set<IGraph.Node> nowNodes = nowGroup.getNodesGroup();

        //Получаем все ребра группы
        Iterable<IGraph.Edge> nowEdges = g.getEdges();
        for(IGraph.Node n: nowNodes){
            for(IGraph.Edge e: nowEdges){
                if(e.firstNode() == n || e.secondNode() == n){
                    list.add(e);
                }
            }
        }
        if(list.size() > 0) {
            //Выбираем мин ребро

            double min = 2000000;
            IGraph.Edge minEdge = null;
            for (IGraph.Edge e : list) {
                if (e.getWeight() < min & !blockedEdges.contains(e)) {
                    min = e.getWeight();
                    minEdge = e;
                }
            }
            blockedEdges.add(minEdge);
            IGraph.Node search;
            if (nowNodes.contains(minEdge.firstNode())) {
                search = minEdge.secondNode();
            } else {
                search = minEdge.firstNode();
            }
            Group mg = null;
            //Ищем группу, куда ведет ребро и объеденяем группы
            for (Group group : all_group) {
                Set<IGraph.Node> now = group.getNodesGroup();
                if (now.contains(search)) {
                    group.merge(nowGroup);
                    SnapShot.add(minEdge);
                    mg = group;
                    break;
                }
            }
            //изменяе all_group

            all_group.remove(nowGroup);
            Collections.sort(all_group, this::MyCompare);
        }
        else {
            nullGroup.addNode(nowNodes.iterator().next());
            all_group.remove(nowGroup);
        }
    }

    public void boruvka() {

        Iterable<IGraph.Node> nodes = g.getNodes();

        int mark = 1;

        amountCompanent = component();

        for (IGraph.Node n : nodes) {
            Group now = new Group();
            now.addNode(n);
            all_group.add(now);
            componentMap.put(n, 0);
        }

        Collections.sort(all_group, this::MyCompare);

        while (hasNext_step()) {
            next_st(mark);
            blist.add(new BoruvkaSnapshot(all_group, new BoruvkaFinalStep(), SnapShot));
            mark++;
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
        if(step < SnapShot.size()){
            return true;
        }
        else {
            return false;
        }
    }

    public void setStep(int st){
        step = st;
    }

    public BoruvkaSnapshot next() {
        step++;
        return blist.get(step-1);
    }
}
