### 1、什么是 Shell

Shell 提供了用户（应用程序）与内核进行交互操作的接口。 它接收用户输入的命令， 并将命令送入内核去执行， 然后将执行结果返回给用户。即：Shell 是一个命令解释器。

```bash
应用 --> Shell --> 内核 --> 硬件
```



### 2、常见的 Shell

Linux 默认使用 bash，Mac OS 默认使用 zsh。

```bash
# 查询当前系统支持的 Shell 类型
cat /etc/shells

# 查询当前系统使用的 Shell 类型
echo $SHELL

# 切换当前系统使用的 Shell 类型
/bin/zsh
```



### 3、什么是 Shell 脚本

当 Linux 命令或语句不在命令下执行，而是通过一个程序文件执行时，该程序就被称为 Shell 脚本或 Shell 程序。如果在 Shell 脚本里内置了很多条命令、语句及循环控制，然后将这些命令一次性执行完毕，这种通过文件执行脚本的方式称为非交互的方式。



### 4、Shell 脚本的创建与执行

Shell 脚本通常由 Linux 命令、bash Shell 命令、程序结构控制语句和注释等内容组成。



**脚本开头**：

一个规范的 Shell 脚本在脚本第一行会指出由哪个解释器来执行脚本中的内容，其中开头的 `#!` 字符又称为幻数。

```bash
#!/bin/bash
```



**脚本执行**：

Shell 脚本是从上至下依次执行每一行的命令及语句的，即执行完了一个命令后再执行下一个，如果在 Shell 脚本中遇到子脚本（即脚本嵌套）时，就会先执行子脚本的内容，完成后再返回父脚本继续执行父脚本内后续的命令及语句。通常情况下，在执行 Shell 脚本时，会向系统内核请求启动一个新的进程，以便在该进程中执行脚本的命令及子 Shell 脚本。



 Shell 脚本的执行通常可以采用以下几种方式。

```bash
# 方式一：创建一个子 Shell 线程执行
sh ./scriptName

# 方式二：创建一个子 Shell 线程执行
./scriptName

# 方式三：在当前的 Shell 线程中执行
source scriptName
```



### 5、 变量

变量用于存储程序中使用的数据，变量存在于内存中，通过 `$变量名` 使用变量。



#### 5.1 变量的定义

变量定义的语法如为：`变量名=变量值`。注意 `=` 的两侧无空格，否则变量名称会被识别为命令，变量的内容一般要加双引号，以防止出错。另外 bash shell 是弱类型语言。



#### 5.2 变量的类型

变量可分为两类：环境变量和普通变量。



##### 5.2.1 普通变量

普通变量也可称为`局部变量`，普通变量只能在用户当前 Shell 生存期的脚本中使用，如果在 Shell 中启动另一个进程或退出，那么变量的值将会无效。



定义一个【普通变量】

```bash
# 通常，数字内容的变量定义可以不加引号，其他没有特别要求的字符串等定义最好都加上双引号（单引号会直接原样输出


# 1. 定义一个字符型变量
username="Alice"
echo "Hello, $name!"								# 输出: Hello, Alice!


# 2. 定义整形变量
num1=3
num2=5
result=$((num1 + num2))  						# 使用 $(( )) 进行运算
echo "The result is: $result"				# 输出: The result is: 8


# 3. 定义只读类型的浮点型变量
readonly pi=3.14159
echo "Value of pi: $pi"   					# 输出: Value of pi: 3.14159
pi=3.14  														# 错误：bash: pi: readonly variable


# 4. 定义数组变量（使用空格分割）
fruits=("apple" "banana" "cherry")
echo "First fruit: ${fruits[0]}"  	# 输出: First fruit: apple
echo "Second fruit: ${fruits[1]}" 	# 输出: Second fruit: banana

for fruit in "${fruits[@]}"					# 遍历数组
do
  echo "$fruit"
done


# 5. 定义关联数组
declare -A person
person["name"]="John"
person["age"]=30
echo "Name: ${person[name]}"  			# 输出: Name: John
echo "Age: ${person[age]}"    			# 输出: Age: 30
```



##### 5.2.2 环境变量

环境变量（使用 `export` 命令导出的变量）也可称为`全局变量`，可用于所有子进程中，这包括编辑器、Shell 脚本和各类应用。



定义一个【环境变量】

```bash
# 方式一：直接定义全局变量
export JAVA_HOME=/user/java8/bin

# 方式二：将一个普通变量提升为全局变量
A=200
export A
```



将【环境变量】变为【普通变量】

```bash
# 定义一个环境变量
export A=200

# 将一个换将变量变为普通变量
unset A
```



环境变量可以在当前进程中（比如当前正在运行的【Shell】或者 【Shell 脚本】）创建，但当进程结束时，这些变量就会丢失，如果希望永久保存环境变量，可在以下位置配置：

- ﻿用户家目录下的 `/User/用户名/.bash_profile`

- ﻿全局配置 `/etc/profile`

    

在将环境变量放入上述的文件中后，每次用户登录时这些变量都将被初始化。环境变量遵循以下规范：

- 所有环境变量的名字均采用大写形式。
- 在将环境变量应用于用户进程程序之前，都应该用 `export` 命令导出定义



#### 5.3 命令结果赋值给变量

把命令的结果作为变量的内容进行赋值的方法在脚本开发时很常见。常用的赋值方式如下：

```bash
# 方式一：变量=$(命令)
A=$(pwd)

# 方式二：变量=${变量}
B=$(ls -la)
echo $B
C=${B}
echo $C
```



#### 5.4 已经定义的特殊变量

在Shell中存在一些特目值要的变量，例如：$0、$1、$#、$@等，我们称之为特殊位置参数变量。

| 位置参数 | 作用说明                                                     |
| -------- | ------------------------------------------------------------ |
| $n       | 获取当前执行的 Shell 脚本的第 n 个参数值，当 n 为 0 时表示脚本的文件名；如果 n 大于9，则用大括号括起来，例如 ${10}，接的参数以空格隔开。 |
| $#       | 获取当前执行的 Shell 脚本所携带的参数的总个数。              |
| $@       | 获取当前执行的 Shell 脚本所携带参数列表（使用 "$@" 时，会将所有的参数被视为一个 String）。 |
| $*       | 获取当前执行的 Shell 脚本所携带参数列表                      |



案例如下

```bash
# 脚本定义如下
#!/bin/bash
echo '$0 is: '$0
echo '$1 is: '$1
echo '$2 is: '$2
echo '$# is: '$#
echo '$@ is: '$@
echo '$* is: '$*
echo '"$*" is: '
for i in "$*"; do echo $i; done
echo '"$@" is: '
for i in "$@"; do echo $i; done

# 脚本执行如下
terry@TerrydeMacBook-Pro IdeaProjects % ./hello.sh p1 p2 p3
terry@TerrydeMacBook-Pro IdeaProjects % ./hello.sh p1 p2 p3
$0 is: ./hello.sh
$1 is: p1
$2 is: p2
$# is: 3
$@ is: p1 p2 p3
$* is: p1 p2 p3
"$*" is: 					# "$*"：所有的参数被视为一个 String						
p1 p2 p3 
"$@" is: 					# "$@"：所有的参数被视为一个 List
p1			 
p2
p3
```



### 6、运算符

在 Shell 中，运算符用于进行不同类型的操作，如算术、逻辑、字符串操作等。以下是常见的 Shell 运算符



#### 6.1 算术运算符

用于执行基本的数学运算。

- `+`：加法
- `-`：减法
- `*`：乘法
- `/`：除法
- `%`：取余
- `++`：自增
- `--`：自减

```bash
#!/bin/bash
a=10
b=5
sum=$((a + b))
diff=$((a - b))
prod=$((a * b))
quot=$((a / b))
rem=$((a % b))

echo "Sum: $sum"
echo "Difference: $diff"
echo "Product: $prod"
echo "Quotient: $quot"
echo "Remainder: $rem"
```



#### 6.2 关系运算符

用于比较两个数值或字符串。

- `-eq`：等于
- `-ne`：不等于
- `-lt`：小于
- `-le`：小于或等于
- `-gt`：大于
- `-ge`：大于或等于

```bash
#!/bin/bash
a=10
b=5

if [ $a -gt $b ]; then
  echo "$a is greater than $b"
else
  echo "$a is not greater than $b"
fi
```



#### 6.3 逻辑运算符

用于处理布尔逻辑操作。

- `&&`：逻辑与（AND）
- `||`：逻辑或（OR）
- `!`：逻辑非（NOT）

```bash
#!/bin/bash
a=10
b=5

if [ $a -gt $b ] && [ $b -gt 0 ]; then
  echo "Both conditions are true"
fi

if [ $a -lt $b ] || [ $b -gt 0 ]; then
  echo "At least one condition is true"
fi
```



#### 6.4 字符串运算符

用于比较字符串或连接字符串。

- `=`：字符串相等
- `!=`：字符串不相等

```bash
#!/bin/bash
str1="hello"
str2="world"

if [ "$str1" = "$str2" ]; then
  echo "Strings are equal"
else
  echo "Strings are not equal"
fi
```



#### 6.6 文件测试运算符

用于测试文件的属性，如是否存在、是否可读等。

- `-e`：文件存在
- `-f`：是普通文件
- `-d`：是目录
- `-r`：文件可读
- `-w`：文件可写
- `-x`：文件可执行
- `-s`：文件非空

```bash
#!/bin/bash
file="test.txt"

if [ -e $file ]; then
  echo "$file exists"
else
  echo "$file does not exist"
fi
```



### 7、条件测试

在 Shell 中，`(())`、`[]` 和 `[[]]` 都是常见的表达式和测试语法。

`(())` 基本上都是用于进行运算

`[[]]` 基本上用于进行判断



#### 7.1  `(())` — 算术运算

`(())` 用于进行算术运算或比较。在这个表达式中，可以直接使用算术运算符（如 `+`、`-`、`*`、`/` 等）来进行数值运算。

```bash
#!/bin/bash

# 进行算术运算
a=5
b=3
((c = a + b))   # c = 5 + 3
echo $c         # 输出：8

((a++))          # a 自增 1
echo $a          # 输出：6

# 进行条件测试（不常用，条件测试一般用 [[]]
if (( a > b && a != 0 )); then
    echo "a 大于 b 且 a 不为 0"
fi
```



#### 7.2  `[]` — 条件测试

`[]` 用于在 Shell 中执行条件测试，检查文件、字符串或数字的状态。它属于传统的 Shell 测试表达式，支持各种操作符，如 `-d`、`-f`、`=`、`-lt` 等（`[]` 是不支持 &&、||、>、< 等运算符的，只能使用对应的 -a、-o、-gt、-lt）。

```bash
#!/bin/bash

file="test.txt"

# 检查文件是否存在
if [ -f "$file" ]; then
    echo "$file 存在"
else
    echo "$file 不存在"
fi

# 字符串比较
str1="hello"
str2="world"
if [ "$str1" = "$str2" ]; then
    echo "字符串相同"
else
    echo "字符串不同"
fi
```



#### 7.3 `[[]]` — 扩展条件测试

`[[]]` 是 bash 扩展的一部分，提供了更多的功能，比如更复杂的字符串比较、更宽松的语法、支持逻辑运算符（如 `&&` 和 `||`）。它比 `[]` 更强大且易于使用，尤其在进行字符串比较时。

```bash
#!/bin/bash

str1="hello"
str2="world"

# 更灵活的字符串比较
if [[ "$str1" == "hello" ]]; then
    echo "str1 是 hello"
fi

# 字符串包含关系
if [[ "$str1" == h* ]]; then
    echo "str1 以 h 开头"
fi
```



三者用法区别

| 测试表达式       | test                  | []                    | [[]]]                                   | (())             |
| ---------------- | --------------------- | --------------------- | --------------------------------------- | ---------------- |
| 边界是否需要空格 | 需要                  | 需要                  | 需要                                    | 不需要           |
| 逻辑操作符       | !、-a、-o             | !、-a、-o             | !、&&、\|\|                             | !、&&、\|\|      |
| 整数比较操作符   | -eq、-gt、-lt. ge、le | -eq、-gt、-lt. ge、le | -eq、-gt、-lt. ge、le、=、＞、<、>=、<= | =、＞、<、>=、<= |
| 字符串比较操作符 | =、==、 !=            | =、==、 !=            | =、==、 !=                              | =、==、 !=       |



### 8、分支语句

在Shell脚本中，分支语句用于根据不同的条件执行不同的代码块。主要有以下几种分支语句。



#### 8.1 `if` 语句

`if` 语句根据给定条件是否成立来执行某个代码块。

语法

```bash
if [ 条件 ]; then
    # 条件为真时执行的命令
fi
```

案例

```bash
#!/bin/bash
number=10

if [ $number -gt 5 ]; then
    echo "数字大于5"
fi
```



#### 8.2 `if-else` 语句

`if-else` 语句根据条件判断来决定执行的代码块。如果条件为真，则执行`if`块，否则执行`else`块。

语法

```bash
if [ 条件 ]; then
    # 条件为真时执行的命令
else
    # 条件为假时执行的命令
fi
```

案例

```bash
#!/bin/bash
number=3

if [ $number -gt 5 ]; then
    echo "数字大于5"
else
    echo "数字小于或等于5"
fi
```



#### 8.3 `if-elif-else` 语句

`if-elif-else` 语句用于多个条件判断，可以在不同条件下执行不同的代码块。

语法

```bash
if [ 条件1 ]; then
    # 条件1为真时执行的命令
elif [ 条件2 ]; then
    # 条件2为真时执行的命令
elif [ 条件3 ]; then
    # 条件3为真时执行的命令
else
    # 条件都不为真时执行的命令
fi
```

案例

```bash
#!/bin/bash
number=7

if [ $number -gt 10 ]; then
    echo "数字大于10"
elif [ $number -eq 7 ]; then
    echo "数字等于7"
else
    echo "数字小于10"
fi
```



#### 8.4 `case` 语句

`case`语句用于多个条件的匹配，相当于多个`if-elif`语句的简化写法。它根据变量的值匹配不同的情况。

语法

```bash
case 变量 in
    模式1)
        # 匹配模式1时执行的命令
        ;;
    模式2)
        # 匹配模式2时执行的命令
        ;;
    *)
        # 没有匹配任何模式时执行的命令
        ;;
esac
```

案例

```bash
#!/bin/bash
fruit="apple"

case $fruit in
    "apple")
        echo "这是苹果"
        ;;
    "banana")
        echo "这是香蕉"
        ;;
    "orange")
        echo "这是橙子"
        ;;
    *)
        echo "未知的水果"
        ;;
esac
```



### 9、循环语句

Shell 循环语句用于反复执行一段代码，直到满足指定条件。



#### 9.1 `for` 语句

`for` 语句用于在一定范围内遍历元素或执行一定次数的循环。基本语法有两种常见形式：

语法

```bash
# 形式一：
for 变量 in 元素列表
do
  # 循环体
done

# 形式二：
for ((初始化; 条件; 更新))
do
  # 循环体
done
```

案例

```bash
#!/bin/bash

# 形式一案例：
for i in 1 2 3 4 5
do
  echo "当前数字：$i"
done

# 形式二案例：
for ((i=1; i<=5; i++))
do
  echo "数字：$i"
done
```



#### 9.2 `while` 语句

`while` 语句用于在条件为真时重复执行某个代码块，直到条件为假为止。基本语法如下：

语法

```bash
while 条件
do
  # 条件为真时执行的代码
done
```

案例

```bash
#!/bin/bash
count=1
while [ $count -le 5 ]; do
  echo "当前计数：$count"
  ((count++))
done
```



### 10、函数

在 Shell 中，函数是一组可以重复使用的命令或代码块，通常用于提高脚本的可读性和复用性。



**Shell 中定义函数**

```bash
# 方式一：
function function_name() {
    commands
}

# 方式二：
function function_name {
    commands
}

# 方式三：
function_name {
    commands
}
```



**Shell 函数的返回值**

Shell 函数通常使用 `return` 来返回一个退出状态码（0 表示成功，非零表示错误），而无法像其他编程语言那样直接返回一个具体值。若要返回具体值，可以使用 `echo` 或将其赋值给一个变量。

```bash
function add() {
    result=$((\$1 + \$2))
    echo $result
}
```



**Shell 函数的调用**

```bash
function_name
```



**Shell 函数的使用案例**

```bash
#!/bin/bash

# 定义一个无参数的函数
greet() {
    echo "Hello, welcome to the Shell scripting world!"
}

# 调用 greet 函数
greet

# 定义一个有参数的函数
sum() {
    echo "Sum of \$1 and \$2 is: $((\$1 + \$2))"
}

# 调用 sum 函数，传入两个参数
sum 5 10
```



**Shell 函数的使用案例 -- 带返回值**

```bash
#!/bin/bash

# 定义一个计算两个数字和的函数，并返回结果
calculate_sum() {
    sum=$((\$1 + \$2))
    echo $sum  # 输出结果
}

# 获取函数的返回值
result=$(calculate_sum 7 8)
echo "The sum is: $result"
```
