package nl.jvdploeg.nfa.internal.testset;

import org.junit.Assert;

import nl.jvdploeg.nfa.TokenMatcher;
import nl.jvdploeg.nfa.internal.NfaImpl;

public class ZeroOrMoreAnyFooTestSet extends AbstractTestSet {

    public ZeroOrMoreAnyFooTestSet() {
    }

    @Override
    public void assertTokenMatcher(final TokenMatcher state) {

        Assert.assertFalse(state.matches(new String[] {}));
        Assert.assertTrue(state.matches(new String[] { "foo" }));
        Assert.assertTrue(state.matches(new String[] { "foo", "foo" }));
        Assert.assertFalse(state.matches(new String[] { "foo", "foo", "bar" }));
        Assert.assertTrue(state.matches(new String[] { "bar", "foo" }));
    }

    @Override
    public NfaImpl build() {

        return factory.sequence(factory.zeroOrMore(factory.any()), factory.token("foo"));
    }
}
