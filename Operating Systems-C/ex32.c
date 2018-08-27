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
#include <dirent.h>

#define STUDENT_ANSWER_FILE "answers.txt"
#define STUDENT_PROGRAM_FILE "program.out"

void ReadPaths(char* filePath, char* students, char* input, char* output);
int FindCFilesOFStudent(char* path, char* CPathFile);
int IsCType(char* fileName);
int GetNumberOfCharsInRow(char* row);
int FileExict(const char * filename);
void DeleteFiles();
int CheckStudentFile(char* studentFilePath, char* input, char* output);
void ErrorExit();


enum boolean{false, true};
enum score{noFile, compileError, timeOut, badOutPut, similar, correct};
const char* MSG[] = {",0,NO_C_FILE\n", ",0,COMPILATION_ERROR\n",",0,TIMEOUT\n",
											",60,BAD_OUTPUT\n", ",80,SIMILAR_OUTPUT\n", ",100,GREAT_JOB\n"};


int main (int argc, char* argv[]){

	DIR *masterDir;
	struct dirent *mDirent;
	struct stat stat_p;
	int score = 0;
	int resultFile;
	char folderPath[160] = {0}, inputPath[160] = {0}, outPutPtah[160];
	char currentFilePath[160] = {0}, studentCFile[160] = {0};
	//if input doesn't contain 3 parameters.
	if(argc != 2) {
		printf("only path to config file needed");
		return -1;
	}
	//open new file to write the result of the chack
	if((resultFile=open("results.csv",O_CREAT|O_TRUNC|O_WRONLY, 0777))==-1) {
		ErrorExit();
	}
	//read from configuration file the info.
	ReadPaths(argv[1],folderPath, inputPath,outPutPtah);
	//open the directory of folders
	if ((masterDir = opendir(folderPath)) == NULL)
		ErrorExit();
	// looping through the directory, to check each student
	while ((mDirent = readdir(masterDir)) != NULL){
		//create full path of correntt item.
		strcpy(currentFilePath,folderPath);
		strcat(currentFilePath,"/");
		strcat(currentFilePath, mDirent->d_name);
		//check if the path is an student folder.
		if((stat(currentFilePath, &stat_p)== -1) &&(stat(mDirent->d_name, &stat_p)== -1)) {/* declare the 'stat' structure */
			ErrorExit();
		}
		//if stat is directory and not "." or ".."-means is a student
		if((S_ISDIR(stat_p.st_mode))&& (strcmp(mDirent->d_name,"..")!=0)&&
				(strcmp(mDirent->d_name,".")!=0)) {
			//check if the student have a c file or not.
			if(FindCFilesOFStudent(currentFilePath, studentCFile)) {
				//compile and run students work and get its score.
				score = CheckStudentFile(studentCFile, inputPath,outPutPtah);
			} else {//means no c files.
				score = noFile;
			}
			//write student score in results file.
			//note:first students name and then its score.
		  if(write(resultFile,mDirent->d_name, strlen(mDirent->d_name)) <=0) {
			  ErrorExit();
		  }
		  if((write(resultFile,MSG[score], strlen(MSG[score])) <=0)) {
			  ErrorExit();
		  }
		  //cleare directions.
			bzero(currentFilePath,160);
			bzero(studentCFile,160);
		}
	}
	closedir(masterDir);
	close(resultFile);
	return 0;
}
/**
 * input: string of file to compile
 * output: if the compilation went correct, "program.out" file created.
 * so we return if the file created or not.
 */
int CompileStudentFile(char* studentFilePath) {
	char* paramCompile[] = { "gcc", "-o", STUDENT_PROGRAM_FILE , studentFilePath, NULL};
	pid_t pid;
	int status;
	pid = fork();
	if(pid == 0) {//this is "child"-compile the program
		if(execvp("gcc",paramCompile) < 0) ErrorExit();
	} else {//"father process" wait until compilation over.
		waitpid(pid,&status, 0);
	}
	//return if compilation went wrong.
	return FileExict(STUDENT_PROGRAM_FILE);
}
/**
 * input: path to input for users program.
 * output: result-if the program didnt stop after 5 sec return false.
 */
int RunStudentProgram(char* input) {
	pid_t pid;
	int status, studentResultFile, inputFd;
	pid = fork();
	if(pid==0) {//child create an answers file for student output from program.
		if((studentResultFile=open(STUDENT_ANSWER_FILE,O_CREAT|O_TRUNC|O_RDWR, S_IRUSR| S_IWUSR))==-1) {
			ErrorExit();;
		}//place in stdout.
		dup2(studentResultFile, 1);
		close(studentResultFile);
		//get the fd of the input and place it in stdin
		if((inputFd=open(input, O_RDONLY))==-1) {
			ErrorExit();
		  }
		dup2(inputFd, 0);
		close(inputFd);
		//run students program.
		execl("./program.out", STUDENT_PROGRAM_FILE, NULL);
	} else {
		sleep(5);//wait 5 sec
		//if student didnt finish running. dont wait and return error.
		if(waitpid(pid, NULL, WNOHANG) == 0) {
	    DeleteFiles();
 			return false;
		}
	}
	return true;
}
/**
 * input: path to output file to compere student answers.
 * return the output from the compere exit status.
 */
int CompereOutPuts(char* output) {
	char* compereParm[] = { "comp.out",STUDENT_ANSWER_FILE, output, NULL };
    pid_t pid;
    int status = 0;
    pid = fork();
    if(pid == 0) {//child-run the compare.
    	execvp("./comp.out", compereParm);
    } else {//wait until process child ends to compare.
    	waitpid(pid, &status, 0);
    }
  DeleteFiles();//delete unnecessary files.
	return WEXITSTATUS(status);//return the outcome.
}
/**
 * input:studentFilePath is string to student file
 * 					input: the input for the program.
 * 					output: path to a file to compare student output to the file.
 * 	output:score the student got.
 */
int CheckStudentFile(char* studentFilePath, char* input, char* output) {
	int score;
	//if compilation didnt worked
	if(!CompileStudentFile(studentFilePath)) return compileError;
	//check if runtime out error.
	if(!RunStudentProgram(input)) return timeOut;
	//check student output.
	score = CompereOutPuts(output);
	return score + 2;//we add 2 beacuse in the enum the answers 1 is 3 and so on.
}


/**
 * delete the answer file and program of student.
 */
void DeleteFiles() {
unlink(STUDENT_ANSWER_FILE);
unlink(STUDENT_PROGRAM_FILE);
}
/**
 * input:file name
 * return:true if file exsit in current path.
 * note:we use it to detemine if program output created.
 */
int FileExict(const char * filename){
	struct stat s;
  char path[160];//path related to the path of the file
  int i;
	getcwd(path, 160);
	strcat(path, "/");
	strcat(path, filename);
    //check if the file exist
  if(stat(path, &s) == 0) {
      i = strtol("0777", 0, 8);//change file
      if (chmod (path,i) < 0)
    	  printf("error in chmod");
    	return true;
    }
    //File doesn't exist
    return false;
}

/**
 * inpue:string to path of students folder and pointer the file(if wil found).
 * find in path C file.
 * if no such file exist return false.
 * note:status if we fined c file. if we fined one we copy path to CpathFile.
 */
int FindCFilesOFStudent(char* path, char* CPathFile) {
	DIR *masterDir;
	struct dirent *mDirent;
	struct stat stat_p;
	int status = false;
	char tampPath[160];
	if ((masterDir = opendir(path)) == NULL)
			return false;
	// looping through the directory, printing the directory entry name
	while ((mDirent = readdir(masterDir)) != NULL){
		//create a full path from the current item
		bzero(tampPath,160);
		strcpy(tampPath, path);
		strcat(tampPath, "/");
		strcat(tampPath,mDirent->d_name);
		//if the path correct
		if(stat(tampPath, &stat_p)== -1) /* declare the 'stat' structure */
			return false;
		//check if the path is a folder and not "." or ".."
		if(S_ISDIR(stat_p.st_mode) && (strcmp(mDirent->d_name,"..")!=0)&&
				(strcmp(mDirent->d_name,".")!=0)) {
			//if it is a directory-find inside the folder-recursive.
			status = FindCFilesOFStudent(tampPath, CPathFile);
			if(status != false) break;//if we fount a file stop and return
		}else if(IsCType(mDirent->d_name)) {//check the item id c type
			status = true;
			//copy the path of file to cPathFile
			strcpy(CPathFile, tampPath);
			break;
		}
	}
	//close directory
	closedir(masterDir);
	return status;
}

/**
 * check if given file name is c type (ends with ".c").
 * return false if not.
 */
int IsCType(char* fileName) {
	int lengthOfName = strlen(fileName);
	if((fileName[lengthOfName - 2] == '.') &&
			(fileName[lengthOfName - 1]== 'c')) {
		return true;
	}
		return false;
}

/**
 * input: pointer to the start of the row
 * return number of chars till end of the row
 */
int GetNumberOfCharsInRow(char* row) {
	int sum = 0;
	while((row[sum] != '\n') && (row[sum] != EOF)) {
		sum++;
	}
	return sum;
}
/**
 * read from file path information and copy it to given pointers to arrays.
 * note: we read each time 160 char's but we copy only till the end of current row.
 */
void ReadPaths(char* filePath, char* students, char* input, char* output) {
	int infoFile, charNum, tamp;//charNum: means how many bytes in the row.
	char buffer[160];
	//open new file to write the result of the chack
	if((infoFile=open(filePath,O_RDONLY))==-1) {
		ErrorExit();
	}
	//read first row
	if((charNum =read(infoFile, buffer,160))<=0) {
		ErrorExit();
	}
	charNum = GetNumberOfCharsInRow(buffer);
	bzero(students,160);
	strncpy(students, buffer, charNum);
	//read second row. "jump" to the end of first row and read from there.
	bzero(buffer,160);
	tamp = charNum;
	if(lseek(infoFile, charNum + 1, SEEK_SET) < 0) return;
	if((charNum =read(infoFile, buffer,160))<=0) {
			//exit
	}
	charNum = GetNumberOfCharsInRow(buffer);
	bzero(input,160);
	strncpy(input, buffer, charNum);
	//read third row. "jump" to the end of seconf row and read from there.
	bzero(buffer,160);
	bzero(output,160);
	if(lseek(infoFile, charNum + tamp + 2, SEEK_SET) < 0) return;
	if((charNum =read(infoFile, buffer,160))<=0) {
		ErrorExit();
	}
	charNum = GetNumberOfCharsInRow(buffer);
	strncpy(output, buffer, charNum);
	close(infoFile);
}
/**
 * error in system call-write to stderr (2) and exit.
 */
void ErrorExit() {
	const char errorMessage[] = "Error in system call";
	write(STDERR_FILENO,errorMessage ,strlen(errorMessage));
	exit(EXIT_FAILURE);
}



