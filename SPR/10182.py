def process(coord):
    if coord == 1:
        print(0, 0)
        return
    
    depth, x, y = 0, 0, 0
    while True:
        depth += 1
        y += 1
        coord -= 1

        if coord == 1:
            print(x, y)
            return

        for _ in range(depth - 1):
            x -= 1
            y += 1
            coord -= 1
            if coord == 1:
                print(x, y)
                return

        for _ in range(depth):
            x -= 1
            coord -= 1
            if coord == 1:
                print(x, y)
                return

        for _ in range(depth):
            y -= 1
            coord -= 1
            if coord == 1:
                print(x, y)
                return

        for _ in range(depth):
            x += 1
            y -= 1
            coord -= 1
            if coord == 1:
                print(x, y)
                return

        for _ in range(depth):
            x += 1
            coord -= 1
            if coord == 1:
                print(x, y)
                return

        for _ in range(depth):
            y += 1
            coord -= 1
            if coord == 1:
                print(x, y)
                return

coords = []
while True:
    try:
        coords.append(int(input()))
    except EOFError:
        break
  
for coord in coords:
    process(coord)

