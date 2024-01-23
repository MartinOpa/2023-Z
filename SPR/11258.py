def eval(str):
    num = 0
    for i in range(len(str)):
        num = num * 10 + int(str[i])
    if num <= 2147483647:
        return num
    else:
        return -1

def parse(str):
    n = len(str)
    arr = [0] * (n+1)

    for i in range(n+1):
        maxNum = 0
        for j in range(min(i, 11)):
            maxNum = max(maxNum, arr[i-j] + eval(str[i-j:i]))
        arr[i] = maxNum

    return arr[n]

output = []
n = int(input())
for _ in range(n):
    str = '0' + input()
    output.append(parse(str))

for out in output:
    print(out)
