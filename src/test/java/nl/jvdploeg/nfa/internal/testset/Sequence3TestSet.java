// The author disclaims copyright to this source code.
package nl.jvdploeg.nfa.internal.testset;

import java.util.Arrays;

import org.junit.Assert;

import nl.jvdploeg.nfa.TokenMatcher;
import nl.jvdploeg.nfa.internal.NfaImpl;

public final class Sequence3TestSet extends AbstractTestSet {

  public Sequence3TestSet() {
  }

  @Override
  public void assertTokenMatcher(final TokenMatcher state) {

    Assert.assertFalse(state.matches(new String[] {}));
    Assert.assertFalse(state.matches(new String[] { "foo" }));
    Assert.assertFalse(state.matches(new String[] { "bar" }));
    Assert.assertFalse(state.matches(new String[] { "baz" }));
    Assert.assertTrue(state.matches(new String[] { "foo", "bar", "baz" }));
    Assert.assertFalse(state.matches(new String[] { "foo", "bar", "baz", "---" }));
    Assert.assertFalse(state.matches(new String[] { "---", "foo", "bar", "baz" }));
  }

  @Override
  public NfaImpl build() {

    return getFactory().sequence(Arrays.asList(getFactory().token("foo"), getFactory().token("bar"), getFactory().token("baz")));
  }
}
