package org.cream.corutines_practice_red

import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.measureTimeMillis

/*
Note
 withContext = 수행이 완료될 때 까지 기다리는 코루틴 빌더

TODO
 공유 객체 문제

me
 천 개의 코루틴이 어떠한 동기화도 없이 다중 스레드에서 공유 중인 counter 변수를 증가시키고 있기 때문에
 최대값이 나올 리가 없음
*/
suspend fun massiveRun(action: suspend () -> Unit) {
    val n = 100 // 시작 할 코루틴의 갯수
    val k = 1000 // 코루틴 내에서 반복할 횟수
    val elapsed = measureTimeMillis {
        coroutineScope {
            repeat(n) {
                launch {
                    repeat(k) {
                        action()
                    }
                }
            }
        }
    }
    println("$elapsed ms동안 ${n * k}개의 액션을 수행")
}

//@Volatile // 가시성 문제만을 해결할 뿐 동시에 읽고 수정해서 생기는 문제를 해결하지 못함
var counter = 0

fun main36() = runBlocking {
    withContext(Dispatchers.Default) {
        massiveRun {
            counter++
        }
    }
    println("Counter = $counter")
}

/*
Note
 AtomicInteger = 원자 변수
 incrementAndGet() = 원자 변수의 멤버 메서드를 사용해 값 증가
 //
 원자 변수란 특정 변수의 증가나 감소, 더하기나 빼기가 기계어 명령으로 수행되는 것을 말하며
 해당 연산이 수행되는 도중에는 누구도 방해하지 못하기 때문에 값의 무결성을 보장할 수 있게 된다.

TODO
 스레드 안전한 자료구조 사용하기

me
 이용 가능한 스레드에 안전한 구현이 없는 복잡한 상태 혹은 연산으로의 확장에는 정답이 아님
 (이런게 있다)
 */
val counter2 = AtomicInteger()

fun main37() = runBlocking {
    withContext(Dispatchers.Default) {
        massiveRun {
            counter2.incrementAndGet()
        }
    }
    println("Counter = $counter")
}

/*
Note
 스레드를 생성해서 단일로 제한하는 방식

TODO
 스레드 한정
 */

@OptIn(DelicateCoroutinesApi::class)
val counterContext = newSingleThreadContext("CounterContext")

//전체 스레드
fun main38() = runBlocking {
    withContext(counterContext) {
        massiveRun {
            counter++
        }
    }
    println("Counter = $counter")
}

// 하나의 스레드
fun main39() = runBlocking {
    withContext(Dispatchers.Default) {
        massiveRun {
            withContext(counterContext)
            {
                counter++
            }
        }
    }
    println("Counter = $counter")
}




