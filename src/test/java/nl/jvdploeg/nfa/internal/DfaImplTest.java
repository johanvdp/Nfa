// The author disclaims copyright to this source code.
package nl.jvdploeg.nfa.internal;

import java.io.IOException;

import org.junit.Test;

import nl.jvdploeg.nfa.dot.DotUtils;
import nl.jvdploeg.nfa.internal.testset.TestSet;
import nl.jvdploeg.nfa.internal.testset.TestSets;

public class DfaImplTest {

  @Test
  public void testCreateAndOptimize() throws IOException {

    for (final TestSet testSet : TestSets.create()) {
      final NfaImpl nfa = testSet.build();
      final DfaImpl dfa = DfaImpl.createUnoptimized(nfa);
      boolean moreOptimal;
      int iteration = 1;
      do {
        moreOptimal = dfa.optimize();
        if (moreOptimal) {

          DotUtils.write(dfa.getEntry(), String.format("generated/dfa%s%d.dot", testSet.getClass().getSimpleName(), Integer.valueOf(iteration)));

          testSet.assertTokenMatcher(dfa);
          iteration++;
        }
      } while (moreOptimal);
    }
  }

  @Test
  public void testCreateOptimized() throws IOException {

    for (final TestSet testSet : TestSets.create()) {
      final NfaImpl nfa = testSet.build();

      final DfaImpl dfa = DfaImpl.createOptimized(nfa);

      testSet.assertTokenMatcher(dfa);
    }
  }
}
