package summer_practice_2020.purple.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class FilteredIterator<T> implements Iterator<T> {
	private final Iterator<T> iter;
	private final Predicate<T> pred;
	private T nextMatching;
	private boolean haveNext = false;

	public FilteredIterator(Iterator<T> iter, Predicate<T> pred) {
		this.iter = iter;
		this.pred = pred;
	}

	private boolean advance() {
		while (iter.hasNext()) {
			T n = iter.next();
			if (pred.test(n)) {
				haveNext = true;
				nextMatching = n;
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean hasNext() {
		return haveNext || advance();
	}
	
	@Override
	public T next() {
		if (haveNext || !advance()) {
			haveNext = false;
			T n = nextMatching;
			nextMatching = null;
			return n;
		} else {
			throw new NoSuchElementException();
		}
	}
}