package top.ezttf.ad.index;

/**
 * 检索系统索引
 *
 * @author yuwen
 * @date 2019/1/25
 */
public interface IIndexAware<K, V> {

    V get(K key);

    void add(K key, V value);

    void update(K key, V value);

    void delete(K key, V value);
}
