package com.cesoft.organizate2.ui.base

import android.content.Context
import android.widget.ExpandableListView

/**
 * Created by ccasanova on 29/05/2018
 */
class CexpandableListView(context: Context) : ExpandableListView(context) {
    private var rows2: Int = 0
    private var rows3: Int = 0

    fun setRows(rows: IntArray) {
        this.rows2 = rows[1]
        this.rows3 = rows[2]
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //Log.e("CesExpaListView", "AAA" + "-----------" + rows2 + " (" + ROW_HEIGHT2 + ")..." + rows3 + " (" + ROW_HEIGHT3 + ") ------" + (rows2 * ROW_HEIGHT2 + rows3 * ROW_HEIGHT3) + "---" + heightMeasureSpec + "::::" + bottom)
        setMeasuredDimension(measuredWidth, rows2 * ROW_HEIGHT2 + rows3 * ROW_HEIGHT3)
    }

    /*override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }*/

    companion object {
        private var ROW_HEIGHT2 = 100
        fun setRowHeight2(v: Int) {
            ROW_HEIGHT2 = v + 1
            ROW_HEIGHT3 = v + 1
        }//TODO:Obtener la dimension 3 antes de mostrar

        private var ROW_HEIGHT3 = 100
        fun setRowHeight3(v: Int) {
            ROW_HEIGHT3 = v + 1
        }
    }
}
