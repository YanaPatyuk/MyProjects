/*
 * Task.h
 */

#ifndef INCLUDE_TASK_H_
#define INCLUDE_TASK_H_

class Task {
public:
	/**
	 * constructor
	 */
 Task(void * (*func)(void *arg), void* arg) :
func(func), arg(arg) {}
 /*
  * start the task
  */
 void execute() {
 func(arg);
 }
 virtual ~Task() {}
private:
 void * (*func)(void *arg);
 void *arg;
};

#endif /* INCLUDE_TASK_H_ */
