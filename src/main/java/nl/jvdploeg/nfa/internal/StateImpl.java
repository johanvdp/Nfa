package nl.jvdploeg.nfa.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.jvdploeg.nfa.State;

/**
 * A {@link State} has (outgoing only) transitions to other {@link State}s.<br>
 * <ul>
 * Two types of transitions
 * <li>Empty: Transition without consuming a token.
 * <li>Token: Transition and consume one token.
 * </ul>
 */
public final class StateImpl implements State<StateImpl> {

    private static final Logger LOG = LoggerFactory.getLogger(StateImpl.class);

    private static boolean isEndReachableFromState(final StateImpl state, final List<StateImpl> visited) {
        visited.add(state);
        if (state.isEnd()) {
            return true;
        }
        final List<StateImpl> emptyTransitions = state.getEmptyTransitions();
        for (final StateImpl nextState : emptyTransitions) {
            if (!visited.contains(nextState)) {
                if (isEndReachableFromState(nextState, visited)) {
                    return true;
                }
            }
        }
        return false;
    }

    /** List of transitions where no token is consumed. */
    private final List<StateImpl> emptyTransitions = new ArrayList<>();
    private final List<StateImpl> unmodifiableEmptyTransitions = Collections.unmodifiableList(emptyTransitions);
    /** List of transitions allowing any token (the token is consumed). */
    private final List<StateImpl> anyTokenTransitions = new ArrayList<>();
    private final List<StateImpl> unmodifiableAnyTokenTransitions = Collections.unmodifiableList(anyTokenTransitions);
    /** Marks this state as an end state. */
    private boolean end;

    /** List of transitions per token (the token is consumed). */
    private final Map<String, List<StateImpl>> tokenTransitions = new HashMap<>();
    private final Map<String, List<StateImpl>> unmodifiableTokenTransitions = Collections.unmodifiableMap(tokenTransitions);

    /**
     * Internal use only.
     *
     * @see NfaFactoryImpl.
     */
    StateImpl() {
    }

    /**
     * Add a transition from this state to next which allows and consumes any token.
     */
    public void addAnyTokenTransition(final StateImpl next) {
        if (!anyTokenTransitions.contains(next)) {
            anyTokenTransitions.add(next);
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("duplicate any token transition {}", next);
            }
        }
    }

    /**
     * Add an empty transition from this state to next that does not consume a token.
     */
    public void addEmptyTransition(final StateImpl next) {
        if (!emptyTransitions.contains(next)) {
            emptyTransitions.add(next);
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("duplicate empty transition {}", next);
            }
        }
    }

    /**
     * Add an token transition from this state to next which consumes the token.
     */
    public void addTokenTransition(final String token, final StateImpl next) {
        List<StateImpl> transitions = tokenTransitions.get(token);
        if (transitions == null) {
            transitions = new ArrayList<>();
            tokenTransitions.put(token, transitions);
        }
        if (!transitions.contains(next)) {
            transitions.add(next);
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("skip duplicate token transition {} {}", token, next);
            }
        }
    }

    /** Clear all transitions. */
    public void clear() {
        emptyTransitions.clear();
        anyTokenTransitions.clear();
        tokenTransitions.clear();
    }

    @Override
    public List<StateImpl> getAnyTokenTransitions() {
        return unmodifiableAnyTokenTransitions;
    }

    @Override
    public List<StateImpl> getEmptyTransitions() {
        return unmodifiableEmptyTransitions;
    }

    @Override
    public Map<String, List<StateImpl>> getTokenTransitions() {
        return unmodifiableTokenTransitions;
    }

    @Override
    public boolean isEnd() {
        return end;
    }

    /**
     * Check if end state is reachable using empty transitions from this state.
     */
    public boolean isEndReachable() {
        return isEndReachableFromState(this, new ArrayList<StateImpl>());
    }

    @Override
    public boolean matches(final String[] tokens) {
        // start recursion at the first token
        // start the visited state list to detect recursion
        return match(tokens, 0, new ArrayList<StateImpl>());
    }

    public void removeAnyTokenTransition(final StateImpl state) {
        final boolean removed = anyTokenTransitions.remove(state);
        if (!removed) {
            LOG.error("can not remove non existing any transition {}", state);
        }
    }

    public void removeEmptyTransition(final StateImpl state) {
        final boolean removed = emptyTransitions.remove(state);
        if (!removed) {
            LOG.error("can not remove non existing empty transition {}", state);
        }
    }

    public void removeTokenTransition(final String token, final StateImpl state) {
        final List<StateImpl> transitions = tokenTransitions.get(token);
        if (transitions == null) {
            LOG.error("can not remove non existing token transition ({}) {}", token, state);
        } else {
            final boolean removed = transitions.remove(state);
            if (!removed) {
                LOG.error("can not remove non existing token transition {} ({})", token, state);
            }
        }
    }

    /** Mark this state as an end state. */
    public void setEnd(final boolean endState) {
        end = endState;
    }

    /**
     * Get all transitions for the token, including the transitions for the 'any' token.
     */
    private List<StateImpl> getAllTokenTransitions(final String token) {
        final List<StateImpl> allTokenTransitions = new ArrayList<>();
        final List<StateImpl> matchingTokenTransitions = tokenTransitions.get(token);
        if (matchingTokenTransitions != null) {
            allTokenTransitions.addAll(matchingTokenTransitions);
        }
        if (anyTokenTransitions != null) {
            allTokenTransitions.addAll(anyTokenTransitions);
        }
        return allTokenTransitions;
    }

    /**
     * Start recursive match token by token. Avoid copying the token list by using an index to indicate the start of the list.
     */
    private boolean match(final String[] tokens, final int tokenIndex) {
        // start the visited state list to detect recursion
        return match(tokens, tokenIndex, new ArrayList<StateImpl>());
    }

    /**
     * Recursive match token by token. Avoid copying the token list by using an index to indicate the start of the list. Maintain the visited state
     * list to detect recursion.
     */
    private boolean match(final String[] tokens, final int tokenIndex, final ArrayList<StateImpl> visited) {

        // recursion detector
        if (visited.contains(this)) {
            // did not match the last time we visited
            return false;
        }
        visited.add(this);

        if (tokens.length - tokenIndex == 0) {

            // there are no more tokens
            if (end) {
                // that is ok if this is an end state
                return true;
            }

            // check if the next state is an end state (recursive)
            for (final StateImpl next : emptyTransitions) {
                if (next.match(new String[] {}, 0, visited)) {
                    return true;
                }
            }

            // no end state found
            return false;
        }

        // check the next token in the list
        final String token = tokens[tokenIndex];
        for (final StateImpl next : getAllTokenTransitions(token)) {
            if (next.match(tokens, tokenIndex + 1)) {
                return true;
            }
        }

        // check all empty transitions
        for (final StateImpl next : emptyTransitions) {
            if (next.match(tokens, tokenIndex, visited)) {
                return true;
            }
        }

        // nothing matches
        return false;
    }
}
