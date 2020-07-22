package android.recycler

import android.recycler.BindingViewArrayAdapter.Holder
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BindingViewArrayAdapter<B : ViewDataBinding, VD : Any> @JvmOverloads constructor(@LayoutRes layoutResId: Int, items: List<VD> = listOf()) : ArrayAdapter<Holder<B>, VD>(layoutResId, items) {

    class Holder<B : ViewDataBinding>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var bb: B

        init {
            runCatching { bb = DataBindingUtil.bind(itemView)!! }.onFailure { itemView.context.javaClass.name }
        }
    }

    override fun getHolder(itemView: View, viewType: Int): Holder<B> {
        return Holder(itemView)
    }

    override fun onBindViewHolder(h: Holder<B>, d: VD, position: Int) {
        runCatching { onBindViewHolder(h.bb, d, h, position) }.onFailure { h.itemView.context.javaClass.name }
    }

    abstract fun onBindViewHolder(bb: B, d: VD, holder: RecyclerView.ViewHolder, position: Int)
}
