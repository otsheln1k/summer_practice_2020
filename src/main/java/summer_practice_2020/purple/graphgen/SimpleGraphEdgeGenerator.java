package summer_practice_2020.purple.graphgen;

import summer_practice_2020.purple.IGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleGraphEdgeGenerator implements GraphEdgeGenerator {

    private final double edgeProb;
    private final Random rng = new Random();

    public SimpleGraphEdgeGenerator(double edgeProb) {
        this.edgeProb = edgeProb;
    }

    @Override
    public void generateEdgesOnNodes(IGraph g, Iterable<IGraph.Node> ns) {
        List<IGraph.Node> nodes = new ArrayList<>();
        ns.forEach(nodes::add);

        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                IGraph.Node ni = nodes.get(i);
                IGraph.Node nj = nodes.get(j);

                if (ni == nj || g.getEdgeBetween(ni, nj) != null) {
                    continue;
                }

                if (rng.nextDouble() < edgeProb) {
                    g.addEdge(ni, nj);
                }
            }
        }
    }

}
