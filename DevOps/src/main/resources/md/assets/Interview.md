## 001、JVM & JDK & JRE 的关系

JVM（Java Virtual Machine，Java虚拟机）、JDK（Java Development Kit，Java开发工具包）和JRE（Java Runtime Environment，Java运行环境）是Java生态系统中的三个核心组件，它们的关系如下：

1. **JVM** 是一个虚拟机，它使得Java程序能够在不同的平台上运行。它负责执行字节码（.class文件），通过解释或即时编译（JIT）将字节码转化为特定平台的机器码。
2. **JRE** 是 Java 运行环境，它包含了 JVM 以及一些用来运行 Java 程序的核心库和文件。简而言之，JRE 提供了执行 Java 程序所需要的环境，但并不包括开发工具。
3. **JDK** 是 Java 开发工具包，它包含了 JRE 和一些开发 Java 程序所需的工具，如编译器（javac）、调试工具、文档生成工具等。换句话说，JDK 提供了开发、运行和调试 Java 程序的完整环境。

总结：

- **JDK** = **JRE** + 开发工具

- **JRE** = **JVM** + 运行 Java 程序所需的库

- **JVM** 负责执行 Java 程序的字节码

    

## 002、什么是Java的字节码？

Java的字节码是Java源代码编译后的中间表示形式。当你编写Java程序并使用Java编译器（`javac`）将源代码（`.java`文件）编译成字节码时，结果是一个`.class`文件。字节码本质上是一种平台无关的中间语言，它并不是直接运行在计算机的硬件上，而是通过Java虚拟机（JVM）来执行的。

JVM会将字节码解释成特定平台上可执行的机器码，然后运行程序。这种机制使得Java程序具备了跨平台的特性，只要在不同的平台上都安装了JVM，Java程序就能运行。



## 003、continue、break 和 return 的区别是什么？

`continue`、`break` 和 `return` 都是用来控制程序流的关键字，但它们的作用和使用场景有所不同：

1. **continue**：
   - 用来跳过当前循环的剩余部分，直接进入下一次循环。
   - 适用于 `for`、`while`、`do-while` 循环中。
   - 它只影响当前的循环，而不会退出整个循环。

   示例：
   ```python
   for i in range(5):
       if i == 3:
           continue  # 跳过 i == 3 的时候
       print(i)
   # 输出：0 1 2 4
   ```

2. **break**：
   - 用来终止当前循环的执行，跳出整个循环。
   - 适用于 `for`、`while`、`do-while` 循环中。
   - 一旦执行 `break`，循环就会立刻结束。

   示例：
   ```python
   for i in range(5):
       if i == 3:
           break  # 当 i == 3 时，退出循环
       print(i)
   # 输出：0 1 2
   ```

3. **return**：
   - 用来从函数中返回，结束函数的执行。
   - 当 `return` 被执行时，函数的控制权会返回到调用该函数的地方，并且可以返回一个值。
   - 它会导致函数退出，而不再执行函数中的后续代码。

   示例：
   ```python
   def example():
       print("Start")
       return 42  # 返回并结束函数
       print("This will not be printed")
   
   result = example()
   print(result)  # 输出：42
   ```

总结：
- **continue** 只跳过当前循环的剩余部分，继续下一次循环。
- **break** 终止整个循环。
- **return** 结束当前函数的执行，并返回一个值（如果有的话）。



## 004、自增自减运算符（`++` 和 `--`）

自增自减运算符（`++` 和 `--`）是常用的算术运算符，用来对变量的值进行增加或减少。

1. **自增运算符（`++`）**：
   - **前置自增（`++a`）**：先将变量`a`的值增加1，再使用该值。
   - **后置自增（`a++`）**：先使用变量`a`的当前值，再将`a`的值增加1。

   例子：
   ```cpp
   int a = 5;
   int b = ++a;  // a先加1，b为6，a也变为6
   int c = a++;  // c为6，a加1后变为7
   ```

2. **自减运算符（`--`）**：
   - **前置自减（`--a`）**：先将变量`a`的值减少1，再使用该值。
   - **后置自减（`a--`）**：先使用变量`a`的当前值，再将`a`的值减少1。

   例子：
   ```cpp
   int a = 5;
   int b = --a;  // a先减1，b为4，a也变为4
   int c = a--;  // c为4，a减1后变为3
   ```

总结：
- **前置运算符**（`++a` 或 `--a`）会先改变变量的值，然后使用修改后的值。
- **后置运算符**（`a++` 或 `a--`）会先使用变量的原值，然后再改变变量的值。



## 005、Java 中的几种基本数据类型有哪些？

Java 中的基本数据类型有 8 种：

1. **byte**：8 位整数，范围从 -128 到 127。
2. **short**：16 位整数，范围从 -32,768 到 32,767。
3. **int**：32 位整数，范围从 -2^31 到 2^31-1。
4. **long**：64 位整数，范围从 -2^63 到 2^63-1。
5. **float**：32 位单精度浮点数。
6. **double**：64 位双精度浮点数。
7. **char**：16 位 Unicode 字符。
8. **boolean**：布尔类型，值为 `true` 或 `false`。



## 006、什么是包装类？

包装类（Wrapper Class）是Java中用于将基本数据类型（如int、char、double等）封装成对象的类。因为Java的基本数据类型（primitive types）不能作为对象直接传递给方法或存储在集合中，所以包装类提供了一种将基本数据类型转换为对象的方式。

Java为每种基本数据类型都提供了对应的包装类，具体如下：

- `byte` → `Byte`
- `short` → `Short`
- `int` → `Integer`
- `long` → `Long`
- `float` → `Float`
- `double` → `Double`
- `char` → `Character`
- `boolean` → `Boolean`

包装类的主要作用包括：
1. **对象化**：包装类允许将基本数据类型封装为对象，可以方便地用于集合（如List、Set等）中。
2. **提供更多功能**：包装类提供了许多方法来处理数据类型的转换、比较、解析等操作，比如`Integer.parseInt()`将字符串转换为整数，`Double.valueOf()`将字符串转换为浮点数等。

例如：
```java
int num = 10;
Integer numObject = Integer.valueOf(num);  // 基本数据类型转为包装类对象
int newNum = numObject.intValue();  // 包装类对象转回基本数据类型
```



## 007、基本类型于包装类怎么相互转换？

在Java中，基本类型（如`int`、`boolean`、`char`）和对应的包装类（如`Integer`、`Boolean`、`Character`）可以通过以下方式相互转换：

### **1. 基本类型 → 包装类**

- **手动装箱（Boxing）**  
  使用包装类的构造函数或静态方法`valueOf()`显式转换：
  ```java
  int num = 10;
  Integer integerObj1 = new Integer(num);  // 构造函数（已过时，不推荐）
  Integer integerObj2 = Integer.valueOf(num); // 推荐方式（可能复用缓存）
  ```

- **自动装箱（Autoboxing）**  
  Java编译器自动完成转换（JDK5+支持）：
  ```java
  int num = 10;
  Integer integerObj = num; // 直接赋值，自动装箱
  ```

---

### **2. 包装类 → 基本类型**
- **手动拆箱（Unboxing）**  
  调用包装类的`xxxValue()`方法：
  ```java
  Integer integerObj = 20;
  int num = integerObj.intValue(); // 显式调用方法
  ```

- **自动拆箱（Auto-unboxing）**  
  Java编译器自动完成转换：
  
  ```java
  Integer integerObj = 20;
  int num1 = integerObj; // 直接赋值，自动拆箱
  int num2 = integerObj + 5; // 运算时自动拆箱
  ```

### **注意事项**

1. **空指针风险**  
   如果包装类对象为`null`，拆箱时会抛出`NullPointerException`：
   
   ```java
   Integer nullObj = null;
   int num = nullObj; // 运行时抛出NullPointerException
   ```
   
2. **性能差异**  
   自动装箱可能隐含对象创建（如未命中缓存），频繁操作时需注意内存开销。

3. **缓存机制**  
   包装类的`valueOf()`方法对部分值（如`Integer`的-128~127）会复用缓存对象，而非创建新实例。

---

### **示例代码**

```java
// 自动装箱
int a = 100;
Integer boxed = a; 

// 自动拆箱
Integer b = 200;
int unboxed = b;

// 显式装箱拆箱
Double d = Double.valueOf(3.14);
double pi = d.doubleValue();
```

通过自动装箱/拆箱，代码更简洁，但需谨慎处理潜在的`null`值和性能问题。



## 008、包装类的缓存机制是什么？

在 Java 中，包装类（如 `Integer`、`Double`、`Character` 等）具有缓存机制，主要是通过缓存一部分常用的对象实例来提高性能，减少内存的消耗。具体来说，包装类的缓存机制存在于以下几个方面：

### 1. **`Integer` 类的缓存机制**
对于 `Integer` 类，Java 会缓存从 `-128` 到 `127` 之间的整数对象。在这个范围内的整数值都会被复用，避免重复创建对象。这个范围的整数值会在 `Integer` 类的内部缓存中共享。

例如：
   ```java
   Integer a = 100;
   Integer b = 100;
   System.out.println(a == b);  // 输出 true，因为缓存中的对象是同一个
   ```

   ```java
   Integer a = 200;
   Integer b = 200;
   System.out.println(a == b);  // 输出 false，因为 200 超出了缓存范围，创建了不同的对象
   ```

 这个缓存范围是通过 `Integer.valueOf(int)` 方法实现的。`valueOf()` 方法首先会检查传入的值是否在缓存范围内，如果在缓存范围内，它会返回缓存中的对象；如果超出了范围，就会新建一个 `Integer` 对象。

### 2. **其他包装类的缓存机制**
 类似于 `Integer`，Java 对其他一些包装类（如 `Byte`、`Short`、`Character`）也有缓存机制。例如：
   - `Byte` 会缓存 `-128` 到 `127` 之间的值。
   - `Character` 会缓存从 `\u0000` 到 `\u007F` 的字符。
   - `Short` 也会缓存 `-128` 到 `127` 之间的值。

   这些缓存机制也通过相应包装类的 `valueOf()` 方法实现。

### 3. **`Boolean` 类**
 对于 `Boolean` 类，Java 只会缓存 `true` 和 `false` 两个值。所以对于 `Boolean.TRUE` 和 `Boolean.FALSE`，它们是相同的实例。

### 4. **缓存的实现方式**
 对于 `Integer` 和其他一些缓存类，它们通过 `valueOf()` 方法与 `Cache` 内部缓存结合。例如：
   ```java
   public static Integer valueOf(int i) {
       if (i >= IntegerCache.low && i <= IntegerCache.high)
           return IntegerCache.cache[i + 128];
       return new Integer(i);
   }
   ```

 这里，`IntegerCache.cache` 就是保存常用 `Integer` 对象的缓存数组，`low` 和 `high` 定义了缓存范围。

### 总结
包装类的缓存机制通过缓存常用值来减少对象的创建和内存消耗，提高程序的性能。对于 `Integer` 类，缓存机制在 `-128` 到 `127` 之间的值上生效，其他包装类（如 `Byte`、`Short`、`Character`）也有类似的机制，`Boolean` 类则只缓存 `true` 和 `false`。



## 009、成员变量与局部变量的区别？

成员变量和局部变量是程序中常见的两种变量类型，它们的主要区别如下：

1. **定义位置**：

    - **成员变量**：定义在类内部，但不在方法内部。它们属于类的对象（实例变量）或类本身（静态变量）。
    - **局部变量**：定义在方法内部或代码块内，仅在该方法或代码块内有效。

2. **作用域**：

    - **成员变量**：作用域为整个类，可以在类中的所有方法中访问。如果是静态变量，还可以通过类名直接访问。
    - **局部变量**：作用域仅限于声明它的方法或代码块，方法调用结束后，局部变量就会被销毁。

3. **生命周期**：

    - **成员变量**：在对象创建时分配内存，直到对象被销毁时才释放内存（对于静态变量，程序结束时释放）。
    - **局部变量**：在方法调用时分配内存，方法调用结束后即被销毁。

4. **默认值**：

    - **成员变量**：如果没有显式初始化，成员变量会自动赋予默认值。例如，整型为0，布尔型为`false`，对象类型为`null`。
    - **局部变量**：局部变量在使用前必须显式初始化，否则会报编译错误。它们没有默认值。

5. **存储位置**：

    - **成员变量**：存储在堆内存中（对于实例变量），或者存储在方法区（对于静态变量）。

    - **局部变量**：存储在栈内存中。

        

总结来说，成员变量是类的一部分，具有较长的生命周期和更广的作用域；而局部变量是方法的一部分，生命周期较短，作用域有限。



## 010、什么是静态变量（static）

**静态变量（static）**是指在类中声明时使用 `static` 关键字修饰的变量。静态变量与普通的实例变量有一些显著的区别，主要体现在以下几个方面：

1. **类级别的变量**：
    - 静态变量属于类本身，而不是类的某个实例。它在所有实例之间共享同一个值。
    - 每个类只有一份静态变量，不管创建多少个实例，静态变量始终只有一份内存空间。
2. **内存分配**：
    - 静态变量在程序运行时被分配内存，并且在类加载时就初始化。它在内存中只会存在一份，直到类被卸载。
    - 实例变量是每个对象独立的，每个对象都有自己的一份实例变量。
3. **访问方式**：
    - 静态变量可以通过类名直接访问，如 `ClassName.staticVariable`，也可以通过对象访问，但不推荐通过对象访问静态变量，因为静态变量与对象无关。
    - 实例变量则必须通过对象来访问。
4. **生命周期**：
    - 静态变量的生命周期从类加载开始，直到类卸载。它在类的所有实例之间共享。
    - 实例变量的生命周期则与对象的生命周期一致，只有在对象创建时才存在，且对象销毁时会被回收。
5. **初始化**：
    - 静态变量会在类加载时初始化，可以使用 `static` 块进行初始化。
    - 实例变量则在创建对象时进行初始化。

**示例：**

```java
public class Example {
    // 静态变量
    static int staticVar = 0;
    
    // 实例变量
    int instanceVar = 0;
    
    public static void main(String[] args) {
        Example obj1 = new Example();
        Example obj2 = new Example();
        
        obj1.staticVar = 5;
        obj1.instanceVar = 10;
        
        // 静态变量对于所有实例都是共享的
        System.out.println(obj1.staticVar); // 输出 5
        System.out.println(obj2.staticVar); // 输出 5
        System.out.println(obj1.instanceVar); // 输出 10
        System.out.println(obj2.instanceVar); // 输出 0
    }
}
```

在上面的例子中，`staticVar` 是静态变量，所有的 `Example` 类实例共享它的值；而 `instanceVar` 是实例变量，每个对象都有独立的实例变量。



## 011、final 关键字的作用

`final` 关键字在 Java 中有几个重要的作用，具体如下：

1. **修饰变量：**

    - 当 `final` 用于修饰变量时，表示这个变量的值在初始化后不能再修改。对于基本数据类型，它的值一旦被赋值后就不能再改变；对于引用数据类型，它的引用地址不能改变，但引用的数据内容是可以修改的。

    ```java
    final int x = 10;
    // x = 20; // 编译错误，不能改变值
    ```

2. **修饰方法：**

    - 当 `final` 用于修饰方法时，表示这个方法不能被子类重写。这样做可以确保方法的实现不会被改变，通常用于一些安全性或性能优化考虑的场景。

    ```java
    class Parent {
        final void show() {
            System.out.println("This is a final method.");
        }
    }
    class Child extends Parent {
        // void show() { // 编译错误，不能重写父类的 final 方法
        //    System.out.println("Trying to override.");
        // }
    }
    ```

3. **修饰类：**

    - 当 `final` 用于修饰类时，表示这个类不能被继承。即不能创建这个类的子类。

    ```java
    final class FinalClass {
        // 代码
    }
    // class SubClass extends FinalClass { // 编译错误，无法继承
    //    // 代码
    // }
    ```



总结一下，`final` 关键字用于确保不被修改的东西，增强了代码的安全性和可靠性，特别是在多线程环境下可以避免不必要的修改。



## 012、字符型常量和字符串常量的区别?

字符型常量和字符串常量的区别主要体现在它们的类型和用途上：

1. **字符型常量**（Character Constant）：
    - 由单引号包围的单个字符，例如：`'a'`、`'1'`、`'#'`。
    - 它是一个单一的字符，表示一个字符数据类型（通常是 `char`）。
    - 在内存中，字符型常量占用一个字节（8位）。
2. **字符串常量**（String Constant）：
    - 由双引号包围的一串字符，例如：`"hello"`、`"123"`、`"world!"`。
    - 它是一个字符数组，表示一个字符串数据类型（通常是 `char[]`）。
    - 在内存中，字符串常量占用多个字节（包括字符串的内容和结束符 `\0`，即空字符）。

**总结**：

- **字符型常量**是单个字符，通常占用一个字节。
- **字符串常量**是由多个字符组成，通常占用多个字节（包括结尾的空字符）。



## 013、静态方法为什么不能调用非静态成员?

静态方法不能调用非静态成员的原因在于静态方法和非静态成员的生命周期及所属对象不同：

1. **静态方法属于类，而非静态成员属于对象**：
    静态方法是属于类本身的，而非静态成员变量（字段）是属于类的实例对象的。在没有创建对象实例时，静态方法就已经可以被调用。因此，静态方法并不知道某个对象的状态，也无法直接访问非静态成员。
2. **非静态成员需要对象实例**：
    非静态成员（如实例变量、实例方法）需要依赖一个类的实例来访问。这是因为每个对象都有自己的独立状态（即非静态成员）。而静态方法没有对象实例，它只能操作静态变量和静态方法，因为这些是属于类的。
3. **实例化对象的过程**：
    静态方法在没有实例化对象之前就可以调用，而非静态成员则需要先创建一个对象实例，再通过该实例来访问。静态方法中如果要访问非静态成员，必须先创建该类的一个实例对象。

简单来说，静态方法无法直接访问非静态成员，因为它们不依赖于任何特定的对象实例，而非静态成员需要通过对象实例来访问。



## 014、重写和重载是什么？有什么区别

在面向对象编程中，**重写（Override）**和**重载（Overload）**是两种常见的概念，它们在功能和使用场景上有所不同。

### 1. **重写（Override）**

重写是指子类对父类方法的重新定义。当子类继承父类时，如果子类提供了与父类相同方法签名（方法名、参数类型、数量等）的方法，便是重写。

#### 特点：

- **方法签名相同**：子类重写父类方法时，方法名、参数类型和参数个数必须与父类方法一致。
- **父类方法的实现被覆盖**：子类方法会覆盖父类方法的实现，调用的是子类版本的方法。
- **运行时多态**：通常与多态结合使用，通过父类引用指向子类对象时，实际调用的是子类重写的方法。
- **使用场景**：用于扩展父类方法的功能或修改其实现。

#### 示例：

```java
class Animal {
    void speak() {
        System.out.println("Animal speaks");
    }
}

class Dog extends Animal {
    @Override
    void speak() {
        System.out.println("Dog barks");
    }
}

public class Test {
    public static void main(String[] args) {
        Animal animal = new Dog();
        animal.speak(); // 输出 "Dog barks"
    }
}
```

在这个例子中，`Dog` 类重写了 `Animal` 类的 `speak` 方法，调用的是子类的 `speak` 方法。

### 2. **重载（Overload）**

重载是指在同一个类中，定义多个方法名相同但参数不同（可以是参数类型、数量或顺序不同）的方法。

#### 特点：

- **方法名相同，参数不同**：重载方法在同一个类中，方法名相同但参数的类型、数量或顺序不同。
- **编译时多态**：在编译时决定调用哪个版本的方法。
- **不改变方法的实现**：每个重载方法有不同的参数列表，但方法体是不同的。
- **使用场景**：常用于处理相似的功能，但参数不同的情况。

#### 示例：

```java
class Calculator {
    int add(int a, int b) {
        return a + b;
    }

    double add(double a, double b) {
        return a + b;
    }

    int add(int a, int b, int c) {
        return a + b + c;
    }
}

public class Test {
    public static void main(String[] args) {
        Calculator calc = new Calculator();
        System.out.println(calc.add(2, 3));       // 输出 5
        System.out.println(calc.add(2.5, 3.5));   // 输出 6.0
        System.out.println(calc.add(1, 2, 3));    // 输出 6
    }
}
```

在这个例子中，`add` 方法被重载了三次，它们有不同的参数类型或数量。

### **重写与重载的区别**

| 特点     | 重写（Override）                       | 重载（Overload）                             |
| -------- | -------------------------------------- | -------------------------------------------- |
| 定义     | 子类重新定义父类的方法                 | 同一个类中方法名相同但参数不同               |
| 方法签名 | 子类方法签名与父类完全相同             | 方法名相同但参数类型、数量或顺序不同         |
| 多态     | 运行时多态（通过父类引用调用子类方法） | 编译时多态（编译器根据参数选择调用哪个方法） |
| 目的是   | 修改或扩展父类的方法实现               | 处理相似的任务，但参数不同                   |



总结：重写主要用于子类修改父类方法的实现，重载则是通过相同的方法名处理不同参数的情况。



## 016、可变长参数

在Java中，可变长参数（Varargs）是指你可以传递任意数量的参数到方法中，而不需要显式地为每个参数创建多个方法重载。这使得方法的调用更简洁且可扩展。

### 语法

可变长参数的语法是在方法参数列表中的最后一个参数前加上省略号 `...`，表示你可以传递任意数量的该类型参数。例如：

```java
public class VarargsExample {
    public static void main(String[] args) {
        // 调用可变长参数方法
        printNumbers(1, 2, 3, 4, 5);
        printNumbers(10, 20);
    }

    // 可变长参数方法
    public static void printNumbers(int... numbers) {
        // 遍历参数数组并打印
        for (int num : numbers) {
            System.out.println(num);
        }
    }
}
```

### 解释：

- `int... numbers` 表示 `numbers` 是一个可以接收多个整数的数组，可以传递任意数量的 `int` 参数，甚至可以什么都不传（默认为空数组）。
- 在方法内部，`numbers` 被当作一个数组来处理。

### 注意事项：

1. **可变长参数必须是方法参数列表的最后一个参数**，因为编译器需要知道何时停止解析可变长参数。
2. 你也可以将传递的可变长参数视为数组进行操作。
3. 如果你既有固定数量的参数，又有可变长参数，固定参数必须出现在可变长参数之前。

例如：

```java
public static void exampleMethod(String first, int... numbers) {
    // 第一参数为String，后面是可变长参数
}
```

### 总结：

可变长参数使得代码更加灵活，可以简化函数的调用，减少方法重载的需要。



## 017、对象相等于引用相等的区别？

在 Java 中，**对象相等**和**引用相等**是两个不同的概念，理解它们的区别对于写出高质量的代码非常重要。

### 1. 引用相等 (`==`)

- **引用相等**是指两个变量是否指向同一个内存地址（即，它们是否指向同一个对象）。
- 使用 `==` 运算符来比较两个对象的引用是否相等。
- **例子**：
  ```java
  String str1 = new String("Hello");
  String str2 = new String("Hello");
  System.out.println(str1 == str2);  // 输出: false
  ```
  在这个例子中，`str1` 和 `str2` 都是不同的对象，即使它们的内容相同（都是 `"Hello"`），`==` 比较的是它们的引用是否相同，而不是它们的内容。

### 2. 对象相等 (`equals()`)
- **对象相等**是指两个对象的内容是否相同，而不是它们的内存地址是否相同。为了检查对象的内容是否相等，通常会重写对象的 `equals()` 方法。
- `equals()` 方法在 `Object` 类中定义，默认是比较对象的引用相等，但许多类（如 `String`, `List`, `Map` 等）都会重写 `equals()` 方法，来比较对象的内容。
- **例子**：
  ```java
  String str1 = new String("Hello");
  String str2 = new String("Hello");
  System.out.println(str1.equals(str2));  // 输出: true
  ```
  在这个例子中，`equals()` 比较的是 `str1` 和 `str2` 的内容是否相等，结果为 `true`，因为它们的内容都为 `"Hello"`。

### 3. 总结
- `==` 比较的是引用（内存地址），即两个对象是否是同一个对象。
- `equals()` 比较的是对象的内容，通常是重写了 `equals()` 方法来实现。

对于 Java 中的基本类型（如 `int`, `char` 等），`==` 比较的是值而不是引用。



## 018、面向对象的三大特征？

Java 面向对象的三大特征是：

1. **封装 (Encapsulation)**
     封装是将数据和操作数据的方法封装到一个类中，从而避免了外部直接访问类的内部实现细节。它通过访问控制符（如 `private`, `public`, `protected`）来限制对类成员的访问，使得类的内部结构对外界透明。外界只能通过公开的接口（方法）与类进行交互。
2. **继承 (Inheritance)**
     继承是面向对象编程中的一个重要特性，它允许一个类继承另一个类的属性和方法，从而实现代码的重用。通过继承，子类可以获取父类的所有公有和保护成员，同时可以对父类的行为进行扩展和修改。Java 使用 `extends` 关键字来表示继承关系。
3. **多态 (Polymorphism)**
     多态是指同一方法或操作可以表现出不同的行为。在 Java 中，多态通常通过方法重载和方法重写实现。方法重载（Overloading）是指同一方法名根据参数不同实现不同的功能，而方法重写（Overriding）则是子类重新定义父类的方法，以改变方法的具体行为。多态的实现提高了代码的灵活性和可扩展性。

这三大特征是 Java 面向对象编程的基础，它们帮助提高代码的可维护性、可扩展性和复用性。



## 019、抽象类于接口的异同点？

Java中的接口和抽象类都有一些相似点和不同点。下面我会分别讲解它们的**共同点**和**区别**。

### 共同点：
1. **不能实例化**：接口和抽象类都不能被直接实例化。你不能创建它们的对象，必须通过继承或实现它们的子类或实现类来使用。
   
2. **可以包含抽象方法**：接口和抽象类都可以包含抽象方法（即没有方法体的方法），这些抽象方法必须在子类中被实现。

3. **继承与实现**：它们都用于实现类与类之间的关系。抽象类可以被继承，接口可以被实现。

### 区别：

| 特性           | 接口 (Interface)                                             | 抽象类 (Abstract Class)                                      |
| -------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| **方法的定义** | 所有方法默认是`public abstract`，Java 8以后可以有`default`和`static`方法 | 可以包含抽象方法和非抽象方法（即有方法体的实现方法）         |
| **构造函数**   | 接口不能有构造函数                                           | 抽象类可以有构造函数                                         |
| **字段**       | 接口中的字段默认为`public static final`，必须初始化          | 抽象类可以有实例字段，可以是任何访问修饰符，且不必初始化     |
| **多继承**     | 一个类可以实现多个接口（Java支持接口的多继承）               | 类只能继承一个抽象类（Java不支持类的多继承）                 |
| **使用场景**   | 适用于描述某种能力或行为，类可以实现多个接口，适用于不相关的类之间共享行为 | 适用于描述具有某些共性特征的类，适用于具有共同父类的类之间的共享功能 |
| **默认实现**   | 从Java 8开始，接口可以有`default`方法提供默认实现            | 抽象类可以提供方法的默认实现                                 |
| **访问修饰符** | 接口中的方法默认是`public`                                   | 抽象类中的方法可以有各种访问修饰符（`public`，`protected`，`private`） |

### 总结：
- **接口**更多的是用来定义一组行为规范，适合于多个不相关的类之间共享相同的行为。
- **抽象类**适用于定义一组具有共性的类的模板，可以为子类提供一些默认的实现或者公共方法。

选择使用接口还是抽象类，主要取决于设计需求：
- 如果类之间有继承关系并且有共同的实现，使用抽象类。
- 如果类之间没有直接的继承关系，但需要共享一组行为规范，使用接口。



## 020、深拷贝与浅拷贝是什么？有什么区别?

在Java中，**深拷贝**（Deep Copy）和**浅拷贝**（Shallow Copy）是复制对象的两种不同方式，它们的主要区别在于对对象中引用类型字段的处理方式。

### 1. 浅拷贝（Shallow Copy）
- **定义**：浅拷贝是指创建一个新的对象，但仅仅复制原对象的基本数据类型字段和对引用类型字段的引用地址。即，浅拷贝创建的新对象与原对象共享对引用类型字段的引用。
- **结果**：如果原对象中的引用类型字段发生变化，浅拷贝后的对象也会受到影响。
- **常用方式**：使用`Object.clone()`方法来实现浅拷贝。

```java
class Person {
    String name;
    int age;
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

class ShallowCopyExample {
    public static void main(String[] args) throws CloneNotSupportedException {
        Person person1 = new Person("Alice", 25);
        Person person2 = (Person) person1.clone();
        // person2是person1的浅拷贝，修改其中的字段会影响到另一个对象
    }
}
```

### 2. 深拷贝（Deep Copy）
- **定义**：深拷贝是指创建一个新的对象，并且不仅复制原对象的基本数据类型字段，而且还递归地复制所有引用类型字段所引用的对象。也就是说，深拷贝会创建整个对象图的新副本，确保原对象和新对象没有任何共享的引用。
- **结果**：修改原对象中的字段不会影响深拷贝后的对象，反之亦然。
- **常用方式**：可以通过`clone()`方法、序列化与反序列化、手动实现等方式来实现深拷贝。

```java
import java.io.*;

class Person implements Serializable {
    String name;
    int age;
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

class DeepCopyExample {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Person person1 = new Person("Bob", 30);
        
        // 使用序列化来实现深拷贝
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(person1);
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Person person2 = (Person) objectInputStream.readObject();
        
        // person2是person1的深拷贝，修改其中的字段不会影响到另一个对象
    }
}
```

### 区别总结：
- **引用类型字段**：
  - **浅拷贝**：新对象和原对象共享引用类型字段的引用，修改其中一个会影响到另一个。
  - **深拷贝**：新对象的引用类型字段是原对象字段的副本，修改其中一个不会影响另一个。
  
- **性能**：
  - **浅拷贝**：通常比深拷贝更高效，因为它没有递归复制整个对象图。
  - **深拷贝**：相对较慢，因为它涉及到对所有引用类型字段的递归复制。

### 使用场景：
- **浅拷贝**：适用于当对象的引用类型字段不需要独立复制，或者对象结构较为简单，修改引用类型字段不会影响其它对象时。
- **深拷贝**：适用于需要完全独立复制对象及其所有引用类型字段，确保每个对象都不互相影响的情况。



## 021、Object 类的常见方法有哪些？

`Object` 类是 Java 中的根类，所有类都直接或间接继承自它。以下是 `Object` 类的一些常见方法：

1. **`toString()`**  
   返回对象的字符串表示。默认实现返回对象的类名和对象的哈希码（以十六进制表示）。通常需要重写此方法以提供更有意义的输出。

2. **`equals(Object obj)`**  
   比较两个对象是否相等。默认实现是比较对象的内存地址（引用是否相同），通常需要重写此方法以进行内容比较。

3. **`hashCode()`**  
   返回对象的哈希码值。它与 `equals()` 方法的实现密切相关。如果两个对象通过 `equals()` 比较相等，它们的 `hashCode()` 也应该相等。

4. **`getClass()`**  
   返回对象的运行时类的 `Class` 对象。可以用来获得对象的类型信息。

5. **`clone()`**  
   创建并返回当前对象的副本。默认实现会进行浅拷贝，但若类需要进行深拷贝，通常需要重写该方法。

6. **`finalize()`**  
   当垃圾回收器准备回收对象时调用的方法。通常用来进行资源释放等操作，但由于它的执行时机不确定，因此不推荐过多依赖。

7. **`notify()`**  
   唤醒正在等待当前对象的线程中的一个线程。

8. **`notifyAll()`**  
   唤醒正在等待当前对象的线程中的所有线程。

9. **`wait()`**  
   使当前线程进入等待状态，直到其他线程调用 `notify()` 或 `notifyAll()` 方法来唤醒。

10. **`wait(long timeout)`**  
    使当前线程等待一段时间（以毫秒为单位），如果超时则自动返回。

11. **`wait(long timeout, int nanos)`**  
    使当前线程等待一段时间，包含毫秒和纳秒部分。

    

这些方法在 `Object` 类中提供了基本的功能，但在实际开发中，根据需要，通常会对其中的一些方法进行重写。



## 023、== 和 equals() 的区别？

在 Java 中，`==` 和 `equals()` 都是用于比较两个对象或基本数据类型是否相等，但它们的行为有所不同：

1. **`==` 操作符：**
   - **用于基本数据类型：** `==` 比较的是值是否相等。例如，对于两个整型变量 `a` 和 `b`，`a == b` 会比较它们的值是否相等。
   - **用于对象引用：** `==` 比较的是两个对象的内存地址（引用）是否相同，即它们是否指向同一个对象。

2. **`equals()` 方法：**
   - **用于对象：** `equals()` 是 `Object` 类中的一个方法，通常用于比较对象的内容是否相等。它默认比较的是对象的内存地址，但许多类（如 `String` 和 `List`）重写了这个方法来比较对象的实际内容，而不是内存地址。

### 举例：

```java
String str1 = new String("hello");
String str2 = new String("hello");

System.out.println(str1 == str2);       // false, 因为它们是不同的对象
System.out.println(str1.equals(str2));  // true, 因为它们的内容相同
```

总结：
- `==` 用于比较基本类型的值或对象的引用地址。
- `equals()` 用于比较对象的内容，通常需要类重写这个方法来实现自定义的内容比较。



## 024、为什么重写 equals() 时必须重写 hashCode() 方法？

在 Java 中，`equals()` 和 `hashCode()` 方法是用来比较对象是否相等的两种方法，它们有一个重要的关系。

根据 Java 的合同（contract）：

1. **如果两个对象通过 `equals()` 方法相等，那么这两个对象的 `hashCode()` 方法必须返回相同的值。**
2. **如果两个对象的 `hashCode()` 方法返回相同的值，那么这两个对象不一定通过 `equals()` 方法相等。**

这就是为什么在重写 `equals()` 方法时，必须同时重写 `hashCode()` 方法的原因。如果你只重写了 `equals()` 方法，而没有重写 `hashCode()` 方法，可能会导致程序的行为不可预测，尤其是在使用集合类（如 `HashMap`、`HashSet` 等）时。

### 具体原因如下：

- **集合类的工作原理**：
  - `HashMap` 和 `HashSet` 这些基于哈希表的数据结构，会使用对象的 `hashCode()` 来确定存储位置。哈希表通过 `hashCode()` 来确定对象是否存放在同一个桶（bucket）中。即使两个对象的 `hashCode()` 相同，依然会调用 `equals()` 方法来进一步判断它们是否相等。
  
- **保持一致性**：
  - 如果你重写了 `equals()` 方法，却没有重写 `hashCode()` 方法，那么当两个对象通过 `equals()` 被认为相等时，它们的 `hashCode()` 可能会不相同，这违反了 `hashCode()` 的合同，可能导致集合类无法正确工作（例如，在 `HashSet` 中，可能插入重复的对象）。

### 例子：

假设我们有两个对象 A 和 B，它们通过 `equals()` 方法比较是相等的：

```java
@Override
public boolean equals(Object obj) {
    // 这里通过某些属性比较对象是否相等
    return (obj instanceof MyClass) && ((MyClass) obj).id == this.id;
}
```

如果我们没有重写 `hashCode()`，Java 会使用默认的 `hashCode()` 实现，它通常基于对象的内存地址生成一个整数。在这种情况下，即使 A 和 B 的 `equals()` 方法返回 `true`，它们的 `hashCode()` 可能会不同，这会导致它们被认为是不同的对象。

如果我们同时重写 `hashCode()` 方法：

```java
@Override
public int hashCode() {
    return Objects.hash(id);  // 使用 id 生成 hashCode
}
```

那么即使 A 和 B 是不同的对象，只要它们的 `id` 相同，它们的 `hashCode()` 就会相同，符合 `equals()` 相等的前提。

### 总结：

- 重写 `equals()` 时，必须保证对象相等的对象返回相同的 `hashCode()` 值。
- 这样可以保证 `HashMap`、`HashSet` 等基于哈希的集合类能够正确处理对象存储和查找操作。



## 025、String、StringBuffer、StringBuilder 的区别？

`String`、`StringBuffer` 和 `StringBuilder` 都是 Java 中用于处理字符串的类，但它们的设计和用途有所不同：

1. **String**:
   - **不可变**：`String` 是不可变的，即一旦创建，就无法修改。任何对字符串的修改都会生成一个新的 `String` 对象。
   - **线程安全**：由于不可变，`String` 是线程安全的。
   - **性能**：因为每次修改都生成一个新的对象，所以在进行大量字符串操作时，性能较差。

2. **StringBuffer**:
   - **可变**：`StringBuffer` 是可变的，意味着它的内容可以在原地修改，不会像 `String` 那样每次修改都会创建新的对象。
   - **线程安全**：`StringBuffer` 是线程安全的，它的方法被 `synchronized` 关键字修饰，确保了多线程环境中的安全性。
   - **性能**：虽然相对于 `String` 更高效，但因为线程安全的实现机制，它的性能通常比 `StringBuilder` 差，尤其在单线程环境下。

3. **StringBuilder**:
   - **可变**：`StringBuilder` 与 `StringBuffer` 类似，也是可变的，可以在原地修改内容。
   - **线程不安全**：`StringBuilder` 不像 `StringBuffer` 那样进行线程同步，因此在单线程环境下性能优于 `StringBuffer`。
   - **性能**：在单线程环境下，`StringBuilder` 的性能比 `StringBuffer` 好，因为它没有线程安全的开销。

### 总结：
- 如果不需要修改字符串，使用 `String`。
- 如果需要修改字符串且在多线程环境中使用，使用 `StringBuffer`。
- 如果在单线程环境下需要高效的字符串操作，使用 `StringBuilder`。



## 026、为什么 String 是不可变的？

从源码角度来看，`String` 类是不可变的，主要体现在以下几个方面：

### 1. **`final` 修饰类**

在 Java 中，`String` 类被声明为 `final` 类，即：

```java
public final class String implements Serializable, Comparable<String>, CharSequence {
    // 省略实现代码
}
```

由于 `String` 是 `final` 类，意味着它不能被继承。不可继承的特性确保了 `String` 对象的行为不会被修改，因此它的状态是固定的。

### 2. **`final` 修饰字段**

`String` 类的核心字段 `value` 是 `final` 的：

```java
private final char value[];
```

`value` 数组用于存储字符串的字符数据，一旦被初始化后，它的引用不能被改变。这意味着 `String` 对象的内容一旦创建后就不能再被修改。这保证了不可变性。

### 3. **没有提供修改字符串的方法**

`String` 类并没有提供直接修改字符串内容的方法。例如，没有像 `append`、`set` 或 `replace` 这样的操作方法可以修改现有字符串的字符数据。大多数与字符串修改相关的方法（如 `concat`、`replace`、`substring` 等）都会返回一个新的 `String` 实例，而不是修改当前的 `String` 对象。

例如，`concat` 方法会返回一个新的字符串：

```java
public String concat(String str) {
    return new String(this.value, 0, this.value.length + str.length());
}
```

如上所示，`concat` 方法并不会修改当前的 `String` 对象，而是创建一个新的 `String` 对象。

### 4. **构造方法和不可变性**

在 `String` 类的构造方法中，虽然可以传入 `char[]` 数组来初始化字符串，但这个数组会被复制到 `value` 字段中，而不是直接使用传入的数组。这进一步确保了外部代码不能修改字符串的内容。

```java
public String(char value[]) {
    this.value = Arrays.copyOf(value, value.length);  // 复制数组，防止外部修改
}
```

这样，即使外部代码修改了传入的 `char[]` 数组，`String` 对象的内容也不会受到影响。

### 5. **不可变对象的影响**

由于 `String` 类中的字段 `value` 是 `final` 的，并且没有提供修改字段值的方法，任何涉及到修改字符串的操作都将返回一个新的 `String` 对象，而不会影响原始字符串。例如：

```java
String str1 = "hello";
String str2 = str1.toUpperCase();
```

在上述代码中，`toUpperCase` 方法会返回一个新的字符串 `"HELLO"`，而原始的字符串 `str1` 的内容 `"hello"` 不会改变。

### 总结

通过使用 `final` 类和 `final` 字段，`String` 确保了对象一旦创建后，其内容不可改变。所有修改字符串的操作都会返回一个新的 `String` 对象，而不会修改现有的 `String` 对象。这是 `String` 不可变性的基础，确保了线程安全、内存效率以及更高的安全性。



## 027、字符串拼接用“+” 还是 StringBuilder?

在Java中，如果你只是拼接少量的字符串，使用`+`是可以的，因为它更简洁且易于理解。但是，如果你需要进行大量的字符串拼接，尤其是在循环中，推荐使用`StringBuilder`，因为它不会像使用`+`那样在每次拼接时创建新的字符串对象，从而提高性能。

### 比较：
- **使用`+`**：
  - 会创建多个中间的字符串对象，导致内存浪费，尤其是在循环中拼接时。
  - 对于少量拼接来说，性能影响较小，但如果拼接次数很多（例如在循环中），则会严重影响性能。

- **使用`StringBuilder`**：
  - `StringBuilder`使用可变字符数组，不会像`+`那样每次都创建新的字符串对象，能够节省内存并提高性能。
  - 适用于大量的字符串拼接。

### 示例：
1. **使用`+`**：
   ```java
   String result = "";
   for (int i = 0; i < 1000; i++) {
       result += "test";
   }
   ```

2. **使用`StringBuilder`**：
   ```java
   StringBuilder result = new StringBuilder();
   for (int i = 0; i < 1000; i++) {
       result.append("test");
   }
   String finalResult = result.toString();
   ```

### 总结：
- **少量拼接**：可以使用`+`，代码更简洁。
- **大量拼接**：推荐使用`StringBuilder`，性能更好。



## 028、字符串常量池的作用？

Java 字符串常量池（String Pool）的作用是为了提高性能并节省内存。具体来说，它的作用包括：

1. **节省内存**：当字符串字面量（即直接在代码中定义的字符串）被创建时，JVM 会检查该字符串常量池中是否已存在相同的字符串。如果存在，JVM 会直接引用池中的现有字符串，而不是创建一个新的对象。这避免了重复存储相同的字符串，节省内存。

2. **提高性能**：因为字符串常量池中存储的是常用的字符串对象，当多个地方使用相同的字符串时，JVM 只需要引用池中的对象，而不需要每次都创建新对象，从而减少了对象的创建和垃圾回收的压力，提升了性能。

3. **减少内存泄漏的风险**：由于字符串常量池中的字符串是由 JVM 管理的，它们在 JVM 生命周期内通常不会被回收，这有助于避免一些由于字符串对象频繁创建而导致的内存泄漏问题。

举个例子：
```java
String str1 = "hello";
String str2 = "hello";
```
在这个例子中，`str1` 和 `str2` 都指向字符串常量池中的同一个 `"hello"` 字符串，而不是各自创建新的字符串对象。

字符串常量池是 JVM 内部的一个特殊缓存机制，确保了字符串的唯一性和效率。



## 029、String s1 = new String("abc")

String s1 = new String("abc");这句话创建了几个字符串对象？

在 Java 中，执行 `String s1 = new String("abc");` 这行代码会创建 **两个** 字符串对象，具体如下：

1. **字符串字面量 `"abc"`**：这会被创建并存储在字符串常量池中。如果 `"abc"` 之前没有存在于池中，JVM 会将它添加到池中。如果 `"abc"` 已经在常量池中，JVM 会直接引用池中的对象。

2. **`new String("abc")` 创建的新对象**：这个构造方法会在堆内存中创建一个新的 `String` 对象，并将常量池中的 `"abc"` 字符串的值复制到这个新的对象中。

所以，最终会创建：

- 一个 `"abc"` 字符串对象在字符串常量池中（如果它还不存在的话）。
- 一个新的 `String` 对象，在堆内存中，引用的是常量池中的 `"abc"` 字符串。

总结来说，`String s1 = new String("abc");` 会创建 **两个** 字符串对象：一个在常量池中，一个在堆内存中。



## 030、String 的 intern 方法有什么作用?

`String.intern()` 方法的作用是：将字符串常量池中的字符串返回。如果字符串常量池中已经存在与当前字符串相同的字符串，那么 `intern()` 方法会返回池中已有的字符串的引用，否则会将当前字符串添加到常量池中，并返回该字符串的引用。

具体来说：

1. **字符串常量池**：JVM 会将所有字面量（例如 `"hello"`）和字符串常量（通过 `String` 类的表达式计算得出）放入一个特殊的区域，叫做 **字符串常量池**。
   
2. **避免重复的字符串对象**：通过 `intern()` 方法，可以确保对于相同内容的字符串，JVM 只会分配一个内存地址。这样，可以节省内存并提高性能。

3. **使用场景**：
   - 如果有多个地方使用相同的字符串，`intern()` 方法会让它们共享同一份内存，而不必每次都新建一个对象。
   - 适用于内存管理需求较高、对字符串操作频繁的情况。

### 示例：
```java
String str1 = new String("hello");  // 创建一个新的 String 对象
String str2 = "hello";              // 直接使用常量池中的字符串
String str3 = str1.intern();        // 使用 intern 方法

// 比较引用
System.out.println(str1 == str2);  // 输出 false，str1 是新创建的对象
System.out.println(str2 == str3);  // 输出 true，str3 和 str2 引用的是同一个对象
```

在这个例子中，`str1` 是通过 `new` 创建的字符串对象，`str2` 是直接使用常量池中的字符串 `"hello"`，而 `str3` 调用了 `intern()` 方法后，引用的是常量池中的 `"hello"`，因此 `str2` 和 `str3` 引用的是同一个对象。



## 031、Exception 和 Error 有什么区别？

在 Java 中，`Exception` 和 `Error` 都是 `Throwable` 类的子类，用于表示程序中的不同类型的错误和异常。它们的主要区别在于它们的用途、发生的场景和是否可以被处理。

### 1. **Exception（异常）**
- **定义**：`Exception` 用于表示程序中可能发生的异常情况。通常是程序可以通过捕获并处理的错误。
- **类型**：
  - **检查型异常（Checked Exception）**：必须在代码中显式处理，否则编译器会报错。常见的有 `IOException`, `SQLException` 等。例如，文件读取失败时，可能会抛出 `IOException`，程序可以捕获并处理这个异常。
  - **非检查型异常（Unchecked Exception）**：即运行时异常，它们继承自 `RuntimeException`，不强制要求处理。常见的有 `NullPointerException`, `ArrayIndexOutOfBoundsException` 等。程序可以选择捕获，也可以忽略。例如，访问空指针时，抛出 `NullPointerException`，但不强制要求处理。
  
- **可处理性**：`Exception` 通常是可以通过 `try-catch` 块进行处理的。开发者可以捕获并处理异常，避免程序崩溃。

### 2. **Error（错误）**
- **定义**：`Error` 用于表示严重的错误，通常是由虚拟机（JVM）或硬件环境引起的，程序通常无法处理这些错误。
- **类型**：常见的错误包括 `OutOfMemoryError`、`StackOverflowError` 等。它们通常表示程序无法继续执行下去。
  
- **不可处理性**：`Error` 通常表示程序无法恢复的错误，通常不应该被捕获或处理。遇到这些错误，通常意味着程序已经无法正常继续执行。因此，不建议使用 `try-catch` 捕获 `Error`。

### 3. **总结**
| 特性             | **Exception**                                          | **Error**                                |
| ---------------- | ------------------------------------------------------ | ---------------------------------------- |
| **继承自**       | `Throwable` → `Exception`                              | `Throwable` → `Error`                    |
| **可处理性**     | 程序可以捕获并处理（尤其是检查型异常）                 | 程序通常无法捕获并处理                   |
| **常见场景**     | 程序中出现的可预料问题（如文件未找到）                 | 系统层面的问题（如内存溢出、栈溢出）     |
| **是否需要处理** | 强制要求处理（检查型异常），或选择处理（非检查型异常） | 不需要处理                               |
| **举例**         | `IOException`, `NullPointerException`                  | `OutOfMemoryError`, `StackOverflowError` |

### 代码示例：
```java
// 例：Exception 示例
try {
    int[] arr = new int[2];
    System.out.println(arr[3]);  // 会抛出 ArrayIndexOutOfBoundsException
} catch (ArrayIndexOutOfBoundsException e) {
    System.out.println("数组越界异常，捕获并处理");
}

// 例：Error 示例
try {
    // 假设这里进行递归调用，导致栈溢出
    throw new StackOverflowError();
} catch (StackOverflowError e) {
    System.out.println("栈溢出错误，程序无法恢复");
}
```

### 结论：
- **`Exception`** 主要用于表示程序中的异常情况，可以通过捕获并处理来恢复程序。
- **`Error`** 用于表示严重的错误，一旦发生，程序通常无法恢复，通常不需要也不应该去捕获它们。