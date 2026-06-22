package com.marco.notifications.part1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas unitarias de la PARTE 1.
 * Verifican deduplicacion, orden ascendente y casos borde.
 */
class UniqueSorterTest {

    @Test
    @DisplayName("Ejemplo del enunciado: [4,2,7,2,4,9,1] -> [1,2,4,7,9]")
    void ejemploDelEnunciado() {
        List<Integer> entrada = List.of(4, 2, 7, 2, 4, 9, 1);
        assertThat(UniqueSorter.uniqueSorted(entrada))
                .containsExactly(1, 2, 4, 7, 9);
    }

    @Test
    @DisplayName("Lista vacia devuelve lista vacia")
    void listaVacia() {
        assertThat(UniqueSorter.uniqueSorted(List.of())).isEmpty();
    }

    @Test
    @DisplayName("Entrada null devuelve lista vacia (robustez)")
    void entradaNull() {
        assertThat(UniqueSorter.uniqueSorted(null)).isEmpty();
    }

    @Test
    @DisplayName("Todos iguales -> un solo elemento")
    void todosIguales() {
        assertThat(UniqueSorter.uniqueSorted(List.of(5, 5, 5, 5)))
                .containsExactly(5);
    }

    @Test
    @DisplayName("Ya ordenada y sin duplicados se mantiene igual")
    void yaOrdenada() {
        assertThat(UniqueSorter.uniqueSorted(List.of(1, 2, 3, 4)))
                .containsExactly(1, 2, 3, 4);
    }

    @Test
    @DisplayName("Orden inverso se ordena ascendente")
    void ordenInverso() {
        assertThat(UniqueSorter.uniqueSorted(List.of(9, 7, 5, 3, 1)))
                .containsExactly(1, 3, 5, 7, 9);
    }

    @Test
    @DisplayName("Maneja numeros negativos y el cero")
    void negativosYCero() {
        assertThat(UniqueSorter.uniqueSorted(List.of(0, -3, 5, -3, 0, 5, -10)))
                .containsExactly(-10, -3, 0, 5);
    }

    @Test
    @DisplayName("Ignora valores nulos dentro de la lista")
    void ignoraNulos() {
        List<Integer> entrada = new ArrayList<>(Arrays.asList(3, null, 1, null, 3));
        assertThat(UniqueSorter.uniqueSorted(entrada))
                .containsExactly(1, 3);
    }
}
