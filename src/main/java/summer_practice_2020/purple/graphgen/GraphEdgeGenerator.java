package summer_practice_2020.purple.graphgen;

import summer_practice_2020.purple.IGraph;

// NOTE: nodes are pre-generated
@FunctionalInterface
public interface GraphEdgeGenerator {
    void generateEdgesOnNodes(IGraph g, Iterable<IGraph.Node> nodes);

    default void generateEdges(IGraph g) {
        generateEdgesOnNodes(g, g.getNodes());
    }
}
