package org.cream.corutines_practice_red

import kotlinx.coroutines.*

/*
Note
 join = suspension이 되어 첫번째 런치블록이 끝날때까지 기다림
 */
suspend fun oneTwoThree() = coroutineScope {

    val job = launch {
        println("launch1: ${Thread.currentThread().name}")
        delay(1000L)
        println("4")
    }
    job.join()

    launch {
        println("launch2: ${Thread.currentThread().name}")
        println("1")
    }

    launch {
        println("launch3: ${Thread.currentThread().name}")
        println("2")
    }
    println("3")

}

fun main12() = runBlocking {
    oneTwoThree()
    println("runBlocking: ${Thread.currentThread().name}")
    println("5")
}

/*
Note
 repeat() = (숫자)를 반복한다.
 */
suspend fun oneTwoFive() = coroutineScope {

    val job = launch {
        println("launch1: ${Thread.currentThread().name}")
        delay(1000L)
        println("4")
    }
    job.join()

    repeat(100) {
        launch {
            println("launch3: ${Thread.currentThread().name}")
            println("2")
        }
    }

}

fun main13() = runBlocking {
    oneTwoFive()
    println("runBlocking: ${Thread.currentThread().name}")
    println("5")
}



