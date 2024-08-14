# 1）前言概述

Java 8 中的 Stream 是对集合（Collection）对象功能的增强，它专注于对集合对象进行各种便利、高效的聚合操作（aggregate operation），或者大批量数据操作（bulk data operation）。Stream API 借助于 Lambda 表达式，极大的提高编程效率和程序可读性。同时它提供串行和并行两种模式进行汇聚操作，并发模式能够充分利用多核处理器的优势，使用 fork/join 并行方式来拆分任务和加速处理过程。通常编写并行代码很难而且容易出错, 但使用 Stream API 无需编写一行多线程的代码，就可以很方便地写出高性能的并发程序。



对于 Stream 流的使用，一共就三个步骤

1. 创建操作
2. 中间操作
3. 终结操作



这是一个 stream 流的使用案例

```java
List<Integer> numberList = List.of(3, 4, 5, 5);

// 创建流
numberList.stream()
  // 中间操作
  .distinct()
  // 终结操作
  .forEach(System.out::println);
```



另外，对于 Stream 流来说，以下特点需要注意

1. 流不是数据结构，不会保存数据；
2. 任何<u>中间操作</u>和<u>终结操作</u>都不会修改数据源，每个操作都会返回一个新的 Stream 对象；
3. 惰性求值，流在执行<u>中间操作</u>时，只是对操作进行了记录，并不会立即执行，需要等到执行<u>终结操作</u>的时候才会进行实际的计算。



# 2）创建操作

根据数据源类型的不同，创建流的方式有以下三种方式

- 数组
  - 通过 Arrays 的 stream()
- 单列集合
  - 通过 Collection 的 stream()
- 双列集合
  - 先将 Map 转为 Collection，然后再调用 Collection 的 stream()



## 2.1）数组

Java 8 在 Arrays 类中新增了 stream() 方法

```java
public static <T> Stream<T> stream(T[] array)
```



所以，可以通过 Arrays 的 stream() 可以创建一个 stream 对象，案例如下

```java
// 数组
Integer[] numberArray = {2, 3, 4, 4};

// 创建流
Arrays.stream(numberArray)
  // 中间操作
  .distinct()
  // 终结操作
	.forEach(System.out::println);
```



## 2.2）单列集合

Java 8 在 Collection 类中新增了 stream() 方法

```java
default Stream<E> stream()
```



所以，可以通过 Collection 的 stream() 可以创建一个 stream 对象，案例如下

```java
// 单列集合
List<Integer> numberList = List.of(3, 4, 5, 5);

// 创建流
numberList.stream()
  // 中间操作
  .distinct()
  // 终结操作
  .forEach(System.out::println);
```



需要注意的是，由于 List 和 Set 都是 Collction 的子接口，所以 List 和 Set 接口都包含 stream()



## 2.3）双列集合

对于双列集合 Map 来说，需要先将 Map 转 Collection，再调用 Collection 中的 stream()。



Map 中提供了将 Map 转为 Collection 的方法

```java
Set<Map.Entry<K, V>> entrySet();
```



所以，可以通过 Map 创建一个 stream 对象，案例如下

```java
// 双列集合
Map<String, Integer> map = new HashMap<>();
map.put("一", 1);
map.put("二", 2);
map.put("二", 2);

// 将 Map 转为 Collection 的子接口
Set<Map.Entry<String, Integer>> entries = map.entrySet();

// 创建流
entries.stream()
  // 中间操作
  .distinct()
  // 终结操作
  .forEach(System.out::println);
```



# 3）中间操作

一个流可以后面跟随零个或多个 intermediate 操作。其目的主要是打开流，做出某种程度的数据映射/过滤，然后返回一个新的流，交给下一个操作使用。这类操作都是惰性化的（lazy），就是说，仅仅调用到这类方法，并没有真正开始流的遍历。



常见的操作如下：

- distinct & filter

- map & flatMap
- sort
- limit & skip
- findFirst & findAny
- max & min & count
- foreach & collect



## 3.1）distinct

过滤重复的元素，依赖于 Object 的 equals()



案例如下（去除重复的作家，并打印作家的姓名

```java
List<Author> authors = getAuthors();

authors.stream()
        // 中间操作：对流中的元素进行去重
        .distinct()
        // 终结操作：遍历流中的各个元素
        .forEach(author -> System.out.println(author.getName()));
```



## 3.2）filter

根据指定的条件进行过滤元素，只保留满足条件的元素



案例如下（过滤重复的作家 & 过滤年龄小于18的作家，并打印作家的姓名

```java
List<Author> authors = getAuthors();

authors.stream()
        .distinct()
        // 中间操作：对流中的元素进行去重
        .filter(author -> author.getAge() < 18)
        // 终结操作：遍历流中的各个元素
        .forEach(author -> System.out.println(author.getName()));
```













# 4）终结操作

