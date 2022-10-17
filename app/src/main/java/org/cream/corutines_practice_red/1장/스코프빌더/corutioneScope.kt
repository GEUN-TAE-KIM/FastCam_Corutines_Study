package org.cream.corutines_practice_red

import kotlinx.coroutines.*

//TODO launch를 실행할라면 코루틴 내에서만 실행 가능함
/*suspend fun doOneTwoThree() {
    launch {
        println("launch1: ${Thread.currentThread().name}")
        delay(1000L)
        println("3")
    }

}*/

/*

Note
 coroutineScope = 함수에 바디를 만들어줘서 코루틴 스코프로 만듬

TODO
 함수내에서 선언해서 사용 가능

me
 코루틴 스코프는 스레드를 멈추게 하지 않고 호출한 쪽이 중단점(suspend)이 됨
 */
suspend fun doOneTwoThree() = coroutineScope {

    launch {
        println("launch1: ${Thread.currentThread().name}")
        delay(1000L)
        println("3")
    }

    launch {
        println("launch1: ${Thread.currentThread().name}")
        println("1")
    }

}

fun main11()= runBlocking<Unit> {
    doOneTwoThree()
    delay(500L)
    doOneTwoThree()
}

