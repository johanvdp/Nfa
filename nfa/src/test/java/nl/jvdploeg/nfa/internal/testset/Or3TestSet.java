package nl.jvdploeg.nfa.internal.testset;

import java.util.Arrays;

import org.junit.Assert;

import nl.jvdploeg.nfa.TokenMatcher;
import nl.jvdploeg.nfa.internal.NfaImpl;

public class Or3TestSet extends AbstractTestSet {

    public Or3TestSet() {
    }

    @Override
    public void assertTokenMatcher(final TokenMatcher state) {

        Assert.assertFalse(state.matches(new String[] {}));
        Assert.assertTrue(state.matches(new String[] { "foo" }));
        Assert.assertTrue(state.matches(new String[] { "bar" }));
        Assert.assertTrue(state.matches(new String[] { "baz" }));
        Assert.assertFalse(state.matches(new String[] { "foo", "bar", "baz" }));
    }

    @Override
    public NfaImpl build() {

        return factory.or(Arrays.asList(factory.token("foo"), factory.token("bar"), factory.token("baz")));
    }
}
