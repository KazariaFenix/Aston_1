package ru.marzuev.aston.myarraylist;

import java.util.Comparator;
import java.util.Iterator;

/**
 Интерфейс MyList, собственная частичная реализация аналога интерфейса List
 @author Марзуев Владимир
 @version 1.0
 */
public interface MyList<T> {

    boolean add(T element);

    void add(T element, int index);

    T get(int index);

    boolean remove(T element);

    T remove(int index);

    void clear();

    void quickSort(Comparator<Object> comparator);

    int binarySearch(T element, Comparator<Object> comparator);

    T set(int index, T element);

    boolean contains(T element);

    boolean isEmpty();

    int indexOf(T element);

    Iterator<T> iterator();

    int size();
}
