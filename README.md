🛒 Inventory Management System
A Java Swing-based Inventory Management System that uses SQLite database for storage.
This application provides an intuitive graphical interface to manage products, sales, and stock effectively.
Built with Maven, includes JUnit testing, and supports features like search, stock alerts, data export/import, and graphs using JFreeChart.

📂 Project Structure
inventory-management/
├── database/
│   └── inventory.db            # SQLite Database
├── lib/
│   └── (JAR libraries)
├── src/
│   ├── main/java/com/
│   │   ├── DBConnection.java
│   │   ├── InventoryGUI.java
│   │   ├── InventorySystem.java
│   │   ├── Product.java
│   │   └── Sale.java
│   └── test/java/com/
│       ├── InventoryIntegrationTest.java
│       └── InventorySystemTest.java
├── .gitignore
├── pom.xml                     # Maven configuration
├── README.md



✨ Features
✅ Add, Update, Delete Products
✅ Real-time Stock Management
✅ Sales Transactions Recording
✅ Search Products by Name or ID
✅ Low Stock Alerts
✅ Data Export/Import (CSV/Excel)
✅ Graphs and Charts for Sales and Stock (JFreeChart)
✅ User Authentication (Login System)
✅ SQLite Database Integration
✅ Unit and Integration Testing (JUnit)


⚙️ Technologies Used
Java 17
Java Swing
SQLite (via JDBC)
JFreeChart (for charts and graphs)
JUnit 5 (for testing)
Maven (build automation)


🚀 Getting Started
1. Clone the Repository
git clone https://github.com/DhruvAggarwalHere/InventoryManagementSystem
cd inventory-management

2. Setup Dependencies
Ensure you have Java 17+ and Maven installed.
Place the necessary .jar files inside the /lib folder or add them via Maven dependencies.

3. Build the Project
mvn clean install

4. Run the Application
mvn exec:java -Dexec.mainClass="com.Main"
Or compile and run manually:
javac -cp "lib/*" src/main/java/com/*.java
java -cp "lib/*:src/main/java" com.Main

5. Database Setup
The inventory.db SQLite file is already provided inside the database/ folder.
No manual setup needed.

🧪 Running Tests
mvn test
Test classes are located under /src/test/java/com/.

🤝 Contributing
Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change.

👨‍💻 Author
Dhruv Aggarwal

GitHub Profile

🚀 Thank You for Visiting! 🚀
