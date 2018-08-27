
// Yana Patyuk

#include <stdio.h>
#include <string.h>
enum boolean{true, false}taWin, soWin; // we create inum for boolean members.
//thous are global members. we use them in each function. we define them in defineSpces func.
char sourceSpace[4], targetSpace[4], endWinSource[2], endWinTarget[2];


void regularCopy(char* argv[]);
void mission2(char* argv[]);
void mission3(char* argv[]);
void defineSpces(char* argv[], enum boolean bigEndian, enum boolean swapTarget);
void swap (char * a, char* b);
void copy(char* a, char * b);



int main (int argc, char* argv[]) {
	 soWin = false;
	 taWin = false;
	switch(argc) {//chack how many arguments was given.
	case 3://mission 1-copy files without changes.
		regularCopy(argv);
		break;
	case 5://mission 2-change only space writing.
		mission2(argv);
		break;
	case 6://mission 3-change space writing and chang endianness.
		if((strcmp(argv[5], "-keep") == 0)) {//if we dont swap we-mission3 is like mission2.
			mission2(argv);
		} else if((strcmp(argv[5], "-swap") == 0)) {//include swaping bytes.(
			mission3(argv);
		}
			break;
	default://any other option of inputs-exit.
		break;
	}
}
/**
 * input: argv users name files.
 * the function create new file and copy without any changes all the bytes.
 * the buffer member read and write 2 bytes each time beacuse the files in UTF-16
 */
void regularCopy(char* argv[]) {
  FILE *source, *target;
  char buffer[2];
  int n;
	source = fopen(argv[1], "r");
	target = fopen(argv[2], "w");
	//if open is failed-return.
	if ((source == NULL) || (target == NULL)) {
		return;
	}//read the file till end of file.
	while((n = fread(buffer, 1, 2, source)) >=1) {
			(n = fwrite(buffer, 1, 2, target));
		}
	fclose(target);
	fclose(source);
}
/**
 * input: list of arguments of users chouce.
 * the function check witch typpe the sorce file and to what type of files operate. than it calls
 * defineSpces to define global members of space types.
 * each round it check if we found a space and write new space insted.
 * if no space- copy regulary.
 * the buffer member read and write 2 bytes each time beacuse the files in UTF-16
 */
void mission2(char* argv[]) {
	   FILE *source, *target;
	   char buffer[2];
	   int n;
		source = fopen(argv[1], "r");
		target = fopen(argv[2], "w");
		if ((source == NULL) || (target == NULL)) { return; }
		n = fread(buffer, 1, 2, source);
		n = fwrite(buffer, 1, 2, target);
		//check witch type is the file-Big or Littel.
		if(buffer[0] == 0xfffffffe) {
			defineSpces(argv, true, true);
		} else {
			defineSpces(argv, false, false);
		}
		//strat copy
		while((n = fread(buffer, 1, 2, source)) >= 1) {//stop the loop when the file ends.
			//check if buffer is space.
			if((buffer[0] == sourceSpace[0]) && (buffer[1] == sourceSpace[1])) {
				if(soWin ==  true) {//if the sorce in win type-check the next byte to find \r\n
					if((n = fread(buffer, 1, 2, source)) >= 1 ) {//if the next one complite the space of win-change.
						if ((buffer[0] == endWinSource[0]) && (buffer[1] == endWinSource[1]) ) {
							//write new space type.
							n = fwrite(targetSpace, 1, 2, target);
							if(taWin == true) {//if new type is win too, copy the next one.
								n = fwrite(endWinTarget, 1, 2, target);
							}
						} else {//if only the first byte is the same as in win.
							n = fwrite(sourceSpace, 1, 2, target);
							n = fwrite(buffer, 1, 2, target);
						}
					}
				} else {//if the sorce type is not win.
					n = fwrite(targetSpace, 1, 2, target);
					if(taWin == true) {//if the new type is win-complete writ
						n = fwrite(endWinTarget, 1, 2, target);
					}
				}
			} else {//copy regularly.
				n = fwrite(buffer, 1, 2, target);
			}
		}
		fclose(target);
		fclose(source);
}

/**
 * idea: change space types and swap endianness file.
 * input: argv list of users input.
 * the function open the source file and create new file.
 * is copy the bytes by users chpices flag.
 * the functuion call defineSpces and define the new space type.
 *  each round we swap the bytes before we write them in the new file.
 * the buffer member read and write 2 bytes each time beacuse the files in UTF-16
 */
void mission3(char* argv[]) {
	   FILE *source, *target;
	   char buffer[2];
	   char tamp[2];
	   int n;
		source = fopen(argv[1], "r");
		target = fopen(argv[2], "w");
		if ((source == NULL) || (target == NULL)) { return; }
		//check witch type is the file(endianness) and define the sorce and new spaces.
		n = fread(buffer, 1, 2, source);
		if(buffer[0] == 0xfffffffe) {
			 defineSpces(argv, true, false);
		} else {
				defineSpces(argv, false, true);
		}//swap type of file.
		swap(&buffer[0],&buffer[1]);
		n = fwrite(buffer, 1, 2, target);
//start copy and switching
		while((n = fread(buffer, 1, 2, source)) >= 1) {
			//if buffer is space
			if((buffer[0] == sourceSpace[0]) && (buffer[1] == sourceSpace[1])) {
				if(soWin ==  true) {//if source file type win :check if next byte completes sourceSpace.
					if ((n = fread(buffer, 1, 2, source)) >=1 ) {
						if ((buffer[0] == endWinSource[0]) && (buffer[1] == endWinSource[1]) ) {
							//copy new space to the file.
							n = fwrite(targetSpace, 1, 2, target);
							if (taWin == true) {//if new file type win-copy the second byte.
								n = fwrite(endWinTarget, 1, 2, target);
							}
						} else { //if only the first byte of win appear-swap the byte and continue write.
							copy(tamp, sourceSpace);
							swap(&tamp[0], &tamp[1]);
							n = fwrite(tamp, 1, 2, target);
							swap(&buffer[0],&buffer[1]);
							n = fwrite(buffer, 1, 2, target);
						}
					}//if sorce file is not type win
				} else {
					n = fwrite(targetSpace, 1, 2, target);
					if (taWin == true) {//if new file type win-complete space sourceSpace
						n = fwrite(endWinTarget, 1, 2, target);
					}
				}
			} else {//if byte is not space-swap the bytes and copy.
				swap(&buffer[0],&buffer[1]);
				n = fwrite(buffer, 1, 2, target);
			}
		}
		fclose(target);
		fclose(source);
}
/**
 * name: swap
 * input: a and b pointers to swap their values.
 * output: non.
 */
void swap (char * a, char* b) {
	char temp;
	temp = *b;
  *b = *a;
  *a = temp;
}
/**
 * input: pointers a and b.
 * copy values of b to a.
 */
void copy(char* a, char * b) {
  a[0] = b[0];
  a[1] = b[1];
}
/**
 * input:argv-that the user gave. contains chooses of flags.
 *    bigEndian boolean to define if sources file endianness.
 *    swapTarget boolean to define if target file should swap.
 *    the function defaine the global members of sorce and targes space with hex by
 *    the choice the user made and the type of source file.
 */
 void defineSpces(char* argv[], enum boolean bigEndian, enum boolean swapTarget) {
	 //check if sorce file is big endian or littel.
	 if(bigEndian ==  true) {
		 if((strcmp(argv[3], "-unix") == 0)) {
			sourceSpace[0] = 0x00;
			sourceSpace[1] = 0x0a;
		} else if ((strcmp(argv[3], "-mac") == 0)) {
			sourceSpace[0] = 0x00;
			sourceSpace[1] = 0x0d;
		} else if ((strcmp(argv[3], "-win") == 0)) {
			soWin = true;
			sourceSpace[0] = 0x00;
			sourceSpace[1] = 0x0d;
			endWinSource[0] = 0x00;
			endWinSource[1] = 0x0a;
		}
	} else {
		if((strcmp(argv[3], "-unix") == 0)) {
			sourceSpace[0] = 0x0a;
			sourceSpace[1] = 0x00;
		} else if ((strcmp(argv[3], "-mac") == 0)) {
			sourceSpace[0] = 0x0d;
			sourceSpace[1] = 0x00;
		} else if ((strcmp(argv[3], "-win") == 0)) {
			soWin = true;
			sourceSpace[0] = 0x0d;
			sourceSpace[1] = 0x00;
			endWinSource[0] = 0x0a;
			endWinSource[1] = 0x00;
		}
	}
	 //check if target file should be swaped.
	if (swapTarget == true) {
		if(strcmp(argv[4], "-unix") == 0) {
			targetSpace[0] = 0x00;
			targetSpace[1] = 0x0a;
		} else if(strcmp(argv[4], "-mac") == 0) {
			targetSpace[0] = 0x00;
			targetSpace[1] = 0x0d;
		} else if (strcmp(argv[4], "-win") == 0) {
			taWin = true;
			targetSpace[0] = 0x00;
			targetSpace[1] = 0x0d;
			endWinTarget[0] = 0x00;
			endWinTarget[1] = 0x0a;
		}
	} else {
	if(strcmp(argv[4], "-unix") == 0) {
		targetSpace[0] = 0x0a;
		targetSpace[1] = 0x00;
	} else if(strcmp(argv[4], "-mac") == 0) {
		targetSpace[0] = 0x0d;
		targetSpace[1] = 0x00;
	} else if (strcmp(argv[4], "-win") == 0) {
		taWin = true;
		targetSpace[0] = 0x0d;
		targetSpace[1] = 0x00;
		endWinTarget[0] = 0x0a;
		endWinTarget[1] = 0x00;
	}

	}
 }
