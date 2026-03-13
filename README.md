# JavaFX Project with SQLite

This is a JavaFX application with three interconnected forms (Form1, Form2, Form3) and SQLite database integration.

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

## How to Launch the Project

1. **Navigate to the project directory:**
   ```
   cd /home/ravel/Documents/CODES/PROJETICT207
   ```

2. **Compile and run the application:**
   ```
   mvn clean install
   mvn javafx:run
   ```

   Alternatively, you can run directly:
   ```
   mvn javafx:run
   ```

## Project Structure

- `src/main/java/com/projetict207/` - Main source code
  - `App.java` - Main application class
  - `Form1.java`, `Form2.java`, `Form3.java` - Form classes
  - `DatabaseConnector.java` - SQLite database connection utility

- `pom.xml` - Maven configuration with JavaFX and SQLite dependencies

## Features

- Three forms: Form1 (start, with buttons to Form2 and Form3), Form2 (back to Form1), Form3 (back to Form1)
- SQLite database connection ready for use

## Database

The application uses SQLite. The database file `database.db` will be created automatically in the project root if it doesn't exist. The `DatabaseConnector` class includes a method to create a sample `users` table.
