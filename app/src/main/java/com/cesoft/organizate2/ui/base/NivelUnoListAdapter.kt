package com.cesoft.organizate2.ui.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.ImageButton
import android.widget.TextView
import com.cesoft.organizate2.R
import com.cesoft.organizate2.entity.TaskReduxEntity
import com.cesoft.organizate2.util.Log
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by ccasanova on 29/05/2018
 */
class NivelUnoListAdapter(private val _context: Context,
                          private val _topExpList: ExpandableListView,
                          lista: List<TaskReduxEntity>)
    : BaseExpandableListAdapter() {

    private val _lista: List<TaskReduxEntity> = TaskReduxEntity.filterByLevel(lista, TaskReduxEntity.LEVEL1)
    private val _inflater = LayoutInflater.from(_context)
    private val _listViewCache = ArrayList<CexpandableListView>()

    init {
        NivelDosListAdapter.setLista(_lista)
    }

    //______________________________________________________________________________________________
    override fun hasStableIds(): Boolean {
        return true
    }

    //______________________________________________________________________________________________
    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    //______________________________________________________________________________________________
    override fun onGroupCollapsed(groupPosition: Int) {}

    //______________________________________________________________________________________________
    override fun onGroupExpanded(groupPosition: Int) {}

    //______________________________________________________________________________________________
    override fun getGroup(groupPosition: Int): Any {
        return _lista[groupPosition].name
    }

    //______________________________________________________________________________________________
    override fun getChild(groupPosition: Int, childPosition: Int): TaskReduxEntity? {
        return _lista[groupPosition].Childs[childPosition]
    }

    //______________________________________________________________________________________________
    override fun getGroupCount(): Int {
        return _lista.size
    }

    //______________________________________________________________________________________________
    override fun getChildrenCount(groupPosition: Int): Int {
        return 1
    }

    //______________________________________________________________________________________________
    override fun getGroupId(groupPosition: Int): Long {
        return (groupPosition * 1024).toLong()  // To be consistent with getChildId
    }

    //______________________________________________________________________________________________
    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return (groupPosition * 1024 + childPosition).toLong()  // Max 1024 children per group
    }


    //______________________________________________________________________________________________
    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        val dev = CexpandableListView(_context)
        dev.setRows(calculateRowCount(groupPosition, null))
        dev.setAdapter(NivelDosListAdapter(
                _context,
                createGroupList(groupPosition), // groupData describes the first-level entries
                R.layout.nivel2, // Layout for the first-level entries
                arrayOf(NIVEL2), // Key in the groupData maps to display
                intArrayOf(R.id.txtNivel2), // Data under "colorName" key goes into this TextView
                createChildList(groupPosition), // childData describes second-level entries
                R.layout.nivel3, // Layout for second-level entries
                arrayOf(NIVEL3), // Keys in childData maps to display
                intArrayOf(R.id.txtNivel3), // Data under the keys above go into these TextViews
                groupPosition
        ))
        dev.setOnGroupClickListener(Level2GroupExpandListener(groupPosition))
        if(_listViewCache.size <= groupPosition) {
            _listViewCache.add(dev)
            Log.e("NivelUno", "a---------------------------------${_listViewCache.size}-------------------------$groupPosition")
        }
        else {
            _listViewCache[groupPosition] = dev
            Log.e("NivelUno", "b---------------------------------${_listViewCache.size}-------------------------$groupPosition")
        }
        return dev
    }

    //______________________________________________________________________________________________
    private fun createGroupList(seccion: Int): List<Map<String, *>> {
        val result = ArrayList<Map<String, *>>()
        for(o in _lista[seccion].Childs) {
            val m = HashMap<String, String>()
            m[NIVEL2] = o.name
            result.add(m)
        }
        return result
    }

    //______________________________________________________________________________________________
    private fun createChildList(seccion: Int): List<List<Map<String, String>>> {
        val result = ArrayList<List<Map<String, String>>>()
        for(o in _lista[seccion].Childs) {
            val secList = ArrayList<HashMap<String, String>>()
            for(o2 in o.Childs) {
                val child = HashMap<String, String>()
                child[NIVEL3] = o2.name
                secList.add(child)
            }
            result.add(secList)
        }
        return result.toList()
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    internal inner class Level2GroupExpandListener(private val level1GroupPosition: Int) : ExpandableListView.OnGroupClickListener {
        override fun onGroupClick(parent: ExpandableListView, v: View, groupPosition: Int, id: Long): Boolean {
            if (parent.isGroupExpanded(groupPosition))
                parent.collapseGroup(groupPosition)
            else
                parent.expandGroup(groupPosition)
            if(parent is CexpandableListView) {
                parent.setRows(calculateRowCount(level1GroupPosition, parent))
            }
            _topExpList.requestLayout()
            return true
        }
    }

    //______________________________________________________________________________________________
    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        val v: View = convertView ?: _inflater.inflate(R.layout.nivel1, parent, false)
        val gt = getGroup(groupPosition).toString()
        val colorGroup = v.findViewById(R.id.txtNivel1) as TextView
        //if (gt != null)
            colorGroup.text = gt

        /// NIVEL 1 --------------------------------------------------------------------------------
        val btnEditar = v.findViewById(R.id.btnEditar) as ImageButton
        btnEditar.setOnClickListener {
            Log.e("NivelUno", " ON CLICK ***********1*********************************${_lista[groupPosition]}")
//            val intent = Intent(_context, ActEdit::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            intent.putExtra(TaskReduxEntity::class.java!!.getName(), _lista[groupPosition])
//            _context.startActivity(intent)
        }
        btnEditar.isFocusable = false//NO HACE CASO EN LAYOUT XML*/
        return v
    }

    //______________________________________________________________________________________________
    // Calculates the rows counts
    private fun calculateRowCount(level1: Int, level2view: ExpandableListView?): IntArray {
        val rowCtr = intArrayOf(0, 0, 0)
        if (level2view == null) {
            rowCtr[1] += _lista[level1].Childs.size
        }
        else {
            ++rowCtr[0]
            val ao = _lista[level1].Childs
            for(j in ao.indices) {
                ++rowCtr[1]
                if (level2view.isGroupExpanded(j))
                    rowCtr[2] += ao[j].Childs.size
            }
        }
        //Log.e(TAG,"calculateRowCount---------------------" + level1 + " / " + level2view + "------------" + rowCtr[0]+":"+rowCtr[1]+":"+rowCtr[2] + "::::" + (level2view != null ? level2view.getCount() : 0));
        return rowCtr
    }

    companion object {
        private val NIVEL2 = "NIVEL2"
        private val NIVEL3 = "NIVEL3"
    }
}
