// The author disclaims copyright to this source code.
package nl.jvdploeg.nfa.internal.testset;

import java.util.Arrays;

import org.junit.Assert;

import nl.jvdploeg.nfa.TokenMatcher;
import nl.jvdploeg.nfa.internal.NfaImpl;

public final class FooZeroOrMoreAnyFooTestSet extends AbstractTestSet {

  public FooZeroOrMoreAnyFooTestSet() {
  }

  @Override
  public void assertTokenMatcher(final TokenMatcher state) {

    Assert.assertFalse(state.matches(new String[] {}));
    Assert.assertFalse(state.matches(new String[] { "foo" }));
    Assert.assertTrue(state.matches(new String[] { "foo", "foo" }));
    Assert.assertTrue(state.matches(new String[] { "foo", "---", "foo" }));
    Assert.assertFalse(state.matches(new String[] { "foo", "---" }));
    Assert.assertFalse(state.matches(new String[] { "foo", "foo", "---" }));
  }

  @Override
  public NfaImpl build() {

    return getFactory().sequence(Arrays.asList(getFactory().token("foo"), getFactory().zeroOrMore(getFactory().any()), getFactory().token("foo")));
  }
}
