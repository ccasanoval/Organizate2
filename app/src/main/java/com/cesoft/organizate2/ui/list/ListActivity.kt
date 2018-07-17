package com.cesoft.organizate2.ui.list

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.cesoft.organizate2.App
import com.cesoft.organizate2.R
import com.cesoft.organizate2.entity.TaskReduxEntity.Companion.LEVEL1
import com.cesoft.organizate2.entity.TaskReduxEntity.Companion.LEVEL2
import com.cesoft.organizate2.entity.TaskReduxEntity.Companion.LEVEL3
import com.cesoft.organizate2.repo.db.Database
import com.cesoft.organizate2.repo.db.TaskTable
import com.cesoft.organizate2.ui.base.NivelUnoListAdapter
import com.cesoft.organizate2.util.di.AppComponent
import com.cesoft.organizate2.util.extension.None

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import javax.inject.Inject

class ListActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //uiTaskList

        fab.setOnClickListener { _ ->
            listViewModel.onAddTask()
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }

        appComponent.inject(this)
        //listViewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)-->Sin dagger
        listViewModel = ViewModelProviders.of(this, viewModelFactory)[ListViewModel::class.java]

        listViewModel.failure.observe(this, Observer { failure ->
            Log.e(TAG, "onCreate:e:------------------------------------------------------------"+failure)
        })
        listViewModel.getTasks().observe(this, Observer { tasks ->
            Log.e(TAG, "getTasks().observe----------------------------------------------------"+tasks?.size)

            tasks!!.onEach { Log.e(TAG, "ITEM:getTasks().observe:---------------------"+it) }
            uiTaskList.setAdapter(NivelUnoListAdapter(applicationContext, uiTaskList, tasks!!))
        })

        listViewModel.loadTask()
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
            R.id.action_settings -> true
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

        listViewModel.loadTask()

        Thread {
            val level1a = getFakeTasks(2, LEVEL1,1)                 //L1: 1..2..3
            val level2a = getFakeTasks(2, LEVEL2,10, 1)     //L2: 10..11..12..13..14
            val level2b = getFakeTasks(2, LEVEL2,20, 2)     //L2: 20..21
            val level3a = getFakeTasks(3, LEVEL3,100, 20)   //L3: 100..101..102
            val level3b = getFakeTasks(3, LEVEL3,200, 21)   //L3: 200..201..202
            level1a.map { task -> Log.e(TAG, "-----------------------"+task) }
            try { db.dao().insert(level1a) }catch (e: Exception){}
            try { db.dao().insert(level2a) }catch (e: Exception){}
            try { db.dao().insert(level2b) }catch (e: Exception){}
            try { db.dao().insert(level3a) }catch (e: Exception){}
            try { db.dao().insert(level3b) }catch (e: Exception){}
        }.start()


        /*Thread {
            Log.e(TAG, "-------------INI")
            val task = db.dao().selectById(696969)
            if(task == null)
                Log.e(TAG, "---------------SEGUN LINT ESTO NUNCA OCURRE-------------")
            Log.e(TAG, "---------------TASK 696969-------------"+task)

        }.start()*/

        /*Thread {
            Log.e(TAG, "-------------INI")

            var lastId = 0
            val list = db.dao().select()
            for(item in list) {
                Log.e(TAG, "-------------"+item)
                val item2 = item.toTaskEntity()
                if(item2.id > lastId)
                    lastId = item2.id
            }

            val task = com.cesoft.organizate2.repo.db.TaskTable(
                    lastId+1, Int.None,
                    "Tarea "+(lastId+1), "Descripción de la Tarea",
                    Int.None, Int.None, Int.None, Int.None, Int.None)
            db.dao().insert(task)

        }.start()*/
    }

    companion object {
        val TAG: String = ListActivity::class.simpleName!!
    }
}
