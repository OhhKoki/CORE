package org.example.steam;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class TestStream {

    public static void main(String[] args) {
        test08();
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
