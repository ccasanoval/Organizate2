package com.cesoft.organizate2.util.exception

/**
 * Created by ccasanova on 23/05/2018
 */
sealed class Failure {
    class Database: Failure()
    class NetworkConnection: Failure()
    class ServerError: Failure()

    /** * Extend this class for feature specific failures.*/
    abstract class FeatureFailure: Failure()
}