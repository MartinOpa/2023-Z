class elephant:
    def __init__(self, line, weight, iq):
        self.line = line
        self.weight = weight
        self.iq = iq

def process(data):
    sortedData = sorted(data, key=lambda elephant:(-elephant.iq, elephant.weight))

    n = len(sortedData)
    arr = [1] * n 

    for i in range(1, n):
        for j in range(i):
            if sortedData[i].weight > sortedData[j].weight and sortedData[i].iq < sortedData[j].iq:
                arr[i] = max(arr[i], arr[j] + 1)

    maxLen = max(arr)  
    last = arr.index(maxLen)

    result = []
    for i in range(last, -1, -1):
        if arr[i] == maxLen and (len(result) == 0 or (sortedData[i].weight < result[-1].weight and sortedData[i].iq > result[-1].iq)):
            result.append(sortedData[i])
            maxLen -= 1

    result.reverse() 

    print(len(result))
    for elephant in result:
        print(elephant.line)

elephants = []
line = 1
while True:
    try:
        input_line = input().strip()
        if not input_line:
            break

        weight, iq = map(int, input_line.split())

        elephants.append(elephant(line, weight, iq))
        line += 1
    
    except EOFError:
        break

process(elephants)