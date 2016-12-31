package nl.jvdploeg.nfa.internal;

import nl.jvdploeg.nfa.TokenMatcher;
import nl.jvdploeg.nfa.internal.testset.TestSet;
import nl.jvdploeg.nfa.internal.testset.TestSets;
import org.junit.Test;

import java.io.IOException;

public class NfaFactoryImplTest {

  @Test
  public void testOptimize() throws IOException {

    for (final TestSet testSet : TestSets.create()) {
      final NfaImpl nfa = testSet.build();
      final NfaFactoryImpl factory = testSet.getFactory();

      final TokenMatcher matcher = factory.optimize(nfa);

      testSet.assertTokenMatcher(matcher);
    }
  }
}
