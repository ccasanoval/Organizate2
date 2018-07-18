package com.cesoft.organizate2.entity

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import android.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.rules.TestRule
import org.junit.Rule


/**
 * Created by ccasanova on 18/07/2018
 */
@RunWith(MockitoJUnitRunner::class)
class TaskReduxEntityTest {

    var taskList: ArrayList<TaskReduxEntity> = arrayListOf()
    lateinit var task1: TaskReduxEntity
    lateinit var task2: TaskReduxEntity
    lateinit var task3: TaskReduxEntity
    lateinit var task4: TaskReduxEntity
    lateinit var task5: TaskReduxEntity

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    /*private fun getFakeTasks(size: Int, level: Int, idFrom: Int, idParent: Int = Int.None): List<TaskTable> {
        val list = ArrayList<TaskTable>()
        for(i in idFrom until idFrom+size) {
            val task = TaskTable(i, idParent, "Title $i",
                    level,"Desc $i",
                    5, System.currentTimeMillis()+10*60*60*1000,
                    System.currentTimeMillis(), System.currentTimeMillis())
            list.add(task)
        }
        return list
    }*/
    private fun getFakeTasks(size: Int, level: Int, idFrom: Int, idSuper: Int = TaskReduxEntity.NO_SUPER): List<TaskReduxEntity> {
        val list = ArrayList<TaskReduxEntity>()
        for(i in idFrom until idFrom+size) {
            val task = TaskReduxEntity(i, idSuper, "Title $i", level)
            list.add(task)
        }
        return list
    }

    @Before
    fun setUp() {
        val taskListLevel_1 = getFakeTasks(4, TaskReduxEntity.LEVEL1, 1)
        //
        val taskListLevel_2_1a = getFakeTasks(4, TaskReduxEntity.LEVEL2, 10,1)
        val taskListLevel_2_1b = getFakeTasks(3, TaskReduxEntity.LEVEL2, 20,2)
        val taskListLevel_2_1c = getFakeTasks(1, TaskReduxEntity.LEVEL2, 30,3)
        //
        val taskListLevel_3_2a_1a = getFakeTasks(2, TaskReduxEntity.LEVEL3, 100,10)
        val taskListLevel_3_2b_1a = getFakeTasks(3, TaskReduxEntity.LEVEL3, 200,11)
        val taskListLevel_3_2a_1b = getFakeTasks(2, TaskReduxEntity.LEVEL3, 300,20)
        val taskListLevel_3_2b_1b = getFakeTasks(3, TaskReduxEntity.LEVEL3, 400,21)
        //
        //      1(task1)                               2                                    3      4
        //      10        11            12     13      20(t2)    21(t3)          22(t4)     30
        //      100 101   200 201 202                  300 301   400 401 402(t5)
        //
        taskList.addAll(taskListLevel_1)
        taskList.addAll(taskListLevel_2_1a)
        taskList.addAll(taskListLevel_2_1b)
        taskList.addAll(taskListLevel_2_1c)
        taskList.addAll(taskListLevel_3_2a_1a)
        taskList.addAll(taskListLevel_3_2b_1a)
        taskList.addAll(taskListLevel_3_2a_1b)
        taskList.addAll(taskListLevel_3_2b_1b)

        task1 = taskList[0]
        task2 = taskList[8]
        task3 = taskList[9]
        task4 = taskList[10]
        task5 = taskList[21]
    }

    @Test
    fun `should filter by level`() {
        val listLevel1 = TaskReduxEntity.filterByLevel(taskList, TaskReduxEntity.LEVEL1)
        val listLevel2 = TaskReduxEntity.filterByLevel(taskList, TaskReduxEntity.LEVEL2)
        val listLevel3 = TaskReduxEntity.filterByLevel(taskList, TaskReduxEntity.LEVEL3)

        assert(taskList.size == 22)

        assert(listLevel1.size == 4)
        assert(listLevel2.size == 8)
        assert(listLevel3.size == 10)

        assert(listLevel1[0].id == 1)
        assert(listLevel1[1].id == 2)
        assert(listLevel1[2].id == 3)
        assert(listLevel1[3].id == 4)

        assert(listLevel2[0].id == 10)
        assert(listLevel2[1].id == 11)
        assert(listLevel2[2].id == 12)
        assert(listLevel2[3].id == 13)
        assert(listLevel2[4].id == 20)
        assert(listLevel2[5].id == 21)
        assert(listLevel2[6].id == 22)
        assert(listLevel2[7].id == 30)

        assert(listLevel3[0].id == 100)
        assert(listLevel3[1].id == 101)
        assert(listLevel3[2].id == 200)
        assert(listLevel3[3].id == 201)
        assert(listLevel3[4].id == 202)
        assert(listLevel3[5].id == 300)
        assert(listLevel3[6].id == 301)
        assert(listLevel3[7].id == 400)
        assert(listLevel3[8].id == 401)
        assert(listLevel3[9].id == 402)
    }

    @Test
    fun `should filter by parent`() {
        // filterByParent() is private
        /*val listLevel1 = TaskReduxEntity.filterByLevel(taskList, TaskReduxEntity.LEVEL1)
        val item : TaskReduxEntity = TaskReduxEntity.None
        //java.lang.NoSuchMethodException: TaskReduxEntity.filterByParent(java.util.ArrayList, TaskReduxEntity)
        val method = TaskReduxEntity::class.java.getDeclaredMethod("filterByParent", listLevel1::class.java, item::class.java)
        method.isAccessible = true
        val listParent1 : List<TaskReduxEntity> = method.invoke(taskList, taskList[0]) as List<TaskReduxEntity>
        //val listParent1 = TaskReduxEntity.filterByParent(taskList, taskList[0])
        */
        // make filterByParent() internal
        val listParent1 = TaskReduxEntity.filterByParent(taskList, task1)
        assert(listParent1.size == 4)
        assert(listParent1[0].id == 10)
        assert(listParent1[1].id == 11)
        assert(listParent1[2].id == 12)
        assert(listParent1[3].id == 13)
        //---

        val listParent2 = TaskReduxEntity.filterByParent(taskList, task2)
        assert(listParent2.size == 2)
        assert(listParent2[0].id == 300)
        assert(listParent2[1].id == 301)
        //---

        val listParent3 = TaskReduxEntity.filterByParent(taskList, task3)
        assert(listParent3.size == 3)
        assert(listParent3[0].id == 400)
        assert(listParent3[1].id == 401)
        assert(listParent3[2].id == 402)
        //---

        val listParent4 = TaskReduxEntity.filterByParent(taskList, task4)
        assert(listParent4.isEmpty())
    }

    @Test
    fun `should create a tree`() {
        val tree = TaskReduxEntity.createTree(taskList)

        assert(task1.Childs.size == 4)
        assert(task2.Childs.size == 2)
        assert(task3.Childs.size == 3)
        assert(task4.Childs.isEmpty())
        assert(task5.Childs.isEmpty())

        assert(task1.Childs[0].Childs.size == 2)
        assert(task1.Childs[0].Childs[0].id == 100)

        assert(task3.Childs.size == 3)
        assert(task3.Childs[2] == task5)

        assert(tree[1].Childs[1].Childs[2] == task5)

        for(task in tree) {
            //if(task.idSuper != TaskReduxEntity.NO_SUPER) {
            if(task.Childs.isNotEmpty()) {
                for(childTask in task.Childs)
                    assert(childTask.idSuper == task.id)
            }
        }
    }
}