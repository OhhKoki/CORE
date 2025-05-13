### 1、什么是 Lambda

Lambda 表达式是 Java 8 的重要更新，它支持将代码块作为方法参数、允许使用更简洁的代码来创建函数式接口的实例。



Lambda 表达式的主要作用就是可以用于简化创建匿名内部类对象。Lambda 表达式的代码块将会用于实现抽象方法的方法体，Lambda 表达式就相当于一个匿名方法。



Lambda 表达式由三部分组成：

- 形参列表：形参列表允许省略类型，如果形参列表中只有一个参数，形参列表的圆括号也可以省略；
- 箭头（`->`）：通过英文画线和大于符号组成；
- 代码块：如果代码块只有一条语句，花括号可以省略。Lambda 代码块只有一条 return 语句，可以省略 return 关键字，Lambda 表达式会自动返回这条语句的值作为返回值。



这是一个 Lambda 表达式的使用案例

```java
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
```



### 2、目标类型

Lambda 表达式的类型，也被称为「目标类型（`target type`）」。目标类型必须是「函数式接口（`functional interface`）」。



函数式接口就是只包含一个抽象方法的接口。函数式接口可以包含多个默认方法、类方法，但仅能声明一个抽象方法（比如 Runnable

> Java 8 专门为函数式接口提供了 `@FunctionalInterface` 注解。该注解用于告诉编译器校验接口必须是函数式接口，否则就报错。



由于 Lambda 表达式的结果就是被当做对象/实例，因此，可以使用 Lambda 表达式进行赋值

```java
Runnable r = () -> {
    for (int i = 0; i < 10; i++) {
        System.out.println(i);
    }
};
```

这是 Runnable 接口的定义

```java
@FunctionalInterface
public interface Runnable {
    public abstract void run();
}
```

看一个错误示例

```java
Object o = () -> {
    for (int i = 0; i < 10; i++) {
        System.out.println(i);
    }
};
```



上面这段代码会报错：`Target type of a lambda conversion must be an interface`。Lambda 表达式的目标类型必须是明确的函数式接口！将 Lambda 表达式赋值给 Object 类型的变量，编译器只能推断出它的表达类型为 Object，而 Object 并不是函数式接口，因此就报错了！



为了保证 Lambda 表达式的目标类型是明确的函数式接口，有如下两种常见方式：

- 将 Lambda 表达式赋值给函数式接口类型的变量；
- 将 Lambda 表达式作为函数式接口类型的参数传给某个方法；



综上，Lambda 表达式的本质很简单，就是使用简单的语法来创建函数式接口的实例，避免匿名内部类的繁琐。



### 3、方法引用

如果 Lambda 表达式的代码块只有一条代码，还可以在代码中使用方法引用和构造器引用。

方法引用和构造器引用的好处是使 Lambda 表达式的代码块更加简洁。方法引用和构造器引用都需要使用两个英文冒号 `::`。



方法引用的类型与作用如下表格

| 种类                   | 示例               | 说明                                                         | 对应的 Lambda 表达式                      |
| ---------------------- | ------------------ | ------------------------------------------------------------ | ----------------------------------------- |
| 引用类方法             | 类名::类方法       | 函数式接口中被实现的方法的全部参数传给该类方法作为参数       | `(a,b,...) -> 类名.类方法(a,b,...)`       |
| 引用特定对象的实例方法 | 特定对象::实例方法 | 函数式接口中被实现的方法的全部参数传给该方法作为参数         | `(a,b,...) -> 特定对象.实例方法(a,b,...)` |
| 引用某类对象的实例方法 | 类名::实例方法     | 函数式接口中被实现的方法的第一个参数作为调用者，后面的参数全部传给该方法作为参数 | `(a,b,...)-> a.实例方法(b,...)`           |
| 引用构造器             | 类名::new          | 函数式接口中被实现方法的全部参数传给该构造器作为参数         | `(a,b,...)-> new 类名(a,b,...)`           |



方法引用案例如下

```java
@FunctionalInterface
interface Converter {
    Integer convert(String from);
}

@FunctionalInterface
interface Test1 {
    String test(String a, int b, int c);
}

@FunctionalInterface
interface Test2 {
    User getUser(String name);
}

public class LambdaRef {
    public static void main(String[] args) {
        // 1 引用类方法
        // 下面使用 Lambda 表达式创建 Converter 对象
        Converter converter1 = from -> Integer.valueOf(from);
        Integer val = converter1.convert("99");

        // 函数式接口中被实现方法的全部参数传给该类方法作为参数
        Converter converter2 = Integer::valueOf;
        Integer val2 = converter2.convert("100");

        // 2 引用特定对象的实例方法
        // 使用 Lmabda 表达式创建 Converter 对象
        Converter converter3 = from -> "hello world".indexOf(from);

        // 调用 "hello world" 这个对象的 indexOf() 实例方法
        // 函数式接口中被实现的全部参数传给该方法作为参数
        Converter converter4 = "hello world"::indexOf;

        // 3 引用某类对象的实例方法
        // 使用 Lambda 表达式创建 Test1 对象
        Test1 t1 = (a, b, c) -> a.substring(b, c);
        String  subStr1 = t1.test("Hello World", 1, 3);

        // 上面 Lambda 表达式只有一行，因此可以使用如下引用进行替换
        // 函数式接口中被实现方法的第一个参数作为调用者，后面的参数全部传给该方法作为参数
        MyTest subStr2 = String::substring;

        // 4 引用构造器
        // 使用 Lambda 表达式创建 Test2 对象
        Test2 test21 = name -> new User(name);
        User zs = test21.getUser("zhangsan");

        // 使用构造器引用进行替换
        // 函数式接口中被实现方法的全部参数传给该构造器作为参数
        Test2 test22 = User::new;
        User ls = test22.getUser("lisi");
    }
}
```

