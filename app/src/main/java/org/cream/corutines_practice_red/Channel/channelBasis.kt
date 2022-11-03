package org.cream.corutines_practice_red.Channel

import kotlinx.coroutines.channels.*
import kotlinx.coroutines.*

/*
Note
 Channel = 일종의 파이프 같은 역활

TODO
 채널

me
 send랑 receive 말그대로 주고 받기
 */
fun main1() = runBlocking {

    val channel = Channel<Int>()

    launch {
        for (x in 1..10) {
            channel.send(x) // 중단점
        }
    }

    repeat(10) {
        println(channel.receive()) // 중단점
    }
    println("완료")
}

/*
Note
  send랑 receive는 중단점(suspension point)이기 때문에 무한 로딩에 빠져버림

TODO
 같은 코루틴에서 채널을 읽고 쓰면?

me
 launch로 새로운 코틀린 스코프를 만들어서 사용하면 됨
 */
fun main2() = runBlocking<Unit> {
    val channel = Channel<Int>()
    launch {
        for (x in 1..10) {
            channel.send(x)
        }

        repeat(10) {
            println(channel.receive())
        }
        println("완료")
    }
}

/*
Note
 close = 채널 닫기

TODO
 채널 close

me
 close를 안하면 명시적으로 repect를 해서 했던 것처럼 가져 오는 것
 */
fun main3() = runBlocking {

    val channel = Channel<Int>()

    launch {
        for (x in 1..10) {
            channel.send(x)
        }
        channel.close()
    }

    for (x in channel) {
        println(x)
    }
    println("완료")
}

/*
Note
  produce = 스코프이면서 채널의 역활을 할수있는 것

TODO
 채널 프로듀서

me
 close를 안하면 명시적으로 repect를 해서 했던 것처럼 가져 오는 것
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun main4() = runBlocking {

    val oneToTen = produce {
        for (x in 1..10) {
            channel.send(x)
        }
    }

    oneToTen.consumeEach {
        println(it)
    }
    println("끝")
}

