// The author disclaims copyright to this source code.
package nl.jvdploeg.nfa.internal.testset;

import org.junit.Assert;

import nl.jvdploeg.nfa.Nfa;
import nl.jvdploeg.nfa.TokenMatcher;

public final class ZeroOrMoreAnyFooTestSet extends AbstractTestSet {

  public ZeroOrMoreAnyFooTestSet() {
  }

  @Override
  public void assertTokenMatcher(final TokenMatcher state) {

    Assert.assertFalse(state.matches(new String[] {}));
    Assert.assertTrue(state.matches(new String[] { "foo" }));
    Assert.assertTrue(state.matches(new String[] { "foo", "foo" }));
    Assert.assertFalse(state.matches(new String[] { "foo", "foo", "bar" }));
    Assert.assertTrue(state.matches(new String[] { "bar", "foo" }));
  }

  @Override
  public Nfa<?> build() {

    return getFactory().sequence(getFactory().zeroOrMore(getFactory().any()), getFactory().token("foo"));
  }
}
