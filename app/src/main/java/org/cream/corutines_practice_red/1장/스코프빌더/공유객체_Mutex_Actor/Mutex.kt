package org.cream.corutines_practice_red.`1장`.스코프빌더.공유객체_Mutex_Actor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.cream.corutines_practice_red.counter
import org.cream.corutines_practice_red.massiveRun

/*
Note
 Mutex = 상호배제의 줄임말
 ->공유 상태를 수정할 때 임계 영역(critical section)를 이용하게 하며,
 임계 영역을 동시에 접근하는 것을 허용하지 않습니다.

TODO
 뮤텍스
 */
val mutex = Mutex()

fun main40() = runBlocking {
    withContext(Dispatchers.Default) {
        massiveRun {
            mutex.withLock {
                counter++
            }
        }
    }
    println("Counter = $counter")
}