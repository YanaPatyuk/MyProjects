/*
*Yana Patyuk
 */
#ifndef __THREAD_POOL__
#define __THREAD_POOL__
#include "osqueue.h"
#include <unistd.h>
#include <sys/stat.h>
#include <fcntl.h>
#include<sys/types.h>
#include <sys/wait.h>
#include<stdlib.h>
#include <string.h>
#include <stdio.h>
#include <dirent.h>
#include <pthread.h>
const enum boolean{FALSE, TRUE}bool;

typedef struct thread_pool
{
 int numberOfThreads;//number of threads
 OSQueue* taskQueue; //tasks list
 pthread_t * threads;//array of threads
 int destroy; //destroy flag.
 pthread_mutex_t m;
 pthread_mutex_t mDestroy;//this tread used only in destroy to prevent deadlocks.
 pthread_cond_t cond;
 int run;//flag for system running.
}ThreadPool;

typedef struct task
{
 void(*func)(void *);
 void* args;
}Task;

ThreadPool* tpCreate(int numOfThreads);

void tpDestroy(ThreadPool* threadPool, int shouldWaitForTasks);

int tpInsertTask(ThreadPool* threadPool, void (*computeFunc) (void *), void* param);

#endif
