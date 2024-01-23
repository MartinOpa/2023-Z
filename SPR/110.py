import sys
import string

output = []
programs = 0
count = 0
alphabet = list(string.ascii_lowercase)

def generateIfTree(index, str):
    if (index == count):
        chars = ""
        for i in range (0, index-1):
            chars += str[i] + ","
        chars += str[index-1]
        output.append(" "*index*2 + "writeln(" + chars + ")")
        return

    for i in range(index - 1, -1, -1):
        line = " "*index*2
        if (i != index-1):
            line += "else "
        line += "if " + str[i] + " < " + alphabet[index] + " then"
        output.append(line)

        tmp = str
        if (i + 1 > index - 1):
            tmp += alphabet[index]
        else:
            tmp = tmp[:i+1] + alphabet[index] + tmp[i+1:]

        generateIfTree(index + 1, tmp)
    
    output.append(" "*index*2 + "else")
    generateIfTree(index + 1, alphabet[index] + str)

programs = int(input())
counts = []

for i in range(0, programs * 2):
    try:
        counts.append(int(input()))
    except:
        continue

for i in range(0, programs):
    count = counts[i]
    output.append("program sort(input,output);\nvar")

    vars = "a"
    for j in range(1, count):
        vars = vars + "," + alphabet[j]

    output.append(vars + " : integer;\nbegin\n  readln(" + vars + ");")
    generateIfTree(1, "a")
    output.append("end.")
    if (i < programs-1):
        output.append("\n")

for line in output:
    if line != "\n":
        print(line)
    else:
        print("")
