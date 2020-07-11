package summer_practice_2020.purple;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class GraphIO {
	public static void writeGraph(OutputStream s, IGraph g) {
		PrintStream ps = new PrintStream(s);
		Map<IGraph.Node, Integer> nodes = new HashMap<>();

		int i = 0;
		for (IGraph.Node n : g.getNodes()) {
			ps.printf("node %d %s%n", i, n.getTitle());
			ps.printf("pos %d %g %g%n", i, n.getPosX(), n.getPosY());

			nodes.put(n, i);
			i++;
		}

		for (IGraph.Edge e : g.getEdges()) {
			ps.printf("edge %d %d %g%n",
					nodes.get(e.firstNode()),
					nodes.get(e.secondNode()),
					e.getWeight());
		}
	}

	private static IGraph.Node getNodeChecked(Map<String, IGraph.Node> map,
			String name) {
		IGraph.Node n = map.get(name);
		if (n == null) {
			throw new GraphFormatException("unknown node :" + name);
		}
		return n;
	}

	public static void readGraph(InputStream s, IGraph g) {
		Map<String, IGraph.Node> nodes = new HashMap<>(); 

		@SuppressWarnings("resource")
		Scanner sc = new Scanner(s);
		sc.useLocale(Locale.ROOT);  // required to use `.' as decimal point

		while (sc.hasNext()) {
			String key = sc.next();
			switch (key) {
			case "node": {
				String name = sc.next();
				IGraph.Node n = nodes.compute(name, (x, prev) -> {
					if (prev != null) {
						throw new GraphFormatException(
								"duplicate node: " + name);
					}
					return g.addNode();
				});
				sc.skip("\\s*");

				String title = sc.nextLine();
				n.setTitle(title);
				break;
			}

			case "edge": {
				IGraph.Node a = getNodeChecked(nodes, sc.next());
				IGraph.Node b = getNodeChecked(nodes, sc.next());
				IGraph.Edge e = g.addEdge(a, b);
				e.setWeight(sc.nextDouble());
				break;
			}

			case "pos": {
				IGraph.Node n = getNodeChecked(nodes, sc.next());
				n.setPosX(sc.nextDouble());
				n.setPosY(sc.nextDouble());
				break;
			}

			default:
				throw new GraphFormatException("unknown key :" + key);
			}
		}
	}
}
