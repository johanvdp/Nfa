package nl.jvdploeg.nfa;

@SuppressWarnings("rawtypes")
public interface Nfa<T extends State> extends TokenMatcher {

  T getEntry();

  T getExit();

  String getLabel();

  void setLabel(String label);
}
