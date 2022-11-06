# 1. Set Up MySQL Database
## 1.1. Start MySQL in Docker Container
```sh
% ls | grep  docker-compose.yml 
docker-compose.yml
% docker-compose up -d
[+] Running 3/3
 ⠿ Network docker_default    Created                                            0.0s
 ⠿ Container docker-redis-1  Started                                            0.3s
 ⠿ Container mysql_host      Started                                            0.3s
admin@gw-mac docker % docker ps -a
CONTAINER ID   IMAGE          COMMAND                  CREATED         STATUS         PORTS                               NAMES
529a1c54113e   mysql:8.0.22   "docker-entrypoint.s…"   8 seconds ago   Up 7 seconds   0.0.0.0:3306->3306/tcp, 33060/tcp   mysql_host
ee8c3ee9b20e   redis:latest   "docker-entrypoint.s…"   8 seconds ago   Up 7 seconds   0.0.0.0:6379->6379/tcp              docker-redis-1
% 
```

## 1.2. Generate password hash
```sh
% htpasswd -n -B pass
New password: 
Re-type new password: 
pass:$2y$05$dKLxqxq8cBj3wYGciicTdOR6zhfHiG3IEYreqyxkfFPU.Qq5w4KKS
% 
```

## 1.3. Create database and table
```sh
% mysql -h 127.0.0.1 --port 3306 -u root
mysql> CREATE DATABASE book_manager;
Query OK, 1 row affected (0.01 sec)

mysql> 
mysql> use book_manager
Database changed
mysql> 
mysql> CREATE TABLE user (
    ->     id bigint NOT NULL,
    ->     email varchar(256) UNIQUE NOT NULL,
    ->     password varchar(128) NOT NULL,
    ->     name varchar(32) NOT NULL,
    ->     role_type enum('ADMIN', 'USER'),
    ->     PRIMARY KEY (id)
    -> ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
Query OK, 0 rows affected, 1 warning (0.03 sec)

mysql> CREATE TABLE book (
    ->     id bigint NOT NULL,
    ->     title varchar(128) NOT NULL,
    ->     author varchar(32) NOT NULL,
    ->     release_date date NOT NULL,
    ->     PRIMARY KEY (id)
    -> ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
Query OK, 0 rows affected, 1 warning (0.01 sec)

mysql> CREATE TABLE rental (
    ->     book_id bigint NOT NULL,
    ->     user_id bigint NOT NULL,
    ->     rental_datetime datetime NOT NULL,
    ->     return_deadline datetime NOT NULL,
    ->     PRIMARY KEY (book_id)
    -> ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
Query OK, 0 rows affected, 1 warning (0.01 sec)

mysql> 
mysql> exit
Bye
% 
```

## 1.4. Insert data
```sh
% mysql -h 127.0.0.1 --port 3306 -u root
mysql> INSERT INTO book values(100, 'kotlin入門', 'コトリン太郎', '1950-10-01'), (200, 'java入門', 'ジャバ太郎', '2005-08-29');
Query OK, 2 rows affected (0.01 sec)
Records: 2  Duplicates: 0  Warnings: 0

mysql> 
mysql> INSERT INTO user values(1, 'admin@test.com', ':$2y$05$dKLxqxq8cBj3wYGciicTdOR6zhfHiG3IEYreqyxkfFPU.Qq5w4KKS', '管理者', 'ADMIN'), (2, 'user@test.com', ':$2y$05$dKLxqxq8cBj3wYGciicTdOR6zhfHiG3IEYreqyxkfFPU.Qq5w4KKS', 'ユーザー', 'USER');
Query OK, 2 rows affected (0.00 sec)
Records: 2  Duplicates: 0  Warnings: 0

mysql> 
mysql> ALTER USER 'root'@'localhost' identified BY 'mysql';
Query OK, 0 rows affected (0.03 sec)

mysql> 
mysql> exit
```

## 1.5. Change password
```sh
% mysql -h 127.0.0.1 --port 3306 -u root
mysql> ALTER USER 'root'@'localhost' identified BY 'mysql';
Query OK, 0 rows affected (0.03 sec)

mysql> 
mysql> exit
% mysql -h 127.0.0.1 --port 3306 -u root -p
Enter password: 
mysql> exit
Bye
% 
```
 


# 2. Generate Code with MybatisGenerator
## 2.1. Create directory
```sh
% mkdir -p src/main/kotlin/com/book/manager/infrastructure/database/
% ls src/main/kotlin/com/book/manager/         
BookManagerApplication.kt       infrastructure
%
% ls src/main/kotlin/com/book/manager/infrastructure/
database
%
```

## 2.2. Execute Gradle task(mbGenerator)
```sh
% ./gradlew mbGenerator
Starting a Gradle Daemon, 3 busy and 1 incompatible Daemons could not be reused, use --status for details

> Task :mbGenerator
[ant:mbgenerator] Loading class `com.mysql.jdbc.Driver'. This is deprecated. The new driver class is `com.mysql.cj.jdbc.Driver'. The driver is automatically registered via the SPI and manual loading of the driver class is generally unnecessary.
[ant:mbgenerator] Column role_type, specified for override in table rental, does not exist in the table.
[ant:mbgenerator] Column role_type, specified for override in table book, does not exist in the table.
[ant:mbgenerator] Existing file /Users/admin/.gradle/daemon/6.9.2/src/main/kotlin/com/book/manager/infrastructure/database/record/Rental.kt was overwritten
[ant:mbgenerator] Existing file /Users/admin/.gradle/daemon/6.9.2/src/main/kotlin/com/book/manager/infrastructure/database/mapper/RentalDynamicSqlSupport.kt was overwritten
[ant:mbgenerator] Existing file /Users/admin/.gradle/daemon/6.9.2/src/main/kotlin/com/book/manager/infrastructure/database/mapper/RentalMapper.kt was overwritten
[ant:mbgenerator] Existing file /Users/admin/.gradle/daemon/6.9.2/src/main/kotlin/com/book/manager/infrastructure/database/record/User.kt was overwritten
[ant:mbgenerator] Existing file /Users/admin/.gradle/daemon/6.9.2/src/main/kotlin/com/book/manager/infrastructure/database/mapper/UserDynamicSqlSupport.kt was overwritten
[ant:mbgenerator] Existing file /Users/admin/.gradle/daemon/6.9.2/src/main/kotlin/com/book/manager/infrastructure/database/mapper/UserMapper.kt was overwritten
[ant:mbgenerator] Existing file /Users/admin/.gradle/daemon/6.9.2/src/main/kotlin/com/book/manager/infrastructure/database/record/Book.kt was overwritten
[ant:mbgenerator] Existing file /Users/admin/.gradle/daemon/6.9.2/src/main/kotlin/com/book/manager/infrastructure/database/mapper/BookDynamicSqlSupport.kt was overwritten
[ant:mbgenerator] Existing file /Users/admin/.gradle/daemon/6.9.2/src/main/kotlin/com/book/manager/infrastructure/database/mapper/BookMapper.kt was overwritten

Deprecated Gradle features were used in this build, making it incompatible with Gradle 7.0.
Use '--warning-mode all' to show the individual deprecation warnings.
See https://docs.gradle.org/6.9.2/userguide/command_line_interface.html#sec:command_line_warnings

BUILD SUCCESSFUL in 5s
1 actionable task: 1 executed
% 
```

