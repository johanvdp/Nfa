// The author disclaims copyright to this source code.
package nl.jvdploeg.nfa.internal;

import java.io.IOException;

import org.junit.Test;

import nl.jvdploeg.nfa.internal.testset.TestSet;
import nl.jvdploeg.nfa.internal.testset.TestSets;

public class NfaImplTest {

  @Test
  public void testTokenMatcher() throws IOException {

    for (final TestSet testSet : TestSets.create()) {
      final NfaImpl nfa = testSet.build();

      testSet.assertTokenMatcher(nfa);
    }
  }
}
