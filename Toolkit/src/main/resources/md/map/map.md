## 1、HashMap



以下是 HashMap 中最核心定义

```java
public class HashMap<K,V> extends AbstractMap<K,V> implements Map<K,V>, Cloneable, Serializable {
    // 默认的 table 容量，规定必须是 2 的幂。
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16
    // 空参构造时使用的默认负载因子。
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    // 链表转红黑树的阈值1：当某个桶的链表长度大于 8
    static final int TREEIFY_THRESHOLD = 8;
    // 链表转红黑树的阈值2：table 的长度不小于 64
    static final int MIN_TREEIFY_CAPACITY = 64;
    
    // 键值对的数量
    transient int size;
    // 扩容阈值：threshold = table.length * loadFactor;
    int threshold;
    // 加载因子（被 final 修饰，在构造方法中就被初始化，不指定就用默认的）
    final float loadFactor;
    
    // 存放数据的数组
    transient Node<K,V>[] table;
    
    // Node 充当数组与链表，是 HashMap 的核心
    static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;
        final K key;
        V value;
        Node<K,V> next;  // 链表

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
    
    // 无参构造（调用 put 方法时才进行 table 的初始化）
    public HashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
    }
}
```



HashMap 的数据存储结构是一个 Node<K,V> 数组 + 链表（JDK7 中是 Entry<K,V>，但只是名字不同，但结构相同）

<img src="./assets/img01.png" alt="img01" style="zoom: 50%;" />



在 Java 8 中 HashMap 的 put() 如下

```java
public V put(K key, V value) {
    return putVal(hash(key), key, value, false, true);
}

// 计算哈希值：与（&）、非（~）、或（|）、异或（^）
static final int hash(Object key) {
    int h;
    // h >>> 16：为了让 hashcode 的高 16 位也参与运算，目的是使 hash 值更加散列
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}

final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) {
   
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    // 如果数组为空，进行 resize() 初始化
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
    // 如果计算的位置上Node不存在，直接创建节点插入
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
    else {
        // 如果计算的位置上Node 存在，链表处理
        Node<K,V> e; K k;
        // 如果 hash 值，k 值完全相同，直接覆盖
        if (p.hash == hash &&((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
        // 如果 index 位置元素已经存在，且是红黑树
        else if (p instanceof TreeNode)
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        else {
            // 如果这次要放入的值不存在
            for (int binCount = 0; ; ++binCount) {
                // 尾插法
                if ((e = p.next) == null) {
                    // 找到节点链表中next为空的节点，创建新的节点插入
                    p.next = newNode(hash, key, value, null);
                    // 如果节点链表中数量超过TREEIFY_THRESHOLD（8）个，转化为红黑树
                    if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                        treeifyBin(tab, hash);
                    break;
                }
                // 如果节点链表中有发现已有相同key
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    break;
                p = e;
            }
        }
        // 如果节点 e 有值，放入数组 table[]
        if (e != null) { // existing mapping for key
            V oldValue = e.value;
            if (!onlyIfAbsent || oldValue == null)
                e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
    }
 
    ++modCount;
    // 当前大小大于临界大小，扩容
    if (++size > threshold)
        resize();
    afterNodeInsertion(evict);
    return null;
}

```

