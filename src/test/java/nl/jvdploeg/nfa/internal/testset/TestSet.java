// The author disclaims copyright to this source code.
package nl.jvdploeg.nfa.internal.testset;

import nl.jvdploeg.nfa.Nfa;
import nl.jvdploeg.nfa.NfaFactory;
import nl.jvdploeg.nfa.TokenMatcher;

public interface TestSet {

  void assertTokenMatcher(TokenMatcher matcher);

  Nfa<?> build();

  NfaFactory<Nfa<?>> getFactory();
}
