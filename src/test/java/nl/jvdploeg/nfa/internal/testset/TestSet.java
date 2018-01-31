// The author disclaims copyright to this source code.
package nl.jvdploeg.nfa.internal.testset;

import nl.jvdploeg.nfa.TokenMatcher;
import nl.jvdploeg.nfa.internal.NfaFactoryImpl;
import nl.jvdploeg.nfa.internal.NfaImpl;

public interface TestSet {

  void assertTokenMatcher(TokenMatcher matcher);

  NfaImpl build();

  NfaFactoryImpl getFactory();
}
