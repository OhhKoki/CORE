### 1、什么是 Optional

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



对于上述案例，使用 Optional 简化

```java
Optional.of(user)
    .map(User::getAddress)
    .map(Address::getCountry)
    .map(Country::getIsocode)
    .ifPresent(String::toUpperCase);
```



### 2、创建 Optional 对象

创建 Optional 的三种方式有三种

- Optional<T> ofNullable(T value)

- Optional<T> of(T value)

- Optional<T> empty()

    ​    

#### 2.1 ofNullable

使用静态方法 `ofNullable()` 创建一个即可空又可非空的 Optional 对象

```java
Optional<String> nullableOptional = Optional.ofNullable(null);
Optional<String> nullableOptional = Optional.ofNullable("Hello");
```

`ofNullable()` 内部对 null 进行了处理（返回 `Optional.EMPTY`），推荐用这个方法

```java
public static <T> Optional<T> ofNullable(T value) {
    return value == null ? (Optional<T>) EMPTY : new Optional<>(value);
}
```



#### 2.2 of

使用静态方法 `of()` 创建一个非空的 Optional 对象

```java
Optional<String> nonEmptyOptional = Optional.of("Hello");
```

注意，传递给 `of()` 方法的参数必须是非空的，也就是说不能为 null，否则仍然会抛出 NullPointerException。

```java
// 会抛出 NPE
Optional<String> optnull = Optional.of(null);
```



#### 2.3 empty

使用静态方法 `empty()` 创建一个空的 Optional 对象

```java
Optional<String> emptyOptional = Optional.empty(); // Optional.EMPTY
```



### 3、Optional 常用方法

Optional 中的方法很多，这里只记录常用推荐的方法

- 安全地消费值

    - void ifPresent(Consumer<? super T> action)

        - 如果 Optional 的 value 不为 null，则执行传入的消费者函数

- 安全地获取值

    - T orElseGet(Supplier<? extends T> supplier)

        - 如果 Optional 的 value 不为 null，则返回 value 值

        - 为 null 则执行传入的供给者函数（返回一个默认值

            ​    

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



### 2.1) 安全消费

ifPresent()：如果 Optional 的 value 不为 null，则执行传入的消费者函数

```java
// 由于 nullOptional 的 value 值为 null，所以调用 ifPresent() 时不会执行
Optional<String> nullOptional = Optional.ofNullable(null);
nullOptional.ifPresent(System.out::println);

System.out.println("----------");

Optional<String> nonEmptyOptional = Optional.ofNullable("Hello");
nonEmptyOptional.ifPresent(System.out::println);
```



### 2.2) 安全获取

orElseGet() 和 orElseThrow() 的作用是类似的，都是当 Optional 的 value 值为 null 时，做一个默认处理。两者的区别在于：

- orElseGet()：执行 `Supplier` 函数，返回一个默认值

- orElseThrow()：执行 `Supplier` 函数，抛出一个异常

      

#### 1）orElseGet

如果 Optional 的 value 不为 null，则返回 value 值，为 null 则执行传入的供给者函数（翻译一个默认值

```java
// 由于 nullOptional 的 value 值为 null，所以执行传入的供给者函数（返回一个默认值 default1
Optional<String> nullOptional = Optional.ofNullable(null);
System.out.println(nullOptional.orElseGet(() -> "default1"));

Optional<String> nonEmptyOptional = Optional.ofNullable("Hello");
System.out.println(nonEmptyOptional.orElseGet(() -> "default2"));
```



#### 2）orElseThrow

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



### 2.4) 过滤数据

filter()：如果当前 Optional 不符合 filter 的过滤条件，则返回 Optional.EMPTY

```java
Optional<Author> authorOptional = getAuthor();
authorOptional
    // 如果当前 Optional 不符合 filter 的过滤条件，则返回 Optional.EMPTY
    .filter(author -> author.getAge() > 18)
    .ifPresent(author -> System.out.println(author.getName()));
```



### 2.5) 非空判断

ifPresent()：判断 Optional 的 value 属性是否为 null，如果不为 null，则返回 true

```java
boolean present = Optional.ofNullable(null).isPresent();
System.out.println(present);
```



### 2.6) 数据转换

map() 和 flatMap() 的作用基本一致，两者区别在于 Map方法会将函数执行结果封装到 Optional 中，然后返回

```java
public <U> Optional<U> map(Function<? super T, ? extends U> mapper) {
    Objects.requireNonNull(mapper);
    if (!isPresent()) {
        return empty();
    } else {
      	// 将执行结果封装到 Optional 后再返回
        return Optional.ofNullable(mapper.apply(value));
    }
}
```



而 flatMap 则是直接将执行结果返回，不做额外的封装（这个函数的存在是有必要的，因为有些函数的返回结果是 Optional<T>，如果使用使用 Map 进行处理，则会出现 Optional<Optional<T>>这种结构）

```java
public <U> Optional<U> flatMap(Function<? super T, ? extends Optional<? extends U>> mapper) {
    Objects.requireNonNull(mapper);
    if (!isPresent()) {
        return empty();
    } else {
        @SuppressWarnings("unchecked")
        Optional<U> r = (Optional<U>) mapper.apply(value);
      	// 直接将执行结果返回
        return Objects.requireNonNull(r);
    }
}
```



#### 1）map()

如果 Optional 的 value 为 null, 则返回 Optional.EMPTY；非空则执行传入的 `Function` 接口，并将执行结果封装到 Optional 中。

```java
Optional<Author> authorOptional = getAuthor();
authorOptional.map(Author::getBooks).ifPresent(System.out::println);
```



#### 2）flatMap()

如果 Optional 的 value 为 null，则返回 Optional.EMPTY；非空则执行传入的 `Function` 接口（直接返回）

```java
Optional.ofNullable("hello")
    .flatMap(value -> Optional.ofNullable(value.toUpperCase()))
    .ifPresent(System.out::println);  // HELLO
```



### 2.7) 使用案例

多层级嵌套结构

```java
class Outer {
    Nested nested;
    Nested getNested() {
        return nested;
    }
}

class Nested {
    Inner inner;
    Inner getInner() {
        return inner;
    }
}

class Inner {
    String foo;
    String getFoo() {
        return foo;
    }
}
```

Java8 以前

```java
Outer outer = new Outer();
if (outer != null && outer.nested != null && outer.nested.inner != null) {
    System.out.println(outer.nested.inner.foo);
}
```

Java8 以后

```java
Optional.of(new Outer())
    .map(Outer::getNested)
    .map(Nested::getInner)
    .map(Inner::getFoo)
    .ifPresent(System.out::println);
```
