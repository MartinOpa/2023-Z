#include "main.h"

using namespace std;

sem_t s_mutex; 
sem_t s_empty; 
sem_t s_full;

int N = (sizeof(names) / sizeof(names[0]));

queue<pair<int, string>> buffer;

int produced = 0;
int consumed = 0;
int current = -1;

string produceItem() {
    current++;
    if (current < N) {
        return string(names[current]);
    } else {
        return "";
    }
}

void insertItem(pair<int, string> item) {
    buffer.push(item);
}

pair<int, string> removeItem() {
    pair<int, string> item = buffer.front();
    return item;
}

void consumeItem(pair<int, string> item) {
    buffer.pop();
}

void *producer(void *t_ptr) {
    pair<int, string> item;
    while (1) {
        item = make_pair(int(gettid()), produceItem());
        if (item.second.empty()) {
            sleep(5);
            continue;
        }
        
        sem_wait(&s_empty);
        sem_wait(&s_mutex);

        insertItem(item);
        produced++;

        sem_post(&s_mutex);
        sem_post(&s_full);
        sleep(3);
    }
}

void *consumer(void *t_ptr) {
    pair<int, string> item;
    while (1) {
        sem_wait(&s_full);
        sem_wait(&s_mutex);

        item = removeItem();
        printf("TID: %d, item: %s\n", item.first, item.second.c_str());
        consumeItem(item);
        consumed++;

        sem_post(&s_mutex);
        sem_post(&s_empty);
        sleep(5);
    }
}

void *stats(void *t_ptr) {
    while (1) {
        string currentItem = "fronta je prázdná";

        if (buffer.size() > 0) {
            currentItem = buffer.front().second;
        }

        fprintf(stderr, "Vyrobeno: %d, spotřebováno: %d, item ve frontě: %s\n", produced, consumed, currentItem.c_str());
        sleep(1);
    } 
}

int main(int argc, char* argv[]) {
    sem_init(&s_mutex, 0, 1);
    sem_init(&s_empty, 0, N);
    sem_init(&s_full, 0, 0);

    pthread_t l_pErr;
    pthread_create(&l_pErr, nullptr, stats, nullptr);

    if (argc == 3) {
        int producers = atoi(argv[1]);
        int consumers = atoi(argv[2]);

        pthread_t l_p[producers];
        pthread_t l_c[consumers];

        for (int i = 0; i < producers; i++) {
            pthread_create(&l_p[i], nullptr, producer, nullptr);
        }

        for (int i = 0; i < consumers; i++) {
            pthread_create(&l_c[i], nullptr, consumer, nullptr);   
        }

        for (int i = 0; i < producers; i++) {
            pthread_join(l_p[i], nullptr);
        }

        for (int i = 0; i < consumers; i++) {
            pthread_join(l_c[i], nullptr);   
        }

        pthread_join(l_pErr, nullptr);
    } else {
        pthread_t l_p1, l_p2;

        pthread_create(&l_p1, nullptr, producer, nullptr);
        pthread_create(&l_p2, nullptr, consumer, nullptr); 

        pthread_join(l_p1, nullptr);
        pthread_join(l_p2, nullptr);
        pthread_join(l_pErr, nullptr);
    }    

    return 0;
}
