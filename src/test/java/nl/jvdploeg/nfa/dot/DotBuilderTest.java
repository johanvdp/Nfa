// The author disclaims copyright to this source code.
package nl.jvdploeg.nfa.dot;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import nl.jvdploeg.nfa.Nfa;
import nl.jvdploeg.nfa.dot.expected.Expected;
import nl.jvdploeg.nfa.internal.DfaImpl;
import nl.jvdploeg.nfa.internal.NfaImpl;
import nl.jvdploeg.nfa.internal.testset.TestSet;
import nl.jvdploeg.nfa.internal.testset.TestSets;

public class DotBuilderTest {

  @Test
  public void generateWriteWithFactory() throws IOException {

    for (final TestSet testSet : TestSets.create()) {
      final Nfa<?> nfa = testSet.build();

      DotUtils.write(nfa.getEntry(), testSet.getFactory(), String.format("generated/nfa%s.dot", testSet.getClass().getSimpleName()));
    }
  }

  @Test
  public void generateWriteWithoutFactory() throws IOException {

    for (final TestSet testSet : TestSets.create()) {
      final NfaImpl nfa = (NfaImpl) testSet.build();
      final DfaImpl dfa = DfaImpl.createOptimized(nfa);

      DotUtils.write(dfa.getEntry(), String.format("generated/dfa%s.dot", testSet.getClass().getSimpleName()));
    }
  }

  @Test
  public void testAutoClosable() throws IOException {

    @SuppressWarnings("resource")
    // DotBuilder AutoCloseable TestWriter
    final TestWriter testWriter = new TestWriter();
    try (DotBuilder builder = new DotBuilder(testWriter, null, null)) {
      nothing();
    }

    Assert.assertTrue(testWriter.isClosed());
  }

  @Test
  public void testWriteWithFactory() throws IOException {

    for (final TestSet testSet : TestSets.create()) {
      final Nfa<?> nfa = testSet.build();

      Assert.assertEquals(Expected.getExpectedNfa(testSet), DotUtils.toDot(nfa.getEntry(), testSet.getFactory()));
    }
  }

  @Test
  public void testWriteWithoutFactory() throws IOException {

    for (final TestSet testSet : TestSets.create()) {
      final NfaImpl nfa = (NfaImpl) testSet.build();
      final DfaImpl dfa = DfaImpl.createOptimized(nfa);

      Assert.assertEquals(Expected.getExpectedDfa(testSet), DotUtils.toDot(dfa.getEntry()));
    }
  }

  private void nothing() {
    // avoid checkstyle errors
  }
}
