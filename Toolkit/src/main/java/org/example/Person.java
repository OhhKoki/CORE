package org.example;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class Person {
    private String name;
    public int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void sayHello() {
        System.out.println("Hello!");
    }

    public void sayAge(int age) {
        System.out.println("Age is: " + age);
    }

    public static void main(String[] args) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, NoSuchFieldException {
            // 获取带有两个参数的构造函数
            Constructor<Person> constructor = Person.class.getConstructor(String.class, int.class);

            // 使用构造函数创建 Person 对象
            Person person = constructor.newInstance("Alice", 25);

            // 输出对象的属性
            System.out.println("Name: " + person.name); // 输出 Name: Alice
            System.out.println("Age: " + person.age);   // 输出 Age: 25
    }
}

