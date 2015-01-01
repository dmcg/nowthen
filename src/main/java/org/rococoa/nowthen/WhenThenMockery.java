package org.rococoa.nowthen;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.internal.ExpectationBuilder;

import java.util.function.Supplier;

public class WhenThenMockery extends JUnitRuleMockery {

	public interface Block<E extends Exception> {
		public void run() throws E;
	}

	public interface Expects<T extends Expectations, E extends Exception> {
		public void apply(T t) throws E;
	}

	public WhenThenMockery given(Expectations expectations) {
		checking(expectations);
		return this;
	}

	public <E extends Exception> WhenThenMockery given(Expects<Expectations, E> expects) throws E {
		return given(Expectations::new, expects);
	}

	public <T extends Expectations, E extends Exception> WhenThenMockery given(Supplier<T> supplier, Expects<T, E> expects) throws E {
		T x = supplier.get();
		expects.apply(x);
		return given(x);
	}

	public <E extends Exception> Then<E> when(Block<E> block) {
		return new Then<>(block);
	}

	public class Then<E extends Exception> {

		private final Block<E> block;

		private Then(Block<E> block) {
			this.block = block;
		}

		public void then(ExpectationBuilder expectations) throws E {
			checking(expectations);
			block.run();
			assertIsSatisfied();
		}

		public void then(Expects<Expectations, E> expects) throws E {
			then(Expectations::new, expects);
		}

		public <T extends Expectations> void then(Supplier<T> supplier, Expects<T, E> expects) throws E {
			T x = supplier.get();
			expects.apply(x);
			then(x);
		}
	}
}
