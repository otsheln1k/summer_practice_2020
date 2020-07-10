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
    //private List<List> blist = new ArrayList<>();
    private Group nullGroup = new Group();
    private int step = 0;

    private Queue<Group> allGroups = new ArrayDeque<>();

    public int MyCompare (IGraph.Edge e1, IGraph.Edge e2){
        return Double.compare(e1.getWeight(), e2.getWeight());
    }

    public Boruvka(IGraph g) {
        this.g = g;
    }

    private void dfs(IGraph.Node v){
        visitedMap.put(v, "visited");
        for(IGraph.Edge now: g.getEdgesFrom(v)) {
            if (v.equals(now.firstNode())) {
                if (visitedMap.get(now.secondNode()).equals("not_visited")) {
                    dfs(now.secondNode());
                }
            }
            else {
                if (visitedMap.get(now.firstNode()).equals("not_visited")) {
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

    private void next_step(){

        /*List<List> snapshot = new ArrayList<>();
        List<IGraph.Edge> snapshotEdges = new ArrayList<IGraph.Edge>();
        List<Group> snapshotGroup = new ArrayList<Group>();*/

        Group nowGroup = allGroups.remove();

        double min = 2000000;
        IGraph.Edge minEdge = null;

        Set<IGraph.Node> nowNodes = nowGroup.getNodesGroup();
        Iterable<IGraph.Edge> nowEdges = g.getEdges();
        for(IGraph.Node n: nowNodes){
            for(IGraph.Edge e: nowEdges){
                if(nowGroup.HasEdge(e) && !blockedEdges.contains(e)){
                    if(!nowGroup.getNodesGroup().contains(e.firstNode()) | !nowGroup.getNodesGroup().contains(e.secondNode())) {
                        //snapshotEdges.add(e);
                        if(e.getWeight() < min) {
                            min = e.getWeight();
                            minEdge = e;
                        }
                    }
                }
            }
        }

        //snapshotEdges.sort(this::MyCompare);

        if(nowNodes.size() == 1){
            nullGroup.addNode(nowNodes.iterator().next());
        }
        Group cloneGroupfirst, cloneGroupsecond;
        if(minEdge != null) {
            blockedEdges.add(minEdge);
            boolean flag = true;
            List<Group> newlist = new ArrayList<Group>();
            newlist.addAll(allGroups);
            for (Group now : newlist) {
                if (flag && now.HasEdge(minEdge)) {
                    cloneGroupfirst = nowGroup.clone();
                    cloneGroupsecond = now.clone();
                    //snapshotGroup.add(cloneGroupfirst);
                    //snapshotGroup.add(cloneGroupsecond);
                    now.merge(nowGroup);
                    SnapShot.add(minEdge);
                }
            }
            allGroups.add(nowGroup);
            //snapshot.add(snapshotEdges);
            //snapshot.add(snapshotGroup);
            //return snapshot;
        }
        //return null;
    }

    public void boruvka() {

        nodes = g.getNodes();

        amountCompanent = component();

        for (IGraph.Node n : nodes) {
            Group now = new Group();
            now.addNode(n);
            allGroups.add(now);
            componentMap.put(n, 0);
        }

        int mark = 1;
        while (hasNext_step() && !allGroups.isEmpty()) {
            //List<List> nowSnapshot = new ArrayList<>();
            //nowSnapshot = next_step();
            //if(nowSnapshot != null)
                //blist.add(nowSnapshot);
            all_group.clear();
            all_group.addAll(allGroups);
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

    /*public  List next(){
        step++;
        return blist.get(step-1);
    }*/
}
