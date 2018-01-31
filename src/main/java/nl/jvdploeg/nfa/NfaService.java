// The author disclaims copyright to this source code.
package nl.jvdploeg.nfa;

import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

public abstract class NfaService {

  private static NfaService service;

  public static synchronized NfaService getInstance() {
    if (service == null) {
      final ServiceLoader<NfaService> loader = ServiceLoader.load(NfaService.class);
      final Iterator<NfaService> implementations = loader.iterator();
      // take first non-null implementation
      while (service == null && implementations.hasNext()) {
        service = implementations.next();
      }
      if (service == null) {
        throw new ServiceConfigurationError("Failed to load " + NfaService.class.getSimpleName() + ".");
      }
    }
    return service;
  }

  protected NfaService() {
  }

  public abstract NfaFactory<?> createNfaFactory();

  public abstract StateNetwork<?> createStateNetwork();
}
