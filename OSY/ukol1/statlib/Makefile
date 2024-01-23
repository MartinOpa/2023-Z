TARGET=matgen

all: lib${TARGET}.a

${TARGET}.o: ${TARGET}.cpp
	g++ -c $< -o $@ -fPIC

lib${TARGET}.a: ${TARGET}.o
	ar rcs $@ $^

clean:
	rm -f ${TARGET}.o lib${TARGET}.a
