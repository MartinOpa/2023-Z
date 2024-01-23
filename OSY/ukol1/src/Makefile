TARGET = generuj
TARGET2 = kalkulacka
LIB_PATH = ../statlib
LIB_NAME = matgen
LIB_PATH2 = ../dynlib2
LIB_NAME2 = mateval

all: ${TARGET} ${TARGET2}

${TARGET}: ${TARGET}.cpp
	g++ $< -o $@ -L${LIB_PATH} -l${LIB_NAME}

${TARGET2}: ${TARGET2}.cpp
	g++ $< -o $@ -L${LIB_PATH2} -l${LIB_NAME2}

clean:
	rm -f ${TARGET} ${TARGET2}