// The author disclaims copyright to this source code.
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
  TokenMatcher optimize(T nfa);

  /**
   * Create an {@link Nfa} that matches one of the provided {@link Nfa}s.
   */
  T or(List<T> elements);

  /**
   * Create an {@link Nfa} that matches either provided {@link Nfa}s.
   */
  T or(T one, T other);

  /**
   * Create an {@link Nfa} that matches a sequence of multiple {@link Nfa}s.
   */
  T sequence(List<T> elements);

  /**
   * Create an {@link Nfa} that matches a sequence of two {@link Nfa}.
   */
  T sequence(T first, T second);

  /**
   * Create an {@link Nfa} which matches the specified token.
   */
  T token(String token);

  /**
   * Create an {@link Nfa} which matches zero or more repetitions of the given
   * {@link Nfa}.
   */
  T zeroOrMore(T nfa);
}
