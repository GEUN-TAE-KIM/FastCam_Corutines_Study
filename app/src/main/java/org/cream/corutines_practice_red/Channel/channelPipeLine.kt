package org.cream.corutines_practice_red.Channel

import kotlinx.coroutines.channels.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

/*
Note
 파이프 라인은 일반적인 패턴, 하나의 스트림을 프로듀서가 만들고, 다른 코루틴에서 그 스트림을 읽어 새로운 스트림을 만드는 패턴.

TODO
 파이프라인
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun CoroutineScope.produceNumbers() = produce { // 1,2,3,4,5,6,7... // 리시브
    var x = 1
    while (true) {
        send(x++)
    }
}

@OptIn(ExperimentalCoroutinesApi::class) // 샌드 채널 -> 코루틴 스코프이며 샌드 채널의 역활도 함
fun CoroutineScope.produceStringNumbers(numbers: ReceiveChannel<Int>): ReceiveChannel<String> = produce {
    for (i in numbers) {
        send("${i}!") // "1!", "2!", ...
    }
    // 홀수
    /*if (i % 2 == 1) {
        send("${i}!")
    }*/
}

fun main5() = runBlocking {
    val numbers = produceNumbers() // 1,2,3,4,5,6,7... //numbers: 채널, 리시브채널. receive 메서드를 호출
    val stringNumbers = produceStringNumbers(numbers)

    repeat(5){
        println(stringNumbers.receive())
    }
    println("끝")
    coroutineContext.cancelChildren()
}

/*
Note
 파이프 라인을 연속으로 타서 결과를 얻어 내는 것

TODO
 소수 필터

me
 이런게 있다.
 */
fun CoroutineScope.numbersFrom(start: Int) = produce<Int> {
    var x = start
    while (true) {
        send(x++)
    }
}

fun CoroutineScope.filter(numbers: ReceiveChannel<Int>, prime: Int): ReceiveChannel<Int> = produce {
    for (i in numbers) {
        if (i % prime != 0) {
            send(i)
        }
    }
}

fun main6() = runBlocking<Unit> {
    var numbers = numbersFrom(2) // 채널 대체 / 채널 대체

    repeat(10) { // 어휴 복잡해
        val prime = numbers.receive()
        println(prime)
        numbers = filter(numbers, prime)
    }
    println("완료")
    coroutineContext.cancelChildren()
}