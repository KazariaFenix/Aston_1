package ru.marzuev.aston.myarraylist;

import ru.marzuev.aston.quicksort.QuickSortComparator;

import java.util.*;

/**
 Класс MyArrayList со свойствами <b>array</b> и <b>size</b>, собственная частичная реализация класса ArrayList
 @author Марзуев Владимир
 @version 1.0
 */
public class MyArrayList<T> implements MyList<T>{
    /**
     @value Емкость массива который создается изначально при использовании конструктора без параметров
     */
    private static final int INIT_CAPACITY = 10;
    /**
     @value Результа отношения свободного пространства массива к переменной size, при превышении которого,
     произойдет сокращение длинны внутреннего массива
     */
    private static final int FREE_SPACE = 4;
    /**
     @value Множитель указыающий во сколько раз увеличится длинна внутреннего массива при расширении
     */
    private static final double INCREASE_MAX_CAPACITY = 1.5;
    private Object[] array ;
    private int size = 0;

    /**
     Конструктор без параметров изначальная емкость внутреннего массива равна INIT_CAPACITY
     */
    public MyArrayList() {
        array = new Object[INIT_CAPACITY];
    }

    /**
     Конструктор с параметром указывающий изначальную емкость внутреннего массива
     @param capacity - емкость массива
     */
    public MyArrayList(int capacity) {
        array = new Object[capacity];
    }

    /**
     Метод добавления элемента в конец списка, при заполнении внутреннего массива и дальнейшем добавлении
     элемента, происходит расширение
     @param element - элемент, который требуется добавить
     @exception IllegalStateException - возникает, когда size равно максимально возможному значению
     для типа данных int до вставки элемента
     */
    @Override
    public boolean add(T element) {
        if(size >= array.length) {
            addArrayLength();
        }
        if (size != Integer.MAX_VALUE) {
            array[size] = element;
        } else {
            throw new IllegalStateException("List Is Full");
        }
        size++;
        return true;
    }

    /**
     Метод добавления элемента в определенное место списка, при заполнении внутреннего массива и дальнейшем добавлении
     элемента, происходит расширение
     @param element - элемент, который требуется добавить
     @param index - индекс списка, куда нужно добавить элемент
     @exception ArrayIndexOutOfBoundsException - возникает если указан не валидный индекс
     @exception IllegalStateException - возникает, когда size равно максимально возможному значению
     для типа данных int, до вставки элемента
     */
    @Override
    public void add(T element, int index) {
        if (index > size || index < 0) {
            throw new ArrayIndexOutOfBoundsException("Index Is Wrong");
        }
        if (size >= array.length) {
            addArrayLength();
        }

        if (size != Integer.MAX_VALUE) {
            Object[] newArray = Arrays.copyOfRange(array, index, size);
            array[index] = element;
            for (int i = 0; i < newArray.length; i++) {
                array[index + 1 + i] = newArray[i];
            }
        } else {
            throw new IllegalStateException("List Is Full");
        }
        size++;
    }

    /**
     Метод по возвращению элемента списка по определенному индексу
     @param index - индекс списка, по которому возвращается элемент
     @return T - возвращаемый элемент списка
     @exception ArrayIndexOutOfBoundsException - возникает если указан не валидный индекс
     */
    @Override
    public T get(int index) {
        if (index >= size || index < 0) {
            throw new ArrayIndexOutOfBoundsException("Index Is Wrong");
        }
        return (T) array[index];
    }

    /**
     Метод по удалению элемента из списка, при достижении отношения свободного пространства к size больше чем
     FREE_SPACE, происходит обрезания длинны внутреннего массива
     @param element - элемент, который требуется удалить
     @return boolean - возвращает true, если элемент найден и удален или false если элемента в списке нет
     */
    @Override
    public boolean remove(T element) {
        int answer = indexOf(element);
        if (answer >= 0) {
            Object[] newArray = Arrays.copyOfRange(array, answer + 1, size);
            for (int i = 0; i < newArray.length; i++) {
                array[answer + i] = newArray[i];
            }

            array[size - 1] = null;
            size--;
            if (array.length / size >= FREE_SPACE ) {
                trimArrayLength();
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     Метод по удалению элемента из списка по определенному индексу, при достижении отношения свободного пространства
     к size больше чем FREE_SPACE, происходит обрезания длинны внутреннего массива
     @param index - индекс элемента, который требуется удалить
     @return T - возвращает элемент, который был удален
     @exception ArrayIndexOutOfBoundsException - возникает если указан не валидный индекс
     */
    @Override
    public T remove(int index) {
        if (index >= size || index < 0) {
            throw new ArrayIndexOutOfBoundsException("Index Is Wrong");
        }
        T element = (T) array[index];
        Object[] newArray = Arrays.copyOfRange(array, index + 1, size);

        for (int i = 0; i < newArray.length; i++) {
            array[index + i] = newArray[i];
        }
        array[size - 1] = null;
        size--;
        if (array.length / size >= FREE_SPACE ) {
            trimArrayLength();
        }
        return element;
    }

    /**
     Метод очищения коллекции, приводит все значения к null и обрезает внутренний массив до значения по умолчанию
     */
    @Override
    public void clear() {
        Arrays.fill(array, null);
        array = Arrays.copyOf(array, INIT_CAPACITY);
        size = 0;
    }

    /**
     Метод быстрой сортировки списка, вызывается класс QuickSortComparator и его методы
     @param comparator - компаратор по которому будет производиться сортировка объектов
     */
    @Override
    public void quickSort(Comparator<Object> comparator) {
        QuickSortComparator quickSort = new QuickSortComparator(comparator, array);
        quickSort.quickSort(0, size - 1);
    }

    /**
     Метод бинарного поиска элемента списка, производится только после сортировки
     @param comparator - компаратор по которому будет производиться сравнение объектов
     @param element - искомый элемент
     @return int - возвращение индекса списка, где находится элемент, если больше либо равно 0, то конкретное место
     в списке, если -1, то элемента нет
     */
    @Override
    public int binarySearch(T element, Comparator<Object> comparator) {
        int low = 0;
        int high = size - 1;
        int answer = -1;

        while(low <= high) {
            int mid = (low + high) / 2;
            if (comparator.compare(array[mid], element) == 0) {
                answer = mid;
                return answer;
            } else if (comparator.compare(element, array[mid]) > 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return answer;
    }

    /**
     Метод изменения элемента списка по определенному индексу
     @param index - индекс элемента, который будет заменен
     @param element - элемент на который будет заменен текущий
     @return T - элемент, который был заменен
     @exception ArrayIndexOutOfBoundsException - возникает, если элемент не валиден
     */
    @Override
    public T set(int index, T element) {
        if (index >= size) {
            throw new ArrayIndexOutOfBoundsException("Index Is Wrong");
        }
        T oldT = (T) array[index];
        array[index] = element;
        return oldT;
    }

    /**
     Метод, который показывает, содержит ли список определенный элемент
     @param element - элемент, который надо искать
     @return boolean - true если коллекция содержит элемент, false если нет
     */
    @Override
    public boolean contains(T element) {
        return indexOf(element) >= 0;
    }

    /**
     Метод, который показывает пуста коллекция или нет
     @return boolean - true если пуста, false если нет
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    /**
     Метод показывающий индекс первого вхождения элемента в списке, принимает и null значение
     @return int - если больше или равно 0, то это и есть индекс, а если равен -1, то элемента в списке
     не существует
     */
    @Override
    public int indexOf(T element) {
        if (element != null) {
            for (int i = 0; i < size; i++) {
                if (element.equals(array[i])) {
                    return i;
                }
            }

        } else {
            for (int k = 0; k < size; k++) {
                if (array[k] == null) {
                    return k;
                }
            }
        }
        return -1;
    }

    /**
     Метод вызова итератора списка, позволяет последовательно проходить по всем данным списка и производить
     необходимые итерации. <b>index</b> переменная аргумент-итератор, показывающая номер элемента в списке
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int index = 0;

            /**
             Метод итератора, позволяющий узнать, есть ли еще один элемент в данном списке
             @return boolean - true если да, false нет, так же при false обнуляется значение <b>index</b>
             */
            @Override
            public boolean hasNext() {
                boolean answer = index < size;
                if (!answer) {
                    index = 0;
                }
                return answer;
            }
            /**
             Метод итератора получения следующего элемента списка
             @return T - следующий элемент в списке
             */
            @Override
            public T next() {
                T element = (T) array[index];
                index++;
                return element;
            }
        };
    }
    /**
     Метод возвращения размера списка, а если конкретно, то переменной size, не путать с емкостью внутреннего массива
     @return int - величина переменной size
     */
    @Override
    public int size() {
        return size;
    }

    /**
     Метод увеличения внутреннего массива при его заполнении, с ограничением на максимальную возможную емкость,
     которую способно вместить переменная типа int
     */
    private void addArrayLength() {
        int oldCapacity = array.length;
        int capacity = (int)(array.length * INCREASE_MAX_CAPACITY);

        if (oldCapacity > Integer.MAX_VALUE / INCREASE_MAX_CAPACITY) {//не совсем нужное ограничение, так как раньше
            capacity = Integer.MAX_VALUE;                             // исключение OutOfMemory()
        }
        array = Arrays.copyOf(array, capacity);
    }

    /**
     Метод сокращения длинны внутреннего массива в 4 раза
     */
    private void trimArrayLength() {
        int capacity = array.length / FREE_SPACE;
        array = Arrays.copyOf(array, capacity);
    }

    /**
     Метод стандартный выводящий массив в строковом виде
     */
    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOf(array, size));
    }
}
