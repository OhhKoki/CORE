### 1、什么是 Docker



### 2、Docker 架构



### 3、Docker 的常用命令

以下是分门别类的 Docker 常用命令总结，包含镜像、容器相关操作。



#### 3.1 镜像相关

Docker 镜像相关的命令用于管理镜像的生命周期，包括拉取、构建、查看、删除和上传镜像等操作。



##### 3.1.1 检索：docker search

用于在 Docker Hub 上查找镜像，并显示相关的镜像列表及其信息。

**语法：**

```bash
docker search [OPTIONS] TERM
```

**参数**：

```bash
# 限制搜索结果数量（默认 25）。
--limit

# 筛选官方镜像。
--filter "is-official=true"
```

**案例**：

```bash
# 在 Docker Hub 上查找 nginx 镜像
docker search nginx

# 仅显示前 5 个 Nginx 镜像
docker search --limit 5 nginx

# 搜索官方 Nginx 镜像
docker search --filter "is-official=true" nginx
```



##### 3.1.2 下载：docker pull

用于从 Docker 仓库下载指定的镜像到本地系统。

**语法：**

```bash
docker pull [OPTIONS] NAME[:TAG]
```

**参数**：

```bash
# （--quiet 的缩写）：静默模式，仅输出镜像 ID。
-q

# （--all-tags 的缩写）：拉取镜像的所有版本。
-a
```

**案例**：

```bash
# 从 Docker 仓库下载 nginx 的镜像到本地系统。
docker pull nginx

# 静默拉取 Nginx 镜像（仅显示 ID）
docker pull -q nginx

# 拉取 Alpine 镜像的所有版本
docker pull -a alpine
```



##### 3.1.3 列表：docker images

用于列出本地所有可用的 Docker 镜像及其相关信息。

**语法：**

```bash
docker images [OPTIONS] [REPOSITORY[:TAG]]
```

**参数**：

```bash
# （--all 的缩写）：显示所有镜像（包括中间层）。
-a

# （--quiet 的缩写）：仅显示镜像 ID。
-q

# （--filter 的缩写）：筛选镜像。
-f
```

**案例**：

```bash
# 用于列出本地所有可用的 Docker 镜像及其相关信息。
docker images

# 显示所有镜像（含中间层）
docker images -a

# 列出所有镜像的 ID
docker images -q

# 列出虚悬镜像
docker images -f "dangling=true"
```



##### 3.1.4 删除：docker rmi

用于删除指定的 Docker 镜像。

**语法：**

```bash
docker rmi [OPTIONS] IMAGE [IMAGE...]
```

**参数**：

```bash
# （--force 的缩写）：强制删除正在使用的镜像。
-f
```

**案例**：

```bash
# 删除指定的 nginx 镜像。
docker rmi nginx

# 强制删除正在使用的 nginx 镜像
docker rmi -f nginx
```



#### 3.2 容器相关

Docker 容器相关的命令用于管理容器的生命周期，包括创建、启动、停止、执行命令、查看状态以及删除容器等操作。



##### 3.2.1 运行：docker run

用于创建并启动一个新的容器实例，基于指定的镜像运行应用程序。

**语法：**

```bash
docker run [OPTIONS] IMAGE [COMMAND] [ARG...]
```

**参数**：

```bash
# （--detach 的缩写）：后台运行容器。
-d

# （--publish 的缩写）：端口映射。
-p

# （--volume 的缩写）：挂载数据卷。
-v

# 指定容器名称。
--name
```

**案例**：

```bash
# 创建并启动一个新的 nginx 容器实例
docker run nginx

# 后台运行 Nginx 容器
docker run -d nginx

# 将主机 8080 端口映射到容器 80 端口
docker run -p 8080:80 nginx

# 挂载主机目录到容器
docker run -v /host/data:/container/data nginx

# 启动名为 my_nginx 的容器
docker run --name my_nginx nginx
```



##### 3.2.2 查看：docker ps

用于列出当前正在运行的 Docker 容器及其相关信息。

**语法：**

```bash
docker ps [OPTIONS]
```

**参数**：

```bash
# （--all 的缩写）：显示所有容器（包括已停止的）。
-a

# （--quiet 的缩写）：仅显示容器 ID。
-q

# （--filter 的缩写）：按条件过滤。
-f
```

**案例**：

```bash
# 列出当前正在运行的 Docker 容器及其相关信息。
docker ps

# 列出所有运行中容器的 ID
docker ps -q

# 仅显示运行中的容器
docker ps -f "status=running"
```



##### 3.2.3 起停：start/stop/restart

分别用于启动、停止和重启 Docker 容器。

**语法：**

```bash
docker stop [OPTIONS] CONTAINER [CONTAINER...]
docker start [OPTIONS] CONTAINER [CONTAINER...]
docker restart [OPTIONS] CONTAINER [CONTAINER...]
```

**参数**：

```bash
#（--time 的缩写）：设置停止超时时间。
-t
```

**案例**：

```bash
# 停止正在运行的 my_nginx 容器
docker stop my_nginx

# 5 秒内强制停止容器
docker stop -t 5 my_nginx
```



##### 3.2.4 删除：docker rm

用于删除一个或多个已停止的 Docker 容器。

**语法：**

```bash
docker rm [OPTIONS] CONTAINER [CONTAINER...]
```

**参数**：

```bash
# （--force 的缩写）：强制删除运行中的容器。
-f

# （--volumes 的缩写）：同时删除关联的卷。
-v
```

**案例**：

```bash
# 删除 my_nginx 容器（已停止）
docker rm -f my_nginx

# 强制删除正在运行的容器
docker rm -f my_nginx

# 删除容器及其关联的卷（只会删除容器挂载的卷，不会删除其他挂载的目录，除非这些目录是在容器内定义为卷的。挂载目录要手动删除
docker rm -v my_nginx
```



##### 3.2.5 日志：docker logs

用于查看运行中的容器输出的日志信息。

**语法：**

```bash
ocker logs [OPTIONS] CONTAINER
```

**参数**：

```bash
# （--follow 的缩写）：实时跟踪日志。
-f

# 显示最后 N 行日志。
--tail
```

**案例**：

```bash
# 查看 my_nginx 的日志
docker logs my_nginx

# 实时查看容器日志
docker logs -f my_nginx

# 查看最后 50 行日志
docker logs --tail 50 my_nginx
```



##### 3.2.6 日志：docker stats

用于实时显示 Docker 容器的资源使用情况，如 CPU、内存、网络和磁盘 I/O 等指标。

**语法：**

```bash
docker stats [OPTIONS] [CONTAINER...]
```

**参数**：

```bash
#（--all 的缩写）：显示所有容器（默认仅显示运行中的容器）。
-a
```

**案例**：

```bash
# 实时显示 Docker 容器的资源使用情况
docker stats

# 监控所有容器的资源使用（包括已停止的）
docker stats -a
```



##### 3.2.7 进入：docker exec

用于在运行中的容器内执行指定的命令或启动一个交互式终端。

**语法：**

```bash
docker exec [OPTIONS] CONTAINER COMMAND [ARG...]
```

**参数**：

```bash
# （-i 是 --interactive 的缩写，-t 是 --tty 的缩写）：进入交互式终端。
-it

# （--env 的缩写）：设置临时环境变量。
-e
```

**案例**：

```bash
# 在运行中的 my_nginx 容器内启动一个交互式的 Bash shell，允许用户进入容器并进行命令行操作。
docker exec -it my_nginx /bin/bash

# 在运行中的 my_nginx 容器内执行 env 命令，并设置一个环境变量 DEBUG=true，用于显示容器内的环境变量信息，包括新设置的 DEBUG 变量。
docker exec -e "DEBUG=true" my_nginx env /bin/bash
```



#### 3.3 分享相关

Docker 分享相关的命令用于创建、保存、加载、标记、登录和推送镜像，以便在不同环境间共享和分发 Docker 镜像。



##### 3.3.1 提交：docker commit

用于将容器的当前状态保存为一个新的镜像。

**语法：**

```bash
docker commit [OPTIONS] CONTAINER [REPOSITORY[:TAG]]
```

**参数**：

```bash
# （--message 的缩写）：添加提交信息（类似 Git commit）。
-m

# （--author 的缩写）：指定镜像作者。
-a
```

**案例**：

```bash
# 将 my_container 的当前状态保存为一个名为 my_image 的新镜像，并指定容器的版本为 lastest.
docker commit -m "安装 Apache 服务" my_container my_image:lastest

# 使用 -m 参数添加提交信息
docker commit -m "安装 Apache 服务" my_container my_image:v1

# 使用 -a 参数标注作者
docker commit -a "Dev Team <dev@example.com>" my_container my_image:v1
```



##### 3.3.2 保存：docker save

用于将 Docker 镜像保存为一个 tar 文件，以便于镜像的备份或迁移。

**语法：**

```bash
docker save [OPTIONS] IMAGE [IMAGE...]
```

**参数**：

```bash
# （--output 的缩写）：指定输出文件路径。
-o
```

**案例**：

```bash
# 使用 -o 参数导出镜像（必须指定文件名：cowardly refusing to save to a terminal. Use the -o flag or redirect
docker save -o nginx.tar nginx:latest
```



##### 3.3.3 加载：docker load

用于从 tar 文件中加载 Docker 镜像。

**语法：**

```bash
docker load [OPTIONS]
```

**参数**：

```bash
# （--input 的缩写）：指定输入文件路径。
-i
```

**案例**：

```bash
# 使用 -i 参数加载镜像
docker load -i nginx.tar
```



##### 3.3.4 登陆：docker login

用于登录到 Docker 仓库（如 Docker Hub），以便进行镜像的推送或拉取操作。

**语法：**

```bash
docker login [OPTIONS] [SERVER]
```

**参数**：

```bash
# （--username 的缩写）：指定用户名。
-u

# （--password 的缩写）：指定密码（明文不安全）。
-p
```

**案例**：

```bash
# 使用 -u 参数指定用户名（推荐交互式输入密码）
docker login -u myuser registry.example.com

# 使用 -u 和 -p 明文登录（不推荐）
docker login -u myuser -p mypassword registry.example.com
```



##### 3.3.5 命名：docker tag

用于为 Docker 镜像创建一个新的标签，以便于更方便地标识和管理镜像版本。

**语法：**

```bash
docker tag SOURCE_IMAGE[:TAG] TARGET_IMAGE[:TAG]
```

**参数**：

```bash
没有常用参数
```

**案例**：

```bash
# 为镜像添加仓库路径标签
docker tag my_image:v1 registry.example.com/my_project/my_image:v1
```



##### 3.3.6 推送：docker push

用于将本地 Docker 镜像上传到远程 Docker 仓库。

**语法：**

```bash
docker push [OPTIONS] NAME[:TAG]
```

**参数**：

```bash
没有常用参数
```

**案例**：

```bash
# 推送镜像到仓库（需先通过 docker tag 重命名）
docker push registry.example.com/my_project/my_image:v1
```



#####  3.3.7 示例

将一个容器打包为镜像，然后推送到仓库的完整流程如下：

```bash
# 1. 提交容器为镜像（同时使用 -m 和 -a）
docker commit -m "初始化配置" -a "Admin <admin@example.com>" my_container my_image:v1

# 2. 添加仓库标签
docker tag my_image:v1 registry.example.com/my_project/my_image:v1

# 3. 登录仓库
docker login -u myuser registry.example.com

# 4. 推送镜像
docker push registry.example.com/my_project/my_image:v1
```