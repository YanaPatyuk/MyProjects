/*
*Yana Patyuk
 */
#include "threadPool.h"

static void* executeTasks(void* arg);
void writeToFile(char* info, int size);
/**
 * @param: numOfThreads to create
 * create mutex objects, threads and cond.
 */
ThreadPool* tpCreate(int numOfThreads) {
	int err = 1;
  int file;
  char Error[] = "Error in system call\n";
  //open new file to write errors. move to STDERR_FILENO and close.
  if((file=open("file",O_CREAT|O_TRUNC|O_WRONLY, 0777))==-1) {
	  return NULL;
  	 }
  //move to file descriptor 2 (error STDERR_FILENO)
   dup2(file,STDERR_FILENO);
   close(file);

  //allocate memory for threadpool
	ThreadPool *pool = malloc(sizeof(ThreadPool));
	if(pool == NULL)//if allocatetion did'nt work
		return NULL;
  if(pthread_mutex_init(&pool->m, NULL) != 0) {
	  writeToFile(Error, sizeof(Error));
	  return NULL;
  }
  if(pthread_mutex_init(&pool->mDestroy, NULL) != 0) {
    writeToFile(Error, sizeof(Error));
    return NULL;
  }
  if(pthread_cond_init(&pool->cond, NULL) != 0){
    writeToFile(Error, sizeof(Error));
    return NULL;
  }
	pool->run = 1;
	pool->numberOfThreads = numOfThreads;
	pool->taskQueue = osCreateQueue();//create queue for tasks.
	//allocate memory for threads and create each one.
	pool->threads =  malloc(numOfThreads * sizeof(pthread_t));
	pool->destroy = FALSE;
	for(int i = 0; i < numOfThreads; i++){
		err =  pthread_create(&(pool->threads[i]) ,NULL, executeTasks,(void*)pool);
		if(err != 0)
	    writeToFile(Error, sizeof(Error));
	}
	return pool;
}

/**
 *
 */
void tpDestroy(ThreadPool* threadPool, int shouldWaitForTasks) {
	Task* task;
	//check if thread pool in destroy mode already by priviuse call.
	if(!threadPool->destroy) {
		//lock the mutex so no operation could destroy.
		pthread_mutex_lock(&threadPool->mDestroy);
		//if we are still not in destroy mode(may caused if two threads eneted and one of them locked)
		if(!threadPool->destroy) {
			//update status of destroy. and unlock the mutex-to free threads from waiting.
			threadPool->destroy = TRUE;
			pthread_mutex_unlock(&threadPool->mDestroy);
			//if we got 0-cleare the task list
			if(shouldWaitForTasks == 0) {
				pthread_mutex_lock(&threadPool->m);//we lock so other threads couldnt add or execute task.
				while(!(osIsQueueEmpty(threadPool->taskQueue))) {
					task = osDequeue(threadPool->taskQueue);//pop task and free memory.
					free(task);
				}
				//destroy the list of task and unlock mutex so other threads could stop.
				pthread_mutex_unlock(&threadPool->m);
				//if we didnt get 0 but the list is not empty-wait for signal from threads that list is clear.
			} else if (!osIsQueueEmpty(threadPool->taskQueue)) {
				pthread_cond_wait(&threadPool->cond, &threadPool->m);
			}
			//notify all threads we stop(so they could exit if they are in waiting.
			threadPool->run = 0;
			pthread_cond_broadcast(&threadPool->cond);
			//wait for all threads to finish their current task.
			for (int i=0;i<threadPool->numberOfThreads;i++)
			    	pthread_join(threadPool->threads[i], NULL);

			//free all memory allocated.
			osDestroyQueue(threadPool->taskQueue);
		  pthread_mutex_destroy(&threadPool->m);
		  pthread_mutex_destroy(&threadPool->mDestroy);
		  pthread_cond_destroy(&threadPool->cond);
			free(threadPool->threads);
			free(threadPool);

		}
	}
}
/**
 * input:threadpool pointer, function for task and param for input function
 */
int tpInsertTask(ThreadPool* threadPool, void (*computeFunc) (void *), void* param) {
	Task *task;
	//if we are not in destroy mode:
	if(!threadPool->destroy) {
		//signal all waiting threads new task.
		//this operation open the mutex so we could lock it
		pthread_cond_broadcast(&threadPool->cond);
		pthread_mutex_lock(&threadPool->m);
		//add the task.
		task = malloc(sizeof(Task));
		task->func = computeFunc;
		task->args = param;
		osEnqueue(threadPool->taskQueue, task);
		//signal who waits
		pthread_cond_signal(&threadPool->cond);
		//unlock.
		pthread_mutex_unlock(&threadPool->m);
		return 0;
	}
	return -1;
}

/**
 * @param: arg -argument thread pool.
 * wait for new tasks and execute them.
 */
static void* executeTasks(void* arg) {
	ThreadPool *pool = (ThreadPool*)arg;
	Task *task;
	while(pool->run) {//run till stop
		//if there is no task to execute and we are not in destroy mode
		//wait until new task arrived.
		if((osIsQueueEmpty(pool->taskQueue)) && (pool->destroy==FALSE)) {
			pthread_mutex_lock(&pool->m);
			pthread_cond_wait(&pool->cond, &pool->m);
			pthread_mutex_unlock(&pool->m);
		}
		//if task list is empy and we destroy the pool
		//signal destroy that no tasks waiting and stop(stopping close thread)
		if((osIsQueueEmpty(pool->taskQueue)) && (pool->destroy==TRUE)) {
			pthread_cond_signal(&pool->cond);
			break;
		}
		//get new task from list.
		pthread_mutex_lock(&pool->m);
		task = osDequeue(pool->taskQueue);
		pthread_mutex_unlock(&pool->m);
		//execute task and in the end free task memroy.
		//note:if task is null means list is empty
		if(task != NULL) {
			//if task is null means no task
			(*task->func)(task->args);
			free(task);
		}
	}
	return NULL;
}
/**
 * write error to fileDescriptor.
 */
void writeToFile(char* info, int size) {
	write(STDERR_FILENO,info ,size);
}

