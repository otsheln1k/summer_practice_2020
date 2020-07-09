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
    private Queue<Group> allGroups = new ArrayDeque<>();

    public Boruvka(IGraph g) {
        this.g = g;
    }

    public int MyCompare (Group ge1, Group ge2){
        return Integer.compare(ge1.getNodesGroup().size(), ge2.getNodesGroup().size());
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
        Group nowGroup = allGroups.remove();
        //System.out.println("Шаг:");

        //System.out.println("Вершины и ребра текущей группы:");
        Set<IGraph.Node> nowNodes = nowGroup.getNodesGroup();
        Iterable<IGraph.Edge> nowEdges = g.getEdges();
        for(IGraph.Node n: nowNodes){
            //System.out.printf(n.getTitle() + " ");
            for(IGraph.Edge e: nowEdges){
                if(nowGroup.hasEdge(e)){
                    list.add(e);
                }
            }
        }
        /*System.out.println();
        System.out.println(list.size());
        for(IGraph.Edge i: list){
            System.out.println(i.firstNode().getTitle() + " " + i.secondNode().getTitle() + " " + i.getWeight());
        }
        System.out.println();*/

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
            //System.out.println("Минимальное ребро: " + minEdge.firstNode().getTitle() + " " + minEdge.secondNode().getTitle() + " " + minEdge.getWeight());
            //Group mg = null;
            //Ищем группу, куда ведет ребро и объеденяем группы
            if(minEdge != null) {
                blockedEdges.add(minEdge);
                // стянет РАЗНЫЕ группы
                if(!nowGroup.getNodesGroup().contains(minEdge.firstNode()) | !nowGroup.getNodesGroup().contains(minEdge.secondNode())) {
                    //Queue<Group> newQueue = new ArrayDeque<Group>();
                    //newQueue.addAll(allGroups);
                    boolean flag = true;
                    List<Group> newlist = new ArrayList<Group>();
                    newlist.addAll(allGroups);
                    for (Group now : newlist) {
                        if (flag && now.hasEdge(minEdge)) {
                            /*//
                            System.out.println("Группы с которой мержим:");
                            Set<IGraph.Node> fn = now.getNodesGroup();
                            for (IGraph.Node n : fn) {
                                System.out.printf(n.getTitle());
                            }
                            System.out.println();
                            //*/
                            now.merge(nowGroup);
                            SnapShot.add(minEdge);
                        }
                    }
                }
                allGroups.add(nowGroup);
            }
        }
        else {
            if(nowNodes.size() == 1){
                nullGroup.addNode(nowNodes.iterator().next());
            }
        }

    }

    public void boruvka() {

        Iterable<IGraph.Node> nodes = g.getNodes();
        /*g.getEdges().forEach(list::add);
        //System.out.println("LIST");
        Collections.sort(list, this::MyCompare);
        /*for(IGraph.Edge le: list){
            System.out.println(le.firstNode().getTitle() + " " + le.secondNode().getTitle() + " " + le.getWeight());
        }
        System.out.println();
        //System.out.println("Число компонент = " + component());
        //System.out.println();*/

        int mark = 1;

        amountCompanent = component();
        //System.out.println("Число компанент: " + amountCompanent);
        /*System.out.println("ГРАФ:");
        Iterable<IGraph.Node> nodes_g = g.getNodes();
        for (IGraph.Node i: nodes_g){
            System.out.printf(i.getTitle() + " ");
        }
        System.out.println();
        Iterable<IGraph.Edge> edges_g = g.getEdges();
        for(IGraph.Edge i: edges_g){
            System.out.println(i.firstNode().getTitle() + " " + i.secondNode().getTitle() + " " + i.getWeight());
        }
        System.out.println("------------------------");*/

        for (IGraph.Node n : nodes) {
            Group now = new Group();
            now.addNode(n);
            //all_group.add(now);
            allGroups.add(now);
            componentMap.put(n, 0);
        }
        /*/Вывод массива групп
        System.out.println("Массив груп до первого шага");
        for(Group gr:all_group){
            Set<IGraph.Node> pg = gr.getNodesGroup();
            for(IGraph.Node pgn: pg){
                System.out.printf(pgn.getTitle());
            }
            System.out.println();
        }
        System.out.println();*/

        while (hasNext_step() && !allGroups.isEmpty()) {
            //System.out.println(allGroups.size());
            ///Вывод очереди групп
            /*System.out.println("Очередь групп до ШАГА алгоритма");
            Queue<Group> newq = new ArrayDeque<Group>();
            newq.addAll(allGroups);
            while (!newq.isEmpty()){
                Group newgroup = newq.remove();
                for(IGraph.Node i: newgroup.getNodes()){
                    System.out.printf(i.getTitle());
                }
                System.out.println();
            }*/
            //System.out.println("Добавлено " + SnapShot.size() + " ребер");
            //System.out.println();
            ///
            next_st(mark);
            all_group.clear();
            all_group.addAll(allGroups);
            /*System.out.println("Массив груп после очередного шага:");
            for(Group gr:all_group){
                Set<IGraph.Node> pg = gr.getNodesGroup();
                for(IGraph.Node pgn: pg){
                    System.out.printf(pgn.getTitle());
                }
                System.out.println();
            }
            System.out.println();
            System.out.println("Сет ребер после очередного шага:");
            for(IGraph.Edge e: SnapShot){
                System.out.printf(e.firstNode().getTitle() + " " + e.secondNode().getTitle() + " " + e.getWeight());
                System.out.println();
            }
            System.out.println();*/
            for(Group gr:all_group){
                Set<IGraph.Node> pg = gr.getNodesGroup();
                if(pg.size() == 1){
                    componentMap.put(pg.iterator().next(), 0);
                }
                else {
                    for (IGraph.Node pgn : pg) {
                        componentMap.put(pgn, mark);
                    }
                }
            }
            //blist.add(new BoruvkaSnapshot(all_group, new BoruvkaFinalStep(), SnapShot));
            /*System.out.println("componentMap после очередного шага:");
            for(Map.Entry<IGraph.Node, Integer> mp: componentMap.entrySet()){
                System.out.printf(mp.getKey().getTitle() + " " + mp.getValue() + " / ");
            }
            System.out.println();
            System.out.println("---------------------------------------------------");*/
            blist.add(BoruvkaSnapshot.fromMapAndSet(componentMap, SnapShot));
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
