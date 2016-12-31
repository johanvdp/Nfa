package nl.jvdploeg.nfa.internal.testset;

import nl.jvdploeg.nfa.TokenMatcher;
import nl.jvdploeg.nfa.internal.NfaImpl;
import org.junit.Assert;

public class Or2TestSet extends AbstractTestSet {

  public Or2TestSet() {
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

    return factory.or(factory.token("foo"), factory.token("bar"));
  }
}
