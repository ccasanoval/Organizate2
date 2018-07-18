package com.cesoft.organizate2.ui.base

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SimpleExpandableListAdapter
import android.widget.TextView
import com.cesoft.organizate2.R
import com.cesoft.organizate2.entity.TaskReduxEntity
import com.cesoft.organizate2.util.Log

/**
 * Created by ccasanova on 29/05/2018
 */

class NivelDosListAdapter(context: Context, groupData: List<Map<String, *>>,
                          groupLayout: Int, groupFrom: Array<String>, groupTo: IntArray,
                          childData: List<List<Map<String, *>>>, childLayout: Int,
                          childFrom: Array<String>, childTo: IntArray, groupPosition: Int)
    : SimpleExpandableListAdapter(context, groupData, groupLayout, groupFrom, groupTo, childData, childLayout, childFrom, childTo) {
    private var _seccion = 0

    init {
        _seccion = groupPosition
    }

    /// NIVEL 3 --------------------------------------------------------------------------------
    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        val v = super.getChildView(groupPosition, childPosition, isLastChild, convertView, parent)

        val txtChild = v.findViewById(R.id.txtNivel3) as TextView
        txtChild.setOnClickListener {
            Log.e("NivelDos", " ON CLICK **********3***********************${_lista!![_seccion].Childs[groupPosition].Childs[childPosition]}")
            //TODO:
            //val intent = Intent(_context, ActEdit::class.java)
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            //intent.putExtra(Objeto::class.java!!.getName(), _lista!![_seccion].getHijos()[groupPosition].getHijos()[childPosition])
            //_context.startActivity(intent)
        }

        if(_bIniRowHeight3) {
            _bIniRowHeight3 = false
            v.measure(android.view.View.MeasureSpec.UNSPECIFIED, android.view.View.MeasureSpec.UNSPECIFIED)
            val height = v.measuredHeight
            CexpandableListView.setRowHeight3(height)
        }

        return v
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        val v = super.getGroupView(groupPosition, isExpanded, convertView, parent)

        /// NIVEL 2 --------------------------------------------------------------------------------
        val btnEditar = v.findViewById(R.id.btnEditarNivel2) as ImageButton
        btnEditar.setOnClickListener {
            Log.e("NivelDos", " ON CLICK **********2***********************${_lista!![_seccion].Childs[groupPosition]}")
            //TODO:
//            val intent = Intent(_context, ActEdit::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            intent.putExtra(Objeto::class.java!!.getName(), _lista!![_seccion].getHijos()[groupPosition])
//            _context.startActivity(intent)
        }
        btnEditar.isFocusable = false//NO HACE CASO EN LAYOUT XML

        if(_bIniRowHeight2) {
            _bIniRowHeight2 = false
            v.measure(android.view.View.MeasureSpec.UNSPECIFIED, android.view.View.MeasureSpec.UNSPECIFIED)
            val height = v.measuredHeight
            CexpandableListView.setRowHeight2(height)
        }

        return v
    }

    companion object {
        private var _lista: List<TaskReduxEntity>? = null

        fun setLista(lista: List<TaskReduxEntity>) {
            _lista = lista
        }

        private var _bIniRowHeight2 = true
        private var _bIniRowHeight3 = true
    }
}

