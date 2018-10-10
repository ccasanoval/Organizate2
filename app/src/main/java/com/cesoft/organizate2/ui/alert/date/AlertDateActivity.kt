package com.cesoft.organizate2.ui.alert.date

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.cesoft.organizate2.App
import com.cesoft.organizate2.R
import com.cesoft.organizate2.entity.AlertDateEntity
import com.cesoft.organizate2.entity.TaskEntity
import com.cesoft.organizate2.repo.db.Database
import com.cesoft.organizate2.util.di.AppComponent
import com.cesoft.organizate2.util.exception.Failure
import kotlinx.android.synthetic.main.activity_alert_date.*
import kotlinx.android.synthetic.main.content_alert_date.*
import javax.inject.Inject
import com.cesoft.organizate2.entity.AlertDateEntity.Companion.TYPE_MINUTES
import com.cesoft.organizate2.entity.AlertDateEntity.Companion.TYPE_HOURS
import com.cesoft.organizate2.entity.AlertDateEntity.Companion.TYPE_DAYS_MONTH
import com.cesoft.organizate2.entity.AlertDateEntity.Companion.TYPE_DAYS_WEEK
import com.cesoft.organizate2.entity.AlertDateEntity.Companion.TYPE_MONTH
import android.app.Activity


class AlertDateActivity : AppCompatActivity() {
    private val appComponent: AppComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as App).appComponent
    }

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var db: Database

    private lateinit var viewModel: AlertDateViewModel

    private val dayOfWeek = ArrayList<String>()
    private val months = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert_date)
        setSupportActionBar(toolbar)
        appComponent.inject(this)

        observeViewModel()
        getTaskFromCaller()
        initButtons()
        //initFields()
        initStrings()
    }
    override fun onResume() {
        super.onResume()
    }

    private fun initStrings() {
        dayOfWeek.add(getString(R.string.monday))
        dayOfWeek.add(getString(R.string.tuesday))
        dayOfWeek.add(getString(R.string.wednesday))
        dayOfWeek.add(getString(R.string.thursday))
        dayOfWeek.add(getString(R.string.friday))
        dayOfWeek.add(getString(R.string.saturday))
        dayOfWeek.add(getString(R.string.sunday))

        months.add(getString(R.string.january))
        months.add(getString(R.string.february))
        months.add(getString(R.string.march))
        months.add(getString(R.string.april))
        months.add(getString(R.string.may))
        months.add(getString(R.string.june))
        months.add(getString(R.string.july))
        months.add(getString(R.string.august))
        months.add(getString(R.string.september))
        months.add(getString(R.string.october))
        months.add(getString(R.string.november))
        months.add(getString(R.string.december))
    }

    private fun observeViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory)[AlertDateViewModel::class.java]
        viewModel.data.observe(this, Observer { data -> updateData(data!!)})
        viewModel.failure.observe(this, Observer { failure -> showError(failure) })
    }
    private fun updateData(data: AlertDateEntity) {
        txtMinutos.text = ""
        if(data.minutes.isNotEmpty()) {
            list2Text(TYPE_MINUTES, data.minutes, txtMinutos)
        }
        txtHoras.text = ""
        if(data.hours.isNotEmpty()) {
            list2Text(TYPE_HOURS, data.hours, txtHoras)
        }
        txtDiasSemana.text = ""
        if(data.daysOfWeek.isNotEmpty()) {
            list2Text(TYPE_DAYS_WEEK, data.daysOfWeek, txtDiasSemana)
        }
        txtDiasMes.text = ""
        if(data.daysOfMonth.isNotEmpty()) {
            list2Text(TYPE_DAYS_MONTH, data.daysOfMonth, txtDiasMes)
        }
        txtMeses.text = ""
        if(data.months.isNotEmpty()) {
            list2Text(TYPE_MONTH, data.months, txtMeses)
        }
    }
    private fun list2Text(type: Int, list: IntArray, textView: TextView) {
        when(type) {
            TYPE_MINUTES, TYPE_HOURS, TYPE_DAYS_MONTH ->
                for(value in list)
                    textView.append("$value, ")
            TYPE_DAYS_WEEK ->
                for(value in list)
                    textView.append("${dayOfWeek[value]}, ")
            TYPE_MONTH ->
                for(value in list)
                    textView.append("${months[value]}, ")
        }
    }
    private fun getTaskFromCaller() {
        //intent.getParcelableExtra<Parcelable>(Objeto::class.java!!.getName())
        val idTask = intent.getIntExtra(TaskEntity::class.java.simpleName, TaskEntity.ID_NIL)
        if(idTask == TaskEntity.ID_NIL) {
            viewModel.isNew()
        }
        else {
            viewModel.loadAlert(idTask)
        }
        Log.e(TAG, "getTaskFromCaller:-----------------------------ID TASK-----------------------: $idTask")
    }


    private fun initButtons() {
        fabBack.setOnClickListener { _ -> onSalir() }
        btnMinutos.setOnClickListener { _ -> onEditarElemento(TYPE_MINUTES) }
        btnHoras.setOnClickListener { _ -> onEditarElemento(TYPE_HOURS) }
        btnDiasMes.setOnClickListener { _ -> onEditarElemento(TYPE_DAYS_MONTH) }
        btnDiasSemana.setOnClickListener { _ -> onEditarElemento(TYPE_DAYS_WEEK) }
        btnMeses.setOnClickListener { _ -> onEditarElemento(TYPE_MONTH) }
    }
    private fun showError(failure: Failure?) {
        Log.e(TAG, "showError:e:----------------------------------------------------: $failure")
        //TODO: Toast
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


    private fun save() {
        /*viewModel.save(
                txtNombre.text.toString(),
                txtDescripcion.text.toString(),
                rbPrioridad.rating.toInt())*/
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


    private fun onEditarElemento(type: Int) {
        val data = when(type) {
            TYPE_MINUTES ->
                EditDateActivity.EditDateParcelable(type, null, viewModel.minutes)
            TYPE_HOURS ->
                EditDateActivity.EditDateParcelable(type, null, viewModel.hours)
            TYPE_DAYS_MONTH ->
                EditDateActivity.EditDateParcelable(type, null, viewModel.daysOfMonth)
            TYPE_DAYS_WEEK ->
                EditDateActivity.EditDateParcelable(type, null, viewModel.daysOfWeek)
            TYPE_MONTH ->
                EditDateActivity.EditDateParcelable(type, null, viewModel.months)
            else ->
                null
        }
        Log.e(TAG, "onEditarElemento:---------------------------------------------------- $type")
        val intent = Intent(this, EditDateActivity::class.java)
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(EditDateActivity.EditDateParcelable::class.java.simpleName, data)
        startActivityForResult(intent, type)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        /*if(requestCode == ) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                // Do something with the contact here (bigger example below)
            }
        }*/
    }

    private fun respond() {
        val res = Intent()
        //res.data =
        //res.putExtra("", IntArray())
        setResult(Activity.RESULT_OK, res)
        finish()
    }


    companion object {
        val TAG: String = AlertDateActivity::class.simpleName!!
    }
}