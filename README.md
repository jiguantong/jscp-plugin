## IntelliJ-based IDEs 一键部署插件, 一键完成 build => upload => server restart && tail log
----
https://plugins.jetbrains.com/plugin/14779-jscp/
### 1. 设置项位置: File => Settings => Jscp Plugin
配置说明:<br>
  - Server ip: 服务器IP, 会将文件上传至该ip
  - Ssh port: 服务器ssh端口, 默认22
  - User name: 服务器登录用户名
  - Password: 登录密码
  - Remote dir: 文件将上传至服务器上的该目录
  - Local dir/file: 选择本地待上传的目录/文件
  - Build cmd: 文件上传前将在本地执行该命令, 可为空代表无构建命令
  > **_NOTE:_** 你可以在这里指定构建命令或任何其他前置命令, 该命令将在本地当前项目根路径执行,
    例: `mvn clean package -DskipTests`, `yarn build`
  - Deploy cmd: 文件上传完成后将在服务器执行该命令, 可为空代表不执行任何命令
  > **_NOTE:_** 你可以在这里指定启动服务或任何其他文件上传后的命令(多个命令用 && 分隔), 该命令将在服务器中执行, 并将执行结果打印在 IDEs 
>下方 Jscp Console 中, 例(重启tomcat并获取日志输出): 
> `source /etc/profile&&${TOMCAT_HOME}/bin/shutdown.sh&&${TOMCAT_HOME}/bin/startup.sh&&tail -f ${TOMCAT_HOME}/logs/catalina.out`
  
### 2. 执行: 按钮在 IDEs 右上角 Build 左侧, 点击即可执行
