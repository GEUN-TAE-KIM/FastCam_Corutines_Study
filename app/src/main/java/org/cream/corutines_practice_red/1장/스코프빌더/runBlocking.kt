package org.cream.corutines_practice_red

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

//TODO 코루틴의 시작은 코루틴 스코프
// 코루틴은 코루틴 스코프 내에서만 실행 가능 그예외는 컴파일러 오류 뜸

/*
Note
 코루틴을 만드는 함수를 = 코루틴 빌더
 runBlocking = 코루틴을 만들고 코드 블록의 수행이 끝날 때까지  runBlocking 이 막음
 */
fun main2() = runBlocking {
    println(Thread.currentThread().name)
    println("Hello")
    println("-------------------------------")
}

//Note
// runBlocking = 안에 this를 사용하면 코루틴이 수신객체(Receiver)인것을 알수있음
//TODO
// Receiver = T.() -> R 반환
// get set이 없어도 출력이 되는 것
fun main3() = runBlocking {
    println(this)
    println(Thread.currentThread().name)
    println("Hello")
    println("-------------------------------")
}

//Note
// coroutineContext = 코루틴의 출력을 위한 정보를 담고 있음
//TODO
// this.coroutineContext 도 가능 (runBlocking 이 수신객체를 제공을 하기 때문)
fun main4() = runBlocking {
    println(coroutineContext)
    println(Thread.currentThread().name)
    println("Hello")
}

/*
Note
 launch = 다른 코루틴 코드를 같이 수행 시키는 빌더

TODO
 다이어리앱에서 ViewModel 에 사용 한 것
 runBlocking 은 출력하고  launch 가 출력할때까지 기다림

me
 launch 의 내용이 runBlocking 보다 늦게 되는 건 둘다 <Main 스레드>를 사용하고 있기 때문에
 runBlocking 의 코드가 끝날 때 까지 launch 가 기다리고 있는 것
 */
fun main5() = runBlocking {
    launch {
        println("launch: ${Thread.currentThread().name}")
        println("World!")
    }
    println("runBlocking: ${Thread.currentThread().name}")
    println("Hello")
}

/*
Note
 delay = 말 그래도 늦게 수행 시키는 것

TODO
 1000L = 1초
 -> 출력이 딜레이가 있음

me
 delay는 반드시 코루틴 안이거나 suspend 함수에서만 사용 가능
 */
fun main6() = runBlocking {
    launch {
        println("launch: ${Thread.currentThread().name}")
        //delay(500L)
        println("World!")
    }
    println("runBlocking: ${Thread.currentThread().name}")
    delay(500L)
    println("Hello")
}

/*
Note
 sleep = 잠시 쉬어가는 것

TODO
 delay는 다른 코드를 수행시키고 실행하지만 sleep은 잠시 내가 쉬고 실행을 한다는 것
 -> 출력이 딜레이가 있음
 */
fun main7() = runBlocking {
    launch {
        println("launch: ${Thread.currentThread().name}")
        //delay(500L)
        println("World!")
    }
    println("runBlocking: ${Thread.currentThread().name}")
    Thread.sleep(500L)
    println("Hello")
}

/*
TODO
 delay는 다른 코드를 수행시키고 실행하지만 sleep은 잠시 내가 쉬고 실행을 한다는 것
 -> 출력이 딜레이가 있음
 */
fun main8() = runBlocking {
    launch {
        println("launch1: ${Thread.currentThread().name}")
        delay(1000L) //suspension point (중단 점)
        println("3!")
    }
    launch {
        println("launch2: ${Thread.currentThread().name}")
        println("1!")
    }
    println("runBlocking: ${Thread.currentThread().name}")
    delay(500L) //suspension point
    println("2!")
}

/*
TODO
 상위 코루틴은 하위 코루틴이 끝날때까지 책임(기다림)
 */
fun main9() {
    // 계층화된 구조적
    runBlocking {

        launch {
            println("launch1: ${Thread.currentThread().name}")
            delay(1000L)
            println("3!")
        }
        launch {
            println("launch2: ${Thread.currentThread().name}")
            println("1!")
        }

        println("runBlocking: ${Thread.currentThread().name}")
        delay(500L) //suspension point
        println("2!")
    }
    println("4")

}

/*
Note
 suspend = 중단 가능 함수

TODO
 다이어리앱에서 Repository 에 사용 한 것

me
 runBlocking 뒤에 타입 지정 가능
 */
suspend fun three() {
    println("launch3 : ${Thread.currentThread().name}")
    delay(1000L)
    println("3")
}

fun one() {
    println("launch1 : ${Thread.currentThread().name}")
    println("1")
}

suspend fun two() {
    println("runBlocking: ${Thread.currentThread().name}")
    delay(500L)
    println("2")
}

// 타입 지정 가능
fun main() = runBlocking<Unit> {

    launch {
        three()
    }

    launch {
        one()
    }
    two()

}

