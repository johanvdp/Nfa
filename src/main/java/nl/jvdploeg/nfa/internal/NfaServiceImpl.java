package nl.jvdploeg.nfa.internal;

import nl.jvdploeg.nfa.NfaFactory;
import nl.jvdploeg.nfa.NfaService;
import nl.jvdploeg.nfa.StateNetwork;

@SuppressWarnings("rawtypes")
public class NfaServiceImpl extends NfaService {

  public NfaServiceImpl() {
  }

  @Override
  public NfaFactory createNfaFactory() {
    return new NfaFactoryImpl();
  }

  @Override
  public StateNetwork createStateNetwork() {
    return new StateNetworkImpl();
  }
}
