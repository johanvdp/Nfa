package nl.jvdploeg.nfa.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nl.jvdploeg.nfa.State;
import nl.jvdploeg.nfa.StateNetwork;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Network of {@link State}s.
 */
public class StateNetworkImpl implements StateNetwork<StateImpl> {

  private static final Logger LOG = LoggerFactory.getLogger(StateNetworkImpl.class);

  /**
   * The collection of all {@link State}s.
   */
  private final List<StateImpl> network = new ArrayList<>();

  /**
   * Unmodifiable access to the network.
   */
  private final List<StateImpl> unmodifiableNetwork = Collections.unmodifiableList(network);

  /**
   * Token transitions.
   */
  private final Set<Pair<StateImpl, StateImpl>> tokenTransitions = new HashSet<>();

  public StateNetworkImpl() {
  }

  @Override
  public List<StateImpl> getStates() {
    return unmodifiableNetwork;
  }

  @Override
  public boolean isTokenTransition(final StateImpl entry, final StateImpl exit) {
    return tokenTransitions.contains(Pair.of(entry, exit));
  }

  @Override
  public void scan(final StateImpl entry) {
    network.clear();
    add(entry, network);
    if (LOG.isDebugEnabled()) {
      LOG.debug("scan {} states", network.size());
    }
  }

  /**
   * Recursively collect the given {@link State} and all reachable {@link State}s.
   */
  private void add(final StateImpl entry, final List<StateImpl> all) {
    // add the entry state itself
    all.add(entry);
    // collect all exit states for this entry state
    final List<StateImpl> exitStates = new ArrayList<>(entry.getEmptyTransitions());
    // token transitions
    final Map<String, List<StateImpl>> tokenStatesList = entry.getTokenTransitions();
    for (final List<StateImpl> tokenStates : tokenStatesList.values()) {
      for (final StateImpl tokenState : tokenStates) {
        exitStates.add(tokenState);
        tokenTransitions.add(Pair.of(entry, tokenState));
      }
    }
    // transitions allowing any token
    final List<StateImpl> anyTokenTransitions = entry.getAnyTokenTransitions();
    for (final StateImpl tokenState : anyTokenTransitions) {
      exitStates.add(tokenState);
      tokenTransitions.add(Pair.of(entry, tokenState));
    }
    // add all exit states to the collection
    for (final StateImpl exitState : exitStates) {
      // avoid against recursion loops
      if (!all.contains(exitState)) {
        add(exitState, all);
      }
    }
  }
}
