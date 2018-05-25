package com.cesoft.organizate2.repo.db

/**
 * Created by ccasanova on 25/05/2018
 */
abstract class TaskTableContract {
    companion object {
        const val TABLE_NAME = "TaskTable"
        const val COLUMN_ID = "entryid"
        val COLUMN_NAME_TITLE = "title"
        val COLUMN_NAME_DESCRIPTION = "description"
        val COLUMN_DATE_TIME = "datetime"
    }
}