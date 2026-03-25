package com.mnb.projet.domain.tools;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.*;

class PerformenceTest {

    private final Performence performence = new Performence() {};

    @Test
    void shouldApplyFunctionToAllElements() {
        List<Integer> input = List.of(1, 2, 3, 4, 5);

        List<Integer> results = Performence.executeMultiThread(input, n -> n * 2);

        assertThat(results).containsExactlyInAnyOrder(2, 4, 6, 8, 10);
    }

    @Test
    void shouldReturnSameSizeAsInput() {
        List<String> input = List.of("a", "b", "c");

        List<String> results = Performence.executeMultiThread(input, String::toUpperCase);

        assertThat(results).hasSize(3);
    }

    @Test
    void shouldReturnEmptyListWhenInputIsEmpty() {
        List<String> results = Performence.executeMultiThread(List.<String>of(), s -> s);

        assertThat(results).isEmpty();
    }

    @Test
    void shouldMapStringToItsLength() {
        List<String> input = List.of("cat", "elephant", "ox");

        List<Integer> results = Performence.executeMultiThread(input, String::length);

        assertThat(results).containsExactlyInAnyOrder(3, 8, 2);
    }

    @Test
    void shouldExecuteAllTasksConcurrently() {
        AtomicInteger counter = new AtomicInteger(0);
        List<Integer> input = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

      Performence.executeMultiThread(input, n -> {
            counter.incrementAndGet();
            return n;
        });

        assertThat(counter.get()).isEqualTo(10);
    }

    @Test
    void shouldWrapExceptionInRuntimeException() {
        List<Integer> input = List.of(1);

        assertThatThrownBy(() -> Performence.executeMultiThread(input, n -> {
            throw new IllegalStateException("boom");
        }))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Error during parallel execution");
    }
}