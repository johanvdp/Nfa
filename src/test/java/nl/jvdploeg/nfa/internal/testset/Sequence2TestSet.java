// The author disclaims copyright to this source code.
package nl.jvdploeg.nfa.internal.testset;

import org.junit.Assert;

import nl.jvdploeg.nfa.Nfa;
import nl.jvdploeg.nfa.TokenMatcher;

public final class Sequence2TestSet extends AbstractTestSet {

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
  public Nfa<?> build() {

    return getFactory().sequence(getFactory().token("foo"), getFactory().token("bar"));
  }
}
