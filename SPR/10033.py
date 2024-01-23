results = []
n = int(input())
input()
for _ in range(n):
    register = [0] * 10
    ram = [0] * 1000
    count = 0

    i = 0
    while True:
        try:
            command = input()
            if command in ['', ' ', '\n']:
                break                
            else:
                ram[i] = int(command)
                i += 1
        except EOFError:
            break

    instruction = 0
    while True:
        f = ram[instruction] // 100
        d = ram[instruction] % 100 // 10
        n = ram[instruction] % 10
        instruction += 1
        count += 1

        if f == 1:
            break
        elif f == 2:
            register[d] = n
        elif f == 3:
            register[d] += n
            register[d] %= 1000
        elif f == 4:
            register[d] *= n
            register[d] %= 1000
        elif f == 5:
            register[d] = register[n]
        elif f == 6:
            register[d] += register[n]
            register[d] %= 1000
        elif f == 7:
            register[d] *= register[n]
            register[d] %= 1000
        elif f == 8:
            register[d] = ram[register[n]]
        elif f == 9:
            ram[register[n]] = register[d]
        elif f == 0:
            if register[n] != 0:
                instruction = register[d]


    results.append(count)

for i in range(len(results)):
    print(results[i])
    if i < len(results) - 1:
        print("")

