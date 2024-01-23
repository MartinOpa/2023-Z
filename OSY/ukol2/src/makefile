TARGET=lsProgram
TARGET2=pipeProgram

all: ${TARGET} ${TARGET2}

${TARGET}: ${TARGET}.cpp
	g++ $^ -o $@

${TARGET2}: ${TARGET2}.cpp
	g++ $^ -o $@

clean:
	rm -f ${TARGET} ${TARGET2}