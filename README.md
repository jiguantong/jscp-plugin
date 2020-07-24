## IntelliJ 一键部署插件, 将文件/目录上传至服务器并执行指定命令, 将服务器日志输出在 IntelliJ IDEs 控制台
----
### 1. 设置项位置: File => Settings => Tools => Jscp Plugin
配置说明:<br>
  - Server ip: 服务器IP, 会将文件上传至该ip
  - Ssh port: 服务器ssh端口, 默认22
  - User name: 服务器登录用户名
  - Password: 登录密码
  - Remote dir: 文件将上传至服务器上的该目录
  - Local dir/file: 选择本地待上传的目录/文件
  - Build cmd: 文件上传前将在本地执行该命令, 可为空代表无构建命令 (示例: 构建maven项目) `mvn build`
  - Deploy cmd: 文件上传完成后将在服务器执行该命令(执行结果会打印在Jscp Console), 可为空代表不执行任何命令 (示例: 重启tomcat并获取日志输出) `source /etc/profile&&${TOMCAT_HOME}/bin/shutdown.sh&&${TOMCAT_HOME}/bin/startup.sh&&tail -f ${TOMCAT_HOME}/logs/catalina.out`
  
### 2. 执行: 按钮在 IDEs 右上角 Build 左侧, 点击即可执行
