package listing6p3;

public interface Executor {
    void execute(Runnable command);
}

/*
The primary abstraction for task execution in the Java class libraries is not Thread, but Executor,
shown in Listing 6.3.

Executor may be a simple interface,
but it forms the basis for a flexible and powerful framework for asynchronous task execution that
supports a wide variety of task execution policies.
It provides a standard means of decoupling task submission from task execution, describing tasks with Runnable.
The Executor implementations also provide lifecycle support and hooks for adding statistics gathering,
application management, and monitoring.

Executor is based on the producer-consumer pattern,
where activities that submit tasks are the producers (producing units of work to be done) and
the threads that execute tasks are the consumers (consuming those units of work).
Using an Executor is usually the easiest path to implementing a producer-consumer design in your application.
 */