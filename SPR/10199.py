from collections import defaultdict

def dfs(node, parent):
    global n, dfn, low, edge, cameras, name

    child = 0
    isAP = False
    n += 1
    dfn[node] = n
    low[node] = n

    for neighbor in edge[node]:
        if dfn[neighbor] == -1:
            child += 1
            dfs(neighbor, node)
            low[node] = min(low[node], low[neighbor])
            if low[neighbor] >= dfn[node]:
                isAP = True
        elif neighbor != parent:
            low[node] = min(low[node], dfn[neighbor])

    if (node == name and child > 1) or (node != name and isAP):
        cameras.append(node)

results = []
d = 0
while True:
    global n, dfn, low, edge, cameras, name

    N = int(input())
    if N == 0:
        break

    d += 1
    n = 0
    dfn = {}
    low = {}
    edge = defaultdict(list)
    cameras = []

    for _ in range(N):
        dfn[input()] = -1

    R = int(input())

    for _ in range(R):
        A, B = input().split()
        edge[A].append(B)
        edge[B].append(A)

    for node, value in dfn.items():
        if value == -1:
            name = node
            dfs(name, "")

    cameras.sort()

    results.append(("City map #" + str(d) + ": " + str(len(cameras)) + " camera(s) found", cameras))

for i in range(len(results)):
    print(results[i][0])
    for camera in results[i][1]:
        print(camera)
    if i < len(results)-1:
        print("")
