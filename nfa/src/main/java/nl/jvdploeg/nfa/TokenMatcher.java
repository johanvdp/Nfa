package nl.jvdploeg.nfa;

/** Capable of matching against list of tokens. */
public interface TokenMatcher {

    /** Check if matching against list of tokens. */
    boolean matches(final String[] tokens);
}
