package net.kamradtfamily.flow;

public interface Transform<I, O> {
  O transform(I i);
}
