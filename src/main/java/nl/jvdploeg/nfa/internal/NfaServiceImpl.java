// The author disclaims copyright to this source code.
package nl.jvdploeg.nfa.internal;

import nl.jvdploeg.nfa.NfaFactory;
import nl.jvdploeg.nfa.NfaService;
import nl.jvdploeg.nfa.StateNetwork;

public final class NfaServiceImpl extends NfaService {

  public NfaServiceImpl() {
  }

  @SuppressWarnings("unchecked")
  @Override
  public NfaFactory<?> createNfaFactory() {
    return new NfaFactoryImpl();
  }

  @Override
  public StateNetwork<?> createStateNetwork() {
    return new StateNetworkImpl();
  }
}
