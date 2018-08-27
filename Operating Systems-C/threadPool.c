/*
*Yana Patyuk
 */
#include "threadPool.h"
pthread_mutex_t m;
pthread_mutex_t mDestroy;
pthread_cond_t cond;
pthread_cond_t cond2;


void *executeTasks(void* arg);
int run;
int tasks = 0;

ThreadPool* tpCreate(int numOfThreads) {
int err = 1;
if (pthread_mutex_init(&m, NULL) != 0)
  {
      printf("\n mutex init failed\n");
      return NULL;
  }
if (pthread_mutex_init(&mDestroy, NULL) != 0)
  {
      printf("\n mutex init failed\n");
      return NULL;
  }
if (pthread_cond_init(&cond, NULL) != 0)
  {
      printf("\n cond init failed\n");
      return NULL;
  }
	ThreadPool *pool = malloc(sizeof(ThreadPool));
	if(pool == NULL)
		return NULL;
	run = 1;
	pool->numberOfThreads = numOfThreads;
	pool->taskQueue = osCreateQueue();
	pool->threads =  malloc(numOfThreads * sizeof(pthread_t));
	pool->destroy = FALSE;
	for(int i = 0; i < numOfThreads; i++){
		err =  pthread_create(&(pool->threads[i]) ,NULL, executeTasks,(void*)pool);
		if(err != 0)
			 printf("\ncan't create thread :[%s]", strerror(err));
	}
	return pool;
}


void tpDestroy(ThreadPool* threadPool, int shouldWaitForTasks) {
	Task* task;
	if(!threadPool->destroy) {
		pthread_mutex_lock(&mDestroy);
		if(!threadPool->destroy) {
			threadPool->destroy = TRUE;
			pthread_mutex_unlock(&mDestroy);
			//pthread_cond_wait()
			if(shouldWaitForTasks == 0) {
				pthread_mutex_lock(&m);
				while(!(osIsQueueEmpty(threadPool->taskQueue))) {
					task = osDequeue(threadPool->taskQueue);
					free(task);
				}
				osDestroyQueue(threadPool->taskQueue);
				pthread_mutex_unlock(&m);
			} else if (!osIsQueueEmpty(threadPool->taskQueue)) {
				pthread_cond_wait(&cond, &m);
				osDestroyQueue(threadPool->taskQueue);
			}
				run = 0;
				pthread_cond_broadcast(&cond);

			for (int i=0;i<threadPool->numberOfThreads;i++)
			    	pthread_join(threadPool->threads[i], NULL);

			free(threadPool->threads);
			free(threadPool);
		  pthread_mutex_destroy(&m);
		  pthread_mutex_destroy(&mDestroy);
		  pthread_cond_destroy(&cond);

		}
	}
}

int tpInsertTask(ThreadPool* threadPool, void (*computeFunc) (void *), void* param) {
	Task *task;
	if(!threadPool->destroy) {
		pthread_cond_broadcast(&cond);
		pthread_mutex_lock(&m);
		task = malloc(sizeof(Task));
		task->func = computeFunc;
		task->args = param;
		osEnqueue(threadPool->taskQueue, task);
		pthread_cond_signal(&cond);
		pthread_mutex_unlock(&m);
		return 0;
	}
	return -1;
}


void* executeTasks(void* arg) {
	ThreadPool *pool = (ThreadPool*)arg;
	Task *task;
	while(run) {
		if((osIsQueueEmpty(pool->taskQueue)) && (pool->destroy==FALSE)) {
			pthread_mutex_lock(&m);
			pthread_cond_wait(&cond, &m);
			pthread_mutex_unlock(&m);
		} else if((osIsQueueEmpty(pool->taskQueue)) && (pool->destroy==TRUE)) {
			pthread_cond_signal(&cond);
			break;
		}
		pthread_mutex_lock(&m);
		task = osDequeue(pool->taskQueue);
		pthread_mutex_unlock(&m);
		(*task->func)(task->args);
		free(task);

	}
	return NULL;
}

