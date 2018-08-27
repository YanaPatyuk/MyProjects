/*
 * ThreadPool.h
 */

#ifndef INCLUDE_THREADPOOL_H_
#define INCLUDE_THREADPOOL_H_

#include <queue>
#include <pthread.h>
#include "Task.h"
#include <iostream>
using namespace std;

class ThreadPool {

	public:
	/**
	 * constructor.
	 * @param threadsNum number of thread.
	 */
		ThreadPool(int threadsNum);
		/**
		 * add new task for the list.
		 */
		void addTask(Task *task);
		/**
		 * stop all threads.
		 */
		void terminate();
		virtual ~ThreadPool();
	private:
		queue<Task *> tasksQueue;
		pthread_t* threads;
		void executeTasks();
		bool stopped;
		pthread_mutex_t lock;
		static void *execute(void *arg);
};
#endif /* INCLUDE_THREADPOOL_H_ */
