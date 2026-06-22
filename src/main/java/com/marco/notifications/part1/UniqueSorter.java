package com.marco.notifications.part1;

import java.util.ArrayList;
import java.util.List;

/**
 * PARTE 1 - Logica de Programacion.
 *
 * Dada una lista de enteros, construye una nueva lista que:
 *   1) Elimina los valores duplicados.
 *   2) Queda ordenada de menor a mayor.
 *
 * RESTRICCIONES (impuestas por el enunciado):
 *   - NO se permite sort / sorted.
 *   - NO se permite set / distinct / unique.
 *   - NO se permiten librerias que automaticen la solucion.
 *
 * Solo se usan ciclos, condicionales, listas basicas y funciones propias.
 *
 * Se usa List<Integer> (ArrayList) unicamente como estructura contenedora
 * dinamica; NO se usa ninguna de sus capacidades de ordenamiento ni de
 * deduplicacion (no hay Collections.sort, no hay Set).
 */
public final class UniqueSorter {

    private UniqueSorter() {
        // Clase de utilidad: no se instancia.
    }

    /**
     * Devuelve una nueva lista sin duplicados y ordenada ascendentemente.
     *
     * Estrategia: en una sola pasada por la entrada, para cada numero
     *   1) se descarta si ya existe en el resultado (deduplicacion manual), y
     *   2) si es nuevo, se inserta directamente en la posicion que le
     *      corresponde para mantener el orden (insertion sort manual).
     *
     * Complejidad: O(n^2) en el peor caso. Es aceptable y a la vez explicito:
     * el objetivo del ejercicio es demostrar el algoritmo, no usar utilidades.
     *
     * @param input lista de enteros de entrada (puede contener duplicados y
     *              estar desordenada). Si es null se trata como vacia.
     * @return nueva lista ordenada ascendente y sin duplicados.
     */
    public static List<Integer> uniqueSorted(List<Integer> input) {
        List<Integer> result = new ArrayList<>();
        if (input == null) {
            return result;
        }

        for (int i = 0; i < input.size(); i++) {
            Integer value = input.get(i);
            if (value == null) {
                continue; // se ignoran nulos por robustez
            }

            // (1) Deduplicacion manual: si ya esta en el resultado, lo saltamos.
            if (contains(result, value)) {
                continue;
            }

            // (2) Insercion ordenada: buscamos la primera posicion cuyo
            //     elemento sea mayor que 'value' e insertamos ahi.
            int pos = findInsertPosition(result, value);
            result.add(pos, value);
        }

        return result;
    }

    /**
     * Indica si 'value' ya se encuentra en la lista. Implementacion propia
     * para no depender de utilidades de busqueda automatica.
     */
    private static boolean contains(List<Integer> list, int value) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == value) {
                return true;
            }
        }
        return false;
    }

    /**
     * Devuelve el indice donde debe insertarse 'value' para que la lista
     * (que ya esta ordenada ascendentemente) siga ordenada. Es el primer
     * indice cuyo elemento es estrictamente mayor que 'value'; si no existe,
     * devuelve el tamano de la lista (se agrega al final).
     */
    private static int findInsertPosition(List<Integer> sorted, int value) {
        for (int i = 0; i < sorted.size(); i++) {
            if (sorted.get(i) > value) {
                return i;
            }
        }
        return sorted.size();
    }

    /**
     * Pequena demo ejecutable: replica el ejemplo del enunciado.
     *   entrada = [4, 2, 7, 2, 4, 9, 1]  ->  salida = [1, 2, 4, 7, 9]
     */
    public static void main(String[] args) {
        List<Integer> entrada = new ArrayList<>(List.of(4, 2, 7, 2, 4, 9, 1));
        System.out.println("entrada = " + entrada);
        System.out.println("salida  = " + uniqueSorted(entrada));
    }
}
