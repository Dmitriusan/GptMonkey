package io.irw.launcher;

import io.irw.hawk.HawkApp;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Launcher {

  public static void main(String[] args) {
    try (ExecutorService executorService = Executors.newCachedThreadPool()) {

      // Start the first Spring Boot application in a separate thread
      executorService.submit(() -> {
        ConfigurableApplicationContext hawkAppContext = SpringApplication.run(HawkApp.class, args);
        hawkAppContext.close();
      });


      // Shutdown the executor service gracefully when both applications are done
      executorService.shutdown();
      try {
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
      } catch (InterruptedException e) {
        Thread.currentThread()
            .interrupt();
      }
    }
  }


}
