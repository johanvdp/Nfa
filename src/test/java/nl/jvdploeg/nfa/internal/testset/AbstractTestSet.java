package nl.jvdploeg.nfa.internal.testset;

import nl.jvdploeg.nfa.NfaService;
import nl.jvdploeg.nfa.internal.NfaFactoryImpl;

public abstract class AbstractTestSet implements TestSet {

  protected final NfaFactoryImpl factory;

  public AbstractTestSet() {
    factory = (NfaFactoryImpl) NfaService.getInstance().createNfaFactory();
  }

  @Override
  public NfaFactoryImpl getFactory() {
    return factory;
  }
}
