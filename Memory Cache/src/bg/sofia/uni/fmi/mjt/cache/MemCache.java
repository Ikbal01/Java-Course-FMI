package bg.sofia.uni.fmi.mjt.cache;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class MemCache<K, V> implements Cache<K, V> {
    private static final long DEFAULT_CAPACITY = 0b10011100010000;

    private Map<K, V> genericCache;
    private Map<K, LocalDateTime> expirationCache;
    private long capacity;
    private int hit;
    private int miss;

    public MemCache() {
        this.capacity = DEFAULT_CAPACITY;
        this.genericCache = new HashMap<K, V>();
        this.expirationCache = new HashMap<K, LocalDateTime>();
        this.hit = 0;
        this.miss = 0;
    }

    public MemCache(long capacity) {
        this.capacity = capacity;
        this.genericCache = new HashMap<K, V>();
        this.expirationCache = new HashMap<K, LocalDateTime>();
        this.hit = 0;
        this.miss = 0;
    }

    @Override
    public V get(K key) {
        if (this.genericCache.containsKey(key)) {
            if (this.expirationCache.get(key).isBefore(LocalDateTime.now())) {
                this.genericCache.remove(key);
                this.expirationCache.remove(key);
                this.miss++;
                return null;
            }

            this.hit++;
            return this.genericCache.get(key);
        }

        this.miss++;
        return null;
    }

    @Override
    public void set(K key, V value, LocalDateTime expiresAt) throws CapacityExceededException {
        if (key == null || value == null) {
            return;
        }

        if (genericCache.containsKey(key)) {
            genericCache.put(key, value);
            expirationCache.put(key, expiresAt);
        } else if (genericCache.size() < capacity) {
            genericCache.put(key, value);
            expirationCache.put(key, expiresAt);
        } else {
            boolean containsExpired = false;
            K removalKey = null;

            for (K keyIterator : expirationCache.keySet()) {
                if (expirationCache.get(keyIterator).isBefore(LocalDateTime.now())) {
                    removalKey = keyIterator;
                    containsExpired = true;
                    break;
                }
            }

            if (containsExpired) {
                genericCache.remove(removalKey);
                expirationCache.remove(removalKey);

                genericCache.put(key, value);
                expirationCache.put(key, expiresAt);
            } else {
                throw new CapacityExceededException();
            }
        }
    }

    @Override
    public LocalDateTime getExpiration(K key) {
        if (this.genericCache.containsKey(key)) {
            return this.expirationCache.get(key);
        } else {
            return null;
        }
    }

    @Override
    public boolean remove(K key) {
        if (!this.genericCache.containsKey(key)) {
            return false;
        }

        this.genericCache.remove(key);
        this.expirationCache.remove(key);
        return true;
    }

    @Override
    public long size() {
        return this.genericCache.size();
    }

    @Override
    public void clear() {
        this.genericCache.clear();
        this.expirationCache.clear();
        this.hit = 0;
        this.miss = 0;
    }

    @Override
    public double getHitRate() {
        if (hit == 0) {
            return 0;
        } else if (miss == 0) {
            return 1;
        } else {
            double x = 100.0 / (hit + miss);

            return ((double) hit * x ) / 100.0;
        }
    }
}
