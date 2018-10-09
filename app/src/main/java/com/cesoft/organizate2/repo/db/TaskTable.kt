package com.cesoft.organizate2.repo.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import android.support.annotation.NonNull
import com.cesoft.organizate2.entity.TaskEntity
import java.util.*

/**
 * Created by ccasanova on 23/05/2018
 */
//@Entity(tableName = "cmrtable", indices = { @Index(value = {"username", "cmr_id"}) })
@Entity(tableName = TablesContracts.TABLE_NAME)
@TypeConverters(Converters::class)
data class TaskTable(
        //@PrimaryKey @ColumnInfo(name = "entryid") var id: String = UUID.randomUUID().toString()
        @NonNull
        @PrimaryKey(autoGenerate = true)
        override val id: Int,
        override val idSuper: Int,
        override val name: String,
        override val level: Int,
        val description: String,
        val priority: Int,
        val limit: Date,
        val created: Date,
        val modified: Date)
    : TaskTableBase(id, idSuper, name, level) {

    //override val childs: ArrayList<TaskTable> = ArrayList()
    @Ignore
    constructor(id: Int,
                idSuper: Int,
                name: String,
                level: Int,
                description: String,
                priority: Int,
                limit: Date,
                created: Date,
                modified: Date,
                childs: ArrayList<TaskTable>)
            : this(id, idSuper, name, level, description, priority, limit, created, modified) {
        this.childs.clear()
        this.childs.addAll(childs)
    }

    @Ignore
    constructor(task: TaskEntity)
            : this(task.id, task.idSuper, task.name, task.level, task.description, task.priority,
            task.limit, task.created, task.modified) {
        this.childs.clear()
        this.childs.addAll(childs)
    }

    fun toTaskEntity(): TaskEntity {
        return TaskEntity(id, idSuper, name, level, description, priority, limit,
                created, modified, childs.map { it -> (it as TaskTable).toTaskEntity() })
    }
}