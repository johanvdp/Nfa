// The author disclaims copyright to this source code.
package nl.jvdploeg.nfa.internal.testset;

import nl.jvdploeg.nfa.NfaService;
import nl.jvdploeg.nfa.internal.NfaFactoryImpl;

public abstract class AbstractTestSet implements TestSet {

  private final NfaFactoryImpl factory;

  protected AbstractTestSet() {
    factory = (NfaFactoryImpl) NfaService.getInstance().createNfaFactory();
  }

  @Override
  public final NfaFactoryImpl getFactory() {
    return factory;
  }
}
