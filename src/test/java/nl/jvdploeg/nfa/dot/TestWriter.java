package nl.jvdploeg.nfa.dot;

import java.io.IOException;
import java.io.Writer;

public class TestWriter extends Writer {

  private boolean closed;

  public TestWriter() {
  }

  @Override
  public void close() throws IOException {
    closed = true;
  }

  @Override
  public void flush() throws IOException {
  }

  public boolean isClosed() {
    return closed;
  }

  @Override
  public void write(final char[] cbuf, final int off, final int len) throws IOException {
  }
}
