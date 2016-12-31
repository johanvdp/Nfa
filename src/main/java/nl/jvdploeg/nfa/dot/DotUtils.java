package nl.jvdploeg.nfa.dot;

import nl.jvdploeg.nfa.Nfa;
import nl.jvdploeg.nfa.NfaFactory;
import nl.jvdploeg.nfa.State;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

@SuppressWarnings("rawtypes")
public abstract class DotUtils {

  /**
   * Create dot representation of the {@link State} network.
   *
   * @param entry
   *          The entry of the network.
   * @throws IOException
   *           On error.
   */
  public static String toDot(final State entry) throws IOException {
    return toDot(entry, null);
  }

  /**
   * Create dot representation of the {@link State} network, including {@link Nfa} nodes.<br>
   *
   * @param entry
   *          The entry of the network.
   * @param factory
   *          The factory that created the network.
   * @throws IOException
   *           On error.
   *
   * @see http://www.graphviz.org
   * @see http://graphviz.org/Documentation/dotguide.pdf
   */
  public static String toDot(final State entry, final NfaFactory factory) throws IOException {
    try (final StringWriter writer = new StringWriter(); //
        final DotBuilder builder = new DotBuilder(writer, entry, factory)) {
      builder.write();
      return writer.toString();
    }
  }

  /**
   * Write dot representation of the {@link State} network, including {@link Nfa} nodes, to a file.
   *
   * @param entry
   *          The entry of the network.
   * @param factory
   *          The factory that created the network.
   */
  public static void write(final State entry, final NfaFactory factory, final String filename)
      throws IOException {
    try (final FileWriter writer = new FileWriter(filename); //
        final DotBuilder builder = new DotBuilder(writer, entry, factory)) {
      builder.write();
    }
  }

  /**
   * Write dot representation of the {@link State} network, including {@link Nfa} nodes, to a file.
   *
   * @param entry
   *          The entry of the network.
   * @param filename
   *          The file to write to.
   */
  public static void write(final State entry, final String filename) throws IOException {
    write(entry, null, filename);
  }
}
