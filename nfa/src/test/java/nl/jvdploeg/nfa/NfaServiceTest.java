package nl.jvdploeg.nfa;

import org.junit.Assert;
import org.junit.Test;

import nl.jvdploeg.nfa.internal.NfaFactoryImpl;
import nl.jvdploeg.nfa.internal.NfaServiceImpl;
import nl.jvdploeg.nfa.internal.StateNetworkImpl;

public class NfaServiceTest {

    @Test
    public void testCreateNfaFactory() {

        Assert.assertTrue(NfaService.getInstance().createNfaFactory() instanceof NfaFactoryImpl);
    }

    @Test
    public void testCreateStateNetwork() {

        Assert.assertTrue(NfaService.getInstance().createStateNetwork() instanceof StateNetworkImpl);
    }

    @Test
    public void testGetInstance() {

        Assert.assertTrue(NfaService.getInstance() instanceof NfaServiceImpl);
    }
}
