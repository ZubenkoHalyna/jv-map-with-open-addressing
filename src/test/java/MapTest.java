import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

public class MapTest {
    @Test
    public void getSizeOk() {
        MapWithOpenAddressing map = new MapWithOpenAddressingImpl();
        int expectedSize = 100;
        for (int i = 0; i < expectedSize; i++) {
            map.put(i, i);
        }
        Assert.assertEquals(expectedSize, map.getSize());
    }

    @Test
    public void getSizeEmpty() {
        MapWithOpenAddressing map = new MapWithOpenAddressingImpl();
        Assert.assertEquals(0, map.getSize());
    }

    @Test
    public void getSizeOverriddenKey() {
        MapWithOpenAddressing map = new MapWithOpenAddressingImpl();
        for (int i = 0; i < 100; i++) {
            map.put(i % 10, i);
        }
        Assert.assertEquals(10, map.getSize());
    }

    @Test
    public void getDefaultCapacity() {
        MapWithOpenAddressingImpl map = new MapWithOpenAddressingImpl();
        Assert.assertEquals(16, map.getCapacity());
    }

    @Test
    public void getInitialCapacity() {
        int initialCapacity = 10;
        MapWithOpenAddressingImpl map =
                new MapWithOpenAddressingImpl(initialCapacity, 0.7f, -1);
        Assert.assertEquals(initialCapacity, map.getCapacity());
    }

    @Test
    public void getCapacityNotExceededLoadFactor() {
        float loadFactor = 0.7f;
        int initialCapacity = 16;
        MapWithOpenAddressingImpl map =
                new MapWithOpenAddressingImpl(initialCapacity, loadFactor, -1);
        for (int i = 0; i < initialCapacity * loadFactor; i++) {
            map.put(i, i);
        }
        Assert.assertEquals(initialCapacity, map.getCapacity());
    }

    @Test
    public void getCapacityExceededLoadFactor() {
        float loadFactor = 0.7f;
        int initialCapacity = 16;
        MapWithOpenAddressingImpl map =
                new MapWithOpenAddressingImpl(initialCapacity, loadFactor, -1);
        for (int i = 0; i < 1 + initialCapacity * loadFactor; i++) {
            map.put(i, i);
        }
        Assert.assertEquals(initialCapacity * 2, map.getCapacity());
    }

    @Test
    public void putAndGetOk() {
        MapWithOpenAddressing map = new MapWithOpenAddressingImpl();
        for (int i = 0; i < 100; i++) {
            map.put(i, i + 5);
        }
        for (int i = 0; i < 100; i++) {
            Assert.assertEquals(i + 5, map.get(i));
        }
    }

    @Test
    public void putAndGetRandom() {
        MapWithOpenAddressing map = new MapWithOpenAddressingImpl();
        Map<Integer, Long> expectedMap = new HashMap<>();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int key = random.nextInt();
            long value = random.nextLong();
            map.put(key, value);
            expectedMap.put(key, value);
        }
        for (Map.Entry<Integer, Long> entry : expectedMap.entrySet()) {
            Assert.assertEquals("Illegal value for key " + entry.getKey() + "\nGenerated map: " + expectedMap,
                    entry.getValue().longValue(), map.get(entry.getKey()));
        }
    }

    @Test
    public void putAndGetKeyMaxValue() {
        MapWithOpenAddressing map = new MapWithOpenAddressingImpl();
        for (int i = Integer.MAX_VALUE; i > Integer.MAX_VALUE - 50; i--) {
            map.put(i, i);
        }
        for (int i = Integer.MAX_VALUE; i > Integer.MAX_VALUE - 50; i--) {
            Assert.assertEquals(i, map.get(i));
        }
    }

    @Test
    public void putAndGetKeyMinValue() {
        MapWithOpenAddressing map = new MapWithOpenAddressingImpl();
        for (int i = Integer.MIN_VALUE; i < Integer.MIN_VALUE + 50; i++) {
            map.put(i, i);
        }
        for (int i = Integer.MIN_VALUE; i < Integer.MIN_VALUE + 50; i++) {
            Assert.assertEquals(i, map.get(i));
        }
    }

    @Test
    public void putAndGetMaxValue() {
        MapWithOpenAddressing map = new MapWithOpenAddressingImpl();
        map.put(0, Long.MAX_VALUE);
        Assert.assertEquals(Long.MAX_VALUE, map.get(0));
    }

    @Test
    public void putAndGetMinValue() {
        MapWithOpenAddressing map = new MapWithOpenAddressingImpl();
        map.put(0, Long.MIN_VALUE);
        Assert.assertEquals(Long.MIN_VALUE, map.get(0));
    }

    @Test
    public void overrideValueOk() {
        MapWithOpenAddressing map = new MapWithOpenAddressingImpl();
        for (int i = 0; i <= 5; i++) {
            map.put(0, i);
        }
        Assert.assertEquals(5, map.get(0));
    }

    @Test
    public void getByNonExistingKey() {
        MapWithOpenAddressing map = new MapWithOpenAddressingImpl();
        Assert.assertEquals(-1, map.get(1));
    }

    @Test
    public void getByNonExistingKeyNonDefaultNullValue() {
        long nullValue = Long.MIN_VALUE;
        MapWithOpenAddressing map = new MapWithOpenAddressingImpl(10, 0.7f, nullValue);
        Assert.assertEquals(nullValue, map.get(1));
    }
}
