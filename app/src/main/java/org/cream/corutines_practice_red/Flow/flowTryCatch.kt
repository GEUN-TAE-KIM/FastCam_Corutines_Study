package org.cream.corutines_practice_red.Flow

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

/*
Note
 collect을 하는 수집기 측에서도 try-catch 식을 이용해 할 수 있음

TODO
 수집기 측에서 예외처리하기
 */
fun simple5(): Flow<Int> = flow {
    for (i in 1..3) {
        println("Emitting $i")
        emit(i)
    }
}

fun main26() = runBlocking {
    try {
        simple5().collect { value ->
            println(value)
            check(value <= 1) { "Collected $value" }
        }
    } catch (e: Throwable) {
        println("Caught $e")
    }
}

/*
Note
 flow 내의 예외도 처리가능

TODO
 모든 예외는 처리가능
 */
fun simple6(): Flow<String> =
    flow {
        for (i in 1..3) {
            println("Emitting $i")
            emit(i) // emit next value
        }
    }
        .map { value ->
            check(value <= 1) { "Crashed on $value" }
            "string $value"
        }

fun main27() = runBlocking {
    try {
        simple6().collect { value -> println(value)}
    } catch (e: Throwable) {
        println("Caught $e")
    }
}

/*
Note
 빌더 코드 블록 내에서 예외를 처리하는 것은 예외 투명성을 어기는 것

TODO
 예외 투명성
 */
fun main28() = runBlocking {
    simple6()
        .catch { e -> emit("Caught $e") }
        .collect { value -> println(value)}
}

/*
Note
 catch 연산자는 업스트림(catch 연산자를 쓰기 전의 코드)에만 영향을 미치고 다운스트림에는 영향을 미치지 않음
 이를 catch 투명성이라 함

TODO
 catch 투명성
 */
fun main29() = runBlocking {
    simple()
        .catch { e -> println("Caught $e") } // does not catch downstream exceptions
        .collect { value ->
            check(value <= 1) { "Collected $value" }
            println(value)
        }
}