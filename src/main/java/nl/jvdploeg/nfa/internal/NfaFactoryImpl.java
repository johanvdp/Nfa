// The author disclaims copyright to this source code.
package nl.jvdploeg.nfa.internal;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.jvdploeg.nfa.NfaFactory;
import nl.jvdploeg.nfa.TokenMatcher;

public final class NfaFactoryImpl implements NfaFactory<NfaImpl> {

  private static final Logger LOG = LoggerFactory.getLogger(NfaFactoryImpl.class);

  /** Remember all created Nfas. */
  private final List<NfaImpl> nfas = new ArrayList<>();

  public NfaFactoryImpl() {
  }

  /**
   * Create an Nfa which matches any token.
   */
  @Override
  public NfaImpl any() {
    // new states
    final StateImpl entry = new StateImpl();
    final StateImpl exit = new StateImpl();
    // connect states
    entry.addAnyTokenTransition(exit);
    // mark end state
    exit.setEnd(true);
    return create("(.*)", entry, exit);
  }

  /**
   * Create an Nfa which matches no token.
   */
  @Override
  public NfaImpl empty() {
    // take shortcut, no need for empty transition between two states
    final StateImpl empty = new StateImpl();
    empty.setEnd(true);
    return create("()", empty, empty);
  }

  /**
   * Access all Nfas created by this factory.
   */
  @Override
  public List<NfaImpl> getNfas() {
    return nfas;
  }

  @Override
  public TokenMatcher optimize(final NfaImpl nfa) {
    LOG.debug("optimize {}", nfa.getLabel());
    final DfaImpl dfa = DfaImpl.createOptimized(nfa);
    return dfa;
  }

  /**
   * Create an Nfa that matches one of the provided Nfas.
   */
  @Override
  public NfaImpl or(final List<NfaImpl> elements) {
    // at least one element
    NfaImpl or = elements.get(0);
    for (int i = 1; i < elements.size(); i++) {
      or = or(or, elements.get(i));
    }
    return or;
  }

  /**
   * Create an Nfa that matches either provided Nfas.
   */
  @Override
  public NfaImpl or(final NfaImpl one, final NfaImpl other) {
    // existing states
    final StateImpl oneEntry = one.getEntry();
    final StateImpl oneExit = one.getExit();
    final StateImpl otherExit = other.getExit();
    final StateImpl otherEntry = other.getEntry();
    // new states
    final StateImpl entry = new StateImpl();
    final StateImpl exit = new StateImpl();
    // connect states
    entry.addEmptyTransition(oneEntry);
    entry.addEmptyTransition(otherEntry);
    oneExit.addEmptyTransition(exit);
    otherExit.addEmptyTransition(exit);
    // mark end state
    oneExit.setEnd(false);
    otherExit.setEnd(false);
    exit.setEnd(true);
    return create(String.format("(%1$s)|(%2$s)", one.getLabel(), other.getLabel()), entry, exit);
  }

  /**
   * Create an Nfa that matches a sequence of multiple Nfas.
   */
  @Override
  public NfaImpl sequence(final List<NfaImpl> elements) {
    // at least one element
    NfaImpl sequence = elements.get(0);
    for (int i = 1; i < elements.size(); i++) {
      sequence = sequence(sequence, elements.get(i));
    }
    return sequence;
  }

  /**
   * Create an Nfa that matches a sequence of two Nfa.
   */
  @Override
  public NfaImpl sequence(final NfaImpl first, final NfaImpl second) {
    // existing states
    final StateImpl firstEntry = first.getEntry();
    final StateImpl firstExit = first.getExit();
    final StateImpl secondEntry = second.getEntry();
    final StateImpl secondExit = second.getExit();
    // connect states
    firstExit.addEmptyTransition(secondEntry);
    // mark end state
    firstExit.setEnd(false);
    secondExit.setEnd(true);
    return create(String.format("(%1$s)(%2$s)", first.getLabel(), second.getLabel()), firstEntry, secondExit);
  }

  /**
   * Create an Nfa which matches the specified token.
   */
  @Override
  public NfaImpl token(final String token) {
    // new states
    final StateImpl entry = new StateImpl();
    final StateImpl exit = new StateImpl();
    // connect states
    entry.addTokenTransition(token, exit);
    // mark end state
    exit.setEnd(true);
    return create(String.format("(%1$s)", token), entry, exit);
  }

  /**
   * Create an Nfa which matches zero or more repetitions of the given Nfa.
   */
  @Override
  public NfaImpl zeroOrMore(final NfaImpl nfa) {
    // do not generate new states but take a shortcut
    // directly connect entry and exit state
    final String label = nfa.getLabel();
    // express the modification on the label
    if (!StringUtils.isEmpty(label)) {
      nfa.setLabel(String.format("(%1$s)*", label));
    }
    // connect states
    nfa.getExit().addEmptyTransition(nfa.getEntry());
    nfa.getEntry().addEmptyTransition(nfa.getExit());
    // no change in end state
    return nfa;
  }

  /** Create an Nfa and remember it. */
  private NfaImpl create(final String label, final StateImpl entry, final StateImpl exit) {
    LOG.debug("create {}", label);
    final NfaImpl nfa = new NfaImpl(entry, exit);
    nfa.setLabel(label);
    nfas.add(nfa);
    return nfa;
  }
}
