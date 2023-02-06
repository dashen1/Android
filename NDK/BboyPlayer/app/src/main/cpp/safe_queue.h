//
// Created by se0891 on 2022/4/7.
//

#ifndef BBOYPLAYER_SAFE_QUEUE_H
#define BBOYPLAYER_SAFE_QUEUE_H

#include <queue>
#include <pthread.h>

using namespace std;
template <typename T>
class SafeQueue{
private:
    typedef void (*ReleaseCallback)(T *);//函数指针定义 用于回调 释放T里面的内容
    typedef void (*SyncCallback)(queue<T> &);

private:
    queue<T> queue;
    pthread_mutex_t mutex;//互斥锁 不允许有野指针 所以必须初始化
    pthread_cond_t cond;//等待和唤醒  条件变量
    int work;
    ReleaseCallback releaseCallback;
    SyncCallback syncCallback;

public:
    SafeQueue(){
        pthread_mutex_init(&mutex,0);
        pthread_cond_init(&cond,0);
    }

    ~SafeQueue(){
        pthread_mutex_destroy(&mutex);
        pthread_cond_destroy(&cond);
    }


    /*
     * 入队 void * 可以传递任何类型
     */
    void insertToQueue(T value){
        pthread_mutex_lock(&mutex);//多线程访问 先锁住

        if(work){
            queue.push(value);
            pthread_cond_signal(&cond);//当插入数据包 进入队列后 要发出通知唤醒其它队列
        } else{
            //非工作状态，释放value 因为value类型不明确 无法释放 只能通过外界释放
            if(releaseCallback){
                releaseCallback(&value);
            }
        }
        pthread_mutex_unlock(&mutex);
    }

    /*
     * 出队  线程里面的函数指针相当于 Thread.run()
     */

    int getQueueAndDel(T &value){
        int ret=0;
        pthread_mutex_lock(&mutex);

        while(work && queue.empty()){
            //如果正在工作 且队列数据为空 就等待阻塞 睡眠
            pthread_cond_wait(&cond,&mutex);
        }
        if(!queue.empty()){
            value=queue.front();
            queue.pop();
            ret = 1;
        }

        pthread_mutex_unlock(&mutex);
        return ret;
    }

    void setWork(int work){
        pthread_mutex_lock(&mutex);
        this->work=work;
        pthread_cond_signal(&cond);
        pthread_mutex_unlock(&mutex);
    }

    int empty(){
        return queue.empty();
    }

    int size(){
        return queue.size();
    }

    void clear(){
        pthread_mutex_lock(&mutex);

        unsigned int size = queue.size();
        for(int i=0;i<size;i++){
            T value = queue.front();
            if(releaseCallback){
                releaseCallback(&value);
            }
            queue.pop();
        }
        pthread_mutex_unlock(&mutex);
    }

    /*
     * 函数指针回调
     */
    void setReleaseCallback(ReleaseCallback releaseCallback1){
        this->releaseCallback=releaseCallback1;
    }

    void setSyncCallback(SyncCallback syncCallback1){
        this->syncCallback=syncCallback1;
    }

    /*
     * 同步操作 丢包
     */
    void sync(){
        pthread_mutex_lock(&mutex);
        syncCallback(queue);
        pthread_mutex_unlock(&mutex);
    }
};

#endif //BBOYPLAYER_SAFE_QUEUE_H
