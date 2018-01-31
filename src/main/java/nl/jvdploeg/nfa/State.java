// The author disclaims copyright to this source code.
package nl.jvdploeg.nfa;

import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface State<T extends State> extends TokenMatcher {

  /** Access to the live lists of transitions allowing any token. */
  List<T> getAnyTokenTransitions();

  /** Access to the live list of empty transitions. */
  List<T> getEmptyTransitions();

  /** Access to the live lists of transitions per token. */
  Map<String, List<T>> getTokenTransitions();

  /** Is end state. */
  boolean isEnd();
}
