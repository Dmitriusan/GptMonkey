package io.irw.hawk.scraper.model;

import static io.irw.hawk.scraper.model.ProcessingPipelineStep.sortOutDependencies;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.List;
import org.junit.jupiter.api.Test;

class ProcessingPipelineStepTest {

  @Test
  void testShouldSortOutDependencies() {
    ProcessingPipelineStep[] processingPipelineSteps = {new PipelineStepA(), new PipelineStepB(), new PipelineStepC(),
        new PipelineStepD(), new PipelineStepE(), new PipelineStepF()};

    List<ProcessingPipelineStep> sortedDependencies = sortOutDependencies(processingPipelineSteps);
    assertThat(sortedDependencies).hasSize(processingPipelineSteps.length);
    assertThat(sortedDependencies.stream()
        .map(processingPipelineStep -> processingPipelineStep.getClass())
        .toArray(Class<?>[]::new))
        .containsExactly(PipelineStepF.class, PipelineStepD.class, PipelineStepE.class, PipelineStepB.class,
            PipelineStepA.class, PipelineStepC.class);
  }

  @Test
  void testShouldSortOutDependenciesWithMultiplePaths() {
    ProcessingPipelineStep[] processingPipelineSteps = {new PipelineStepA(), new PipelineStepB(), new PipelineStepC(),
        new PipelineStepD(), new PipelineStepE(), new PipelineStepF(), new PipelineStepG()};

    List<ProcessingPipelineStep> sortedDependencies = sortOutDependencies(processingPipelineSteps);
    assertThat(sortedDependencies).hasSize(processingPipelineSteps.length);
    assertThat(sortedDependencies.stream()
        .map(processingPipelineStep -> processingPipelineStep.getClass())
        .toArray(Class<?>[]::new))
        .containsExactly(PipelineStepA.class, PipelineStepG.class, PipelineStepF.class, PipelineStepD.class,
            PipelineStepE.class, PipelineStepB.class, PipelineStepC.class);
  }

  @Test
  void testShouldFailOnCircularDependencies() {
    ProcessingPipelineStep[] processingPipelineSteps = {new PipelineStepA(), new PipelineStepB(),
        new PipelineStepC(), new PipelineStepD(), new PipelineStepE(), new PipelineStepF(), new PipelineStepH()};

    assertThatExceptionOfType(IllegalStateException.class)
        .isThrownBy(() -> sortOutDependencies(processingPipelineSteps));
  }

  @Test
  void testShouldFailOnMutualDependsOn() {
    ProcessingPipelineStep[] processingPipelineSteps = {new PipelineStepMutualDependsOnA(),
        new PipelineStepMutualDependsOnB()};

    assertThatExceptionOfType(IllegalStateException.class)
        .isThrownBy(() -> sortOutDependencies(processingPipelineSteps));
  }

  @Test
  void testShouldFailOnMutualDependencyFor() {
    ProcessingPipelineStep[] processingPipelineSteps = {new PipelineStepMutualDependencyForA(),
        new PipelineStepMutualDependencyForB()};

    assertThatExceptionOfType(IllegalStateException.class)
        .isThrownBy(() -> sortOutDependencies(processingPipelineSteps));
  }

  private class PipelineStepA implements ProcessingPipelineStep {

  }

  private class PipelineStepB implements ProcessingPipelineStep {
    @Override
    public List<Class<? extends ProcessingPipelineStep>> dependsOn() {
      return List.of(PipelineStepD.class);
    }
  }

  private class PipelineStepC implements ProcessingPipelineStep {

    @Override
    public List<Class<? extends ProcessingPipelineStep>> dependsOn() {
      return List.of(PipelineStepA.class, PipelineStepB.class);
    }
  }

  private class PipelineStepD implements ProcessingPipelineStep {
    @Override
    public List<Class<? extends ProcessingPipelineStep>> dependsOn() {
      return List.of(PipelineStepF.class);
    }
  }

  private class PipelineStepE implements ProcessingPipelineStep {

    @Override
    public List<Class<? extends ProcessingPipelineStep>> dependencyFor() {
      return List.of(PipelineStepC.class);
    }
  }

  private class PipelineStepF implements ProcessingPipelineStep {

  }

  private class PipelineStepG implements ProcessingPipelineStep {
    @Override
    public List<Class<? extends ProcessingPipelineStep>> dependsOn() {
      return List.of(PipelineStepA.class);
    }

    @Override
    public List<Class<? extends ProcessingPipelineStep>> dependencyFor() {
      return List.of(PipelineStepF.class);
    }
  }

  private class PipelineStepH implements ProcessingPipelineStep {
    @Override
    public List<Class<? extends ProcessingPipelineStep>> dependsOn() {
      return List.of(PipelineStepC.class);
    }

    @Override
    public List<Class<? extends ProcessingPipelineStep>> dependencyFor() {
      return List.of(PipelineStepF.class);
    }
  }

  private class PipelineStepMutualDependsOnA implements ProcessingPipelineStep {

    @Override
    public List<Class<? extends ProcessingPipelineStep>> dependsOn() {
      return List.of(PipelineStepMutualDependsOnB.class);
    }
  }

  private class PipelineStepMutualDependsOnB implements ProcessingPipelineStep {

    @Override
    public List<Class<? extends ProcessingPipelineStep>> dependsOn() {
      return List.of(PipelineStepMutualDependsOnA.class);
    }
  }

  private class PipelineStepMutualDependencyForA implements ProcessingPipelineStep {

    @Override
    public List<Class<? extends ProcessingPipelineStep>> dependencyFor() {
      return List.of(PipelineStepMutualDependencyForB.class);
    }
  }

  private class PipelineStepMutualDependencyForB implements ProcessingPipelineStep {

    @Override
    public List<Class<? extends ProcessingPipelineStep>> dependencyFor() {
      return List.of(PipelineStepMutualDependencyForA.class);
    }
  }

}