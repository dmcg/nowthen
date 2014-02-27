package org.jmock;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.internal.ExpectationBuilder;

public class WhenThenMockery extends Mockery {

	public interface Block extends TypedBlock<Exception> {
		@Override public void run() throws Exception;
	}

	public interface TypedBlock<E extends Exception> {
		public void run() throws E;
	}

	public When<RuntimeException> when(final Runnable runnable) {
		return when(new TypedBlock<RuntimeException>() {
			@Override public void run() throws RuntimeException {
				runnable.run();
			}
		});
	}

	public <E extends Exception> When<E> when(TypedBlock<E> block) {
		return new When<E>(block);
	}

	public class When<E extends Exception> {

		private final TypedBlock<E> block;

		private When(TypedBlock<E> block) {
			this.block = block;
		}

		public void then(ExpectationBuilder expectations) throws E {
			checking(expectations);
			block.run();
			assertIsSatisfied();
		}
	}
}
