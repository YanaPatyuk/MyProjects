# Ori Cohen
# Yana Patyuk

a.out: compileAll
	g++ *.o -pthread
	rm -f *.o

compileAll: include/*.h src/*.cpp
	g++ -Iinclude/ -c src/*.cpp

run:
	./a.out
