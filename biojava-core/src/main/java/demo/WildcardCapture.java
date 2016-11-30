package demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class WildcardCapture {

	@SuppressWarnings("serial")
	static class NumberLists<L extends List<T>,T extends Number> extends ArrayList<L> {}

	static <L extends List<T>,T extends Number>
	Set<NumberLists<L,T>> useList(L list) {
		NumberLists<L,T> nl = new NumberLists<L, T>();
		nl.add(list);
		return Collections.singleton(nl);
	}

	// Note that this is not assignable to List<Number>
	static List<? extends Number> makeList() {
		if(Math.random()<.5) {
			return new ArrayList<Integer>();
		} else {
			return new ArrayList<Double>();
		}
	}

	static <L extends List<T>,T extends Number>
	NumberLists<L,T> createAndAdd(L list) {
		NumberLists<L,T> paraNL = new NumberLists<>();
		paraNL.add(list);
		return paraNL;
	}
	
	private static <T extends Number, L extends List<T>> void helper(L numList) {
		Set<NumberLists<L, T>> paramSet = useList(numList);
		NumberLists<L, T> paraNL1 = paramSet.iterator().next();
		paraNL1.add(numList);
	}

	public static void main(String[] args) {
		// Everything works with a concrete class
		List<Integer> intList = new ArrayList<Integer>();
		// Use with parametric functions
		Set<NumberLists<List<Integer>,Integer>> concreteSet = useList(intList);
		// Create related class of the same type
		NumberLists<List<Integer>,Integer> concreteNL = concreteSet.iterator().next();
		concreteNL.add(intList);

		// But now I need to handle the case where the input is also parametric
		List<? extends Number> numList = makeList();

		// Need to move all code to a wildcard capture helper function
		helper(numList);
	}

}
