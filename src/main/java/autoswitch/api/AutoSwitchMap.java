package autoswitch.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;

public class AutoSwitchMap<K, V> extends ConcurrentHashMap<K, V> {

    public static final Logger apiLogger = LogManager.getLogger("AutoSwitch-API");

    public AutoSwitchMap() {
        super();
    }

    /**
     * Restricted version of put method.
     *
     * @see ConcurrentHashMap#put(V, V)
     * @return null if addition was rejected
     */
    @Override
    public V put(K key, V value) {
        if (get(key) != null) { // Only additions!
            apiLogger.error("A mod tried to overwrite a preexisting value! This key was already present: {}", key);
            return null;
        }
        return super.put(key, value);
    }

    public void putTarget(K key, V value) {

    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) { // Only additions!
        AtomicBoolean shouldCancel = new AtomicBoolean(false);

        m.keySet().forEach(key -> {
            if (get(key) != null) {
                apiLogger.error("A mod tried to overwrite a preexisting value! This key was already present: {}", key);
                shouldCancel.set(true);
            }
        });

        if (!shouldCancel.get()) {
            return;
        }
        super.putAll(m);
    }

    @Override
    public boolean remove(Object key, Object value) {
        return false; // No one should be messing with the list!
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) { // Only additions!
        return false;
    }

    @Override
    public V replace(K key, V value) {// Only additions!
        return null;
    }

    /**
     * Restricted version of merge method.
     *
     * @see ConcurrentHashMap#merge
     * @return null if merge was rejected
     */
    @Override
    // Only additions!
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        if (get(key) != null) {
            apiLogger.error("A mod tried to overwrite a preexisting value! This key was already present: {}", key);
            return null;
        }
        return super.merge(key, value, remappingFunction);
    }

    @Override
    public void clear() {

    }
}
