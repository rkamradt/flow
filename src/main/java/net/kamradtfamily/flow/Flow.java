package net.kamradtfamily.flow;

import java.util.function.Predicate;

public interface Flow<T> {
  public void process(T message);
  public void addSink(Sink<T> s);
  public <O> Flow<O> addTransform(Transform<T, O> o);
  public Flow<T> addFlow(Flow<T> f); // add a side flow, but continue the main flow
  public Flow<T> addFilter(Predicate<T> p);
}
