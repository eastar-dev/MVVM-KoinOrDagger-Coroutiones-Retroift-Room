@file:Suppress("unused")

package android.recycler

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BindingDataArrayAdapter(@LayoutRes layoutResId: Int, var item_brId: Int, items: List<Any> = listOf()) : BindingViewArrayAdapter<ViewDataBinding, Any>(layoutResId, items) {
    override fun onBindViewHolder(bb: ViewDataBinding, d: Any, holder: RecyclerView.ViewHolder, position: Int) {
        bb.setVariable(item_brId, d)
    }
}