// The author disclaims copyright to this source code.
package nl.jvdploeg.nfa.internal.testset;

import org.junit.Assert;

import nl.jvdploeg.nfa.Nfa;
import nl.jvdploeg.nfa.TokenMatcher;

public final class ZeroOrMoreTestSet extends AbstractTestSet {

  public ZeroOrMoreTestSet() {
  }

  @Override
  public void assertTokenMatcher(final TokenMatcher state) {

    Assert.assertTrue(state.matches(new String[] {}));
    Assert.assertTrue(state.matches(new String[] { "foo" }));
    Assert.assertTrue(state.matches(new String[] { "foo", "foo" }));
    Assert.assertFalse(state.matches(new String[] { "foo", "foo", "bar" }));
    Assert.assertFalse(state.matches(new String[] { "bar", "foo" }));
  }

  @Override
  public Nfa<?> build() {

    return getFactory().zeroOrMore(getFactory().token("foo"));
  }
}
