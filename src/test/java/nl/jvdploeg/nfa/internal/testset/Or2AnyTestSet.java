// The author disclaims copyright to this source code.
package nl.jvdploeg.nfa.internal.testset;

import org.junit.Assert;

import nl.jvdploeg.nfa.Nfa;
import nl.jvdploeg.nfa.TokenMatcher;

public final class Or2AnyTestSet extends AbstractTestSet {

  public Or2AnyTestSet() {
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
  public Nfa<?> build() {

    return getFactory().or(getFactory().any(), getFactory().any());
  }
}
