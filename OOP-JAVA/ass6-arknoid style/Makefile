# Yana Patyuk

compile: bin
	javac -d bin -cp biuoop-1.4.jar src/*/*.java src/*.java
run:
	java -cp biuoop-1.4.jar:bin:resources Ass6Game 
jar:
	jar cfm ass6game.jar Manifest.mf -C bin . -C resources .
check:
	java -jar checkstyle-5.7-all.jar -c biuoop.xml src/*/*.java src/*.java
bin:
	mkdir bin
