// The author disclaims copyright to this source code.
package nl.jvdploeg.nfa;

import java.util.List;

@SuppressWarnings("rawtypes")
public interface StateNetwork<T extends State> {

  /** View all {@link State}s in the network. */
  List<T> getStates();

  /**
   * Check if there is a transition consuming a token between two
   * {@link State}s.
   */
  boolean isTokenTransition(T state, T nextState);

  /** Scan the network starting at the entry {@link State}. */
  void scan(T entry);
}
