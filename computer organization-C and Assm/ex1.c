// Yana Patyuk


#include <stdio.h>
//#include "ex1.h"
/**
 * we create word type long with hex.
 * the function check with pointers ,the first byte in memory.
 * if the 0 place equals 01 means it little endian.
 * if its 00 means it Big endian.
 */
int is_little_endian() {
	long word = 0x0001;
	char* memory = (char*) &word;
	if (memory[0] == 0x01) {//littel
		return 1;
	} else {//big
		return 0;
	}
}
/**
 * the functions check witch type is the machin.
 * if the mechin is littel, means LSB in the start (place 0) and sopy it.
 * else-means LSB in the end.
 */
unsigned long merge_bytes(unsigned long x, unsigned long int y) {
	char* xMemory = (char*)&x;
	char* yMemory = (char*)&y;
	if (is_little_endian() == 1) {//Little endian
		xMemory[0] = yMemory[0];
	} else {//Big
		xMemory[sizeof(x)-1] = yMemory[sizeof(x)-1];
	}
	return x;
}
/**
 * first we check witch type is the machine.
 * if little- place i is in order. if not-place i we count from end.
 * the end of the bytes calculate by its size-1.
 */
unsigned long put_byte(unsigned long x, unsigned char b, int i) {
	char* xMemory = (char*)&x;
	char* bMemory = (char*)&b;
	if (is_little_endian() == 0) {//big endian
		xMemory[sizeof(x) - i] = bMemory[0];
	} else {//little endian
		xMemory[i] = bMemory[0];
	}
	return x;
}
