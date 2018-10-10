package com.cesoft.organizate2.ui.alert.date

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.cesoft.organizate2.R
import kotlinx.android.synthetic.main.activity_edit_date.*
import com.cesoft.organizate2.entity.AlertDateEntity.Companion.TYPE_MINUTES
import com.cesoft.organizate2.entity.AlertDateEntity.Companion.TYPE_HOURS
import com.cesoft.organizate2.entity.AlertDateEntity.Companion.TYPE_DAYS_MONTH
import com.cesoft.organizate2.entity.AlertDateEntity.Companion.TYPE_DAYS_WEEK
import com.cesoft.organizate2.entity.AlertDateEntity.Companion.TYPE_MONTH
import kotlinx.android.synthetic.main.alert_date_item.view.*

class EditDateActivity : AppCompatActivity() {

    class EditDateParcelable(val type: Int, val valueNames: List<String>?, val currentValues: IntArray?) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.createStringArrayList(),
                parcel.createIntArray())

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(type)
            parcel.writeStringList(valueNames)
            parcel.writeIntArray(currentValues)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<EditDateParcelable> {
            override fun createFromParcel(parcel: Parcel): EditDateParcelable {
                return EditDateParcelable(parcel)
            }

            override fun newArray(size: Int): Array<EditDateParcelable?> {
                return arrayOfNulls(size)
            }
        }
    }

    class ValuesAdapter(val items : ArrayList<String>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.alert_date_item, parent, false))
        }
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.tvValue.text = items[position]
        }
        override fun getItemCount(): Int {
            return items.size
        }
    }
    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val tvValue: TextView = view.tvValue
    }


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_edit_date)
        Log.e(TAG, "onCreate:----------------------------------------------------")
        lstCurrentValues.layoutManager = LinearLayoutManager(this)
        getDataFromCaller()
    }

    private fun getDataFromCaller() {
        val o:EditDateParcelable = intent.getParcelableExtra(EditDateParcelable::class.java.name)
        Log.e(TAG, "getDataFromCaller:----------------------------------------------------: $o")
        initValueNameList(o.type, o.valueNames)
        initCurrentValueList(o.type, o.valueNames, o.currentValues)
    }
    private fun initCurrentValueList(type: Int, valueNames: List<String>?, currentValues: IntArray?) {
        val data = ArrayList<String>()
        if(currentValues == null)return
        when(type) {
            TYPE_MINUTES, TYPE_HOURS, TYPE_DAYS_MONTH ->
                for(value in currentValues) {
                    data.add("$value")
                }
            TYPE_DAYS_WEEK, TYPE_MONTH ->
                for(value in currentValues) {
                    val s = valueNames?.get(value)
                    if(s != null) data.add(s)
                }
        }
        Log.e(TAG, "getDataFromCaller:----------------------------------------------------: $data")
        //lstCurrentValues.layoutManager = GridLayoutManager(this, 2)
        lstCurrentValues.adapter = ValuesAdapter(data, this)
    }
    private fun initValueNameList(type: Int, valueNames: List<String>?) {
        val data = ArrayList<String>()
        if(valueNames == null) {
            when(type) {
                TYPE_MINUTES, TYPE_HOURS ->
                    for (i in 0..59)
                        data.add(i.toString())
                TYPE_DAYS_MONTH ->
                    for (i in 1..31)
                        data.add(i.toString())
                /*TYPE_MONTH ->
                    for(i in 1..12)
                        data.add(i.toString())*/
            }
        }
        else {
            data.addAll(valueNames)
        }

        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        lstAllValues.adapter = arrayAdapter
    }


    private fun respond() {
        val res = Intent()
        //res.data =
        //res.putExtra("", IntArray())
        setResult(Activity.RESULT_OK, res)
        finish()
    }


    companion object {
        val TAG: String = EditDateActivity::class.simpleName!!
    }
}