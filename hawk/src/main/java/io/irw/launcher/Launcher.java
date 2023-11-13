package io.irw.launcher;

import io.irw.hawk.HawkApp;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Launcher {

  public static void main(String[] args) {
    try (ExecutorService executorService = Executors.newCachedThreadPool()) {
      // Start the Spring Boot application in a separate thread
      Future<ConfigurableApplicationContext> future = executorService.submit(() -> {
        return SpringApplication.run(HawkApp.class, args);
      });

      // Get the application context from the future and manage its lifecycle
      ConfigurableApplicationContext hawkAppContext = future.get();
      // Perform any necessary operations with hawkAppContext

      // Close the application context gracefully
      Runtime.getRuntime()
          .addShutdownHook(new Thread(hawkAppContext::close));
    } catch (InterruptedException e) {
      Thread.currentThread()
          .interrupt(); // Preserve interrupt status
      throw new RuntimeException("Thread interrupted", e);
    } catch (ExecutionException e) {
      throw new RuntimeException("Execution exception in application startup", e);
    }
  }
}

