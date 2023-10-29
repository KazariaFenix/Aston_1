import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.marzuev.aston.myarraylist.MyArrayList;
import ru.marzuev.aston.myarraylist.MyList;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

public class MyArrayListTest {
    private static final int TEST_INTEGER = 12345;
    private static final int UNKNOWN_ELEMENT = 1151255;
    private static final int NUMBER_FOR_INCREASE_ARRAY_LENGTH = 3;
    private static final int INIT_LIST_SIZE_OF_RANDOM = 9;
    private MyList<Integer> list;
    private Random random;

    @BeforeEach
    public void createMyArrayList() {
        list = new MyArrayList<>();
        random = new Random();
        for (int i = 0; i < INIT_LIST_SIZE_OF_RANDOM; i++) {
            list.add(random.nextInt(500));
        }
    }

    @AfterEach
    public void clearMyArrayList() {
        list.clear();
    }

    @Test
    public void addElementNormal() {
        list.add(TEST_INTEGER);
        assertEquals(list.size(), 10);
        assertEquals(list.get(list.size() - 1), TEST_INTEGER);
    }

    @Test
    public void addElementWithIncreaseArrayLength() {
        for (int i = 0; i < NUMBER_FOR_INCREASE_ARRAY_LENGTH; i++) {
            list.add(random.nextInt());
        }
        assertEquals(list.size(), 12);
    }

    @Test
    public void addElementByIndexNormalInStart() {
        list.add(TEST_INTEGER, 0);
        assertEquals(list.size(), 10);
        assertEquals(list.get(0), TEST_INTEGER);
    }

    @Test
    public void addElementByIndexNormalInMiddle() {
        list.add(TEST_INTEGER, 5);
        assertEquals(list.size(), 10);
        assertEquals(list.get(5), TEST_INTEGER);
    }

    @Test
    public void addElementByIndexNormalIncreaseArrayLength() {
        for (int i = 0; i < NUMBER_FOR_INCREASE_ARRAY_LENGTH; i++) {
            list.add(random.nextInt(), i);
        }
        assertEquals(list.size(), 12);
    }

    @Test
    public void addElementByFailedIndex() {
        ArrayIndexOutOfBoundsException e = assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> list.add(TEST_INTEGER, list.size()));
    }

    @Test
    public void removeElementNormal() {
        list.add(TEST_INTEGER, 5);
        boolean answer = list.remove(new Integer(TEST_INTEGER));
        assertEquals(list.size(), 9);
        assertNotEquals(list.get(5), TEST_INTEGER);
        assertTrue(answer);
    }

    @Test
    public void removeElementUnknown() {
        list.add(TEST_INTEGER, 5);
        boolean answer = list.remove(new Integer(UNKNOWN_ELEMENT));
        assertEquals(list.size(), 10);
        assertFalse(answer);
    }

    @Test
    public void removeElementByIndexNormal() {
        list.add(TEST_INTEGER, 5);
        list.remove(5);
        assertEquals(list.size(), 9);
        assertNotEquals(list.get(5), TEST_INTEGER);
    }

    @Test
    public void removeElementByIndexFailed() {
        ArrayIndexOutOfBoundsException e = assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> list.remove(list.size()));
    }

    @Test
    public void removeElementByIndexWithTrimArrayLength() {
        for (int i = 0; i < TEST_INTEGER; i++) {
            list.add(random.nextInt());
        }
        for (int i = TEST_INTEGER + 8; i >= 9; i--) {
            list.remove(i);
        }
        assertEquals(list.size(), 9);
    }

    @Test
    public void clearList() {
        list.clear();
        assertEquals(list.size(), 0);
        ArrayIndexOutOfBoundsException e = assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> list.remove(list.size()));
    }

    @Test
    public void setElementNormal() {
        Integer element = list.get(5);
        Integer oldElement = list.set(5, TEST_INTEGER);
        assertEquals(list.get(5), TEST_INTEGER);
        assertEquals(list.size(), 9);
        assertEquals(element, oldElement);
    }

    @Test
    public void setElementWrongIndex() {
        ArrayIndexOutOfBoundsException e = assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> list.set(TEST_INTEGER, TEST_INTEGER));
    }

    @Test
    public void containsElementNormal() {
        list.add(TEST_INTEGER, 5);
        boolean answer = list.contains(TEST_INTEGER);
        assertTrue(answer);
    }

    @Test
    public void containsElementUnknown() {
        boolean answer = list.contains(UNKNOWN_ELEMENT);
        assertFalse(answer);
    }

    @Test
    public void indexOfElementNormal() {
        list.add(TEST_INTEGER, 5);
        int index = list.indexOf(TEST_INTEGER);
        assertEquals(5, index);
    }

    @Test
    public void indexOfElementUnknown() {
        int index = list.indexOf(UNKNOWN_ELEMENT);
        assertEquals(-1, index);
    }

    @Test
    public void indexOfElementNull() {
        list.set(5, null);
        int index = list.indexOf(null);
        assertEquals(5, index);
    }

    @Test
    public void isEmptyListTrue() {
        list.clear();
        assertTrue(list.isEmpty());
    }

    @Test
    public void isEmptyListFalse() {
        assertFalse(list.isEmpty());
    }

    @Test
    public void createAndUseIterator() {
        int count = 0;
        MyList<Integer> newList = new MyArrayList<>(15);
        for (int i = 0; i < list.size(); i++) {
            newList.add(list.get(i));
        }

        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            list.set(count, iterator.next() + 1);
            count++;
        }
        assertEquals(newList.get(0) + 1, list.get(0));
        assertEquals(newList.get(5) + 1, list.get(5));
        assertEquals(newList.get(list.size() - 1) + 1, list.get(list.size() - 1));
    }

    @Test
    public void getFailedIndex() {
        ArrayIndexOutOfBoundsException e = assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> list.get(TEST_INTEGER));
    }

    @Test
    public void quickSortWithNormalComparator() {
        boolean answer = true;
        list.quickSort(Comparator.comparingInt(o -> (int) o));
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i) > list.get(i + 1)) {
                answer = false;
            }
        }
        assertTrue(answer);
    }

    @Test
    public void quickSortWithReversedComparator() {
        boolean answer = true;
        list.quickSort(Comparator.comparingInt(o -> (int) o).reversed());
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i) < list.get(i + 1)) {
                answer = false;
            }
        }
        assertTrue(answer);
        System.out.println(list);
    }

    @Test
    public void binarySearchNormal() {
        Integer element = list.get(5);
        list.quickSort(Comparator.comparingInt(o -> (int) o));
        int index = list.binarySearch(element, Comparator.comparingInt(o -> (int) o));
        assertNotEquals(index, -1);
    }

    @Test
    public void binarySearchNotFound() {
        Integer element = UNKNOWN_ELEMENT;
        list.quickSort(Comparator.comparingInt(o -> (int) o));
        int index = list.binarySearch(element, Comparator.comparingInt(o -> (int) o));
        assertEquals(index, -1);
    }

    @Test
    public void removeElementWithTrimArrayLength() {
        for (int i = 0; i < TEST_INTEGER; i++) {
            list.add(i);
        }
        for (int i = 0; i < TEST_INTEGER; i++) {
            list.remove(new Integer(i));
        }
        assertEquals(list.size(), 9);
    }
}
