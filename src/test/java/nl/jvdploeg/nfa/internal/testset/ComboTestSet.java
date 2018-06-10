// The author disclaims copyright to this source code.
package nl.jvdploeg.nfa.internal.testset;

import org.junit.Assert;

import nl.jvdploeg.nfa.Nfa;
import nl.jvdploeg.nfa.TokenMatcher;

public final class ComboTestSet extends AbstractTestSet {

  public ComboTestSet() {

  }

  @Override
  public void assertTokenMatcher(final TokenMatcher state) {

    Assert.assertTrue(state.matches(new String[] {}));
    Assert.assertTrue(state.matches(new String[] { "foo" }));
    Assert.assertTrue(state.matches(new String[] { "bar" }));
    Assert.assertTrue(state.matches(new String[] { "foo", "bar" }));
    Assert.assertFalse(state.matches(new String[] { "foo", "---" }));
    Assert.assertFalse(state.matches(new String[] { "---", "bar" }));
    Assert.assertTrue(state.matches(new String[] { "bar", "foo" }));
    Assert.assertFalse(state.matches(new String[] { "foo", "foo", "bar", "---" }));
    Assert.assertTrue(state.matches(new String[] { "foo", "foo", "bar", "foo" }));
  }

  @Override
  public Nfa<?> build() {

    return getFactory().sequence(getFactory().zeroOrMore(getFactory().or(getFactory().token("foo"), getFactory().token("bar"))),
        getFactory().empty());
  }
}
