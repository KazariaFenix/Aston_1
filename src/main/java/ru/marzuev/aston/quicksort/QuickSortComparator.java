package ru.marzuev.aston.quicksort;

import java.util.Comparator;

/**
 Класс MyArrayList со свойствами <b>comparator</b> и <b>array</b>, собственная частичная реализация класса ArrayList
 @author Марзуев Владимир
 @version 1.0
 */
public class QuickSortComparator {
    private final Comparator<Object> comparator;
    private final Object[] array;

    public QuickSortComparator(Comparator<Object> comparator, Object[] array) {
        this.comparator = comparator;
        this.array = array;
    }

    /**
     Метод добавления элемента в конец списка, при заполнении внутреннего массива и дальнейшем добавлении
     элемента, происходит расширение
     @param begin - номер первого элемента подмассива
     @param end - номер конечного элемента подмассива
     */
    public void quickSort(int begin, int end) {
        if (begin >= end) {
            return;
        }
        int pivot = partition(begin, end);
        quickSort(begin, pivot - 1);
        quickSort(pivot + 1, end);
    }

    /**
     Метод поиска опорного элемента подмассива, вокруг которого будет строиться сортировка
     @param begin - номер первого элемента подмассива
     @param end - номер конечного элемента подмассива
     */
    private int partition(int begin, int end) {
        int pivot = end;
        int counter = begin;

        for (int i = begin; i < end; i++) {
            if (comparator.compare(array[pivot], array[i]) > 0) {
                Object temp = array[counter];
                array[counter] = array[i];
                array[i] = temp;
                counter++;
            }
        }
        Object swap = array[counter];
        array[counter] = array[pivot];
        array[pivot] = swap;

        return counter;
    }
}
