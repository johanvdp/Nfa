// The author disclaims copyright to this source code.
package nl.jvdploeg.nfa.internal.testset;

import nl.jvdploeg.nfa.Nfa;
import nl.jvdploeg.nfa.NfaFactory;
import nl.jvdploeg.nfa.NfaService;

public abstract class AbstractTestSet implements TestSet {

  private final NfaFactory<Nfa<?>> factory;

  protected AbstractTestSet() {
    factory = NfaService.getInstance().createNfaFactory();
  }

  @Override
  public final NfaFactory<Nfa<?>> getFactory() {
    return factory;
  }
}
