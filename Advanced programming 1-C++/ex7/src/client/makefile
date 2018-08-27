# Ori Cohen
# Yana Patyuk

a.out: compileAll
	g++ *.o 
	rm -f *.o

compileAll: include/*.h src/*.cpp
	g++ -Iinclude/ -c src/*.cpp

run:
	./a.out
