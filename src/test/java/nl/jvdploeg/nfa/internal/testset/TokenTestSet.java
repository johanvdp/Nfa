// The author disclaims copyright to this source code.
package nl.jvdploeg.nfa.internal.testset;

import org.junit.Assert;

import nl.jvdploeg.nfa.TokenMatcher;
import nl.jvdploeg.nfa.internal.NfaImpl;

public final class TokenTestSet extends AbstractTestSet {

  public TokenTestSet() {
  }

  @Override
  public void assertTokenMatcher(final TokenMatcher state) {

    Assert.assertFalse(state.matches(new String[] {}));
    Assert.assertTrue(state.matches(new String[] { "foo" }));
    Assert.assertFalse(state.matches(new String[] { "bar" }));
    Assert.assertFalse(state.matches(new String[] { "foo", "bar" }));
    Assert.assertFalse(state.matches(new String[] { "bar", "foo" }));
  }

  @Override
  public NfaImpl build() {

    return getFactory().token("foo");
  }
}
