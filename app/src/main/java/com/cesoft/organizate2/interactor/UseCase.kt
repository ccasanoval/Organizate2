package com.cesoft.organizate2.interactor

/**
 * Created by ccasanova on 24/05/2018
 */

import com.cesoft.organizate2.util.exception.Failure
import com.cesoft.organizate2.util.functional.Either
import kotlinx.coroutines.*

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This abstraction represents an execution unit for different use cases (this means than any use
 * case in the application should implement this contract).
 *
 * By convention each [UseCase] implementation will execute its job in a background thread
 * (kotlin coroutine) and will post the result in the UI thread.
 */
//TODO: https://github.com/Kotlin/kotlinx.coroutines/blob/master/coroutines-guide.md
abstract class UseCase<out Type, in Params> where Type : Any {

    abstract suspend fun run(params: Params): Either<Failure, Type>

    /*fun execute(onResult: (Either<Failure, Type>) -> Unit, params: Params) {
        val job = GlobalScope.async { run(params) }
        GlobalScope.launch(Dispatchers.Main) { onResult.invoke(job.await()) }
    }*/

    operator fun invoke(params: Params, onResult: (Either<Failure, Type>) -> Unit = {}) {
        val job = GlobalScope.async { run(params) }
        GlobalScope.launch(Dispatchers.Main) { onResult(job.await()) }
    }

    object None
}
