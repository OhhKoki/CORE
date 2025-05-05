## 一、Linux 总体架构

Linux是一个开源的类Unix操作系统，具有高效、稳定和灵活的特点。

Linux 整体架构采用分层设计，分为用户空间和内核空间，两者通过系统调用接口（syscall 指令）交互。

```
+-----------------------------
|        **用户空间**         
|-----------------------------
| 1. 应用程序                 
|   - Shell (Bash/Zsh)        
|   - GUI (GNOME/KDE)         
|   - 服务器软件 (Nginx)      
|                             
| 2. 系统库                   
|   - glibc、libm、pthread    
|                             
| 3. 运行时环境               
|   - JVM、Python 解释器等    
+-----------------------------
              ↓
+-----------------------------
| **系统调用接口**   
| 用户空间与内核空间的沟通桥梁
| 通过syscall命令触发内核模式切换 
+-----------------------------
              ↓
+-----------------------------
|        **内核空间**         
|-----------------------------
| 1. 进程管理                 
|   - 调度器（CFS）           
|   - IPC、Namespaces         
|                             
| 2. 内存管理                 
|   - 虚拟内存、Page Cache    
|   - Buddy/Slab 分配器       
|                             
| 3. 文件系统（VFS）          
|   - ext4/Btrfs/XFS          
|                             
| 4. 设备驱动                 
|   - 字符/块/网络设备        
|                             
| 5. 网络子系统               
|   - TCP/IP、Netfilter       
|                             
| 6. 安全模块（SELinux）      
| 7. 虚拟化支持（KVM）        
| 8. 中断处理                 
+-----------------------------
              ↓
+-----------------------------
|        **硬件层**           
|-----------------------------
| CPU、内存、磁盘、网卡等     
+-----------------------------
```



### 1、用户空间（User Space）

​	用户空间是用户运行应用程序的环境，包含了各种用户级的工具和服务。这个层次包括了标准库（如glibc）、Shell、应用程序等。



- **应用程序**

    - Shell（Bash、Zsh）
    - 图形界面（GNOME、KDE）
    - 服务器软件（Apache、Nginx）

- **系统库**

    - GNU C 库（glibc）
    - 数学库（libm）
    - 线程库（pthread）

- **运行时环境**

    - Java 虚拟机（JVM）

    - Python 解释器

    - C++ 解释器

        

### 2、系统调用接口（Syscall Interface）

​	这是用户程序与内核进行交互的方式，应用程序通过系统调用与内核沟通（通过 syscall 指令触发内核模式切换），执行如文件操作、进程管理等任务。



### 3、内核空间（Kernel Space）

​	Linux内核是操作系统的核心，负责管理硬件资源、进程调度、内存管理、文件系统、网络通信等。内核是系统与硬件之间的接口，直接与硬件交互。



- **进程管理**

    - 进程调度（CFS 调度器）、创建/终止（`fork`, `exec`）、进程间通信（IPC：管道、信号、共享内存等）。
    - 线程管理（轻量级进程）及命名空间（Namespaces）支持容器隔离。

- **内存管理**

    - 虚拟内存管理：分页、地址空间映射（MMU）、页面置换算法（LRU）。
    - 物理内存分配（Buddy 系统）、缓存（Page Cache）及 Slab 分配器。

- **文件系统（VFS 抽象层）**

    - 支持多种文件系统（ext4、Btrfs、XFS），通过虚拟文件系统（VFS）统一接口。
    - 处理文件读写、权限控制及挂载点管理。

- **设备驱动**

    - 管理硬件设备，分三类：字符设备（键盘）、块设备（SSD）、网络设备（网卡）。
    - 通过设备文件（如 `/dev/sda`）和 sysfs 提供用户接口。

- **网络子系统**

    - 实现 TCP/IP、UDP、ICMP 等协议栈，处理数据包路由、过滤（Netfilter）及套接字（Socket）通信。

- **其他子系统**

    - **安全模块**：SELinux、AppArmor 基于 LSM 框架实施强制访问控制。
    - **虚拟化支持**：KVM 模块与硬件虚拟化扩展（Intel VT-x）协作，支持虚拟机监控。
    - **内核模块**：动态加载驱动或功能（如 `ext4.ko`），增强扩展性。

- **中断与异常处理**

    - 响应硬件中断（IRQ）、软件异常（如缺页），确保实时性和稳定性。

- **初始化与启动**

    - 由 Bootloader 加载内核，初始化子系统后挂载根文件系统，启动 systemd（或传统 init）进入用户空间。

        

### 4、硬件层（Hardware）

​	这是Linux系统运行的基础，指的是计算机的硬件资源，如CPU、内存、硬盘、网络设备等，由内核直接管理或通过驱动交互。



## 二、Linux 常用命令

​	在 Linux 系统上常用的命令主要是用于文件管理、系统监控、进程管理、网络配置等方面。以下是 Linux 系统上使用频率较高的 30 个命令，以及它们的常见用法和参数：



### 01. `ls` - 列出目录内容

**参数**：

- `-l`：以长格式显示文件和目录的详细信息。
- `-a`：显示所有文件，包括隐藏文件。
- `-h`：以可读性更高的方式显示文件大小（**文件**大小的单位转为K、M、G）。

**示例**：

```bash
# 列出当前目录下的所有文件（包括隐藏文件）
ls -a

# 列出当前目录下所有的文件，并显示文件和目录的详细信息，并以可读性更高的方式显示文件大小
ls -lha

# 列出 /home/user 目录目录下的文件，并显示文件和目录的详细信息，并以可读性更高的方式显示文件大小
ls -lh /home/user
```



### 02. `cd` - 改变当前目录

**参数**：

- `~`：用户的主目录。
- `..`：上一级目录。

**示例**：

```bash
# 进入 /home/user 目录
cd /home/user

# 进入当前目录下的 document 目录
cd ./document

# 进入当前目录的爷爷级目录
cd ../..
```



### 03. `pwd` - 显示当前所在路径

**参数**：

**示例**：

```bash
# 进入用户的主目录
cd ~

# 显示当前的工作目录的路径：/Users/terry
pwd
```



### 04. `mkdir` - 创建目录

**参数：**

- `-p`：递归创建目录，即创建嵌套目录时自动创建父目录。

**示例：**

```bash
# 创建一个名为 test 的目录
mkdir test

# 递归创建目录 /home/user/documents/newdir
mkdir -p /home/user/documents/newdir
```



### 05. `mv` - 移动或重命名文件

**参数：**

- `-i`：在覆盖现有文件时提示确认。
- `-f`：强制移动文件，不提示确认。

**示例：**

```bash
# 将文件 file1.txt 移动到 /tmp 目录
mv file1.txt /tmp/

# 重命名文件 file1.txt 为 file2.txt
mv file1.txt file2.txt

# 强制移动文件，覆盖已有文件
mv -f file1.txt /tmp/
```



### 06. `cp` - 复制文件或目录

**参数**：

- `-r`：递归复制目录及其内容。
- `-i`：在覆盖现有文件时提示确认。

**示例**：

```bash
# 复制文件 file1.txt 到 /tmp 目录
cp file1.txt /tmp/

# 递归复制目录 dir1 到 /tmp 目录
cp -r dir1 /tmp/
```



### 07. `rm` - 删除文件或目录

**参数：**

- `-r`：递归删除目录及其内容。
- `-f`：强制删除文件或目录，不提示确认。
- `-i`：删除前提示确认。

**示例：**

```bash
# 删除文件 file1.txt
rm file1.txt

# 递归删除目录 dir1 及其内容
rm -r dir1

# 强制删除文件，不提示确认
rm -f file1.txt
```



### 08. `touch` - 创建空文件

**参数**

**示例：**

```bash
# touch：更新现有文件的时间戳，如果文件不存在则创建文件。

# 更新 file1.txt 的时间戳，由于此时 file1.txt 不存在，所以改为创建 file1.txt
touch file1.txt

# 更新现有文件 file1.txt 的时间戳
touch file1.txt
```



### 17. `tar` - 压缩和解压文件

`tar` 命令用于创建压缩包或解压文件。

**常用参数：**

- `-c`：创建压缩包。
- `-x`：解压缩包。
- `-f`：指定待`创建/解压`的压缩包名称。
- `-z`：使用 gzip `压缩`或`解压`压缩包。
- `-C`：解压到指定目录

**示例：**

```bash
# 将 file1.txt 和 file2.txt 打包为一个名为 file3.tar.gz 的 gzip 压缩包
tar -cf test.tar.gz file1.txt file2.txt

# 将名为 test.tar.gz 的 gzip 压缩包解压到当前目录下
tar -xf test.tar.gz

# 将名为 test.tar.gz 的 gzip 压缩包解压到 /user 目录下
tar -xf test.tar.gz -C /user
```



### 09. `cat` - 显示文件内容

**参数：**

- `-n`：显示行号。

**示例：**

```
# 显示文件 settings.xml 的内容
cat settings.xml

# 显示带行号的文件内容
cat -n settings.xml
```



### 10. `echo` - 输出字符串

**参数**

**示例：**

```bash
# echo 命令用于在终端输出字符串，也可以用于将数据重定向到文件。

# 输出一行文本
echo "Hello, World!"

# 输出环境变量
echo $JAVA_HOME

# 将字符串写入文件
echo "Hello, File!" > file1.txt
```



### 11. `less` - 分页查看文件内容

**参数**

**示例：**

```bash
# less 命令用于分页查看文件内容，适合查看长文件。

# 分页显示文件内容
less settings.xml
```



### 18. `grep` - 搜索字符串

**参数：**

- `-i`：忽略大小写。
- `-r`：递归搜索目录中的文件。

**示例：**

```bash
# grep 命令用于在文件或输出中搜索特定字符串。

# 在文件中搜索字符串 "repository"
grep "repository" settings.xml

# 忽略大小写搜索
grep -i "repository" settings.xml

# 递归搜索目录中的文件
grep -r "repository" settings.xml
```



### 19. `head` - 查看文件头部内容

**常用参数：**

- `-n`：指定显示的行数。

**示例：**

```bash
# head 命令用于查看文件的前几行内容，默认显示前10行

# 查看文件的前10行
head settings.xml

# 查看文件的前5行
head -n 5 settings.xml
```



### 20. `tail` - 查看文件尾部内容

**常用参数：**

- `-n`：指定显示的行数。
- `-f`：持续监视文件内容的变化，特别适合用于查看实时日志文件的更新。

**示例：**

```bash
# tail 命令用于查看文件的最后几行内容，默认显示最后10行。

# 查看文件的最后10行
tail file1.txt

# 查看文件的最后5行
tail -n 5 file1.txt

# 持续监视日志文件的内容
tail -f /var/log/syslog
```



### 28. `ssh` - 远程安全登录

`ssh` 命令用于通过安全外壳协议（SSH）远程登录到另一台Linux服务器。

**常用参数：**

- `-p`：指定SSH端口。
- `-i`：指定私钥文件。

**示例：**

```bash
# 使用默认端口远程登录
ssh user@remote_server

# 指定端口进行登录
ssh -p 2222 user@remote_server

# 使用私钥文件登录
ssh -i ~/.ssh/id_rsa user@remote_server
```



### 29. `service` - 管理系统服务

`service` 命令用于启动、停止、重启或查看系统服务的状态。

**常用参数：**

- `start`：启动服务。
- `stop`：停止服务。
- `restart`：重启服务。
- `status`：查看服务状态。

**示例：**

```bash
# 启动Apache服务
service apache2 start

# 停止MySQL服务
service mysql stop

# 重启SSH服务
service ssh restart

# 查看服务状态
service apache2 status
```



### 30. `ps` - 显示进程

`ps` 命令用于显示当前系统中的进程信息。

**常用参数：**

- `-e`：显示所有进程。
- `-f`：显示完整格式。
- `-u`：显示指定用户的进程。

**示例：**

```bash
# 显示当前用户的所有进程
ps

# 显示所有进程
ps -e

# 显示完整格式的进程信息
ps -f

# 显示特定用户的进程
ps -u username
```



### 31. `kill` 和 `killall` - 终止进程

`kill` 命令用于通过进程ID终止进程，而 `killall` 命令用于根据进程名终止所有匹配的进程。

**常用参数：**

- `-9`：强制终止进程。

**示例：**

```bash
# 通过进程ID终止进程
kill 1234

# 强制终止进程
kill -9 1234

# 终止名为 "httpd" 的所有进程
killall httpd
```



### 34. `chmod` - 更改文件权限

`chmod` 命令用于更改文件或目录的权限。

**常用参数：**

- `-R`：递归更改权限。

**示例：**

```bash
# 将文件权限设置为755
chmod 755 file1.txt

# 递归更改目录权限
chmod -R 755 /path/to/directory
```



### 36. `ifconfig` - 显示网络接口信息

`ifconfig` 命令用于配置和显示网络接口的相关信息。

**常用参数：**

- `up`：启用网络接口。
- `down`：禁用网络接口。

**示例：**

```bash
# 显示所有网络接口的状态
ifconfig

# 禁用 eth0 接口
ifconfig eth0 down

# 启用 eth0 接口
ifconfig eth0 up
```



### 38. `ping` - 测试网络连通性

`ping` 命令用于向目标主机发送ICMP回显请求包，测试本地主机与目标主机之间的连通性。

**常用参数：**

- `-c`：指定发送的回显请求数。
- `-i`：指定发送每个请求的间隔时间。
- `-s`：指定发送的数据包大小。

**示例：**

```bash
# 向目标主机发送4个ICMP请求
ping -c 4 www.baidu.com

# 每隔2秒发送一个请求
ping -i 2 www.baidu.com

# 发送指定大小的包
ping -s 1000 www.baidu.com
```



### 39. `netstat` - 显示网络状态

`netstat` 命令用于显示网络连接、路由表、接口统计、伪装连接等。

**常用参数：**

- `-a`：显示所有连接和监听端口。
- `-t`：显示TCP连接。
- `-u`：显示UDP连接。
- `-n`：以数字形式显示地址和端口号。

**示例：**

```bash
# 显示所有连接和监听端口
netstat -a

# 显示TCP连接
netstat -t

# 显示UDP连接
netstat -u

# 显示数字格式的连接信息
netstat -n
```



### 40. `iptables` - 管理防火墙规则

`iptables` 命令用于配置Linux内核的网络包过滤规则，可以用于设置防火墙。

**常用参数：**

- `-L`：列出当前的防火墙规则。
- `-A`：添加新的规则。
- `-D`：删除规则。
- `-F`：清空所有规则。

**示例：**

```bash
# 列出当前的防火墙规则
iptables -L

# 允许所有入站流量
iptables -A INPUT -j ACCEPT

# 阻止所有入站流量
iptables -A INPUT -j DROP

# 清空所有规则
iptables -F
```



### 41. `wget` - 下载文件

`wget` 命令用于从网络中下载文件，支持HTTP、HTTPS和FTP协议。

**常用参数：**

- `-b`：后台下载模式。
- `-O`：指定输出文件名。
- `-c`：断点续传。

**示例：**

```bash
# 下载文件到当前目录
wget http://example.com/file.zip

# 将文件保存为指定名称
wget -O myfile.zip http://example.com/file.zip

# 断点续传
wget -c http://example.com/file.zip
```



### 42. `curl` - 数据传输工具

`curl` 命令用于通过多种协议传输数据，常用于HTTP请求。

**常用参数：**

- `-o`：指定输出文件。
- `-O`：使用URL中的文件名保存文件。
- `-I`：获取HTTP头信息。
- `-X`：指定HTTP方法（如GET、POST）。

**示例：**

```bash
# 下载文件
curl -o myfile.zip http://example.com/file.zip

# 获取HTTP头信息
curl -I http://example.com

# 发送POST请求
curl -X POST -d "name=user&password=123" http://example.com/login
```



### 44. `top` - 实时显示系统资源使用情况

`top` 命令用于动态显示系统中进程的资源使用情况，如CPU、内存、运行时间等。

**常用参数：**

- `-u`：显示特定用户的进程。
- `-d`：设置刷新间隔。

**示例：**

```bash
# 实时查看系统资源使用情况
top

# 查看指定用户的进程
top -u username

# 设置刷新间隔为5秒
top -d 5
```