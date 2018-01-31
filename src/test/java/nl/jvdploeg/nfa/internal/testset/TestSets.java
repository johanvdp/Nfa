// The author disclaims copyright to this source code.
package nl.jvdploeg.nfa.internal.testset;

public abstract class TestSets {

  public static TestSet[] create() {
    return new TestSet[] { //
        new AnyTestSet(), //
        new ComboTestSet(), //
        new EmptyTestSet(), //
        new FooZeroOrMoreAnyFooTestSet(), //
        new Or2TestSet(), //
        new Or3TestSet(), //
        new Or2AnyTestSet(), //
        new Sequence2TestSet(), //
        new Sequence3TestSet(), //
        new TokenTestSet(), //
        new ZeroOrMoreAnyFooTestSet(), //
        new ZeroOrMoreTestSet() };
  }
}
