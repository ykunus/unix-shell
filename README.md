# 🚀 Concurrent Unix Shell (COSI 131 - Operating Systems)

## 📌 Project Overview
This project is an implementation of a **concurrent Unix shell**, developed as part of **COSI 131: Operating Systems** at Brandeis University. The goal was to extend a sequential Unix shell by incorporating **concurrency** using Java Threads.

### 🔹 Key Features:
- **Pipeline Execution** - Commands connected via pipes (`|`) execute in parallel.
- **Foreground & Background Execution** - Use `&` to run commands in the background.
- **Process Management** - List and kill running background jobs with `repl_jobs` and `kill <job_id>`.
- **Concurrency Handling** - Utilizes Java Threads and synchronized queues.
- **Poison Pill Mechanism** - Ensures proper thread termination.

---

## 🛠️ My Contribution
This project was built on a provided skeleton. My work focused on the **Java implementation**, specifically:
- **Developing command execution logic**.
- **Implementing concurrency** in command processing.
- **Using synchronized data structures** (`LinkedBlockingQueue`) to handle concurrent execution.
- **Ensuring proper execution order** while maintaining parallel processing.

---

## 🏗️ How to Run the Shell

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/ykunus/unix-shell.git
cd unix-shell/YunusKocamanPA2
```
### 2️⃣ Compile the Code
Ensure Java 11+ is installed, then compile:
```bash
javac -d bin src/main/java/cs131/pa2/filter/concurrent/*.java
```
### 3️⃣ Run the Shell
```
java -cp bin cs131.pa2.filter.concurrent.ConcurrentREPL
```
### 4️⃣ Example Commands:
```sh
> ls | grep .txt          # Lists all .txt files
> cat file.txt | wc       # Counts words in file.txt
> cat large-file.txt &    # Runs in background
> repl_jobs               # Lists running background jobs
> kill 1                  # Kills background job #1
```
##🎯 What I Learned
###This programming assignment helped me:

- **Understand fundamental Unix shell commands (ls, cat, grep, wc).
- **Gain hands-on experience with concurrency in operating systems.
- **Learn how to manage thread synchronization, parallel execution, and inter-process communication.
- **Handle race conditions, blocking queues, and thread interruptions in Java.


## 📂 Project Structure
```bash
📂 unix-shell/
 ├── 📂 YunusKocamanPA2/
 │   ├── 📂 src/main/java/cs131/pa2/filter/concurrent/
 │   │   ├── ConcurrentFilter.java
 │   │   ├── ConcurrentCommandBuilder.java
 │   │   ├── (other core files)
 │   ├── 📂 bin/
 │   ├── README.md
```
### The core implementation is located in src/main/java/cs131/pa2/filter/concurrent/.

## 📌 Project Purpose
This project was completed as part of COSI 131 - Operating Systems at Brandeis University. The provided skeleton code was extended to include concurrent execution and process management.

##Acknowledgments
This project was completed as part of COSI 131 - Operating Systems at Brandeis University. The skeleton code was provided, and I extended it by implementing command logic and concurrency in Java.

Note: This repository contains only my Java contributions—implementing command execution and concurrency.
The project skeleton was provided as part of the COSI 131 Operating Systems coursework.
