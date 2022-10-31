package org.cream.corutines_practice_red.Flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/*
TODO
 코루틴 컨텍스트
 */
fun log(msg: String) {
    println("[${Thread.currentThread().name}] $msg")
}

fun simple(): Flow<Int> = flow {
    log("flow를 시작합니다.")
    for (i in 1..10) {
        emit(i)
    }
}

fun main15() = runBlocking<Unit> {
    launch(Dispatchers.IO) {
        simple()
            .collect { value -> log("${value}를 받음.") }
    }
}

/*
Note
 플로우 내에서는 컨텍스트를 바꿀 수 없음
TODO
 다른 컨텍스트로 옮겨갈 수 없는 플로우
 */
fun simple2(): Flow<Int> = flow { // 불가
    withContext(Dispatchers.Default) {
        for (i in 1..10) {
            emit(i)
        }
    }
}

fun main16() = runBlocking<Unit> {
    launch(Dispatchers.IO) {
        simple2()
            .collect { value -> log("${value}를 받음") }
    }
}

/*
Note
 flowOn = 컨텍스트를 올바르게 바꿀 수 있습니다.

TODO
 flowOn 연산자

me
 플로위 위치에 따라 스트림 위치 결정이 되고 플로우 위에 있는 것들은 스트림이 디스팩쳐로 수행
 flowOn이 두개가 Default든 IO든 이 중에서 위에 있는 걸로 결정되서 수행
 */
fun simple3(): Flow<Int> = flow {

    for (i in 1..10) {
        delay(100L)
        log("값 ${i}를 emit함")
        emit(i)
    } // 업 스트림

}.flowOn(Dispatchers.Default) // 위치
    .map {
        it * 2
    }

fun main17() = runBlocking {
    simple3().collect { value -> // 다운스트림
        log("${value}를 받음")
    }
}