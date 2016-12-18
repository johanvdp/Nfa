package nl.jvdploeg.nfa.internal.testset;

import org.junit.Assert;

import nl.jvdploeg.nfa.TokenMatcher;
import nl.jvdploeg.nfa.internal.NfaImpl;

public class ComboTestSet extends AbstractTestSet {

    public ComboTestSet() {

    }

    @Override
    public void assertTokenMatcher(final TokenMatcher state) {

        Assert.assertTrue(state.matches(new String[] {}));
        Assert.assertTrue(state.matches(new String[] { "foo" }));
        Assert.assertTrue(state.matches(new String[] { "bar" }));
        Assert.assertTrue(state.matches(new String[] { "foo", "bar" }));
        Assert.assertFalse(state.matches(new String[] { "foo", "---" }));
        Assert.assertFalse(state.matches(new String[] { "---", "bar" }));
        Assert.assertTrue(state.matches(new String[] { "bar", "foo" }));
        Assert.assertFalse(state.matches(new String[] { "foo", "foo", "bar", "---" }));
        Assert.assertTrue(state.matches(new String[] { "foo", "foo", "bar", "foo" }));
    }

    @Override
    public NfaImpl build() {

        return factory.sequence(factory.zeroOrMore(factory.or(factory.token("foo"), factory.token("bar"))), factory.empty());
    }
}
