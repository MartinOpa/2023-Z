TARGET = mateval

all: ${TARGET}

${TARGET}: ${TARGET}.cpp
	g++ -Wall -fPIC -shared -o lib${TARGET}.so ${TARGET}.cpp

clean:
	rm -f lib$(LIB_NAME).so