/*
 * Yana Patyuk
 */
#include <unistd.h>
#include <sys/stat.h>
#include <fcntl.h>
#include<sys/types.h>
#include <sys/wait.h>
#include<stdlib.h>
#include <string.h>
#include <stdio.h>



enum boolean{false, true};
enum states{error, notEqual, similar, equal};


int IsEqual(char first, char second);
int IsSpace(char c);


int main (int argc, char* argv[]){
	char errorString[] = "Error in system call\n";
	char fBuff = 0,sBuff = 0;
	int checking = true, endFirst = false, endSecond = false;
	int firstFileDes, secondFileDes;//file disacriprors.
	int finalState = equal, status = equal;
	int readFirst = true;
	int readSecond = true;
	int n = 0;
//if the input dint have to strings.
	if (argc <= 1) {
		write(2, errorString, strlen(errorString)+1);
		exit(-1);
	}
	if((firstFileDes=open(argv[1],O_RDONLY))==-1) {
		write(2, errorString, strlen(errorString)+1);
		exit(-1);
	}
	if((secondFileDes=open(argv[2],O_RDONLY))==-1) {
		write(2, errorString, strlen(errorString)+1);
		exit(-1);
	}

	while(checking) {//reads from each file one byte and compare them.

		if((!endFirst) && (readFirst)) {//if its not the end of file or we need to read:
			if((n =read(firstFileDes,&fBuff,sizeof(fBuff)))<=0) {
				if(n == 0) endFirst = true;//if we read 0 bytes means its the end
				else write(2, errorString, strlen(errorString)+1);

			}
		}
		if((!endSecond) && readSecond) {//if its not the end of file or we need to read:
			if((n=read(secondFileDes,&sBuff,sizeof(sBuff)))<=0){
				if(n == 0)endSecond = true;//if we read 0 bytes means its the end
				else write(2, errorString, strlen(errorString)+1);
			}
		}
		//if we get to the end of both files-stop the loop.
		if(endSecond && endFirst) checking = false;


		//check if buffers are identical
		status = IsEqual(fBuff, sBuff);
		if(status == notEqual) {
			if(IsSpace(fBuff)) {
				readSecond = false;//read only first file
				readFirst = true;//i add this part to prevent "dead-lock"
				finalState = similar;
				continue;
			} else if (IsSpace(sBuff)) {
				readFirst = false;//read only second file
				readSecond = true;//i add this part to prevent "dead-lock"
				finalState = similar;
				continue;
			}//if they are not identical/similar-stop reading.
			finalState = notEqual;
			break;
		} else if(status == similar) {
			finalState = status;
		}
		//update to read from both files in next iter;
		readFirst = true;
		readSecond = true;
	}
	//close files
	close(firstFileDes);
	close(secondFileDes);
	//return final answer
	return finalState;
}

/*
 * chack if given two chare are equal.
 * if not-check if they are letters and one is bigger/smaller then other one.
 */
int IsEqual(char first, char second) {
	if(first==second) {
		return equal;
	}else if((first >= 'A' && first <= 'Z') && ((first + 32) == second)) {
		return similar;
	} else if((second >= 'A' && second <= 'Z') && ((second + 32) == first)) {
		return similar;
	}
	return notEqual;
}
/**
 * check if given char is a space or new row or a tube
 */
int IsSpace(char c) {
	if(c == '\n' || c == '\t' || c == ' ') {
		return true;
	}
	return false;
}


