package com.cesoft.organizate2.ui.list

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.cesoft.organizate2.App
import com.cesoft.organizate2.R
import com.cesoft.organizate2.entity.TaskEntity
import com.cesoft.organizate2.entity.TaskReduxEntity
import com.cesoft.organizate2.entity.TaskReduxEntity.Companion.LEVEL1
import com.cesoft.organizate2.entity.TaskReduxEntity.Companion.LEVEL2
import com.cesoft.organizate2.entity.TaskReduxEntity.Companion.LEVEL3
import com.cesoft.organizate2.repo.db.Database
import com.cesoft.organizate2.repo.db.TaskTable
import com.cesoft.organizate2.ui.base.NivelUnoListAdapter
import com.cesoft.organizate2.ui.item.ItemActivity
import com.cesoft.organizate2.util.di.AppComponent
import com.cesoft.organizate2.util.extension.None

import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.content_list.*
import javax.inject.Inject

/**
 * Created by ccasanova on 23/05/2018
 */
class ListActivity : AppCompatActivity(), ListViewInterface {

    private val appComponent: AppComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as App).appComponent
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var db: Database

    private lateinit var listViewModel: ListViewModel

    //private var expListView: ExpandableListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        setSupportActionBar(toolbar)
        appComponent.inject(this)

        //listViewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)-->Sin dagger
        listViewModel = ViewModelProviders.of(this, viewModelFactory)[ListViewModel::class.java]

        listViewModel.failure.observe(this, Observer { failure ->
            Log.e(TAG, "onCreate:e:----------------------------------------------------$failure")
        })
        listViewModel.getTasksReady().observe(this, Observer {
            if(it!!)listViewModel.getTasks()?.observe(this, Observer { tasks -> updateTaskList(tasks!!)})
        })

        listViewModel.loadTask()
        listViewModel.setView(this)
        fab.setOnClickListener { _ ->
            listViewModel.onAddTask()
        }
    }

    fun updateTaskList(tasks: List<TaskReduxEntity>) {
        //Log.e(TAG, "getTasks().observe----------------------------------------------------"+tasks.size)
        //tasks.onEach { Log.e(TAG, "ITEM:getTasks().observe:---------------------$it") }
        uiTaskList.setAdapter(NivelUnoListAdapter(applicationContext, listViewModel, uiTaskList, tasks))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.config -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getFakeTasks(size: Int, level: Int, idFrom: Int, idParent: Int = Int.None): List<TaskTable> {
        val list = ArrayList<TaskTable>()
        for(i in idFrom until idFrom+size) {
            val task = TaskTable(i, idParent, "Title $i",
                    level,"Desc $i",
                    5, System.currentTimeMillis()+10*60*60*1000,
                    System.currentTimeMillis(), System.currentTimeMillis())
            list.add(task)
        }
        return list
    }
    override fun onResume() {
        super.onResume()

        ///DEVELOPING...
        Thread {
            val level1a = getFakeTasks(3, LEVEL1,1)                 //L1: 1..2..3
            val level2a = getFakeTasks(2, LEVEL2,10, 1)     //L2: 10..11..12..13..14
            val level2b = getFakeTasks(2, LEVEL2,20, 2)     //L2: 20..21
            val level3a = getFakeTasks(3, LEVEL3,100, 20)   //L3: 100..101..102
            val level3b = getFakeTasks(3, LEVEL3,200, 21)   //L3: 200..201..202
            level1a.map { task -> Log.e(TAG, "onResume-1a----------------------$task") }
            level2a.map { task -> Log.e(TAG, "onResume-2a----------------------$task") }
            level2b.map { task -> Log.e(TAG, "onResume-2b----------------------$task") }
            try { db.dao().insert(level1a) }catch (e: Exception){}
            try { db.dao().insert(level2a) }catch (e: Exception){}
            try { db.dao().insert(level2b) }catch (e: Exception){}
            try { db.dao().insert(level3a) }catch (e: Exception){}
            try { db.dao().insert(level3b) }catch (e: Exception){}
        }.start()
        ///DEVELOPING...
    }

    override fun startActivity(taskId: Int) {//task: TaskReduxEntity) {
        val intent = Intent(this, ItemActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(TaskEntity::class.java.simpleName, taskId)//task.id)
        startActivity(intent)
    }

    companion object {
        val TAG: String = ListActivity::class.simpleName!!
    }
}
