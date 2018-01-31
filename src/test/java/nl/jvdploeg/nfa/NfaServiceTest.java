// The author disclaims copyright to this source code.
package nl.jvdploeg.nfa;

import org.junit.Assert;
import org.junit.Test;

import nl.jvdploeg.nfa.internal.NfaServiceImpl;

public class NfaServiceTest {

  @Test
  public void testCreateNfaFactory() {

    Assert.assertNotNull(NfaService.getInstance().createNfaFactory());
  }

  @Test
  public void testCreateStateNetwork() {

    Assert.assertNotNull(NfaService.getInstance().createStateNetwork());
  }

  @Test
  public void testGetInstance() {

    Assert.assertTrue(NfaService.getInstance() instanceof NfaServiceImpl);
  }
}
