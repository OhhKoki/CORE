package org.example.optional;

import org.example.steam.Author;
import org.example.steam.Book;
import java.util.List;
import java.util.Optional;

public class OptionalTest {

    public static void main(String[] args) {
        test08();
    }

    /**
     * 创建 Optional 的三种方式
     *      1）使用静态方法 ofNullable() 创建一个即可空又可非空的 Optional 对象
     *      2）使用静态方法 of() 创建一个非空的 Optional 对象
     *      3）使用静态方法 empty() 创建一个空的 Optional 对象
     */
    private static void test01() {
        // 创建包含可能为空的值的 Optional 对象
        Optional<String> nullableOptional = Optional.ofNullable(null);

        // 创建包含值的 Optional 对象
        // 传递给 `of()` 方法的参数必须是非空的，也就是说不能为 null，否则仍然会抛出 NPE
        Optional<String> nonEmptyOptional = Optional.of("Hello");

        // 创建一个空的 Optional 对象
        Optional<String> emptyOptional = Optional.empty();
    }

    /**
     *  ifPresent(): 如果 Optional 的 value 不为 null，则执行传入的消费者函数
     *                  为 null 则不执行
     */
    private static void test02() {
        // 由于 nullOptional 的 value 值为 null，所以调用 ifPresent() 时不会执行
        Optional<String> nullOptional = Optional.ofNullable(null);
        nullOptional.ifPresent(System.out::println);

        System.out.println("----------");

        Optional<String> nonEmptyOptional = Optional.ofNullable("Hello");
        nonEmptyOptional.ifPresent(System.out::println);
    }

    /**
     *  orElseGet()：如果 Optional 的 value 不为 null，则返回 value 值
     *                  为 null 则执行传入的供给者函数
     */
    private static void test03() {
        // 由于 nullOptional 的 value 值为 null，所以执行传入的供给者函数（返回一个默认值 default1
        Optional<String> nullOptional = Optional.ofNullable(null);
        System.out.println(nullOptional.orElseGet(() -> "default1"));

        Optional<String> nonEmptyOptional = Optional.ofNullable("Hello");
        System.out.println(nonEmptyOptional.orElseGet(() -> "default2"));
    }

    /**
     * orElseThrow()：如果 Optional 的 value 不为 null，则返回 value 值
     *                  为 null 则执行传入的供给者函数（抛出一个异常
     */
    private static void test04() {
        // 由于 nullOptional 的 value 值为 null，所以抛出异常
        Optional<String> nullOptional = Optional.ofNullable(null);
        Optional<String> nonEmptyOptional = Optional.ofNullable("Hello");

        try {
            nullOptional.orElseThrow(() -> new RuntimeException("获取 nullOptional 的 value 值时出现异常！"));
            nonEmptyOptional.orElseThrow(() -> new RuntimeException("获取 nonEmptyOptional 的 value 值时出现异常！"));
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * filter()：如果当前 Optional 不符合 filter 的过滤条件，则返回 Optional.empty
     */
    private static void test05() {
        Optional<Author> authorOptional = getAuthor();
        authorOptional
                // 如果当前 Optional 不符合 filter 的过滤条件，则返回 Optional.empty
                .filter(author -> author.getAge() > 18)
                .ifPresent(author -> System.out.println(author.getName()));
    }

    /**
     * isPresent()：判断 Optional 的 value 属性是否为 null，如果不为 null，则返回 true
     */
    private static void test06() {
        boolean present = Optional.ofNullable(null).isPresent();
        System.out.println(present);
    }

    /**
     * map()：如果 Optional 的 value 不为 null，则执行传入的 Function 接口
     *      - 如果 Function 接口返回值不为 null，则将返回值封装到 Optional 并返回
     *      - 如果 Function 接口返回值为 null，则返回 Optional.empty
     */
    private static void test07() {
        Optional<Author> authorOptional = getAuthor();
        authorOptional.map(Author::getBooks)
                .ifPresent(System.out::println);
    }

    /**
     * flatMap()：
     *      如果 Optional 的 value 不为 null，则执行传入的 Function 接口，并返回 Optional 类型返回值
     *      如果 Optional 的 value 为 null，否则返回 Optional.empty
     */
    private static void test08() {
        Optional.ofNullable("hello")
                .flatMap(value -> Optional.ofNullable(value.toUpperCase()))
                .ifPresent(System.out::println);
    }

    private static Optional<Author> getAuthor() {
        Author author = new Author(1L, "张三", 33, "一个作者张三", null);
        Book book1 = new Book(1L, "书本1", "哲学,爱情", 88, "这是书本1");
        Book book2 = new Book(2L, "书本2", "个人,爱情", 99, "这是书本2");
        author.setBooks(List.of(book1, book2));
        return Optional.ofNullable(author);
    }

}
