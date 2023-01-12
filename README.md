# Part1  LineCounting

This program demonstrates the use of multi-threading in Java to count the number of lines in a large number of text
files.
It includes three methods for counting the lines, each with a different approach for handling the threads.

- getNumOfLines(String[] fileNames)
- getNumOfLinesThreads(String[] fileNames)
- getNumOfLinesThreadPool(String[] fileNames)

## Table of Contents-Part1

- [Methods](#Methods)
- [Class dependencies](#Class-dependencies)
- [Comparing Each Approach](#Comparing-Each-Approach)

## Methods

- `createTextFiles(int n, int seed, int bound)`: A helper method that creates 'n' text files with random number of lines
  between 0 and bound.

- `getNumOfLines(String[] fileNames)`: A method that reads all files iteratively and counts the number of lines, uses a
  single thread, and returns the total number of lines.

- `getNumOfLinesThreads(String[] fileNames)`: A method that uses multiple threads to read the files and count the number
  of lines in each file concurrently and returns the total number of lines after all the threads are done.

- `getNumOfLinesThreadPool(String[] fileNames)`: A method that uses a fixed(number of txt files) thread pool to read the
  files and count the number of lines in each file concurrently and returns the total number of lines.
  like in the Threads method, each thread will read a file and after all the threads are done it will be added to the "
  SumOfLine" variable.

## Class dependencies

```mermaid
classDiagram
direction BT
class Ex2_1 {
  + Ex2_1() 
  + createTextFiles(int, int, int) String[]
  + getNumOfLinesThreads(String[]) int
  + getNumOfLines(String[]) int
  + getNumOfLinesThreadPool(String[]) int
  + main(String[]) void
}
class MyCallable {
  + MyCallable(String) 
  + call() Integer
}
class threadFileHelper {
  + threadFileHelper(String) 
  - AtomicInteger lineCounter
  + run() void
   int lineCounter
}

Ex2_1  ..>  MyCallable : «create»
Ex2_1  ..>  threadFileHelper : «create»

```

## Comparing Each Approach

we ran this program 4 times with different amount of files and line in each run, we wanted to see how much of a
difference the amount of files / the amount of lines in the file
will affect the time elapsed and see how every approach managed to do.

- In the first run, we created 500 files, each containing a random number of lines between 0 and a maximum of 2500
  lines:

| Method | Runtime(ms) | Sum of Lines |
-------------------------| -------- | -------- | -------- |
| getNumOfLines | 2759 | 638603 |
| getNumOfLinesThreads | 114 | 638603 |
| getNumOfLinesThreadPool | 107 | 638603 |

- In the second run, we created 500 files, each containing a random number of lines between 0 and a maximum of 75000
  lines.

| Method | Runtime(ms) | Sum of Lines |
-------------------------| -------- | -------- | -------- |
| getNumOfLines | 3346 | 18186103 |
| getNumOfLinesThreads | 477 | 18186103 |
| getNumOfLinesThreadPool | 514 | 18186103 |

- In the third run, we created 3000 files, each containing a random number of lines between 0 and a maximum of 2500
  lines.

| Method | Runtime(ms) | Sum of Lines |
-------------------------| -------- | -------- | -------- |
| getNumOfLines | 11492 | 3685966 |
| getNumOfLinesThreads | 479 | 3685966 |
| getNumOfLinesThreadPool | 394 | 3685966 |

- In the fourth run, we created 3000 files, each containing a random number of lines between 0 and a maximum of 75000
  lines.

| Method | Runtime(ms) | Sum of Lines |
-------------------------| -------- | -------- | -------- |
| getNumOfLines | 17829 | 109993466 |
| getNumOfLinesThreads | 2117 | 109993466 |
| getNumOfLinesThreadPool | 1849 | 109993466 |

### Conclusion

`getNumOfLines` uses a single thread, it takes longer time to count the lines in all files, it is slower than the other
two methods since it is not utilizing the efficent of multi-threading, and since the program is waiting for each file to
be read iterativly and calculated before it proceeds to the next file.

both of `getNumOfLinesThreads` and `getNumOfLinesThreadPool` are using multi-threading to count the lines, but they have
different way of handling threads,in our tests we can see that using ThreadPool in a big workload (lots of file &lines)
is more efficient than using multi-threading without the executer service.
This can happen because:

`getNumOfLinesThreads` creates a thread for each file that is being read, which can consume more resources, but it also
enables the program to handle each file independently, therefore in high workload it will be less efficent than reusing
threads we created, its important to mention that for each thread we create,it takes the JVM system roughly 1MB of ram
if using Unix based OS, and for Windows OS it will take roughly 512KB.

`getNumOfLinesThreadPool(String[] fileNames)` uses a fixed thread pool that creates a set number of threads, this allows
for more efficient use of resources, since threads can be reused for multiple tasks, but it may also limit the program's
ability to handle each file independently, since the threads in the pool are shared among all files.

to sum up: we can see that it is depends on what we are going to do with our program, but for the most use-cases we
should implement the Executer-service and use a ThreadPool for more efficient and faster preformence.

# Part2 Custom Executor service for Threadpool

This project provides a custom `ThreadPoolExecutor`, that allows tasks to be submitted with a priority.
The priority of a task is determined by its `TaskType`. The `CustomExecutor` uses a PriorityBlockingQueue as the
priority queue for the tasks, which allows for tasks to be executed in order of their priority.

## Table of Contents-Part2

- [Design and Development Considerations](#Design-and-Development-Considerations)
- [Difficulties Encountered](#Difficulties-Encountered)
- [How Difficulties were Handled](#How-Difficulties-were-Handled)
- [Design Contribution](#Design-Contribution)
-

```mermaid
classDiagram
direction BT
class ComparableFuture~V~ {
  + ComparableFuture(Task~V~) 
  ~ int priority
  + compareTo(V) int
   int priority
}
class CustomExecutor {
  + CustomExecutor() 
  - waitForTermination() void
  + submit(Callable~V~) Future~V~
  # beforeExecute(Thread, Runnable) void
  + submit(Task~V~) Future~V~
  + gracefullyTerminate() void
  # afterExecute(Runnable, Throwable) void
  + submit(Callable~V~, TaskType) Future~V~
   int currentMax
}
class Task~V~ {
  + Task(Callable~V~, TaskType) 
  + Task(Callable~V~) 
  - Callable~V~ task
  - TaskType type
  + call() V
  + toString() String
  + equals(Object) boolean
  + createTask(Callable~V~, TaskType) Task~V~
   Callable~V~ task
   TaskType type
   int priority
}
class TaskType {
<<enumeration>>
  - TaskType(int) 
  - validatePriority(int) boolean
  + valueOf(String) TaskType
  + values() TaskType[]
   TaskType type
   int priority
   int priorityValue
}
class Tests {
  + Tests() 
  + anotherTest() void
  + partialTest() void
}

CustomExecutor  ..>  ComparableFuture~V~ : «create»
CustomExecutor  ..>  Task~V~ : «create»
Task~V~ "1" *--> "type 1" TaskType 
Tests  ..>  CustomExecutor : «create»

```

## Design and Development Considerations

The main design consideration for this project was to create a `ThreadPoolExecutor` that could handle a queue of tasks
with different priorities.
To achieve this, we extended the `ThreadPoolExecutor` class and added a PriorityBlockingQueue as the priority queue for
the tasks.
We also created an adapter class called `ComparableFuture` that implements the Comparable interface, so that the
PriorityBlockingQueue can order the tasks based on their priority.
We also created a Task class that implements the Callable interface. This class is used to wrap the actual task that
needs to be executed and also includes the priority of the task through the TaskType.
To enhance the flexibility of the code, we added several methods to the `CustomExecutor` class. These methods allow for
tasks to be submitted with or without a specific TaskType and also allows for the executor to be gracefully terminated.
To enhance the performance of the code, we used the PriorityBlockingQueue, which allows for tasks to be executed in
order of their priority. This ensures that the most important tasks are executed first.

## Difficulties Encountered

One difficulty encountered during the development of this project was understanding how to use the
PriorityBlockingQueue. We had to research the usage of the queue and how it can be used with a ThreadPoolExecutor.
Another difficulty was implementing the Comparable interface in the `ComparableFuture` class. We had to ensure that the
compareTo method was implemented correctly so that the PriorityBlockingQueue could properly order the tasks.

### How Difficulties were Handled

We handled the difficulties by researching the usage of the PriorityBlockingQueue and the Comparable interface. We
looked at examples of similar implementations.

### Design Contribution

The use of the PriorityBlockingQueue in the `CustomExecutor` class contributed to the enhance the performance of the
code by ensuring that the most important tasks are executed first.
The addition of methods to the `CustomExecutor` class contributed to the enhance the flexibility of the code by allowing
for tasks to be submitted with or without a specific TaskType and also allowing for the executor to be gracefully
terminated.
The use of Junit Tests contributed for us to know that our program works as required and for debugging it.