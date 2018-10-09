package com.cesoft.organizate2.ui.item

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
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
import com.cesoft.organizate2.ui.alert.date.AlertDateActivity
import com.cesoft.organizate2.ui.alert.geo.AlertGeoActivity
import com.cesoft.organizate2.util.Text2Speech
import com.cesoft.organizate2.util.exception.Failure


/**
 * Created by ccasanova on 18/07/2018
 */
//TODO: Make Frame for landscape and portrait...
class ItemActivity : AppCompatActivity() {

    private val appComponent: AppComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as App).appComponent
    }

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var db: Database
    @Inject lateinit var text2Speech: Text2Speech

    private lateinit var viewModel: ItemViewModel
    private var superPopupWindow : PopupWindow? = null
    //private var idSuper: Int = Task.ID_NIL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)
        setSupportActionBar(toolbar)
        appComponent.inject(this)

        observeViewModel()
        getTaskFromCaller()
        initButtons()
        initFields()
    }

    private fun initFields() {
        txtNombre.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                /*if(s.toString() != viewModel.task.value?.name)
                    viewModel.isDirty = true*/
                checkDirtyFlag()
                Log.e(TAG, "txtNombre.addTextChangedListener-------------------------------------"+viewModel.isDirty)
            }
        })
        txtDescripcion.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                checkDirtyFlag()
                /*if(s.toString() != viewModel.task.value?.description)
                    viewModel.isDirty = true*/
                Log.e(TAG, "txtDescripcion.addTextChangedListener-------------------------------------"+viewModel.isDirty)
            }
        })
        rbPrioridad.setOnRatingBarChangeListener { ratingBar: RatingBar, rating: Float, fromUser: Boolean ->
            /*if(fromUser) {
                if(rating.toInt() != viewModel.task.value?.priority)
                    viewModel.isDirty = true*/
            checkDirtyFlag()
            Log.e(TAG, "rbPrioridad.setOnRatingBarChangeListener-------------------------------------"+viewModel.isDirty)
        }
    }
    private fun checkDirtyFlag() {
        viewModel.setDirtyFlag(
                txtNombre.text.toString(),
                txtDescripcion.text.toString(),
                rbPrioridad.rating.toInt())
    }
    private fun observeViewModel() {
        //listViewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)-->Sin dagger
        viewModel = ViewModelProviders.of(this, viewModelFactory)[ItemViewModel::class.java]
        viewModel.task.observe(this, Observer { task -> updateTask(task!!)})
        viewModel.tasks.observe(this, Observer { _ -> updateParentName() })
        viewModel.finish.observe(this, Observer { finish() })
        viewModel.failure.observe(this, Observer { failure -> showError(failure) })
    }
    private fun getTaskFromCaller() {
        //intent.getParcelableExtra<Parcelable>(Objeto::class.java!!.getName())
        val idTask = intent.getIntExtra(TaskEntity::class.java.simpleName, TaskEntity.ID_NIL)
        if(idTask == TaskEntity.ID_NIL) {
            viewModel.newTask()
            btnEliminar.isEnabled = false
        }
        else {
            viewModel.loadTask(idTask)
        }
        Log.e(TAG, "onCreate:-----------------------------ID TASK-----------------------: $idTask")
    }
    private fun initButtons() {
        btnPadre.setOnClickListener {
            superPopupWindow = popupPadre()
            superPopupWindow?.showAsDropDown(it, 0, 0)
        }
        fabBack.setOnClickListener { _ -> onSalir() }
        btnEliminar.setOnClickListener { _ -> onEliminar() }
        btnHablar.setOnClickListener { _ -> onHablar() }
        btnAvisoFecha.setOnClickListener { _ -> onAvisoFecha() }
        btnAvisoGeo.setOnClickListener { _ -> onAvisoGeo() }
    }
    private fun showError(failure: Failure?) {
        Log.e(TAG, "onCreate:e:----------------------------------------------------: $failure")
        val msg = when(failure) {
            is Failure.TaskIdNotFound -> getString(R.string.error_task_not_found)
            is Failure.Database -> getString(R.string.error_database)
            is Failure.FeatureFailure -> getString(R.string.error_feature)
            else -> getString(R.string.error_unexpected)
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.home -> {
                onSalir()
                true
            }
            R.id.action_save -> {
                save()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onResume() {
        super.onResume()
    }

    private fun updateTask(task: TaskEntity) {
        txtNombre.setText(task.name)
        txtDescripcion.setText(task.description)
        rbPrioridad.rating = task.priority.toFloat()
    }

    private fun updateParentName() {
        btnPadre.text = viewModel.getNameById() ?: getString(R.string.nodo_padre)
    }


    private fun popupPadre(): PopupWindow? {
        val popupWindow = PopupWindow(this)
        val list = ListView(this)

        val listData = viewModel.getTaskSupers()
        listData?.let {
            if(listData.isEmpty()) {
                Toast.makeText(this, getString(R.string.no_task_list), Toast.LENGTH_LONG).show()
                return null
            }
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
            @SuppressLint("SetTextI18n")
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val listItem = TextView(this@ItemActivity)
                var prefix = ""
                val task = getItem(position)!!
                when(task.level) {
                    TaskEntity.LEVEL1 -> prefix = " "
                    TaskEntity.LEVEL2 -> prefix = "    "
                    TaskEntity.LEVEL3 -> prefix = "       "
                }
                listItem.text = "$prefix${task.name}"
                listItem.tag = getItem(position)//getItem(position)?.id
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
            btnPadre.text = (v as TextView).text.toString().trim()
            // get the id
            v.getTag().let { it ->
                val task: TaskReduxEntity = it as TaskReduxEntity
                viewModel.setIdSuper(task.id)
                viewModel.setLevel(TaskEntity.levelChildOf(task.level))
            }
            checkDirtyFlag()
        }
    }

    private fun save() {
        viewModel.save(
                txtNombre.text.toString(),
                txtDescripcion.text.toString(),
                rbPrioridad.rating.toInt())
    }

    private fun onAvisoFecha() {
        val intent = Intent(this, AlertDateActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(TaskEntity::class.java.simpleName, viewModel.taskId)
        startActivityForResult(intent, REQUEST_AVISO_FECHA)
        Log.e(TAG, "onAvisoFecha------------------------------- ${viewModel.taskId}")
    }
    private fun onAvisoGeo() {
        val intent = Intent(this, AlertGeoActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(TaskEntity::class.java.simpleName, viewModel.taskId)
        startActivityForResult(intent, REQUEST_AVISO_GEO)
    }
    private fun onHablar() {
        val name = viewModel.taskName
        val description = viewModel.taskDescription
        val priority = viewModel.taskPriority
        val phrase = getString(R.string.text2speech_task)
        val text = String.format(phrase, name, description, priority)
        text2Speech.hablar(text)
    }
    private fun onEliminar() {
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_sure_delete))
                .setMessage(getString(R.string.ask_sure_delete))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    viewModel.delete()
                }
                .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                    dialog.cancel()
                }
                .show()
    }
    private fun onSalir() {
        Log.e(TAG, "onSalir---------------------------------")
        if(viewModel.isDirty)
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_exit_save))
                .setMessage(getString(R.string.ask_exit_save))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    save()
                    onBackPressed()
                }
                .setNegativeButton(getString(R.string.no)) { _, _ ->
                    onBackPressed()
                }
                .setNeutralButton(getString(R.string.cancel)) { dialog, _ ->
                    dialog.cancel()
                }
                .show()
        else
            onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.e(TAG, "onActivityResult-------------------------------------------")
        /*if(requestCode == ) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                // Do something with the contact here (bigger example below)
            }
        }*/
    }

    companion object {
        val TAG: String = ItemActivity::class.simpleName!!
        const val REQUEST_AVISO_FECHA = 69
        const val REQUEST_AVISO_GEO = 70
    }
}
