package nl.jvdploeg.nfa.internal.testset;

import org.junit.Assert;

import nl.jvdploeg.nfa.TokenMatcher;
import nl.jvdploeg.nfa.internal.NfaImpl;

public class Or2AnyTestSet extends AbstractTestSet {

    public Or2AnyTestSet() {
    }

    @Override
    public void assertTokenMatcher(final TokenMatcher state) {

        Assert.assertFalse(state.matches(new String[] {}));
        Assert.assertTrue(state.matches(new String[] { "foo" }));
        Assert.assertTrue(state.matches(new String[] { "bar" }));
        Assert.assertFalse(state.matches(new String[] { "foo", "bar" }));
        Assert.assertFalse(state.matches(new String[] { "bar", "foo" }));
    }

    @Override
    public NfaImpl build() {

        return factory.or(factory.any(), factory.any());
    }
}
