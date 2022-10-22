package org.cream.corutines_practice_red

import kotlinx.coroutines.*


/*
Note
 cancel = 취소

me
 delay가 800이 넘어가고 job1의 delay는 1000이 넘어가서 그전에 job1.cancel이 실행되기떄문에
 delay(1000L) 후에 있는 3이 출력이 안됨
 */

suspend fun cancelOneTwo() = coroutineScope {

    val job1 = launch {
        println("launch1: ${Thread.currentThread().name}")
        delay(1000L)
        println("3")
    }

    val job2 = launch {
        println("launch2: ${Thread.currentThread().name}")
        println("1")
    }

    val job3 = launch {
        println("launch3: ${Thread.currentThread().name}")
        delay(500L)
        println("2")
    }
    delay(800L)
    job1.cancel()
    job2.cancel()
    job3.cancel()

}

fun main14() = runBlocking {
    cancelOneTwo()
    println("runBlocking: ${Thread.currentThread().name}")
    println("5")
}

/*
Note
 launch(Dispatchers.Default) = 그 다음 코드 블록을 다른 스레드에서 수행을 시킬 것

TODO
 calcel 이 인식을 못하고 출력이됨

me
 join을 넣어서 println("Count")를 순서대로 출력하게 했지만 job1의 코드가 cancel을 인식을 하는게 아님
 -> 내 생각에는 Dispatchers.Default 때문인듯 블록을 다른 스레드에 수행을 하기 때문에 코드 자체를
    cancel이 안되는것 그래서 지워보면 제대로 순서대로는 되지만 캔슬은 여전히 안됨 22/10/23
 */

suspend fun doCount() = coroutineScope {

    val job1 = launch(Dispatchers.Default) {
        var i = 1
        var nextTime = System.currentTimeMillis() + 100L

        while (i <= 10) {
            val currentTime = System.currentTimeMillis()
            if (currentTime >= nextTime) {
                println(i)
                nextTime = currentTime + 100L
                i++
            }
        }
    }
    delay(200L)
    job1.cancel()
    job1.join()
    println("Count")

}

fun main15() = runBlocking {
    doCount()

}

/*
Note
 cancelAndJoin() = cancel과 join을 한번에 하는 것
 isActive = 코루틴이 활성화 되어있는지 확인

TODO
 실제 cancel 가능한 코루틴틴

 */

suspend fun doCounts() = coroutineScope {

    val job1 = launch(Dispatchers.Default) {
        var i = 1
        var nextTime = System.currentTimeMillis() + 100L

        while (i <= 10 && isActive) {
            val currentTime = System.currentTimeMillis()
            if (currentTime >= nextTime) {
                println(i)
                nextTime = currentTime + 100L
                i++
            }
        }
    }

    delay(200L)
    job1.cancelAndJoin()
    println("Count")
}

fun main16() = runBlocking {
    doCounts()
}

/*

TODO
 finally를  사용
 
me
 cancel을 해서 try코드 말고 finally의 코드를 사용

 */

suspend fun tryCatch() = coroutineScope {
    val job1 = launch {
        try {
            println("launch1: ${Thread.currentThread().name}")
            delay(1000L)
            println("3!")
        } finally {
            println("job1 is finishing!")
        }
    }

    val job2 = launch {
        try {
            println("launch2: ${Thread.currentThread().name}")
            delay(1000L)
            println("1!")
        } finally {
            println("job2 is finishing!")
        }
    }

    delay(800L)
    job1.cancel()
    job2.cancel()
    println("4!")
}

fun main17() = runBlocking {
    tryCatch()
    println("runBlocking: ${Thread.currentThread().name}")
    println("5!")
}

/*
Note
 withContext(NonCancellable) = 취소를 못하게 하며 finally에도 사용 가능

TODO
 취소를 불가능하게 하는 블록

 */

suspend fun nonCancellable() = coroutineScope {

    val job1 = launch {
        withContext(NonCancellable) {
            println("launch1: ${Thread.currentThread().name}")
            delay(1000L)
            println("3")
        }
        delay(1000L)
        print("job1: end")
    }

    val job2 = launch {
        withContext(NonCancellable) {
            println("launch1: ${Thread.currentThread().name}")
            delay(1000L)
            println("1")
        }
        delay(1000L)
        print("job2: end")
    }
    delay(800L)
    job1.cancel()
    job2.cancel()
    println("4!")
}

fun main18() = runBlocking {
    doOneTwoThree()
    println("runBlocking: ${Thread.currentThread().name}")
    println("5!")
}

/*
Note
 withTimeout = 일정 시간이 지나고 끝내는 것

TODO
 타임 아웃

me
 취소가 되면 Exception이 발생하므로 예외를 둬야함
 trycatch는 간결하지 않기 때문에 withTimeoutOrNull 를 사용하여 간결하게 가능
 */

suspend fun timeOut() = coroutineScope {
    val job1 = launch(Dispatchers.Default) {
        var i = 1
        var nextTime = System.currentTimeMillis() + 100L

        while (i <= 10 && isActive) {
            val currentTime = System.currentTimeMillis()
            if (currentTime >= nextTime) {
                println(i)
                nextTime = currentTime + 100L
                i++
            }
        }
    }
}

fun main19() = runBlocking {
    withTimeout(500L) {
        timeOut()
    }

    /*val result = withTimeoutOrNull(500L) {
        doCount()
        true
    } ?: false
    println(result)*/
}
