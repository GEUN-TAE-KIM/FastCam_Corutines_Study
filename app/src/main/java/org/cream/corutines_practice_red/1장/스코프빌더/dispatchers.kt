package org.cream.corutines_practice_red

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

/*
Note
 Default = 코어 수에 비례하는 스레드 풀
    ->CPU를 많이 사용하는 작업을 기본 스레드 외부에서 실행하도록 최적화되어 있음.
        예를 들어 목록을 정렬하고 JSON 작업에 최적화
    //
 IO = 코어 수 보다 훨씬 많은 스레드를 가지는 스레드 풀이며 CPU를 덜 소모
    ->스레드 외부에서 디스크 또는 네트워크 I/O를 실행하도록 최적화되어 있음
        예를 들어 회의실 구성요소를 사용하고 파일에서 읽거나 파일에 쓰며 네트워크 작업을 실행
    //
 Unconfined = 어디에도 속하지 않음, 어디서 수행될지 모름
 newSingleThreadContext = 새로운 스레드를 만듬

TODO
 코루틴 Dispatchers

me
  Dispatchers는 스레드 풀을 제어함 Dispatchers에 코루틴을 보내면 스레드에 분산시킴
 */
fun main24() = runBlocking<Unit> {

    launch {
        println("부모의 콘텍스트 / ${Thread.currentThread().name}")
    }

    launch(Dispatchers.Default) {
        println("Default / ${Thread.currentThread().name}")
    }

    launch(Dispatchers.IO) {
        println("IO / ${Thread.currentThread().name}")
    }

    launch(Dispatchers.Unconfined) {
        println("Unconfined / ${Thread.currentThread().name}")
    }

    launch(newSingleThreadContext("Fast Campus")) {
        println("newSingleThreadContext / ${Thread.currentThread().name}")
    }
}

/*
Note
 디스패처를 사용은 가능

TODO
 async에서 코루틴 디스패처 사용
 */
fun main25() = runBlocking<Unit> {

    async {
        println("부모의 콘텍스트 / ${Thread.currentThread().name}")
    }

    async(Dispatchers.Default) {
        println("Default / ${Thread.currentThread().name}")
    }

    async(Dispatchers.IO) {
        println("IO / ${Thread.currentThread().name}")
    }

    async(Dispatchers.Unconfined) {
        println("Unconfined / ${Thread.currentThread().name}")
    }

    async(newSingleThreadContext("Fast Campus")) {
        println("newSingleThreadContext / ${Thread.currentThread().name}")
    }
}

/*
Note
 어디서 수행될지 모름 랜덤

TODO
 Confined 디스패처 테스트
 */
fun main26() = runBlocking<Unit> {

    async(Dispatchers.Unconfined) {
        println("Unconfined / ${Thread.currentThread().name}")
        delay(1000L)
        println("Unconfined / ${Thread.currentThread().name}")
    }

}

/*
Note
 Job을 만들면 부모 자식의 관계가 끊어져서 캔슬이 안된 것

TODO
 부모가 있는 Job과 없는 Job
 */
fun main27() = runBlocking<Unit> {

    val job = launch {
        launch(Job()) {
            println(coroutineContext[Job])
            println("launch1: ${Thread.currentThread().name}")
            delay(1000L)
            println("3!")
        }

        launch {
            println(coroutineContext[Job])
            println("launch2: ${Thread.currentThread().name}")
            delay(1000L)
            println("1!")
        }
    }

    delay(500L)
    job.cancelAndJoin()
    delay(1000L)
}

/*
Note
 자식이 끝날 때 까지 기다림

TODO
 부모의 마음
 */
fun main28() = runBlocking<Unit> {

    val elapsed = measureTimeMillis {
        val job = launch { // 부모
            launch { // 자식 1
                println("launch1: ${Thread.currentThread().name}")
                delay(5000L)
            }

            launch { // 자식 2
                println("launch2: ${Thread.currentThread().name}")
                delay(10L)
            }
        }
        job.join()
    }
    println(elapsed)
}

/*
Note
 자식이 끝날 때 까지 기다림

TODO
 코루틴 엘리먼트 결합

me
  Dispatchers는 스레드 풀을 제어함 Dispatchers에 코루틴을 보내면 스레드에 분산시킴
 */

@OptIn(ExperimentalStdlibApi::class)
fun main29() = runBlocking<Unit> {
    launch { // a
        launch(Dispatchers.IO + CoroutineName("launch1")) { // 3개의 컨텍스트 a + Dispatchers + CoroutineName
            println("launch1: ${Thread.currentThread().name}")
            println(coroutineContext[CoroutineDispatcher])
            println(coroutineContext[CoroutineName])
            delay(5000L)
        }

        launch(Dispatchers.Default + CoroutineName("launch1")) {
            println("launch2: ${Thread.currentThread().name}")
            println(coroutineContext[CoroutineDispatcher])
            println(coroutineContext[CoroutineName])
            delay(10L)
        }
    }
}


