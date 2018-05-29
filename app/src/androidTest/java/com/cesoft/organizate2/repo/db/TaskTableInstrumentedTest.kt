package com.cesoft.organizate2.repo.db

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.cesoft.organizate2.util.extension.None
import junit.framework.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.ArrayList

/**
 * Created by ccasanova on 25/05/2018
 */
@RunWith(AndroidJUnit4::class)
class TaskTableInstrumentedTest {

    //@Rule
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private var mDatabase: Database? = null
    private var dao: TaskDao? = null

    @Before
    @Throws(Exception::class)
    fun initDb() {
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), Database::class.java)
                .allowMainThreadQueries()
                .build()
        dao = mDatabase!!.dao()
    }

    @After
    @Throws(Exception::class)
    fun closeDb() {
        mDatabase!!.close()
    }

    fun getFakeTasks(size: Int, idFrom: Int = 1, idParent: Int = Int.None): List<TaskTable> {
        val list = ArrayList<TaskTable>()
        for(i in idFrom until idFrom+size) {
            val task = TaskTable(i, idParent, "Title $i", "Desc $i",
                    5, i,System.currentTimeMillis()+10*60*60*1000,
                    System.currentTimeMillis(), System.currentTimeMillis())
            list.add(task)
        }
        return list
    }

    @Test
    @Throws(InterruptedException::class)
    fun should_get_empty_list_if_table_is_empty() {
        val tasks = dao!!.select()
        assertTrue(tasks.isEmpty())
    }
    @Test
    @Throws(InterruptedException::class)
    fun should_get_empty_list_if_table_is_empty_REDUX() {
        val tasks = dao!!.selectRedux()
        assertTrue(tasks.isEmpty())
    }

    @Test
    @Throws(InterruptedException::class)
    fun onInserting_checkIf_RowCountIsCorrect_REDUX() {
        val list1 = getFakeTasks(5)
        list1.forEach { task -> dao!!.insert(task) }
        val list2 = dao!!.selectRedux()
        val list3 = list1.map { task -> TaskReduxTable(task.id, task.idSuper, task.name, task.ordering) }//task.toTaskEntity() }
        val list4 = list3.map { task -> task.toTaskEntity() }
        val list5 = list2.map { task -> task.toTaskEntity() }

        assertEquals(list1.size, list2.size)
        assertTrue(list3.toTypedArray() contentDeepEquals list2.toTypedArray())
        assertEquals(list3, list2)
        assertTrue(list4.toTypedArray() contentDeepEquals list5.toTypedArray())
        assertEquals(list4, list5)
    }
    @Test
    @Throws(InterruptedException::class)
    fun onInserting_checkIf_RowCountIsCorrect() {
        val list1 = getFakeTasks(5)
        list1.forEach { task -> dao!!.insert(task) }
        val list2 = dao!!.select()
        val list3 = list1.map { task -> task.toTaskEntity() }
        val list4 = list2.map { task -> task.toTaskEntity() }

        assertEquals(list1.size, list2.size)
        //Why it DID fail? --> 'Cos TaskTable was not data class and didn't override equals!!!
        assertTrue(list1.toTypedArray() contentDeepEquals list2.toTypedArray())
        assertEquals(list1, list2)
        assertTrue(list3.toTypedArray() contentDeepEquals list4.toTypedArray())
        assertEquals(list3, list4)
    }

    @Test
    @Throws(InterruptedException::class)
    fun onUpdating_checkIf_UpdateHappensCorrectly() {
        val task = getFakeTasks(1)[0]
        dao!!.insert(task)
        val sUpdated = "-UPDATED-"
        val task2 = TaskTable(task.id, task.idSuper, sUpdated, task.description,
                task.priority, task.ordering,task.limit,task.created,task.modified)
        dao!!.update(task2)
        val list = dao!!.select()
        val task3 = dao!!.selectById(task.id)

        assertEquals(1, list.size)
        assertEquals(sUpdated, task3!!.name)
    }
    /* No need to update Redux
    @Test
    @Throws(InterruptedException::class)
    fun onUpdating_checkIf_UpdateHappensCorrectly_REDUX() {
        val taskOri = getFakeTasks(1)[0]
        val task = taskOri//TaskReduxTable(taskOri)//TaskReduxTable(taskOri.id, taskOri.idSuper, taskOri.name, taskOri.ordering)
        dao!!.insert(task)
        val sUpdated = "-UPDATED-"
        val task2 = TaskTable(task.id, task.idSuper, sUpdated, task.description,
                task.priority, task.ordering,task.limit,task.created,task.modified)
        dao!!.update(task2)
        val list = dao!!.select()
        val task3 = dao!!.selectById(task.id)

        assertEquals(1, list.size)
        assertEquals(sUpdated, task3!!.name)
    }*/

    @Test
    @Throws(InterruptedException::class)
    fun onDeletion_CheckIf_IsDeletedFromTable() {
        val tasks = getFakeTasks(5)
        tasks.forEach { task -> dao!!.insert(task) }
        dao!!.delete(tasks[2])
        assertNull(dao!!.selectById(tasks[2].id))
    }


    //-------------------------- CHILDS --------------------------
    @Test
    @Throws(InterruptedException::class)
    fun onInserting_checkIf_taskHasItsChilds_CHILD() {
        val id = 1
        val parents = getFakeTasks(3, id)
        val childs1 = getFakeTasks(5, 10, id )
        val childs2 = getFakeTasks(7, 20, id+1 )
        parents.forEach { task -> dao!!.insert(task) }
        childs1.forEach { task -> dao!!.insert(task) }
        childs2.forEach { task -> dao!!.insert(task) }
        val listAll = dao!!.select()

        val task1 = dao!!.selectById(id)!!
        task1.initChilds(listAll)
        System.err.println("---------(${childs1.size})--------- 5  ===  "+task1.childs.size)
        assertEquals(task1.childs.size, 5)

        val task2 = dao!!.selectById(id+1)!!
        task2.initChilds(listAll)
        System.err.println("---------(${childs2.size})--------- 7  ===  "+task2.childs.size)
        assertEquals(task2.childs.size, 7)
    }
    @Test
    @Throws(InterruptedException::class)
    fun onInserting_checkIf_taskHasItsChilds_CHILD_REDUX() {
        val id = 1
        val parents = getFakeTasks(3, id)
        val childs1 = getFakeTasks(5, 10, id )
        val childs2 = getFakeTasks(7, 20, id+1 )
        parents.forEach { task -> dao!!.insert(task) }
        childs1.forEach { task -> dao!!.insert(task) }
        childs2.forEach { task -> dao!!.insert(task) }
        val listAll = dao!!.selectRedux()

        val task1 = dao!!.selectById(id)!!
        task1.initChilds(listAll)
        System.err.println("---------(${childs1.size})--------- 5  ===  "+task1.childs.size)
        assertEquals(task1.childs.size, 5)

        val task2 = dao!!.selectById(id+1)!!
        task2.initChilds(listAll)
        System.err.println("---------(${childs2.size})--------- 7  ===  "+task2.childs.size)
        assertEquals(task2.childs.size, 7)
    }

}