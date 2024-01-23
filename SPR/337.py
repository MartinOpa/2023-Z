def write(c):
    global cursor, insertMode, terminal

    if insertMode:
        for i in range(9, cursor[0], -1):
            terminal[cursor[1]][i] = terminal[cursor[1]][i - 1]

    terminal[cursor[1]][cursor[0]] = c

    if cursor[0] < 9:
        cursor = (cursor[0] + 1, cursor[1])

    return

t = 0
while True:
    n = int(input())
    if n == 0:
        break

    cursor = (0, 0)
    insertMode = False
    terminal = [[' ' for _ in range(10)] for _ in range(10)]

    for _ in range(n):
        line = input()

        j = 0
        while j < len(line):
            if line[j] == '^':
                j += 1
                command = line[j]

                if command == 'b':
                    cursor = (0, cursor[1])
                elif command == 'c':
                    terminal = [[' ' for _ in range(10)] for _ in range(10)]
                elif command == 'd':
                    if cursor[1] < 9:
                        cursor = (cursor[0], cursor[1] + 1)
                elif command == 'e':
                    terminal[cursor[1]][cursor[0]:] = [' '] * (10 - cursor[0])
                elif command == 'h':
                    cursor = (0, 0)
                elif command == 'i':
                    insertMode = True
                elif command == 'l':
                    if cursor[0] > 0:
                        cursor = (cursor[0] - 1, cursor[1])
                elif command == 'o':
                    insertMode = False
                elif command == 'r':
                    if cursor[0] < 9:
                        cursor = (cursor[0] + 1, cursor[1])
                elif command == 'u':
                    if cursor[1] > 0:
                        cursor = (cursor[0], cursor[1] - 1)
                elif command == '^':
                    write('^')
                else:
                    cursor = (int(line[j + 1]), int(command))
                    j += 1

            else:
                write(line[j])

            j += 1

    print(f"Case {t + 1}")
    print("+----------+")
    for i in range(10):
        print("|" + ''.join(terminal[i]) + "|")
    print("+----------+")
    t += 1

