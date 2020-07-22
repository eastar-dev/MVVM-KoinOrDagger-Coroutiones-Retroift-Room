@file:Suppress("LocalVariableName", "unused")

package android.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class DiffBindingDataArrayAdapter @JvmOverloads constructor(private vararg var diffTypes: DiffBindingDataType, items: List<Any> = listOf()) :
    BindingViewArrayAdapter<ViewDataBinding, Any>(0, items) {

    override fun getItemViewType(position: Int): Int {
        val d = getItem(position)

        if (d is DiffArrayAdapter.DiffData)
            return d.getItemViewType()

        val clz = getItem(position).javaClass
        diffTypes.forEachIndexed { index, diffType ->
            if (diffType.data_clz == clz)
                return index
        }
        return 0
    }

    override fun getItemView(@LayoutRes layer: Int, parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(diffTypes[viewType].layer_id, parent, false)
    }

    override fun onBindViewHolder(bb: ViewDataBinding, d: Any, holder: RecyclerView.ViewHolder, position: Int) {
        val brId: Int = diffTypes[holder.itemViewType].brId
        bb.setVariable(brId, d)
        bb.executePendingBindings()
    }

    //-----------------------------------------------------------------------------
    data class DiffBindingDataType(@LayoutRes var layer_id: Int, var data_clz: Class<*>, var brId: Int)
}