package nl.jvdploeg.nfa;

import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

@SuppressWarnings("rawtypes")
public abstract class NfaService {

    private static NfaService service;

    public static final synchronized NfaService getInstance() {
        if (service == null) {
            final ServiceLoader<NfaService> loader = ServiceLoader.load(NfaService.class);
            final Iterator<NfaService> implementations = loader.iterator();
            while (implementations.hasNext()) {
                service = implementations.next();
            }
            if (service == null) {
                throw new ServiceConfigurationError("Failed to load NfaFactory service.");
            }
        }
        return service;
    }

    protected NfaService() {
    }

    public abstract NfaFactory createNfaFactory();

    public abstract StateNetwork createStateNetwork();
}
