TARGET = main
LS = lsProgram
PIPE = pipeProgram
DIR = src
SRC = $(TARGET).cpp $(DIR)/${LS}.cpp $(DIR)/${PIPE}.cpp
OBJ = $(SRC:.cpp=.o)

all: $(TARGET)

$(TARGET): $(OBJ)
	g++ $^ -o $@

$(OBJ): %.o: %.cpp
	g++ -c $< -o $@

clean:
	rm -f $(OBJ) $(TARGET)
