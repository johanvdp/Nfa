package nl.jvdploeg.nfa.internal.testset;

import nl.jvdploeg.nfa.TokenMatcher;
import nl.jvdploeg.nfa.internal.NfaImpl;
import org.junit.Assert;

public class AnyTestSet extends AbstractTestSet {

  public AnyTestSet() {
  }

  @Override
  public void assertTokenMatcher(final TokenMatcher state) {

    Assert.assertFalse(state.matches(new String[] {}));
    Assert.assertTrue(state.matches(new String[] { "foo" }));
    Assert.assertFalse(state.matches(new String[] { "foo", "foo" }));
  }

  @Override
  public NfaImpl build() {

    return factory.any();
  }
}
