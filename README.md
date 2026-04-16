🏦 Bank Management System (Java + JDBC + MySQL)

A console-based banking application developed using Java, JDBC, and MySQL that simulates core banking operations with proper database integration and transaction management.

🚀 Features
🔐 User Registration & Login
🏦 Account Creation (Savings & Salary Accounts)
💰 Deposit Money
💸 Withdraw Money (with minimum balance validation)
🔁 Fund Transfer between accounts
📄 Transaction History (Last 25 transactions)
🆔 Auto-generated Account Numbers (e.g., ACC20260329001)
🔄 ACID-compliant transaction handling (commit & rollback)
🛡️ Secure database interaction using PreparedStatement
🛠️ Tech Stack
Java
JDBC
MySQL
🧠 Concepts Used
DAO Design Pattern
Transaction Management (commit/rollback)
PreparedStatement (SQL injection prevention)
Input Validation
📁 Project Structure
src/
├── model/    # Entity classes (User, Account)
├── Dao/      # Database operations
├── Service/  # Input validation logic
├── util/     # DB connection utility
├── Main/     # Menu-driven application
