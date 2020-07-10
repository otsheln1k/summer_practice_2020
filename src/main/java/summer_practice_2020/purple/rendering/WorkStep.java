package summer_practice_2020.purple.rendering;

import summer_practice_2020.purple.BoruvkaSnapshot;
import summer_practice_2020.purple.IGraph;

public class WorkStep {
    private IGraph.Edge edge;
    private String description;

    public WorkStep(BoruvkaSnapshot boruvkaSnapshot, IGraph graph) {
        for (IGraph.Edge edge : graph.getEdges()) {
            if (boruvkaSnapshot.getEdgePicked(edge)) {
                this.edge = edge;
                this.description = "Выбрано ребро между " + edge.firstNode().getTitle()
                        + " и " + edge.secondNode().getTitle();
                break;
            }
        }
    }

    public IGraph.Edge getEdge() {
        return this.edge;
    }

    public String getDescription() {
        return this.description;
    }
}
