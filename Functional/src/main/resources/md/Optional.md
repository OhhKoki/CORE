# 1）前言概述

在 Java 中，由于 null 值的存在，导致在访问对象的方法或属性时，都有可能会导致 `NullPointerException`。比如这段代码

```java
String isocode = user.getAddress().getCountry().getIsocode().toUpperCase();
```

如果我们需要确保不触发 `NPE`，就得在访问每一个值之前对其进行明确地检查

```java
if (user != null) {
    Address address = user.getAddress();
    if (address != null) {
        Country country = address.getCountry();
        if (country != null) {
            String isocode = country.getIsocode();
            if (isocode != null) {
                isocode = isocode.toUpperCase();
            }
        }
    }
}
```

为了解决由于 null 导致的过多检查，Java 8 引入 Optional 类。本质上，这是一个包含有可选值的包装类，这意味着 Optional 类既可以含有对象也可以为空。



使用 Optional 优化上述案例

```java
Optional.ofNullable(user).
```



# 2）创建 Optional 对象

创建 Optional 的三种方式有三种

- Optional<T> ofNullable(T value)

- Optional<T> of(T value)

- Optional<T> empty()

    

## 2.1) ofNullable

使用静态方法 `ofNullable()` 创建一个即可空又可非空的 Optional 对象

```java
Optional<String> nullableOptional = Optional.ofNullable(null);
Optional<String> nullableOptional = Optional.ofNullable("Hello");
```

`ofNullable()` 内部对 null 进行了处理（返回 `Optional.empty`），推荐用这个方法

```java
public static <T> Optional<T> ofNullable(T value) {
    return value == null ? (Optional<T>) EMPTY : new Optional<>(value);
}
```



## 2.2) of

使用静态方法 `of()` 创建一个非空的 Optional 对象

```java
Optional<String> nonEmptyOptional = Optional.of("Hello");
```

注意，传递给 `of()` 方法的参数必须是非空的，也就是说不能为 null，否则仍然会抛出 NullPointerException。

```java
// 会抛出 NPE
Optional<String> optnull = Optional.of(null);
```



## 2.3) empty

使用静态方法 `empty()` 创建一个空的 Optional 对象

```java
Optional<String> emptyOptional = Optional.empty(); // Optional.empt
```



# 3）Optional 常用方法

Optional 中的方法很多，这里只记录最最最推荐的方法

- 安全地消费值

    - void ifPresent(Consumer<? super T> action)

        - 如果 Optional 的 value 不为 null，则返回 value 值

        

- 安全地获取值

    - T orElseGet(Supplier<? extends T> supplier)

        - 如果 Optional 的 value 不为 null，则返回 value 值

        - 为 null 则执行传入的供给者函数（返回一个默认值

            

    - T orElseThrow(Supplier<? extends X> exceptionSupplier)

        - 如果 Optional 的 value 不为 null，则返回 value 值

        - 为 null 则执行传入的供给者函数（抛出一个异常

            

- 非空判断

    - boolean isPresent()

        - 判断 Optional 的 value 值是否为 null

            

- 过滤数据

    - Optional<T> filter(Predicate<? super T> predicate)

        - 如果当前 Optional 不符合 filter 的过滤条件，则返回 Optional.empty

        - 其实就相当于一个 if 判断，类似于 `if(user.getAge() > 18)` ，如果符合条件在进行其他操作

            

- 数据转换

    - Optional<U> map(Function<? super T, ? extends U> mapper)
    - Optional<U> flatMap(Function<? super T, ? extends Optional<? extends U>> mapper)



## 3.1) 安全消费

ifPresent()：如果 Optional 的 value 不为 null，则执行传入的消费者函数

```java
// 由于 nullOptional 的 value 值为 null，所以调用 ifPresent() 时不会执行
Optional<String> nullOptional = Optional.ofNullable(null);
nullOptional.ifPresent(System.out::println);

System.out.println("----------");

Optional<String> nonEmptyOptional = Optional.ofNullable("Hello");
nonEmptyOptional.ifPresent(System.out::println);
```

执行结果

```java
----------
Hello
```



## 3.2) 安全获取

orElseGet() 和 orElseThrow() 的作用是类似的，都是当 Optional 的 value 值为 null 时，做一个默认处理。两者的区别在于：

- orElseGet()：返回一个默认值
- orElseThrow()：抛出一个异常

### 1）orElseGet

如果 Optional 的 value 不为 null，则返回 value 值，为 null 则执行传入的供给者函数

```java
// 由于 nullOptional 的 value 值为 null，所以执行传入的供给者函数（返回一个默认值 default1
Optional<String> nullOptional = Optional.ofNullable(null);
System.out.println(nullOptional.orElseGet(() -> "default1"));

Optional<String> nonEmptyOptional = Optional.ofNullable("Hello");
System.out.println(nonEmptyOptional.orElseGet(() -> "default2"));
```

执行结果

```java
default1
Hello
```



### 2）orElseThrow

如果 Optional 的 value 不为 null，则返回 value 值，为 null 则执行传入的供给者函数（抛出一个异常

```java
// 由于 nullOptional 的 value 值为 null，所以抛出异常
Optional<String> nullOptional = Optional.ofNullable(null);
Optional<String> nonEmptyOptional = Optional.ofNullable("Hello");

try {
    nullOptional.orElseThrow(() -> new RuntimeException("获取 nullOptional 时出现异常！"));
    nonEmptyOptional.orElseThrow(() -> new RuntimeException("获取 nonEmptyOptional 时出现异常！"));
}catch (Exception e) {
    System.out.println(e.getMessage());
}
```

执行结果

```java
获取 nonEmptyOptional 时出现异常！
```



## 3.4) 过滤数据

filter()：如果当前 Optional 不符合 filter 的过滤条件，则返回 Optional.empty

```java
Optional<Author> authorOptional = getAuthor();
authorOptional
    // 如果当前 Optional 不符合 filter 的过滤条件，则返回 Optional.empty
    .filter(author -> author.getAge() > 18)
    .ifPresent(author -> System.out.println(author.getName()));
```

执行结果

```java
张三
```



## 3.5) 非空判断

ifPresent()：判断 Optional 的 value 属性是否为 null，如果不为 null，则返回 true

```java
boolean present = Optional.ofNullable(null).isPresent();
System.out.println(present);
```

执行结果

```java
false
```



## 3.6) 数据转换

map() 和 flatMap() 的作用基本一致，两者区别在于 Map方法会讲返回结果封装到 Optional 中，而 flatMap 则是直接返回

```java
public <U> Optional<U> map(Function<? super T, ? extends U> mapper) {
    Objects.requireNonNull(mapper);
    if (!isPresent()) {
        return empty();
    } else {
        return Optional.ofNullable(mapper.apply(value));
    }
}
```



### 1）map()

数据转换，对 Optional 执行传入的消费者函数

```java
Optional<Author> authorOptional = getAuthor();
authorOptional.map(Author::getBooks).ifPresent(System.out::println);
```

执行结果

```java
[Book(id=1, name=书本1, category=哲学,爱情, score=88, intro=这是书本1), Book(id=2, name=书本2, category=个人,爱情, score=99, intro=这是书本2)]
```



### 2）flatMap()