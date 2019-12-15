package net.kamradtfamily.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

public class FlowListImplementation<T> implements Flow<T> {
  public static ExecutorService executor = Executors.newCachedThreadPool();

  List<FlowStep> transforms = new ArrayList<>();

  public void process(T message) {
    try {
      executor.submit(()-> {
        Object [] container = new Object[1];
        container[0] = message;
        transforms.forEach(t -> processStep(t, container));
      });
    } catch (ShortCircuitException e) {
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
    }
  }

  private void processStep(FlowStep step, Object [] container) {
    step.transform().ifPresent(tr -> container[0] = tr.transform(container[0]));
    step.flow().ifPresent(f -> f.process(container[0]));
    step.predicate().ifPresent(p -> { if(!p.test(container[0])) throw new ShortCircuitException(); });
    step.sink().ifPresent(s -> { s.process(container[0]); });
  }

  public <O> Flow<O> addTransform(Transform<T, O> t) {
    transforms.add(ImmutableFlowStep.builder()
      .transform(t)
      .build());
    return (Flow<O>)this;
  };

  public Flow<T> addFlow(Flow<T> f) {
    transforms.add(ImmutableFlowStep.builder()
        .flow(f)
        .build());
    return (Flow<T>)this;
  }

  public Flow<T> addFilter(Predicate<T> p) {
    transforms.add(ImmutableFlowStep.builder()
        .predicate(p)
        .build());
    return this;
  }

  public void addSink(Sink<T> s) {
    transforms.add(ImmutableFlowStep.builder()
        .sink(s)
        .build());
  }
}
