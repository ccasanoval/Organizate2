package com.cesoft.organizate2.ui.item

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.cesoft.organizate2.App
import com.cesoft.organizate2.R
import com.cesoft.organizate2.entity.TaskEntity
import com.cesoft.organizate2.repo.db.Database
import com.cesoft.organizate2.util.di.AppComponent
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_item.*
import kotlinx.android.synthetic.main.content_item.*
import kotlinx.android.synthetic.main.nivel1.*

/**
 * Created by ccasanova on 18/07/2018
 */
class ItemActivity : AppCompatActivity() {

    private val appComponent: AppComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as App).appComponent
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var db: Database

    private lateinit var itemViewModel: ItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)
        setSupportActionBar(toolbar)
        appComponent.inject(this)

        Log.e(TAG, "onCreate:---------------------0000000000000000-------------------------------")


        //listViewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)-->Sin dagger
        itemViewModel = ViewModelProviders.of(this, viewModelFactory)[ItemViewModel::class.java]

        itemViewModel.failure.observe(this, Observer { failure ->
            Log.e(TAG, "onCreate:e:----------------------------------------------------$failure")
        })

        itemViewModel.getTaskReady().observe(this, Observer {
            //if(it!!)
            itemViewModel.getTask()?.observe(this, Observer { task -> updateTask(task!!)})
            //itemViewModel.getTaskSuper()?.observe(this, Observer { task -> updateTaskSuper(task!!)})
        })

        //intent.getParcelableExtra<Parcelable>(Objeto::class.java!!.getName())
        val idTask = intent.getIntExtra(TaskEntity::class.java.simpleName, TaskEntity.ID_NIL)
        if(idTask != TaskEntity.ID_NIL) {
            itemViewModel.loadTask(idTask)
        }
        else {
            itemViewModel.newTask()
        }
        Log.e(TAG, "onCreate:-----------------------------ID TASK-----------------------$idTask")

        fab.setOnClickListener { _ ->
        }

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
            R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
        /*
			case R.id.action_user:
				saveValores();
				return true;
        * */
    }


    override fun onResume() {
        super.onResume()
    }

    private fun updateTask(task: TaskEntity) {
        Log.e(TAG, "updateTask:e:------------------------TASK-----------------------$task")
        txtNombre.setText(task.name)
        txtDescripcion.setText(task.description)
        rbPrioridad.numStars = task.priority

        //TODO: Select parent from list...
    }

    companion object {
        val TAG: String = ItemActivity::class.simpleName!!
    }
}
