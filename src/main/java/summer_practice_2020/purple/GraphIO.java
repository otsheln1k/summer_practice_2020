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

	public static void readGraph(InputStream s, IGraph g) {
		Map<String, IGraph.Node> nodes = new HashMap<>(); 

		@SuppressWarnings("resource")
		Scanner sc = new Scanner(s);
		sc.useLocale(Locale.ROOT);  // required to use `.' as decimal point

		while (sc.hasNext()) {
			String key = sc.next();
			switch (key) {
			case "node":
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

			case "edge":
				String an = sc.next();
				IGraph.Node a = nodes.get(an);
				if (a == null) {
					throw new GraphFormatException("unknown node :" + an);
				}
				String bn = sc.next();
				IGraph.Node b = nodes.get(bn);
				if (b == null) {
					throw new GraphFormatException("unknown node :" + bn);
				}
				IGraph.Edge e = g.addEdge(a, b);
				double weight = sc.nextDouble();
				e.setWeight(weight);
				break;

			default:
				throw new GraphFormatException("unknown key :" + key);
			}
		}
	}
}
