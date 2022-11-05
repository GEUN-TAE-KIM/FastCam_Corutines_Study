package org.cream.corutines_practice_red.Channel

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.selects.select

/*
Note
  팬 아웃  = 여러 코루틴이 동시에 채널을 구독할 수 있음

TODO
 팬 아웃

me
 파라미터가 없는거와 있는걸로 중복차이가 나는 구나
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun CoroutineScope.produceNumbers2() = produce {
    var x = 1
    while (true) {
        send(x++)
        delay(100L)
    }
}

fun CoroutineScope.processNumber(id: Int, channel: ReceiveChannel<Int>) = launch {
    channel.consumeEach {
        println("${id}가 ${it}을 받았습니다.")
    }
}

fun main7() = runBlocking {
    val producer = produceNumbers2()
    repeat(5) {
        processNumber(it, producer)
    }
    delay(1000L)
    producer.cancel()
}

/*
Note
 팬 인  = 팬 인은 반대로 생산자가 많은 것입니다.

TODO
 팬 인
 */
suspend fun produceNumbers(channel: SendChannel<Int>, from: Int, interval: Long) {
    var x = from
    while (true) {
        channel.send(x)
        x += 2
        delay(interval)
    }
}

fun CoroutineScope.processNumber(channel: ReceiveChannel<Int>) = launch {
    channel.consumeEach {
        println("${it}을 받음")
    }
}

fun main8() = runBlocking {

    val channel = Channel<Int>()

    launch {
        produceNumbers(channel, 1, 100L)
    }
    launch {
        produceNumbers(channel, 2, 150L)
    } // 생산자 2 , 소비자 1

    processNumber(channel)
    delay(1000L)
    coroutineContext.cancelChildren()
}

/*
Note
 두 개의 코루틴에서 채널을 서로 사용할 때 공정하게 기회를 준다는 것을 알 수 있음

TODO
 공정한 채널
 */
suspend fun someone(channel: Channel<String>, name: String) {
    for (comment in channel) {
        println("${name}: ${comment}")
        channel.send(comment.drop(1) + comment.first())
        delay(100L)
    }
}

fun main9() = runBlocking {
    val channel = Channel<String>()
    launch {
        someone(channel, "하시모토")
    }
    launch {
        someone(channel, "칸나")
    }
    channel.send("내 여친이면 좋겠다.")
    delay(1000L)
    coroutineContext.cancelChildren()
}

/*
Note
 먼저 끝나는 요청을 처리하는 것

TODO
 select

me
 채널에 대해 onReceive를 사용하는 것 이외에도 아래의 상황에서 사용할 수 있음
 Job - onJoin
 Deferred - onAwait -> async: Deffered. deffered.await()
 SendChannel - onSend
 ReceiveChannel - onReceive, onReceiveCatching
 delay - onTimeout
 */

fun CoroutineScope.sayFast() = produce<String> {
    while (true) {
        delay(100L)
        send("패스트")
    }
}

fun CoroutineScope.sayCampus() = produce<String> {
    while (true) {
        delay(150L)
        send("캠퍼스")
    }
}

fun main10() = runBlocking<Unit> {
    val fasts = sayFast()
    val campuses = sayCampus()
    repeat (5) {
        select<Unit> {
            fasts.onReceive {
                println("fast: $it")
            }
            campuses.onReceive {
                println("campus: $it")
            }
        }
    }
    coroutineContext.cancelChildren()
}