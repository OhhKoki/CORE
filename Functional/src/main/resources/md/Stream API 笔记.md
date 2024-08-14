# 1）前言概述

Java 8 中的 Stream 是对集合（Collection）对象功能的增强，它专注于对集合对象进行各种便利、高效的聚合操作（aggregate operation），或者大批量数据操作（bulk data operation）。Stream API 借助于 Lambda 表达式，极大的提高编程效率和程序可读性。同时它提供串行和并行两种模式进行汇聚操作，并发模式能够充分利用多核处理器的优势，使用 fork/join 并行方式来拆分任务和加速处理过程。通常编写并行代码很难而且容易出错, 但使用 Stream API 无需编写一行多线程的代码，就可以很方便地写出高性能的并发程序。



# 2）问题引出

详细讨论之前，先展示一个排序、取值案例在 Java 8 之前版本的实现：

```java
// 在原文实现代码基础上添加枚举类 Transaction
enum Transaction {
    GROCERY(0, "grocery"), NONE(1, "none");

    private final int id;
    private final String value;

    Transaction(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public Object getType() { return this; }
    public int getId() { return this.id; }
    public String getValue() { return this.value; }
}

public static void main(String[] args) {
    // 使用泛型初始化一个 Transaction 集合，代替原文使用的原始类型
    List<Transaction> transactions = new ArrayList<>() {
        {
            add(Transaction.NONE);
            add(Transaction.GROCERY);
        }
    };

    List<Transaction> groceryTransactions = new Arraylist<>();
    for(Transaction t: transactions){
        if(t.getType() == Transaction.GROCERY){
            groceryTransactions.add(t);
        }
    }
    Collections.sort(groceryTransactions, new Comparator(){
        public int compare(Transaction t1, Transaction t2){
            return t2.getValue().compareTo(t1.getValue());
        }
    });
    List<Integer> transactionIds = new ArrayList<>();
    for(Transaction t: groceryTransactions){
        transactionsIds.add(t.getId());
    }
｝
```



在 Java 8 中使用 Stream 实现的版本更加简洁：

```java
List<Integer> transactionsIds = transactions.parallelStream().
    filter(t -> t.getType() == Transaction.GROCERY).
    sorted(comparing(Transaction::getValue).reversed()).
    map(Transaction::getId).
    collect(toList());
```



# 3）Stream 流



当我们使用一个流的时候，通常包括三个基本步骤：

- 获取一个数据源（source）
- 数据转换
- 执行操作获取想要的结果

每次转换，原有 Stream 对象不改变，返回一个新的 Stream 对象（可以有多次转换），这就允许对其操作可以像链条一样排列，变成一个管道。



## 1）创建操作

创建流的操作包含以下三种方式

```java
public class LambdaTest {

    public static void main(String[] args) {
        test02();
    }

    /**
     * Lambda 入门案例
     */
    private static void test01() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程 t1 执行");
            }
        }, "t1").start();


        // 使用 Lambda 表达式简化匿名内部类
        /**
         * 直接把参数列表和方法体拷过来
         *      只关心要操作的数据（参数列表）和要进行什么操作（方法体）
         *      () {
         *          System.out.println("线程 t1 执行");
         *      }
         *
         * Lambda 表达式的格式为 () -> {}，所以只需要再加一个 ->
         *      () -> {
         *          System.out.println("线程 t1 执行");
         *      }
         *
         * 简而言之，将匿名内部类的 参数列表 & 方法体 拷出来，中间再加一个 -> 就构成了 Lambda 表达式
         *
         */
        new Thread(() -> {
            System.out.println("线程 t2 执行");
        }, "t2").start();
    }

    /**
     * Lambda 的省略规则
     *      1、参数类型可以省略
     *      2、形参列表只有一个参数时，小括号可以省略
     *      3、方法体只有一句代码时，大括号 & return & 分号 都可以省略
     */
    private static void test02() {
        System.out.println("----- 匿名内部类形式-----");
        calculator((new IntFunction<Integer>() {
            @Override
            public Integer apply(int x) {
                return x * 2;
            }
        }));

        System.out.println("----- Lambda 形式-----");
        calculator((int x) -> {
            return x * 3;
        });

        System.out.println("----- Lambda 省略参数列表形式-----");
        calculator(x -> {
            return x * 4;
        });

        System.out.println("----- Lambda 省略大括号形式-----");
        calculator(x -> x * 5);
    }

    private static void calculator(IntFunction<Integer> function) {
        int x = 10;
        System.out.println(function.apply(x));
    }

}
```



## 2）中间操作

中间操作包含以下方式

```java
public class StreamTest {

    public static void main(String[] args) {
        test15();
    }

    /**
     * 创建 Stream 流的方式
     *      1、Collection 类的 stream()
     *      2、Arrays 类中的 stream()
     *      3、Stream 类中的 of()
     */
    private static void test01() {
        /**
         * JAVA8 在 Collection 类中新增了 stream()
         *      Stream<E> stream()
         *
         *  Collection 对象调用改方法后，返回一个 Stream 对象
         */
        System.out.println("---------- 方式一：单列集合 ----------");
        List<Integer> numberList = List.of(3, 4, 5, 5);
        numberList.stream().distinct().forEach(System.out::println);


        /**
         * JAVA8 在 Arrays 类中新增了 stream()
         *      Stream<T> stream(T[] array)
         */
        System.out.println("---------- 方式二：数组 ----------");
        Integer[] numberArray = {2, 3, 4, 4};
        Arrays.stream(numberArray).distinct().forEach(System.out::println);
        System.out.println("----------");
        // Stream 类中的 of()，底层调用的还是 Arrays.stream()  --> return Arrays.stream(values);
        Stream.of(numberArray).distinct().forEach(System.out::println);


        /**
         * 简单来说就是 Map 转 Collection，再用 Collection 中的 stream 方法
         *      Map 的 entrySet() 能实现双列转单列
         *          Set<Map.Entry<K, V>> entrySet()
         */
        System.out.println("---------- 方式三：双列集合 ----------");
        Map<String, Integer> map = new HashMap<>();
        map.put("一", 1);
        map.put("二", 2);
        map.put("二", 2);
        Set<Map.Entry<String, Integer>> entries = map.entrySet();
        entries.stream().distinct().forEach(System.out::println);

    }

    /**
     * distinct()：过滤重复的元素（依赖于 Object 的 equals()
     *      需求：去除重复的作家，并打印作家的姓名
     */
    private static void test02() {
        List<Author> authors = getAuthors();

        authors.stream()
                // 中间操作：对流中的元素进行去重
                .distinct()
                // 终结操作：遍历流中的各个元素
                .forEach(author -> System.out.println(author.getName()));

    }

    /**
     * filter()：根据指定的条件进行过滤元素，只保留满足条件的元素
     *      需求：过滤重复的作家 & 过滤年龄小于18的作家，并打印作家的姓名
     */
    private static void test03() {
        List<Author> authors = getAuthors();

        authors.stream()
                .distinct()
                // 中间操作：对流中的元素进行去重，依赖于 String 类的 compareTo()
                .filter(author -> author.getAge() < 18)
                // 终结操作：遍历流中的各个元素
                .forEach(author -> System.out.println(author.getName()));

    }

    /**
     * map()：对流中的每个元素应用指定的函数，并将结果映射为一个新的元素
     *      需求：打印所有作家的姓名
     */
    private static void test04() {
        List<Author> authors = getAuthors();
        authors.stream()
                // 对流中的各个元素，挨个进行计算或者转换，得到一个新的元素
                .map(author -> author.getName())
                .forEach(System.out::println);
    }

    /**
     * sorted(): 对流中的元素进行排序（依赖于比较器：Comparator 接口
     *      需求：对作家进行去重，并对作家的年龄进行升序排序，然后打印作家的姓名和年龄
     */
    private static void test05() {
        List<Author> authors = getAuthors();
        authors.stream()
                .distinct()
                .sorted(new Comparator<Author>() {
                    @Override
                    public int compare(Author o1, Author o2) {
                        return o1.getAge() - o2.getAge();
                    }
                })
                .forEach(author -> System.out.println("姓名：" + author.getName() + ", 年龄：" + author.getAge()));
    }

    /**
     * limit()：用于截断流，保留指定数量的元素，超出的部分直接抛弃
     *      skip 则用于跳过指定数量的元素
     *      需求：将作家按年龄降序排序，并切不能有重复元素，然后打印年龄最大的两个作家的姓名
     */
    private static void test06() {
        List<Author> authors = getAuthors();
        authors.stream()
                .distinct()
                .sorted(new Comparator<Author>() {
                    @Override
                    public int compare(Author o1, Author o2) {
                        return o2.getAge() - o1.getAge();
                    }
                })
                .limit(2)
                .map(author -> author.getName())
                .forEach(System.out::println);
    }

    /**
     * skip(): 跳过流中指定数量的元素
     *      需求：打印除了年龄最大的作家，并且不能有重复的作家，按照年龄降序排序
     */
    private static void test07() {
        List<Author> authors = getAuthors();
        authors.stream()
                .distinct()
                .sorted(new Comparator<Author>() {
                    @Override
                    public int compare(Author o1, Author o2) {
                        return o2.getAge() - o1.getAge();
                    }
                })
                .skip(1)
                .forEach(System.out::println);
    }

    /**
     * flatMap(): 将流中的每个元素都转换为一个流，然后将这些流连接起来成为一个新的流（类似于 union）
     *      需求1：打印所有书籍的名字，并且对重复的元素去重
     *      需求2：打印所有书籍的分类，并且要求对分类去重，且需要拆分逗号：哲学,爱情 -> [哲学,爱情]
     */
    private static void test08() {
        List<Author> authors = getAuthors();

        // 需求1：打印所有书籍的名字，并且对重复的元素去重
        System.out.println("---------- 需求1 ----------");
        authors.stream()
                .flatMap(new Function<Author, Stream<?>>() {
                    @Override
                    public Stream<?> apply(Author author) {
                        return author.getBooks().stream();
                    }
                })
                .distinct()
                .forEach(System.out::println);


        // 需求2：打印所有书籍的分类，并且要求对分类去重，且需要拆分逗号：哲学,爱情 -> [哲学,爱情]
        System.out.println("---------- 需求2 ----------");
        authors.stream()
                .flatMap(author -> author.getBooks().stream())
                .distinct()
                .flatMap(book -> Arrays.stream(book.getCategory().split(",")))
                .distinct()
                .forEach(System.out::println);

    }

    /**
     * forEach():
     *      需求：打印所有作家的名字，并且去重
     */
    private static void test09() {
        List<Author> authors = getAuthors();
        authors.stream()
                .map(author -> author.getName())
                .distinct()
                .forEach(authorName -> System.out.println(authorName));
    }

    /**
     * count(): 返回流中元素的个数
     *      需求：打印所有作家所出的书籍的总和，需要去重
     */
    private static void test10() {
        List<Author> authors = getAuthors();
        long count = authors.stream()
                .map(author -> author.getBooks())
                .flatMap(books -> books.stream())
                .distinct()
                .count();
        System.out.println(count);
    }

    /**
     * max(): 返回流中的最大值；min(): 返回流中的最小值
     *      需求：分别获取这些作家的所出书籍的最高评分和最低评分，并打印
     */
    private static void test11() {
        List<Author> authors = getAuthors();
        Optional<Integer> max = authors.stream()
                .flatMap(author -> author.getBooks().stream())
                .distinct()
                .map(book -> book.getScore())
                .max(((o1, o2) -> o1 - o2));

        Optional<Integer> min = authors.stream()
                .flatMap(author -> author.getBooks().stream())
                .distinct()
                .map(book -> book.getScore())
                .min(((o1, o2) -> o1 - o2));

        System.out.println("min = " + min.get());
        System.out.println("max = " + max.get());
    }

    /**
     * collect(): 将流中的元素收集到一个集合中，例如：List、Set 或 Map
     *      需求1：获取一个所有作者名字的 List 集合
     *      需求2: 获取一个 Map 集合，Map 的 key 为作家名，value 为 List<Book>
     */
    private static void test12() {
        // 需求1：获取一个所有作者名字的 List 集合
        List<String> authorNames = getAuthors().stream()
                .map(author -> author.getName())
                .distinct()
                .collect(Collectors.toList());

        authorNames.stream().forEach(System.out::println);

        // 需求2: 获取一个 Map 集合，Map 的 key 为作家名，value 为 List<Book>
        Map<String, List<Book>> authorMap = getAuthors().stream()
                .distinct()
                .collect(Collectors.toMap(Author::getName, Author::getBooks));

        authorMap.entrySet().stream().forEach(System.out::println);
    }

    /**
     * anyMatch(): 流中是否存在满足指定条件的元素
     * allMatch(): 流中的元素是否全部满足指定条件
     * noneMatch(): 流中的元素是否全部都不满足指定条件
     *      需求：判断是否有年龄在 29 以上的作家
     */
    private static void test13() {
        boolean anyMatch = getAuthors().stream()
                .anyMatch(author -> author.getAge() > 29);

        System.out.println("anyMatch: " + anyMatch);
    }

    /**
     * findFirst(): 获取流中的第一个元素
     * findAny(): 随机获取流中的一个元素
     *      需求：获取一个年龄最小的作家，并打印他的姓名
     */
    private static void test14() {
        Optional<Author> first = getAuthors().stream()
                .distinct()
                .sorted(new Comparator<Author>() {
                    @Override
                    public int compare(Author o1, Author o2) {
                        return o1.getAge() - o2.getAge();
                    }
                })
                .findFirst();
        first.ifPresent(author -> System.out.println(author.getName()));
    }

    /**
     * reduce(): 对流中的元素进行归约操作，可以用于求和、求最大值、最小值等
     *      需求1：使用 reduce() 求所有作者的年龄之和
     *      需求2：使用 reduce() 求所有作者中年龄的最大值
     *      需求3：使用 reduce() 求所有作者中年龄的最小值
     */
    private static void test15() {
        Integer sum = getAuthors().stream()
                .distinct()
                .map(author -> author.getAge())
                .reduce(0, new BinaryOperator<Integer>() {
                    @Override
                    public Integer apply(Integer result, Integer element) {
                        return result + element;
                    }
                });
        System.out.println(sum);
    }

    private static List<Author> getAuthors() {
        Author author1 = new Author(1L, "张三1", 33, "一个作者张三1", null);
        Author author2 = new Author(2L, "张三2", 15, "一个作者张三2", null);
        Author author3 = new Author(3L, "张三3", 14, "一个作者张三3", null);
        Author author4 = new Author(3L, "张三3", 14, "一个作者张三3", null);

        List<Book> books1 = new ArrayList<>();
        List<Book> books2 = new ArrayList<>();
        List<Book> books3 = new ArrayList<>();

        books1.add(new Book(1L, "书本1", "哲学,爱情", 88, "这是书本1"));
        books1.add(new Book(2L, "书本2", "个人,爱情", 99, "这是书本2"));

        books2.add(new Book(3L, "书本3", "哲学", 85, "这是书本1"));
        books2.add(new Book(3L, "书本3", "哲学", 85, "这是书本1"));
        books2.add(new Book(4L, "书本4", "爱情,传记", 56, "这是书本2"));

        books3.add(new Book(5L, "书本5", "爱情", 56, "这是书本5"));
        books3.add(new Book(6L, "书本6", "传记", 100, "这是书本6"));
        books3.add(new Book(6L, "书本6", "传记", 100, "这是书本6"));

        author1.setBooks(books1);
        author2.setBooks(books2);
        author3.setBooks(books3);
        author4.setBooks(books3);

        return new ArrayList<>(Arrays.asList(author1, author2, author3, author4));
    }

}
```



## 3）终结操作

