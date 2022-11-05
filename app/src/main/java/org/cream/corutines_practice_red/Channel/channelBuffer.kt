package org.cream.corutines_practice_red.Channel

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

/*
Note
 Channel 생성자는 인자로 버퍼의 사이즈를 지정 받는다.
 지정하지 않으면 버퍼를 생성하지 않는다.

TODO
 버퍼
 */
fun main11() = runBlocking {

    val channel = Channel<Int>(10)

    launch {
        for (x in 1..20) {
            println("${x} 전송중")
            channel.send(x)
        }
        channel.close()
    }

    for (x in channel) {
        println("${x} 수신")
        delay(100L)
    }
    println("완료")
}

/*
Note
 랑데뷰 = 버퍼가 없는건 이 예제 빼고는 다 랑데뷰이다.
 랑데뷰는 버퍼 사이즈를 0으로 지정하는 것입니다. 생성자에 사이즈를 전달하지 않으면 랑데뷰가 디폴트

TODO
 랑데뷰

me
 이외에도 사이즈 대신 사용할 수 있는 다른 설정 값이 있음
 UNLIMITED - 무제한으로 설정
 CONFLATED - 오래된 값이 지워짐. -> 첫번쨰 인자를 지움
 BUFFERED - 64개의 버퍼. 오버플로우엔 suspend
 */
fun main12() = runBlocking {
    val channel = Channel<Int>(Channel.RENDEZVOUS)
    launch {
        for (x in 1..20) {
            println("${x} 전송중")
            channel.send(x)
        }
        channel.close()
    }

    for (x in channel) {
        println("${x} 수신")
        delay(100L)
    }
    println("완료")
}

/*
Note
 버퍼의 오버플로우 정책에 따라 다른 결과가 나올 수 있음

TODO
 버퍼 오버플로우

me
 SUSPEND - 잠이 들었다 깨어납니다.
 DROP_OLDEST - 예전 데이터를 지웁니다. -> 2번째 인자를 지움
 DROP_LATEST - 새 데이터를 지웁니다.
 */
fun main13() = runBlocking<Unit> {
    val channel = Channel<Int>(2, BufferOverflow.DROP_OLDEST)
    launch {
        for (x in 1..50) {
            channel.send(x)
        }
        channel.close()
    }

    delay(500L)

    for (x in channel) {
        println("${x} 수신")
        delay(100L)
    }
    println("완료")
}