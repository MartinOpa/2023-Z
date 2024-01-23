def processHand(hand):
    if ((hand[0][0] == hand[1][0] + 1) and 
        (hand[1][0] == hand[2][0] + 1) and 
        (hand[2][0] == hand[3][0] + 1) and 
        (hand[3][0] == hand[4][0] + 1) and 

        (hand[0][1] == hand[1][1]) and 
        (hand[1][1] == hand[2][1]) and 
        (hand[2][1] == hand[3][1]) and 
        (hand[3][1] == hand[4][1])):
        return (8, hand[0][0], [x[0] for x in hand])

    for i in range(len(hand) - 3):
        if ((hand[i][0] == hand[i + 1][0]) and
            (hand[i + 1][0] == hand[i + 2][0]) and
            (hand[i + 2][0] == hand[i + 3][0])):
            return (7, hand[i][0], [x[0] for x in hand])

    if ((hand[0][0] == hand[1][0]) and 
        (hand[1][0] == hand[2][0]) and 
        (hand[3][0] == hand[4][0])):
        return (6, [hand[0][0], hand[3][0]], [x[0] for x in hand])
    
    if ((hand[0][0] == hand[1][0]) and 
        (hand[2][0] == hand[3][0]) and 
        (hand[3][0] == hand[4][0])):
        return (6, [hand[2][0], hand[0][0]], [x[0] for x in hand])

    if ((hand[0][1] == hand[1][1]) and 
        (hand[1][1] == hand[2][1]) and 
        (hand[2][1] == hand[3][1]) and 
        (hand[3][1] == hand[4][1])):
        return (5, hand[0][0], [x[0] for x in hand])

    if ((hand[0][0] == hand[1][0] + 1) and 
        (hand[1][0] == hand[2][0] + 1) and 
        (hand[2][0] == hand[3][0] + 1) and 
        (hand[3][0] == hand[4][0] + 1)):
        return (4, hand[0][0], [x[0] for x in hand])

    for i in range(len(hand) - 2):
        if (hand[i][0] == hand[i + 1][0]) and (hand[i + 1][0] == hand[i + 2][0]):
            return (3, hand[i][0], [x[0] for x in hand])

    for i in range(len(hand) - 3):
        if (hand[i][0] == hand[i + 1][0]) and (hand[i + 2][0] == hand[i + 3][0]):
            return (2, [hand[i][0], hand[i + 2][0]], [x[0] for x in hand])
    
    for i in range(len(hand) - 4):
        if (hand[i][0] == hand[i + 1][0]) and (hand[i + 3][0] == hand[i + 4][0]):
            return (2, [hand[i][0], hand[i + 3][0]], [x[0] for x in hand])
    
    for i in range(len(hand) - 1):
        if hand[i][0] == hand[i + 1][0]:
            return (1, hand[i][0], [x[0] for x in hand])

    return (0, [x[0] for x in hand], 0)

def process(deck):
    cards = deck.split()
    cards = [card.replace('T', '10').replace('J', '11').replace('Q', '12').replace('K', '13').replace('A', '14') for card in cards]
    cards = [(int(card[:-1]), card[-1]) for card in cards]
    hand1 = sorted(cards[:5], key=lambda x: x[0], reverse=True)
    hand2 = sorted(cards[5:], key=lambda x: x[0], reverse=True)
    p1 = processHand(hand1)
    p2 = processHand(hand2)

    if p1[0] > p2[0]:
        print("Black wins.")
    elif p2[0] > p1[0]:
        print("White wins.")
    else:
        if p1[1] > p2[1]:
            print("Black wins.")
        elif p2[1] > p1[1]:
            print("White wins.")
        else:
            if p1[2] > p2[2]:
                print("Black wins.")
            elif p2[2] > p1[2]:
                print("White wins.")
            else:
                print("Tie.")

hands = []
while True:
    try:
        hands.append(input())
    except EOFError:
        break
  
for hand in hands:
    process(hand)

