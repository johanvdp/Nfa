// The author disclaims copyright to this source code.
package nl.jvdploeg.nfa.internal;

import java.io.IOException;

import org.junit.Test;

import nl.jvdploeg.nfa.Nfa;
import nl.jvdploeg.nfa.NfaFactory;
import nl.jvdploeg.nfa.TokenMatcher;
import nl.jvdploeg.nfa.internal.testset.TestSet;
import nl.jvdploeg.nfa.internal.testset.TestSets;

public class NfaFactoryImplTest {

  @Test
  public void testOptimize() throws IOException {

    for (final TestSet testSet : TestSets.create()) {
      final Nfa<?> nfa = testSet.build();
      final NfaFactory<Nfa<?>> factory = testSet.getFactory();

      final TokenMatcher matcher = factory.optimize(nfa);

      testSet.assertTokenMatcher(matcher);
    }
  }
}
