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



### 6、条件判断



### 7、运算符