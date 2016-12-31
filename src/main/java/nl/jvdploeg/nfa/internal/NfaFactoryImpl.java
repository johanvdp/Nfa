package nl.jvdploeg.nfa.internal;

import nl.jvdploeg.nfa.Nfa;
import nl.jvdploeg.nfa.NfaFactory;
import nl.jvdploeg.nfa.TokenMatcher;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class NfaFactoryImpl implements NfaFactory<NfaImpl> {

  private static final Logger LOG = LoggerFactory.getLogger(NfaFactoryImpl.class);

  /**
   * Remember all created {@link Nfa}s.
   */
  private final List<NfaImpl> nfas = new ArrayList<>();

  public NfaFactoryImpl() {
  }

  /**
   * Create an {@link Nfa} which matches any token.
   */
  @Override
  public final NfaImpl any() {
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
   * Create an {@link Nfa} which matches no token.
   */
  @Override
  public final NfaImpl empty() {
    // take shortcut, no need for empty transition between two states
    final StateImpl empty = new StateImpl();
    empty.setEnd(true);
    return create("()", empty, empty);
  }

  /**
   * Access all {@link Nfa}s created by this factory.
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
   * Create an {@link Nfa} that matches one of the provided {@link Nfa}s.
   */
  @Override
  public final NfaImpl or(final List<NfaImpl> elements) {
    // at least one element
    NfaImpl or = elements.get(0);
    for (int i = 1; i < elements.size(); i++) {
      or = or(or, elements.get(i));
    }
    return or;
  }

  /**
   * Create an {@link Nfa} that matches either provided {@link Nfa}s.
   */
  @Override
  public final NfaImpl or(final NfaImpl one, final NfaImpl other) {
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
   * Create an {@link Nfa} that matches a sequence of multiple {@link Nfa}s.
   */
  @Override
  public final NfaImpl sequence(final List<NfaImpl> elements) {
    // at least one element
    NfaImpl sequence = elements.get(0);
    for (int i = 1; i < elements.size(); i++) {
      sequence = sequence(sequence, elements.get(i));
    }
    return sequence;
  }

  /**
   * Create an {@link Nfa} that matches a sequence of two {@link Nfa}.
   */
  @Override
  public final NfaImpl sequence(final NfaImpl first, final NfaImpl second) {
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
    return create(String.format("(%1$s)(%2$s)", first.getLabel(), second.getLabel()), firstEntry,
        secondExit);
  }

  /**
   * Create an {@link Nfa} which matches the specified token.
   */
  @Override
  public final NfaImpl token(final String token) {
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
   * Create an {@link Nfa} which matches zero or more repetitions of the given {@link Nfa}.
   */
  @Override
  public final NfaImpl zeroOrMore(final NfaImpl nfa) {
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

  /**
   * Create an {@link Nfa} and remember it.
   */
  private NfaImpl create(final String label, final StateImpl entry, final StateImpl exit) {
    LOG.debug("create {}", label);
    final NfaImpl nfa = new NfaImpl(entry, exit);
    nfa.setLabel(label);
    nfas.add(nfa);
    return nfa;
  }
}
