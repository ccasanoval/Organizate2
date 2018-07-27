package com.cesoft.organizate2.ui.item

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import com.cesoft.organizate2.App
import com.cesoft.organizate2.R
import com.cesoft.organizate2.entity.TaskEntity
import com.cesoft.organizate2.entity.TaskReduxEntity
import com.cesoft.organizate2.repo.db.Database
import com.cesoft.organizate2.util.di.AppComponent
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_item.*
import kotlinx.android.synthetic.main.content_item.*
import android.view.animation.AnimationUtils
import android.widget.*
import com.cesoft.organizate2.entity.Task


/**
 * Created by ccasanova on 18/07/2018
 */
//TODO: Make Frame for landscape and portrait...
class ItemActivity : AppCompatActivity() {

    private val appComponent: AppComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as App).appComponent
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var db: Database

    private lateinit var itemViewModel: ItemViewModel

    private var superPopupWindow : PopupWindow? = null


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
            itemViewModel.getTask()?.observe(this, Observer { task -> updateTask(task!!)})
        })

        itemViewModel.getTasksReady().observe(this, Observer {
            itemViewModel.getTasks()?.observe(this, Observer { _ -> updateParentName() })
        })

        //intent.getParcelableExtra<Parcelable>(Objeto::class.java!!.getName())
        val idTask = intent.getIntExtra(TaskEntity::class.java.simpleName, Task.ID_NIL)
        if(idTask == Task.ID_NIL) {
            itemViewModel.newTask()
        }
        else {
            itemViewModel.loadTask(idTask)
        }
        Log.e(TAG, "onCreate:-----------------------------ID TASK-----------------------$idTask")

        btnPadre.setOnClickListener {
            superPopupWindow = popupPadre()
            superPopupWindow?.showAsDropDown(it, -7, 0)
        }

        fab.setOnClickListener { _ -> onBackPressed() }
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

    /*private fun updateTasks(tasks: List<TaskReduxEntity>) {
        Log.e(TAG, "updateTasks:------------------------TASKS-----------------------${tasks.size}")
        tasksList = tasks
    }*/
    private fun updateTask(task: TaskEntity) {
        Log.e(TAG, "updateTask:------------------------TASK-----------${rbPrioridad.numStars}-----priority--${task.priority}----- $task  ")
        txtNombre.setText(task.name)
        txtDescripcion.setText(task.description)
        rbPrioridad.numStars = task.priority
        rbPrioridad.numStars = 4
        //TODO:
    }

    private fun updateParentName() {
        btnPadre.text = itemViewModel.getNameById() ?: getString(R.string.nodo_padre)
    }

    //____________________________________________________________________________________________________________________________________________________
    // POPUP PADRE
    //______________________________________________________________________________________________
    private fun popupPadre(): PopupWindow? {
        val popupWindow = PopupWindow(this)
        val list = ListView(this)

        val listData = itemViewModel.getTaskSupers()
        listData?.let {
            list.adapter = padreAdapter(it)
            list.onItemClickListener = PadreDropdownOnItemClickListener()
            // some other visual settings
            val metrics = resources.displayMetrics
            popupWindow.width = metrics.widthPixels - 40
            popupWindow.isFocusable = true
            popupWindow.height = WindowManager.LayoutParams.WRAP_CONTENT
            // set the list view as pop up window content
            popupWindow.contentView = list
            return popupWindow
        }
        Toast.makeText(this, getString(R.string.no_task_with_super_level), Toast.LENGTH_LONG).show()
        return null
    }

    //______________________________________________________________________________________________
    private fun padreAdapter(padreArray: Array<TaskReduxEntity>): ArrayAdapter<TaskReduxEntity> {
        return object : ArrayAdapter<TaskReduxEntity>(this, android.R.layout.simple_list_item_1, padreArray) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

                val listItem = TextView(this@ItemActivity)
                listItem.text = getItem(position).name
                listItem.tag = getItem(position).id
                listItem.textSize = 22f
                listItem.setPadding(10, 10, 10, 10)
                listItem.setTextColor(Color.WHITE)

                return listItem
            }
        }
    }

    //______________________________________________________________________________________________
    private inner class PadreDropdownOnItemClickListener : AdapterView.OnItemClickListener {
        override fun onItemClick(arg0: AdapterView<*>, v: View, arg2: Int, arg3: Long) {
            // add some animation when a list item was clicked
            val fadeInAnimation = AnimationUtils.loadAnimation(v.context, android.R.anim.fade_in)
            fadeInAnimation.duration = 10
            v.startAnimation(fadeInAnimation)

            // dismiss the pop up
            superPopupWindow?.dismiss()

            // get the text and set it as the button text
            val id = getItemSelectedId((v as TextView).text.toString())
            /*String selectedItemText = ((TextView)v).getText().toString();
			selectedItemText = selectedItemText.replace(HIJO, "").replace(PADRE, "");*/
            btnPadre.text = id
            // get the id
            itemViewModel.setIdSuper(v.getTag() as Int)
        }
    }

    companion object {
        val TAG: String = ItemActivity::class.simpleName!!
        private val PADRE = "+ "
        private val HIJO = "   - "
        fun getItemSelectedId(txt: String): String {
            return txt.replace(HIJO, "").replace(PADRE, "")
        }
    }
}