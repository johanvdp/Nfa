package nl.jvdploeg.nfa.internal.testset;

import nl.jvdploeg.nfa.TokenMatcher;
import nl.jvdploeg.nfa.internal.NfaImpl;
import org.junit.Assert;

public class Sequence2TestSet extends AbstractTestSet {

  public Sequence2TestSet() {
  }

  @Override
  public void assertTokenMatcher(final TokenMatcher state) {

    Assert.assertFalse(state.matches(new String[] {}));
    Assert.assertFalse(state.matches(new String[] { "foo" }));
    Assert.assertFalse(state.matches(new String[] { "bar" }));
    Assert.assertTrue(state.matches(new String[] { "foo", "bar" }));
    Assert.assertFalse(state.matches(new String[] { "foo", "bar", "---" }));
    Assert.assertFalse(state.matches(new String[] { "---", "foo", "bar" }));
  }

  @Override
  public NfaImpl build() {

    return factory.sequence(factory.token("foo"), factory.token("bar"));
  }
}
