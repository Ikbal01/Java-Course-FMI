package bg.sofia.uni.fmi.mjt.cache;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class MemCacheTest {

    private static final long CAPACITY = 2;
    private static final String NOT_EXISTING_KEY = "notExistingKey";
    private static final int PLUS_DAYS = 10;
    private static final int MINUS_DAYS = 10;
    private static final double DELTA = 0.01;

    private MemCache<String, String> memCache;

    @Before
    public void sutup() {
        memCache = new MemCache<>(CAPACITY);
    }

    @Test
    public void testAddTheValueToTheCacheAndAssociateItWithTheKey() {
        memCache.set(NOT_EXISTING_KEY, null, LocalDateTime.now());
        assertEquals("Size must be 0", 0, memCache.size());

        memCache.set("firstKey", "firstValue", LocalDateTime.now().plusDays(PLUS_DAYS));
        assertEquals("The size must be 1", 1, memCache.size());

        memCache.set("firstKey", "secondValue", LocalDateTime.now().minusDays(MINUS_DAYS));
        assertEquals("The previous key must be replaced", 1, memCache.size());

        memCache.set("secondKey", "secondValue", LocalDateTime.now().plusDays(PLUS_DAYS));
        memCache.set("thirdKey", "thirdValue", LocalDateTime.now().plusDays(PLUS_DAYS));

        assertNull("firstKey must be removed", memCache.getExpiration("firstKey"));
    }

    @Test(expected = CapacityExceededException.class)
    public void testCapacityExceededException() {
        memCache.set("firstKey", "firstValue", LocalDateTime.now().plusDays(PLUS_DAYS));
        memCache.set("secondKey", "secondValue", LocalDateTime.now().plusDays(PLUS_DAYS));

        // The cache is full and there is not a single expired item
        memCache.set("thirdKey", "thirdValue", LocalDateTime.now().plusDays(PLUS_DAYS));
    }

    @Test
    public void testGetTheValueAssociatedWithTheKey() {
        String value = memCache.get(NOT_EXISTING_KEY);
        assertNull("The key must not be contained in the cache", value);

        memCache.set("firstKey", "firstValue", LocalDateTime.now().plusDays(PLUS_DAYS));
        value = memCache.get("firstKey");
        assertEquals("The value must not be expired", "firstValue", value);

        memCache.set("secondKey", "secondValue", LocalDateTime.now().minusDays(MINUS_DAYS));
        value = memCache.get("secondKey");
        assertNull("The value must be null because it is expired", value);
    }

    @Test
    public void testRemoveTheItemAssociatedWithTheSpecifiedKey() {
        boolean found = memCache.remove(NOT_EXISTING_KEY);
        assertFalse("An item with the specified NotExistingKey was found", found);
    }

    @Test
    public void testTheNumberOfAllActualItemsStoredCurrentlyInTheCache() {
        long size = memCache.size();
        assertEquals("The size must be 0", 0, size);

        memCache.set("firstKey", "firstValue", LocalDateTime.now());
        assertEquals("The size must be 1", 1, memCache.size());
    }

    @Test
    public void testGetExpiration() {
        LocalDateTime tempTime = LocalDateTime.now().plusDays(PLUS_DAYS);
        memCache.set("firstKey", "firstValue", tempTime);

        LocalDateTime time = memCache.getExpiration(NOT_EXISTING_KEY);
        assertNull("An item with this key must not be found", time);

        time = memCache.getExpiration("firstKey");
        assertEquals(tempTime, time);
    }

    @Test
    public void testClear() {
        memCache.set("key", "value", LocalDateTime.now());
        memCache.clear();
        assertEquals("The size must be 0", 0, memCache.size());
    }

    @Test
    public void testGetHitRate() {
        memCache.set("firstKey", "firstValue", LocalDateTime.now().plusDays(PLUS_DAYS));
        memCache.set("secondKey", "secondValue", LocalDateTime.now().plusDays(PLUS_DAYS));

        assertEquals("Hit rate must be 0", 0, memCache.getHitRate(), DELTA);

        memCache.get("firstKey");
        assertEquals("Hit rate must be 1", 1, memCache.getHitRate(), DELTA);

        memCache.get("secondKey");
        assertEquals("Hit rate must be 1", 1, memCache.getHitRate(), DELTA);

        double expected = 0.66;
        memCache.get("third");
        assertEquals("Hit rate must be 0.66", expected, memCache.getHitRate(), DELTA);

        memCache.get("asd");
        memCache.get("asd");
        memCache.get("asd");

        expected = 0.33;
        assertEquals("Hit rate must be 0.33", expected, memCache.getHitRate(), DELTA);
    }

    @Test
    public void testRemove() {
        memCache.set("key", "value", LocalDateTime.now());

        assertFalse(memCache.remove("notContains"));

        assertTrue(memCache.remove("key"));
    }

    @Test
    public void testDefaultConstructor() {
        Cache<String, Integer> cache = new MemCache<>();
        cache.set("key", 10, LocalDateTime.now().plusDays(PLUS_DAYS));

        assertEquals("The value of the key must be 10", 10, (int) cache.get("key"));
    }
}

