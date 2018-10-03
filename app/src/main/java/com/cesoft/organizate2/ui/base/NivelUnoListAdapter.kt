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
import com.cesoft.organizate2.entity.Task
import com.cesoft.organizate2.entity.TaskReduxEntity
import com.cesoft.organizate2.ui.list.ListViewModel
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by ccasanova on 29/05/2018
 */
class NivelUnoListAdapter(
        private val exContext: Context,
        private val viewModel: ListViewModel,
        private val topExpList: ExpandableListView,
        listaCompleta: List<TaskReduxEntity>)
    : BaseExpandableListAdapter() {

    private val lista: List<TaskReduxEntity> = TaskReduxEntity.filterByLevel(listaCompleta, Task.LEVEL1)
    private val inflater = LayoutInflater.from(exContext)
    private val listViewCache = ArrayList<CexpandableListView>()

    init {
        NivelDosListAdapter.setLista(lista)
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
        return lista[groupPosition].name
    }

    //______________________________________________________________________________________________
    override fun getChild(groupPosition: Int, childPosition: Int): TaskReduxEntity? {
        return lista[groupPosition].childs[childPosition]
    }

    //______________________________________________________________________________________________
    override fun getGroupCount(): Int {
        return lista.size
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
        val dev = CexpandableListView(exContext)
        dev.setRows(calculateRowCount(groupPosition, null))
        dev.setAdapter(NivelDosListAdapter(
                exContext, viewModel,
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
        if(listViewCache.size <= groupPosition) {
            listViewCache.add(dev)
            //Log.e("NivelUno", "a---------------------------------${listViewCache.size}-------------------------$groupPosition")
        }
        else {
            listViewCache[groupPosition] = dev
            //Log.e("NivelUno", "b---------------------------------${listViewCache.size}-------------------------$groupPosition")
        }
        return dev
    }

    //______________________________________________________________________________________________
    private fun createGroupList(seccion: Int): List<Map<String, *>> {
        val result = ArrayList<Map<String, *>>()
        for(o in lista[seccion].childs) {
            val m = HashMap<String, String>()
            m[NIVEL2] = o.name
            result.add(m)
        }
        return result
    }

    //______________________________________________________________________________________________
    private fun createChildList(seccion: Int): List<List<Map<String, String>>> {
        val result = ArrayList<List<Map<String, String>>>()
        for(o in lista[seccion].childs) {
            val secList = ArrayList<HashMap<String, String>>()
            for(o2 in o.childs) {
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
            topExpList.requestLayout()
            return true
        }
    }

    //______________________________________________________________________________________________
    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        val v: View = convertView ?: inflater.inflate(R.layout.nivel1, parent, false)
        val gt = getGroup(groupPosition).toString()
        val colorGroup = v.findViewById(R.id.txtNivel1) as TextView
        //if (gt != null)
            colorGroup.text = gt

        /// NIVEL 1 --------------------------------------------------------------------------------
        val btnEditar = v.findViewById(R.id.btnEditar) as ImageButton
        btnEditar.setOnClickListener {
            //Log.e("NivelUno", " ON CLICK ***********1*********************************${lista[groupPosition]}")
            viewModel.onClickTask(lista[groupPosition].id)
        }
        btnEditar.isFocusable = false//NO HACE CASO EN LAYOUT XML*/
        return v
    }

    //______________________________________________________________________________________________
    // Calculates the rows counts
    private fun calculateRowCount(level1: Int, level2view: ExpandableListView?): IntArray {
        val rowCtr = intArrayOf(0, 0, 0)
        if (level2view == null) {
            rowCtr[1] += lista[level1].childs.size
        }
        else {
            ++rowCtr[0]
            val ao = lista[level1].childs
            for(j in ao.indices) {
                ++rowCtr[1]
                if (level2view.isGroupExpanded(j))
                    rowCtr[2] += ao[j].childs.size
            }
        }
        //Log.e(TAG,"calculateRowCount---------------------" + level1 + " / " + level2view + "------------" + rowCtr[0]+":"+rowCtr[1]+":"+rowCtr[2] + "::::" + (level2view != null ? level2view.getCount() : 0));
        return rowCtr
    }

    companion object {
        private const val NIVEL2 = "NIVEL2"
        private const val NIVEL3 = "NIVEL3"
    }
}
