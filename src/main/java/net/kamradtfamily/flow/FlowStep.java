package net.kamradtfamily.flow;

import java.util.Optional;
import java.util.function.Predicate;
import org.immutables.value.Value;

@Value.Immutable
public interface FlowStep {
  Optional<Flow> flow();
  Optional<Predicate> predicate();
  Optional<Transform> transform();
  Optional<Sink> sink();
}
