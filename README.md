# 1. Project Overview

## (1)App Features
This app is able to use below function.

**User Story**
* You can add any book.
* You can read the list of books.
* You can read the details of any book.
* You can search for a book by part of its title.
* You can edit any book.
* You can delete any book.

## (2)Project Structure

```sh
.
├── docker
│   ├── db
│   └── redis
├── gradle
│   └── wrapper
├── sql
└── src
    ├── main
    │   ├── kotlin
    │   │   └── com
    │   │       └── book
    │   │           └── manager
    │   │               ├── application
    │   │               │   └── service
    │   │               │       └── security
    │   │               ├── domain
    │   │               │   ├── enum
    │   │               │   ├── model
    │   │               │   └── repository
    │   │               ├── infrastructure
    │   │               │   └── database
    │   │               │       ├── mapper
    │   │               │       │   └── custom
    │   │               │       ├── record
    │   │               │       │   └── custom
    │   │               │       └── repository
    │   │               └── presentation
    │   │                   ├── aop
    │   │                   ├── config
    │   │                   ├── controller
    │   │                   ├── form
    │   │                   └── handler
    │   └── resources
    └── test
        ├── kotlin
        │   └── com
        │       └── book
        │           └── manager
        │               ├── application
        │               │   └── service
        │               ├── domain
        │               │   └── model
        │               └── presentation
        │                   └── controller
        └── resources
            └── mockito-extensions
```


# 2. Usage
## 2.0. Prerequisite
* Install MySQL with Homebrew.
* Install Docker Desktop(for Apple Ailicon Mac).
* Install Azul JDK(for Apple Ailicon Mac).

## 2.1. Start MySQL in Docker Container
```sh
% ls | grep docker-compose.yml
docker-compose.yml
% docker-compose up -d
[+] Running 3/3
 ⠿ Network docker_default    Created                                            0.0s
 ⠿ Container docker-redis-1  Started                                            0.3s
 ⠿ Container mysql_host      Started                                            0.3s
% docker ps
CONTAINER ID   IMAGE          COMMAND                  CREATED          STATUS          PORTS                               NAMES
2996c7b3802e   mysql:latest   "docker-entrypoint.s…"   16 seconds ago   Up 15 seconds   0.0.0.0:3306->3306/tcp, 33060/tcp   mysql_host
c03499455b93   redis:latest   "docker-entrypoint.s…"   17 seconds ago   Up 15 seconds   0.0.0.0:6379->6379/tcp              docker-redis-1
% 
```

## 2.2. Generate password hash
```sh
% htpasswd -n -B pass
New password: 
Re-type new password: 
pass:$2y$05$dKLxqxq8cBj3wYGciicTdOR6zhfHiG3IEYreqyxkfFPU.Qq5w4KKS
% 
```

## 2.3. Create database and table
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

## 2.4. Insert data
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

## 2.5. Change password
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


## 2.6 Running the application

```sh
% ./gradlew bootRun
```

## 2.3. Packaging and run the application

```
% ./gradlew build
% java -jar build/libs/book-manager-0.0.1-SNAPSHOT.jar 
```


# 3. Operation verification
## 3.1. GET /login
```sh
% curl -i -c cookie.txt -H 'Content-Type: application/x-www-f
orm-urlencoded' -X POST -d 'email=user@test.com' -d 'pass=1234' http://localhost:8080/login
HTTP/1.1 200 
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Set-Cookie: SESSION=NWE5ZTFmOWYtMzQ3Ny00NTkyLWFkYTgtMTI4YmZiNDYxZmJm; Path=/; HttpOnly; SameSite=Lax
Content-Length: 0
Date: Sun, 06 Nov 2022 11:58:09 GMT

% 
```

## 3.2. GET /book/list
```sh
% curl -sS -b cookie.txt http://localhost:8080/book/list | jq -r '.'
{
  "book_list": [
    {
      "id": 100,
      "title": "kotlin入門",
      "author": "コトリン太郎",
      "is_rental": false
    },
    {
      "id": 200,
      "title": "java入門",
      "author": "ジャバ太郎",
      "is_rental": false
    }
  ]
}
% 
```