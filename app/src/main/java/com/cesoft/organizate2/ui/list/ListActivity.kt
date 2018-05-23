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
import com.cesoft.organizate2.entity.TaskEntity.Companion.NONE
import com.cesoft.organizate2.repo.Database
import com.cesoft.organizate2.repo.TaskTable
import com.cesoft.organizate2.util.di.AppComponent

import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class ListActivity : AppCompatActivity() {

    val appComponent: AppComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as App).appComponent
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var db: Database

    private lateinit var listViewModel: ListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        appComponent.inject(this)
        //listViewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)-->Sin dagger
        listViewModel = ViewModelProviders.of(this, viewModelFactory)[ListViewModel::class.java]
        listViewModel.loadTask()
        listViewModel.getTasks().observe(this,
                Observer {
                    tasks ->
                    tasks!!.map { Log.e(TAG, "ITEM----------------"+it) }
                })

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

    override fun onResume() {
        super.onResume()

        Thread {
            Log.e(TAG, "-------------INI")

            var lastId = 0
            /*var db : Database = Room.databaseBuilder(applicationContext, Database::class.java, "organizate.db")
                    .fallbackToDestructiveMigration()
                    .build()*/
            val list = db.dao().select()
            for(item in list) {
                Log.e(TAG, "-------------"+item)
                val item2 = item.toTaskEntity()
                if(item2.id > lastId)
                    lastId = item2.id
            }

            val task = TaskTable(
                    lastId+1, NONE,
                    "Tarea "+(lastId+1), "Descripción de la Tarea",
                    NONE, NONE, NONE, NONE, NONE)
            db.dao().insert(task)

        }.start()
    }

    companion object {
        val TAG: String = ListActivity::class.simpleName!!
    }
}
