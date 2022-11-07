package org.cream.corutines_practice_red.Flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.random.Random

/*
Note
 Flow = 코틀린에서 쓸 수 있는 비동기 스트림
 emit() = 방출을 한다는 의미

TODO
 플로우 기초

me
 플로우는 콜드 스트림이기 때문에 요청 측에서 collect를 호출해야 값을 발생하기 시작
    ->콜드 스트림 - 요청이 있는 경우에 보통 1:1로 값을 전달하기 시작.
    ->핫 스트림 - 0개 이상의 상대를 향해 지속적으로 값을 전달.
 */
fun flowSomething(): Flow<Int> = flow {
    repeat(10) {
        emit(Random.nextInt(0, 500))
        delay(100L)
    }
}

fun main1() = runBlocking {
    flowSomething().collect { value ->
        println(value)
    }
}

/*
Note
 withTimeoutOrNull을 이용해 간단히 취소

TODO
 플로우 취소
 */
fun main2() = runBlocking {
    val result = withTimeoutOrNull(500L) {
        flowSomething().collect { value ->
            println(value)
        }
        true
    } ?: false
    if (!result) {
        println("취소됨")
    }
}

/*
Note
 flowOf = 여러 값을 인자로 전달해 플로우를 만듬

TODO
 플로우 빌더 flowOf
 */
fun main3() = runBlocking {
    flowOf(1, 2, 3, 4, 5).collect { value ->
        println(value)
    }
    // 본질은 같음
    flow {
        emit(1)
        emit(2)
    }.collect { println(it) }
}

/*
Note
 asFlow = 컬렉션이나 시퀀스를 전달해 플로우를 만듬

TODO
 플로우 빌더 asFlow
 */
fun main4() = runBlocking {
    listOf(1, 2, 3, 4, 5).asFlow().collect { value ->
        println(value)
    }
    (6..10).asFlow().collect {
        println(it)
    }
}

