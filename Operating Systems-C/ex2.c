/*
*Yana Payuk
 */

#include<sys/types.h>
#include <sys/wait.h>
#include<unistd.h>
#include<stdlib.h>
#include <string.h>
#include <stdio.h>
#include <malloc.h>
#include <stdlib.h>

struct node {
	int val;
  int head;//indicate if its the first node=head.
  char* name;
  struct node *nextJob;
};
typedef struct node JOB;

void addToList(JOB** list, pid_t pid, char* name, int size);
void refreshList(JOB** list);
void deleteAll(JOB** list);
void printList(JOB** list);
void checkCd(char* string, char* pathDirection[]);
const int SIZE = 1000;

int main(int argc, char *argv[]) {
	char argumant[SIZE], copy[SIZE], prompt[8] = "prompt>";
	char* token;
	char*arrayArgs[SIZE];
	int exit = 1, i, val;
	JOB* list = NULL;
	pid_t value;

	while(exit) {//run untill you need to stop
		i = 0;
		/***first step: get from user the command, copy it
		 * 	for backup and split to words.
		 *	this step is not inside a function for convice reasons.
		 * ***/
		printf("%s", prompt);
		fgets(argumant, SIZE, stdin);
		//delete space
		argumant[strlen(argumant) - 1] = 0;
		strcpy(copy, argumant);
		token =  strtok(argumant, " ");
		/* split string and append tokens to 'arrayArgs' */
		while (token) {
			arrayArgs[i] = token;
			token = strtok (NULL, " ");
			i++;
		}
	 /*one extra element for the last NULL**/
		arrayArgs[i] = 0;

	/**Second Part: check users command.*/
			//print jobs list the currently running
		if(strcmp("jobs", arrayArgs[0]) == 0) {
			//refresh jobs in list
			refreshList(&list);
			//print the jobs
			printList(&list);
			//print the PID prossess and exit
		}else if(strcmp("exit", arrayArgs[0]) == 0) {
			exit = 0;
			printf("%d\n",getpid());
			deleteAll(&list);

			//cd commend wont create fork.
		}	else if(strcmp(arrayArgs[0],"cd")== 0) {
			printf("%d\n",getpid());
			checkCd(copy, arrayArgs);
		}else {//any other command-create a fork.
			value = fork();
			if(value < 0) {
				//error
				fprintf(stderr, "Error in system call\n");
		  } else if(value == 0) {//child pro
				if(strcmp(arrayArgs[i - 1], "&") == 0) {
					arrayArgs[i - 1] = 0;
					}
			  if(execvp(arrayArgs[0], arrayArgs) < 0) {
				  fprintf(stderr, "Error in system call\n");
			    return 0;//not suppose to return to here.
			  }
		  } else {//parent pro
				//print pid number and add to the list.
			  //note:if we use & we need to delete it first before operation.
				printf("%d\n",value);
				if(strcmp(arrayArgs[i - 1], "&") == 0) {
					//delete the sigh.
					copy[strlen(copy) - 1] = 0;
					copy[strlen(copy) - 1] = 0;
					addToList(&list, value, copy, strlen(copy));

				} else {
					addToList(&list, value, copy, strlen(copy));
					val=getpid();
					wait(&val);
				}
		   }
		}
		//clear the list
 	for(int j=0; j < i; j++){
			arrayArgs[i] = 0;
		}
	}

//free the memory
	return 0;
}



/**
 * This function gets pointer to the jobs list, pid value to add,
 * name of job and size.
 * its allocate memmory to the name and whole job
 * and add to the linked list.
 *
 */
void addToList(JOB** list, pid_t pid, char* name, int size) {
	JOB* new_node = (JOB*)malloc(sizeof(JOB));
	if(new_node == NULL) {
		fprintf(stderr, "Error in system call\n");
		return;
	}
	new_node->name  = malloc(size);
	if(new_node->name == NULL) {
		fprintf(stderr, "Error in system call\n");
		return;
	}
	//copy info
	 int i;
	 for (i=0; i<size; i++)
		 *(char *)(new_node->name + i) = *(char *)(name + i);

	 if(*list != NULL) {
		 new_node->nextJob = *list;
		 new_node->head = 1;
		 new_node->nextJob->head = 0;
		 *list = new_node;
	 } else {
		 new_node->nextJob = NULL;
		 new_node->head = 1;
		 *list = new_node;
	 }
	 new_node->val = (int) pid;
}

/**
 * get a pointer of the list jobs.
 * go over the list and delete process that ended.
 * each move save prev value to update if its next node deleted.
 */
void refreshList(JOB** list) {
	JOB* buff = *list, *buff2, *prev = NULL;
	//buff2 is temp buffer.
	while (buff != NULL) {
	// check if the job ended? and delete from list.
		if((waitpid(buff->val, NULL, WNOHANG)) != 0) {
			//update the head
			if(buff->head == 1) {
				*list = buff->nextJob;
				if(*list != NULL){
					buff->nextJob->head = 1;
				  }
			  }
			buff2 = buff;
			buff = buff->nextJob;
			if(prev != NULL)//if its not the head
				prev->nextJob = buff2->nextJob;
			free(buff2->name);
			free(buff2);
		}else {
			prev = buff;
		  buff = buff->nextJob;
		  }
	    }
}
/**
 * delete all memory from the list
 */
void deleteAll(JOB** list) {
	JOB* buff = *list;
	JOB* buff2;
	while (buff != NULL) {
		buff2 = buff;
		buff = buff->nextJob;
		free(buff2->name);
		free(buff2);
  }
}
/**
 * print the job list
 */
void printList(JOB** list) {
	JOB* buff;
	if(list != NULL) {
		buff = *list;
	  while (buff != NULL) {
		  printf("%d \t %s\n", buff->val, buff->name);
			buff = buff->nextJob;
	  }
	}
}
/**
 * execute all cd operations.
 * node:save last path for cd - operation.
 * word[1] is the second input of user: could be .. - ~
 *path or else zero.
 */
void checkCd(char* string, char* pathDirection[]) {
	static char lastPath[1000];
	if(pathDirection[1] == 0) {
		getcwd(lastPath, SIZE);
		if(chdir(getenv("HOME")) < 0)
				fprintf(stderr, "SError in system call\n");
	}	else if(strcmp(pathDirection[1], "..")==0) {
		getcwd(lastPath, SIZE);
		if(chdir(pathDirection[1]) < 0)
			fprintf(stderr, "Error in system call\n");
	} else if(strcmp(pathDirection[1], "~")==0) {
		getcwd(lastPath, SIZE);
		if(chdir(getenv("HOME")))
			fprintf(stderr, "Error in system call\n");
	} else if(strcmp(pathDirection[1], "-")==0) {
		strcpy(pathDirection[1], lastPath);
		checkCd(string, pathDirection);//use last path
	} else {//regular path: cd /path
		getcwd(lastPath, SIZE);
		if(chdir(pathDirection[1]))
			fprintf(stderr, "Error in system call\n");
	}
}

