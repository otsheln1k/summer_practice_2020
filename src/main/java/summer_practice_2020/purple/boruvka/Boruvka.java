package summer_practice_2020.purple.boruvka;

import summer_practice_2020.purple.IGraph;

import java.util.*;

public class Boruvka{

    private IGraph g;
    private HashMap<IGraph.Node, String> visitedMap = new HashMap<IGraph.Node, String>();
    private int amountCompanent = 1;
    private Iterable<IGraph.Node> nodes;
    private Set<IGraph.Edge> blockedEdges = new HashSet<IGraph.Edge>();
    private List<IGraph.Edge> list = new ArrayList<IGraph.Edge>();
    private Set<IGraph.Edge> SnapShot = new HashSet<IGraph.Edge>();
    private List<BoruvkaSnapshot> blist = new ArrayList<BoruvkaSnapshot>();
    private int step = 0;
    private  Group cloneGroupfirst, cloneGroupsecond;
    private IGraph.Edge currentMinEdge = null;

    private Queue<Group> allGroups = new ArrayDeque<>();

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

        list.clear();
        Group nowGroup = allGroups.remove();

        double min = 2000000;
        IGraph.Edge minEdge = null;

        Set<IGraph.Node> nowNodes = nowGroup.getNodesGroup();
        Iterable<IGraph.Edge> nowEdges = g.getEdges();
        for(IGraph.Node n: nowNodes){
            for(IGraph.Edge e: nowEdges){
                if(nowGroup.HasEdge(e) && !blockedEdges.contains(e)){
                    if(!nowGroup.getNodesGroup().contains(e.firstNode()) | !nowGroup.getNodesGroup().contains(e.secondNode())) {
                        list.add(e);
                        if(e.getWeight() < min) {
                            min = e.getWeight();
                            minEdge = e;
                        }
                    }
                }
            }
        }
        currentMinEdge = minEdge;

        if(minEdge != null) {
            blockedEdges.add(minEdge);
            boolean flag = true;
            List<Group> newlist = new ArrayList<Group>();
            newlist.addAll(allGroups);
            for (Group now : newlist) {
                if (flag && now.HasEdge(minEdge)) {
                    cloneGroupfirst = nowGroup.clone();
                    cloneGroupsecond = now.clone();
                    now.merge(nowGroup);
                    SnapShot.add(minEdge);
                }
            }
            allGroups.add(nowGroup);

        }
    }

    public void boruvka() {

        nodes = g.getNodes();

        amountCompanent = component();

        int i = 0;
        for (IGraph.Node n : nodes) {
            Group now = new Group(i++);
            now.addNode(n);
            allGroups.add(now);
        }

        int mark = 1;
        while (hasNext_step() && !allGroups.isEmpty()) {
            next_step();
            if(list.size() > 0 && currentMinEdge != null && cloneGroupfirst != null && cloneGroupsecond != null) {
                List<IGraph.Edge> cEdges = new ArrayList<IGraph.Edge>();
                cEdges.addAll(list);
                IGraph.Edge cEdge = currentMinEdge;
                blist.add(new BoruvkaSnapshot(allGroups, g.getEdges(), cloneGroupfirst, cloneGroupsecond, cEdges, cEdge));
            }
        }

    }

    public Iterable<IGraph.Edge> getSnapShotSet(){
        return SnapShot;
    }

    public Set<IGraph.Edge> resultEdgeSet(){
        return SnapShot;
    }

    public boolean hasNext() {
        if(step < blist.size()){
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
