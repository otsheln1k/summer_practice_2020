package summer_practice_2020.purple.graphgen;

public class AlphabetNodeNameGenerator implements GraphNodeNameGenerator {
	private final StringBuilder next = new StringBuilder("A");

	@Override
	public String generateName() {
		String prev = next.toString();

		for (int i = next.length(); i-- > 0;) {
			char c = next.charAt(i);
			char c1 = (c == 'Z') ? 'A' : (char)(c+1);
			String s = String.valueOf(c1);
			next.replace(i, i+1, s);

			if (c != 'Z') {
				return prev;
			}
		}

		next.insert(0, 'A');
		return prev;
	}

}
