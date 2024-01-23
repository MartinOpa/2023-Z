class station:
    def __init__(self, distance, price):
        self.distance = distance
        self.price = price

def process(stations, dst):
    stations.append(station(dst, 999999999))

    n = len(stations) - 1
    arr = [[999999999 for _ in range(201)] for _ in range(n + 1)]
    arr[0][100] = 0

    for i in range(1, n + 1):
        d = stations[i].distance - stations[i - 1].distance

        for j in range(d, 201):
            arr[i][j - d] = arr[i - 1][j]

        for j in range(1, 201):
            arr[i][j] = min(arr[i][j], arr[i][j - 1] + stations[i].price)

    res = min(arr[n][100:])
    if res >= 999999999:
        return "Impossible"
    else:
        return str(res)

cases = int(input())
input()
results = []

for i in range(cases):
    dst = int(input())

    stations = [station(0, 0)]
    while True:
        try:
            input_line = input()
            if not input_line:
                break
            distance, price = map(int, input_line.split())
            stations.append(station(distance, price))

        except EOFError:
            break
    
    results.append(process(stations, dst))
    if i < cases - 1:
        results.append("")

for result in results:
    print(result)
