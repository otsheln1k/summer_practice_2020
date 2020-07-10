package summer_practice_2020.purple.rendering;

import summer_practice_2020.purple.BoruvkaSnapshot;
import summer_practice_2020.purple.IGraph;

public class WorkStep {
    private final IGraph.Edge edge;
    private final String description;

    public WorkStep(BoruvkaSnapshot boruvkaSnapshot) {
        this.edge = boruvkaSnapshot.getSelectedEdge();
        this.description = "Выбрано ребро между " + edge.firstNode().getTitle()
                + " и " + edge.secondNode().getTitle();

    }

    public IGraph.Edge getEdge() {
        return this.edge;
    }

    public String getDescription() {
        return this.description;
    }
}
