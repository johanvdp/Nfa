// The author disclaims copyright to this source code.
package nl.jvdploeg.nfa.internal;

import nl.jvdploeg.nfa.Nfa;

/**
 * Nondeterministic finite automaton.
 */
public final class NfaImpl implements Nfa<StateImpl> {

  private final StateImpl entry;
  private final StateImpl exit;
  /** Optional label attached to this automaton. */
  private String label;

  /**
   * Internal use only.
   *
   * @see NfaFactoryImpl.
   */
  NfaImpl(final StateImpl entry, final StateImpl exit) {
    this.entry = entry;
    this.exit = exit;
  }

  @Override
  public StateImpl getEntry() {
    return entry;
  }

  @Override
  public StateImpl getExit() {
    return exit;
  }

  @Override
  public String getLabel() {
    return label;
  }

  @Override
  public boolean matches(final String[] tokens) {
    return entry.matches(tokens);
  }

  @Override
  public void setLabel(final String label) {
    this.label = label;
  }
}
