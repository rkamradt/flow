package net.kamradtfamily.flow;

public interface Sink<T> {
  void process(T t);
}
