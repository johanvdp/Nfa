package nl.jvdploeg.nfa.internal.testset;

import java.util.Arrays;

import org.junit.Assert;

import nl.jvdploeg.nfa.TokenMatcher;
import nl.jvdploeg.nfa.internal.NfaImpl;

public class FooZeroOrMoreAnyFooTestSet extends AbstractTestSet {

    public FooZeroOrMoreAnyFooTestSet() {
    }

    @Override
    public void assertTokenMatcher(final TokenMatcher state) {

        Assert.assertFalse(state.matches(new String[] {}));
        Assert.assertFalse(state.matches(new String[] { "foo" }));
        Assert.assertTrue(state.matches(new String[] { "foo", "foo" }));
        Assert.assertTrue(state.matches(new String[] { "foo", "---", "foo" }));
        Assert.assertFalse(state.matches(new String[] { "foo", "---" }));
        Assert.assertFalse(state.matches(new String[] { "foo", "foo", "---" }));
    }

    @Override
    public NfaImpl build() {

        return factory.sequence(Arrays.asList(factory.token("foo"), factory.zeroOrMore(factory.any()), factory.token("foo")));
    }
}
