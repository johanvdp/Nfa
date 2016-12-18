package nl.jvdploeg.nfa.internal.testset;

import org.junit.Assert;

import nl.jvdploeg.nfa.TokenMatcher;
import nl.jvdploeg.nfa.internal.NfaImpl;

public class EmptyTestSet extends AbstractTestSet {

    public EmptyTestSet() {
    }

    @Override
    public void assertTokenMatcher(final TokenMatcher state) {

        Assert.assertTrue(state.matches(new String[] {}));
        Assert.assertFalse(state.matches(new String[] { "foo" }));
        Assert.assertFalse(state.matches(new String[] { "bar" }));
        Assert.assertFalse(state.matches(new String[] { "foo", "bar" }));
        Assert.assertFalse(state.matches(new String[] { "bar", "foo" }));
    }

    @Override
    public NfaImpl build() {

        return factory.empty();
    }
}
