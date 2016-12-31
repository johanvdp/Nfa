package nl.jvdploeg.nfa.internal;

import nl.jvdploeg.nfa.Nfa;
import nl.jvdploeg.nfa.State;
import nl.jvdploeg.nfa.TokenMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Deterministic finite automaton.
 */
public class DfaImpl implements TokenMatcher {

  private static final Logger LOG = LoggerFactory.getLogger(DfaImpl.class);

  /**
   * Create a {@link DfaImpl} from a {@link Nfa}.
   */
  public static DfaImpl createOptimized(final NfaImpl nfa) {
    LOG.debug("createOptimized");
    final StateImpl entry = nfa.getEntry();
    final DfaImpl dfa = new DfaImpl(entry);

    boolean moreOptimal;
    do {
      moreOptimal = dfa.optimize();
    }
    while (moreOptimal);

    return dfa;
  }

  /**
   * Create a {@link DfaImpl} from a {@link Nfa}.
   */
  public static DfaImpl createUnoptimized(final NfaImpl nfa) {
    LOG.debug("createUnoptimized");
    final StateImpl entry = nfa.getEntry();
    return new DfaImpl(entry);
  }

  /**
   * Collapse two states. State keep will remain.
   */
  private static void collapse(final StateNetworkImpl network, final StateImpl keep,
      final StateImpl remove) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("collapse keep {} remove {}", network.getStates().indexOf(keep),
          network.getStates().indexOf(remove));
    }

    // keep will be an end state if remove can reach an end state without
    // token consumption
    if (remove.isEndReachable()) {
      keep.setEnd(true);
    }

    // transitions from any state to remove will be redirected to keep
    // (except empty transitions from keep)
    for (final StateImpl state : network.getStates()) {
      collapseEmptyTransitions(state, keep, remove);
      collapseTokenTransitions(state, keep, remove);
      collapseAnyTokenTransitions(state, keep, remove);
    }

    // transitions from remove will be moved to keep (except empty
    // transitions to keep)
    moveEmptyTransitions(keep, remove);
    moveTokenTransitions(keep, remove);
    moveAnyTokenTransitions(keep, remove);

    // state remove will be deleted (disconnect all transitions)
    remove.clear();
  }

  /**
   * Any token transitions from any state to remove will be redirected to keep.
   */
  private static void collapseAnyTokenTransitions(final StateImpl state, final StateImpl keep,
      final StateImpl remove) {
    final List<StateImpl> anyTokenTransitions = new ArrayList<>(state.getAnyTokenTransitions());
    final List<StateImpl> newAnyTokenTransitions = new ArrayList<>();
    for (final StateImpl anyTokenState : anyTokenTransitions) {
      if (anyTokenState == remove) {
        state.removeAnyTokenTransition(anyTokenState);
        if (state != keep) {
          newAnyTokenTransitions.add(keep);
        }
      }
    }
    for (final StateImpl newAnyTokenTransition : newAnyTokenTransitions) {
      state.addAnyTokenTransition(newAnyTokenTransition);
    }
  }

  /**
   * Empty transitions from any state to remove will be redirected to keep. Except empty transitions
   * from keep.
   */
  private static void collapseEmptyTransitions(final StateImpl state, final StateImpl keep,
      final StateImpl remove) {
    final List<StateImpl> emptyTransitions = new ArrayList<>(state.getEmptyTransitions());
    final List<StateImpl> newEmptyTransitions = new ArrayList<>();
    for (final StateImpl emptyState : emptyTransitions) {
      if (emptyState == remove) {
        state.removeEmptyTransition(emptyState);
        if (state != keep) {
          newEmptyTransitions.add(keep);
        }
      }
    }
    for (final StateImpl newEmptyTransition : newEmptyTransitions) {
      state.addEmptyTransition(newEmptyTransition);
    }
  }

  /**
   * Token transitions from any state to remove will be redirected to keep.
   */
  private static void collapseTokenTransitions(final StateImpl state, final StateImpl keep,
      final StateImpl remove) {
    final List<StateImpl> newTokenTransitions = new ArrayList<>();
    final Map<String, List<StateImpl>> tokenTransitions = state.getTokenTransitions();
    for (final Entry<String, List<StateImpl>> transitions : tokenTransitions.entrySet()) {
      final String token = transitions.getKey();
      final List<StateImpl> states = new ArrayList<>(transitions.getValue());
      for (final StateImpl tokenState : states) {
        if (tokenState == remove) {
          state.removeTokenTransition(token, tokenState);
          newTokenTransitions.add(keep);
        }
      }
      for (final StateImpl newTokenTransition : newTokenTransitions) {
        state.addTokenTransition(token, newTokenTransition);
      }
    }
  }

  private static void moveAnyTokenTransitions(final StateImpl keep, final StateImpl remove) {
    final List<StateImpl> removeAnyTokenTransitions = remove.getAnyTokenTransitions();
    for (final StateImpl removeAnyTokenTransition : removeAnyTokenTransitions) {
      keep.addAnyTokenTransition(removeAnyTokenTransition);
    }
  }

  private static void moveEmptyTransitions(final StateImpl keep, final StateImpl remove) {
    final List<StateImpl> removeEmptyTransitions = remove.getEmptyTransitions();
    for (final StateImpl removeEmptyTransition : removeEmptyTransitions) {
      // move all but transitions to self
      if (removeEmptyTransition != keep) {
        keep.addEmptyTransition(removeEmptyTransition);
      }
    }
  }

  private static void moveTokenTransitions(final StateImpl keep, final StateImpl remove) {
    final Map<String, List<StateImpl>> removeTokenTransitions = remove.getTokenTransitions();
    for (final Entry<String, List<StateImpl>> entries : removeTokenTransitions.entrySet()) {
      final String token = entries.getKey();
      final List<StateImpl> transitions = entries.getValue();
      for (final StateImpl state : transitions) {
        keep.addTokenTransition(token, state);
      }
    }
  }

  private final StateImpl entry;

  private DfaImpl(final StateImpl entry) {
    this.entry = entry;
  }

  public State<StateImpl> getEntry() {
    return entry;
  }

  @Override
  public boolean matches(final String[] tokens) {
    return entry.matches(tokens);
  }

  /**
   * Scan the network and collapse two states that have only empty transitions to each other.
   *
   * @return True if more optimal.
   */
  public boolean optimize() {
    final StateNetworkImpl network = new StateNetworkImpl();
    network.scan(entry);
    final List<StateImpl> states = network.getStates();

    for (final StateImpl state : states) {
      final List<StateImpl> emptyTransitions = state.getEmptyTransitions();
      for (final StateImpl nextState : emptyTransitions) {
        if (!network.isTokenTransition(state, nextState)
            && !network.isTokenTransition(nextState, state)) {
          collapse(network, state, nextState);
          return true;
        }
      }
    }
    return false;
  }
}
