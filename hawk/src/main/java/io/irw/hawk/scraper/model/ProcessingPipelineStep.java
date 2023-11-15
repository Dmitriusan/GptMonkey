package io.irw.hawk.scraper.model;

import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.traverse.TopologicalOrderIterator;

public interface ProcessingPipelineStep extends Comparable<ProcessingPipelineStep> {


  default List<Class<? extends ProcessingPipelineStep>> dependsOn() {
    return List.of();
  }

  default List<Class<? extends ProcessingPipelineStep>> dependencyFor() {
    return List.of();
  }

  @Override
  default int compareTo(@NotNull ProcessingPipelineStep other) {
    return this.getClass()
        .getName()
        .compareTo(other.getClass()
            .getName());
  }

  static List<ProcessingPipelineStep> linearExecutionGraphByDependencies(ProcessingPipelineStep... steps) {
    DirectedPseudograph<ProcessingPipelineStep, DefaultEdge> dependencyGraph = new DirectedPseudograph<>(
        DefaultEdge.class);
    Map<Class<? extends ProcessingPipelineStep>, ProcessingPipelineStep> classToStep = Arrays.stream(steps)
        .collect(toMap(ProcessingPipelineStep::getClass, step -> step));

    // To ensure the stable order of resolved dependency graph, so tests do not fail depending on implementation details
    List<ProcessingPipelineStep> alphabeticallySortedVertexes = Arrays.asList(steps);
    Collections.sort(alphabeticallySortedVertexes);

    for (ProcessingPipelineStep step : steps) {
      dependencyGraph.addVertex(step);
    }

    for (ProcessingPipelineStep step : steps) {
      for (Class<? extends ProcessingPipelineStep> dependencyClass : step.dependsOn()) {
        dependencyGraph.addEdge(step, getDependencyByClass(classToStep, dependencyClass));
      }
      for (Class<? extends ProcessingPipelineStep> dependentClass : step.dependencyFor()) {
        dependencyGraph.addEdge(getDependencyByClass(classToStep, dependentClass), step);
      }
    }

    CycleDetector<ProcessingPipelineStep, DefaultEdge> cycleDetector = new CycleDetector<>(dependencyGraph);
    boolean hasCycle = cycleDetector.detectCycles();
    if (hasCycle) {
      throw new IllegalStateException(
          "Dependency graph has cycles. Participating vertexes: " + cycleDetector.findCycles());
    }

    TopologicalOrderIterator<ProcessingPipelineStep, DefaultEdge> orderIterator = new TopologicalOrderIterator<>(
        dependencyGraph);
    List<ProcessingPipelineStep> pathInDependencyTree = new ArrayList<>();
    while (orderIterator.hasNext()) { // Build a reversed path
      pathInDependencyTree.add(0, orderIterator.next());
    }
    return pathInDependencyTree;
  }

  private static ProcessingPipelineStep getDependencyByClass(
      Map<Class<? extends ProcessingPipelineStep>, ProcessingPipelineStep> classToStep,
      Class<? extends ProcessingPipelineStep> dependencyClass) {
    if (! classToStep.containsKey(dependencyClass)) {
      throw new IllegalArgumentException("Unknown dependency class: " + dependencyClass.getName() + ".");
    }
    return classToStep.get(dependencyClass);
  }
}
