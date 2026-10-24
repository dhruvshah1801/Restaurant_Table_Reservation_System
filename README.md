# Restaurant Table Reservation System

**Name:** Dhruv Shah 
**Enrollment Number:** 12402080601031
**Div:** IT-A
**Subject:** Design and Analysis of Algorithms (DAA)

## Project Overview
The **Restaurant Table Reservation System** is a Java-based command-line application developed as a Design and Analysis of Algorithms (DAA) project. It helps manage restaurant table reservations using in-memory storage.

## Objectives
- Manage restaurant table reservations.
- Check table availability.
- Prevent duplicate reservations for the same table and time slot.
- Apply suitable data structures and algorithms.
- Analyze algorithm correctness and time and space complexity.

## Technologies Used
- **Programming Language:** Java
- **Interface:** Command-Line Interface (CLI)
- **Storage:** In-memory data structures

## Features
- View available tables.
- Book a table for a selected time slot.
- View reservation details.
- Cancel an existing reservation.
- Validate reservation requests.
- Prevent conflicting bookings.

## Algorithm Design
The system uses data structures to store tables and reservation details. It checks existing reservations before confirming a new booking and verifies reservation information when cancelling a booking.

The project includes:
- Algorithm design and pseudocode
- Correctness analysis
- Time and space complexity analysis
- Test cases and sample results

## Complexity Analysis
The exact complexity depends on the selected data structures and implementation.

- **Reservation search:** Depends on the data structure used.
- **Booking validation:** Depends on the number of existing reservations checked.
- **Space complexity:** \(O(n)\), where \(n\) is the number of stored reservations, excluding any additional table storage.

## How to Run
1. Install the Java Development Kit (JDK).
2. Save the Java source code in a `.java` file.
3. Open a terminal in the project directory.
4. Compile the program:
   ```bash
   javac Main.java
   ```
5. Run the program:
   ```bash
   java Main
   ```

*Note: Replace `Main.java` with the actual Java filename if it is different.*

## Limitations
- Reservation data is stored in memory and is lost when the program terminates.
- The application uses a command-line interface.
- It is intended for educational purposes rather than full-scale restaurant operations.

## Future Enhancements
- Add permanent storage using a database.
- Develop a graphical user interface or web application.
- Add customer details and reservation history.
- Support multiple users and advanced availability searches.

## Conclusion
The Restaurant Table Reservation System demonstrates how algorithms and data structures can be applied to a practical reservation problem. It provides a foundation for understanding booking validation, conflict checking, correctness, and complexity analysis.


