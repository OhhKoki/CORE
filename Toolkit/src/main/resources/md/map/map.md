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



### 1.1 put()

HashMap 的插入流程大概为：首先是先定位要插入的键值对属于哪个桶，定位到桶后，再判断桶是否为空。如果为空，则将键值对存入即可。如果不为空，则需将键值对接在链表最后一个位置，或者更新键值对。

```java
public V put(K key, V value) {
    return putVal(hash(key), key, value, false, true);
}

final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
               boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    // 初始化桶数组 table，table 被延迟到插入新数据时再进行初始化
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
    // 如果桶中没有元素，则将新节点的引用存入桶中即可
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
    else {
        Node<K,V> e; K k;
        // 如果键的值以及节点 hash 等于链表中的第一个键值对节点时，则将 e 指向该键值对
        if (p.hash == hash && ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
        // 如果桶中的引用类型为 TreeNode，则调用红黑树的插入方法
        else if (p instanceof TreeNode)  
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        else {
            // 对链表进行遍历，并统计链表长度
            for (int binCount = 0; ; ++binCount) {
                // 链表中不包含要插入的键值对节点时，则将该节点接在链表的最后
                if ((e = p.next) == null) {
                    p.next = newNode(hash, key, value, null);
                    // 如果链表长度大于或等于树化阈值，则进行树化操作
                    if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                        treeifyBin(tab, hash);
                    break;
                }
                // 条件为 true，表示当前链表包含要插入的键值对，终止遍历
                if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))
                    break;
                p = e;
            }
        }
        
        // 判断要插入的键值对是否存在 HashMap 中
        if (e != null) { // existing mapping for key
            V oldValue = e.value;
            // onlyIfAbsent 表示是否仅在 oldValue 为 null 的情况下更新键值对的值
            if (!onlyIfAbsent || oldValue == null)
                e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
    }
    ++modCount;
    // 键值对数量超过阈值时，则进行扩容
    if (++size > threshold)
        resize();
    afterNodeInsertion(evict);
    return null;
}
```



插入操作的入口方法是 `put(K,V)`，但核心逻辑在`V putVal(int, K, V, boolean, boolean)` 方法中。主要做了这么几件事情：

1. 计算 key 的 hash 值

   - 计算方式是 `(key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);`

2. 检查当前数组是否为空，为空需要进行初始化，初始化容量是 **16** ，负载因子默认 **0.75**。

3. 计算 key 在数组中的坐标

   - 计算方式：`(容量 - 1) & hash`.

   - 因为容量总是2的次方，所以 length-1的值的二进制**总是全1**。方便与 hash 值进行**与**运算。

4. 如果计算出的坐标元素为空，创建节点加入，put() 结束

   - 如果当前数组容量大于负载因子设置的容量，**进行扩容**。

5. 如果计算出的坐标元素有值。

   1. 如果坐标上的元素值和要加入的值 key 完全一样，覆盖原有值。
   2. 如果坐标上的元素是**红黑树**，把要加入的值和 key 加入到红黑树。
   3. 如果坐标上的元素和要加入的元素不同（**尾插法**增加）。
      1. 如果 next 节点为空，把要加入的值和 key 加入 next 节点。
      2. 如果 next 节点不为空，循环查看 next 节点。
      3. 如果发现有 next 节点的 key 和要加入的 key 一样，对应的值替换为新值。
      4. 如果循环 next 节点查找**超过 8 层**还不为空，把这个位置元素转换为**红黑树**。



### 1.2 get()

HashMap 的查找操作比较简单，先定位键值对所在的桶的位置，然后再对链表或红黑树进行查找。

```java
public V get(Object key) {
    Node<K,V> e;
    return (e = getNode(hash(key), key)) == null ? null : e.value;
}

final Node<K,V> getNode(int hash, Object key) {
    Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
    // 1. 定位键值对所在桶的位置
    if ((tab = table) != null && (n = tab.length) > 0 && (first = tab[(n - 1) & hash]) != null) {
        if (first.hash == hash && ((k = first.key) == key || (key != null && key.equals(k))))
            // 如果是头节点则直接返回
            return first;
        if ((e = first.next) != null) {
            // 2. 如果 first 是 TreeNode 类型，则调用黑红树查找方法
            if (first instanceof TreeNode)
                return ((TreeNode<K,V>)first).getTreeNode(hash, key);
                
            // 2. 对链表进行查找
            do {
                if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))
                    return e;
            } while ((e = e.next) != null);
        }
    }
    return null;
}
```



### 1.3 resize()

HashMap 的扩容机制与其他变长集合的套路不太一样，HashMap 按当前桶数组长度的 2 倍进行扩容，阈值也变为原来的 2 倍。扩容之后，要重新计算键值对的位置，并把它们移动到合适的位置上去。以上就是 HashMap 的扩容大致过程。

```java
final Node<K,V>[] resize() {
    Node<K,V>[] oldTab = table;
    int oldCap = (oldTab == null) ? 0 : oldTab.length;
    int oldThr = threshold;
    int newCap, newThr = 0;
    // 如果 table 不为空，表明已经初始化过了
    if (oldCap > 0) {
        // 当 table 容量超过容量最大值，则不再扩容
        if (oldCap >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return oldTab;
        } else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY && oldCap >= DEFAULT_INITIAL_CAPACITY)
            // 按旧容量和阈值的 2 倍计算新容量和阈值的大小
            newThr = oldThr << 1; // double threshold
    } else if (oldThr > 0)
        // 初始化时，将 threshold 的值赋值给 newCap，HashMap 使用 threshold 变量暂时保存 initialCapacity 参数的值
        newCap = oldThr;
    else {
        // 调用无参构造方法时，桶数组容量为默认容量，阈值为默认容量与默认负载因子乘积
        newCap = DEFAULT_INITIAL_CAPACITY;
        newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }
    
    // newThr 为 0 时，按阈值计算公式进行计算
    if (newThr == 0) {
        float ft = (float)newCap * loadFactor;
        newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ? (int)ft : Integer.MAX_VALUE);
    }
    threshold = newThr;
    // 创建新的桶数组，桶数组的初始化也是在这里完成的
    Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
    table = newTab;
    if (oldTab != null) {
        // 如果旧的桶数组不为空，则遍历桶数组，并将键值对映射到新的桶数组中
        for (int j = 0; j < oldCap; ++j) {
            Node<K,V> e;
            if ((e = oldTab[j]) != null) {
                oldTab[j] = null;
                if (e.next == null)
                    newTab[e.hash & (newCap - 1)] = e;
                else if (e instanceof TreeNode)
                    // 重新映射时，需要对红黑树进行拆分
                    ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                else { // preserve order
                    Node<K,V> loHead = null, loTail = null;
                    Node<K,V> hiHead = null, hiTail = null;
                    Node<K,V> next;
                    // 遍历链表，并将链表节点按原顺序进行分组
                    do {
                        next = e.next;
                        if ((e.hash & oldCap) == 0) {
                            if (loTail == null)
                                loHead = e;
                            else
                                loTail.next = e;
                            loTail = e;
                        }
                        else {
                            if (hiTail == null)
                                hiHead = e;
                            else
                                hiTail.next = e;
                            hiTail = e;
                        }
                    } while ((e = next) != null);
                    // 将分组后的链表映射到新桶中
                    if (loTail != null) {
                        loTail.next = null;
                        newTab[j] = loHead;
                    }
                    if (hiTail != null) {
                        hiTail.next = null;
                        newTab[j + oldCap] = hiHead;
                    }
                }
            }
        }
    }
    return newTab;
}
```

