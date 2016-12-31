package nl.jvdploeg.nfa.internal.testset;

import nl.jvdploeg.nfa.TokenMatcher;
import nl.jvdploeg.nfa.internal.NfaImpl;
import org.junit.Assert;

import java.util.Arrays;

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

    return factory
        .or(Arrays.asList(factory.token("foo"), factory.token("bar"), factory.token("baz")));
  }
}
