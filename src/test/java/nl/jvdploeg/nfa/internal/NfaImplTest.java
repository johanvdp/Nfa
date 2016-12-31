package nl.jvdploeg.nfa.internal;

import nl.jvdploeg.nfa.internal.testset.TestSet;
import nl.jvdploeg.nfa.internal.testset.TestSets;
import org.junit.Test;

import java.io.IOException;

public class NfaImplTest {

  @Test
  public void testTokenMatcher() throws IOException {

    for (final TestSet testSet : TestSets.create()) {
      final NfaImpl nfa = testSet.build();

      testSet.assertTokenMatcher(nfa);
    }
  }
}
