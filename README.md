# Система управления билетами на JavaFX

## Обзор

Этот проект представляет собой систему управления билетами, созданную с использованием JavaFX для пользовательского интерфейса, MySQL для базы данных и VS Code как среды разработки. Система позволяет пользователям управлять билетами на рейсы, включая создание, обновление и удаление билетов.

## Функции

- Удобный интерфейс с использованием JavaFX
- Операции CRUD для управления билетами
- Обновления в реальном времени с серверно-клиентской архитектурой
- Интеграция с MySQL для постоянного хранения данных

## Предварительные требования

Перед началом работы убедитесь, что у вас установлены следующие компоненты:

- **Java Development Kit (JDK)**: Скачайте и установите последнюю версию JDK с [официального сайта Oracle](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html).
- **Visual Studio Code (VS Code)**: Скачайте и установите VS Code с [этого сайта](https://code.visualstudio.com/).
- **JavaFX SDK**: Скачайте JavaFX SDK с [этого сайта](https://gluonhq.com/products/javafx/).
- **MySQL Server**: Скачайте и установите MySQL Server с [этого сайта](https://dev.mysql.com/downloads/mysql/).
- **MySQL Connector/J**: Скачайте MySQL Connector/J с [этого сайта](https://dev.mysql.com/downloads/connector/j/).

## Инструкции по установке

### Установка JavaFX

1. **Скачайте JavaFX SDK**:
   - Извлеките JavaFX SDK в желаемое место на вашем компьютере.

2. **Настройте VS Code**:
   - Откройте VS Code.
   - Установите "Java Extension Pack" из представления Extensions.
   - Создайте новый Java проект.
   - Добавьте папку lib из JavaFX SDK в classpath вашего проекта.

### Установка MySQL

1. **Установите MySQL Server**:
   - Следуйте инструкциям по установке MySQL Server из официальной [документации MySQL](https://dev.mysql.com/doc/mysql-installation-excerpt/5.7/en/).

2. **Запустите MySQL Server**:
   - Запустите MySQL сервер из терминала или командной строки:
     ```sh
     mysql -u root -p
     ```

3. **Создайте базу данных и таблицы**:
   - После входа в MySQL сервер создайте базу данных и таблицы с использованием предоставленного SQL скрипта (замените `path_to_script.sql` на путь к вашему скрипту):
     ```sh
     source path_to_script.sql;
     ```

### Настройка проекта

1. **Клонируйте репозиторий**:
   ```sh
   git clone <https://github.com/IlyaFedoroff/motorships>

2. **Откройте проект в VS Code:**
   - Запустите Visual Studio Code.
   - Откройте склонированный репозиторий в VS Code через меню `File -> Open Folder...`.

3. **Настройте переменные окружения для JavaFX:**
   - Запустите Visual Studio Code.
   - Добавьте переменные окружения в конфигурационный файл запуска. В VS Code создайте или отредактируйте файл `launch.json` в папке `.vscode`:
   
     ```sh
     {
         "version": "0.2.0",
         "configurations": [
             {
                 "type": "java",
                 "name": "Launch Main",
                 "request": "launch",
                 "mainClass": "com.example.Main",
                 "vmArgs": "--module-path /path/to/javafx-sdk-11/lib --add-modules javafx.controls,javafx.fxml"
             }
         ]
     }
     ```
     Замените `/path/to/javafx-sdk-11/lib` на реальный путь к библиотеке JavaFX SDK.

4. **Добавьте MySQL Connector/J в проект:**
   - Скачайте MySQL Connector/J и переместите jar-файл в папку `lib` вашего проекта.
   - Добавьте путь к этому jar-файлу в `classpath` проекта. Это можно сделать, добавив следующую строку в `launch.json` в `vmArgs`:

     ```sh
     "--module-path /path/to/javafx-sdk-11/lib --add-modules javafx.controls,javafx.fxml -classpath lib/mysql-connector-java-8.0.23.jar"
     ```

5. **Запустите проект:**:
  В VS Code откройте Main.java и запустите проект, нажав Run или Debug.
