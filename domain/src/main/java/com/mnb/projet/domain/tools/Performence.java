package com.mnb.projet.domain.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

public class Performence {

  public static  <T, E> List<E> executeMultiThread(List<T> elts, Function<T, E> fun) {

    int threads = Runtime.getRuntime().availableProcessors();

    ExecutorService executor = Executors.newFixedThreadPool(threads);

    List<Future<E>> futures = new ArrayList<>();

    elts.forEach(elt -> futures.add(executor.submit(() -> fun.apply(elt))));

    executor.shutdown();

    List<E> results = new ArrayList<>();

    futures.forEach(future -> {
      try {
        results.add(future.get());
      } catch (Exception e) {
        throw new RuntimeException("Error during parallel execution", e);
      }
    });

      return results;
    }
  }