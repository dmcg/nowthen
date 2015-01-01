package org.rococoa.nowthen;

import org.jmock.Expectations;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;

import static org.jmock.Expectations.returnValue;
import static org.junit.Assert.*;

@SuppressWarnings("CodeBlock2Expr")
public class WhenThenMockeryTest {

    @Rule
    public final WhenThenMockery mockery = new WhenThenMockery();

    private final CharSequence charSequence  = mockery.mock(CharSequence.class);
    private final Appendable writer = mockery.mock(Appendable.class);

    @Test
    public void given_with_expectations() {
        mockery.given(new Expectations() {{
            allowing(charSequence).length();
            will(returnValue(42));
        }});
        assertEquals(42, charSequence.length());
    }

    @Test
    public void given_with_lambda() {
        mockery.given(g -> {
            g.allowing(charSequence).length();
            g.will(returnValue(42));
        });
        assertEquals(42, charSequence.length());
    }

    @Test
    public void given_with_expectations_supplier() {
        mockery.given(MyExpectations::new, g -> {
            g.sequenceWithLength(charSequence, 42);
        });
        assertEquals(42, charSequence.length());
    }

    @Test
    public void given_when_then() throws IOException {
        mockery.given(MyExpectations::new, g -> {
            g.sequenceWithLength(charSequence, 42);
        }).when(() -> {
            writer.append(String.valueOf(charSequence.length()));
        }).then(t -> {
            t.oneOf(writer).append("42");
        });
    }

    @Test
    public void given_split_from_when_then() throws IOException {
        mockery.given(MyExpectations::new, g -> {
            g.sequenceWithLength(charSequence, 42);
        });
        mockery.when(() -> {
            writer.append(String.valueOf(charSequence.length()));
        }).then(t -> {
            t.oneOf(writer).append("42");
        });
    }

    @Test
    public void when_then() throws IOException {
        mockery.when(() -> {
            writer.append("42");
        }).then(t -> {
            t.oneOf(writer).append("42");
        });
    }

    @Test
    public void when_throwing_multiple_exceptions() throws Exception {
        mockery.when(() -> {
            writer.append("42");
            //noinspection ConstantConditions,ConstantIfStatement
            if (false)
                throw new ParseException("", 0);
        }).then(t -> {
            t.oneOf(writer).append("42");
        });
    }

    private static class MyExpectations extends Expectations {
        public void sequenceWithLength(CharSequence s, int length) {
            allowing(s).length();
            will(returnValue(length));
        }
    }
}