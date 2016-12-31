package nl.jvdploeg.nfa;

import java.util.List;

@SuppressWarnings("rawtypes")
public interface NfaFactory<T extends Nfa<? extends State>> {

  /**
   * Create an {@link Nfa} which matches any token.
   */
  T any();

  /**
   * Create an {@link Nfa} which matches no token.
   */
  T empty();

  /**
   * Access all {@link Nfa}s created by this factory.
   */
  List<T> getNfas();

  /**
   * Create an {@link TokenMatcher} implementing the given {@link Nfa}.
   */
  TokenMatcher optimize(final T nfa);

  /**
   * Create an {@link Nfa} that matches one of the provided {@link Nfa}s.
   */
  T or(final List<T> elements);

  /**
   * Create an {@link Nfa} that matches either provided {@link Nfa}s.
   */
  T or(final T one, final T other);

  /**
   * Create an {@link Nfa} that matches a sequence of multiple {@link Nfa}s.
   */
  T sequence(final List<T> elements);

  /**
   * Create an {@link Nfa} that matches a sequence of two {@link Nfa}.
   */
  T sequence(final T first, final T second);

  /**
   * Create an {@link Nfa} which matches the specified token.
   */
  T token(final String token);

  /**
   * Create an {@link Nfa} which matches zero or more repetitions of the given {@link Nfa}.
   */
  T zeroOrMore(final T nfa);
}
