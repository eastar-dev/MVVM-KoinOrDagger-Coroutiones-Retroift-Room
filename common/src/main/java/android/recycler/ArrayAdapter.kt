package android.recycler

import android.log.Log
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

@Suppress("unused")
abstract class ArrayAdapter<VH : RecyclerView.ViewHolder, VD> @JvmOverloads constructor(@LayoutRes val layoutId: Int, val items: List<VD> = listOf()) : RecyclerView.Adapter<VH>() {

    private var objects: MutableList<VD> = items.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val itemView = getItemView(layoutId, parent, viewType)
        setOnItemClickListener(parent, itemView)
        return getHolder(itemView, viewType)
    }

    private fun setOnItemClickListener(parent: ViewGroup, itemView: View) {
        itemView.setOnClickListener { v ->
            val position = (parent as RecyclerView).getChildLayoutPosition(v)
            onItemClick(parent, itemView, position, getItem(position))
            onItemClickListener?.invoke(parent, itemView, position, getItem(position))
        }
    }

    open fun onItemClick(parent: RecyclerView, itemView: View, position: Int, item: VD) = Log.e("[clicled item] $position, $item")

    protected open fun getItemView(@LayoutRes layer: Int, parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(layer, parent, false)
    }

    abstract fun getHolder(itemView: View, viewType: Int): VH

    override fun onBindViewHolder(holder: VH, position: Int) {
        onBindViewHolder(holder, objects[position], position)
    }

    abstract fun onBindViewHolder(h: VH, d: VD, position: Int)

    override fun getItemCount(): Int {
        return objects.size
    }

    fun getItem(position: Int): VD {
        return objects[position]
    }


    //----------------------------------------------------------------------------------
    private val lock = Any()

    fun set(collection: Collection<VD>?) {
        synchronized(lock) {
            objects.clear()
            if (collection == null)
                return
            objects.addAll(collection)
        }
        notifyDataSetChanged()
    }

    fun add(data: VD) {
        synchronized(lock) {
            objects.add(data)
        }
        notifyItemInserted(objects.size)
    }

    fun add(index: Int, item: VD) {
        Log.e(index, item)
        if (index !in 0..objects.size)
            return

        synchronized(lock) {
            objects.add(index, item)
        }
        notifyItemInserted(index)
    }

    fun addAll(collection: Collection<VD>?) {
        if (collection == null)
            return
        val last = objects.size
        synchronized(lock) {
            objects.addAll(collection)
        }
        notifyItemRangeInserted(last, collection.size)
    }

    fun addAll(index: Int, collection: Collection<VD>?) {
        if (index !in 0..objects.size)
            return

        if (collection == null)
            return
        synchronized(lock) {
            objects.addAll(index, collection)
        }
        notifyItemRangeInserted(index, collection.size)
    }

    fun remove(index: Int) {
        Log.w(index)
        if (index !in 0 until objects.size)
            return
        synchronized(lock) {
            objects.removeAt(index)
        }
        notifyItemRemoved(index)
    }

    fun remove(index: Int, count: Int) {
        if (index !in 0 until objects.size) {
            Log.w("!index is must in 0 until objects.size  Current index is [$index]")
            return
        }
        if (count > objects.size) {
            Log.w("!count is must count <= objects.size  Current objects.size is [objects.size] Current index is [$index]")
            return
        }
        Log.w(index, count)
        synchronized(lock) {
            repeat(count) {
                objects.removeAt(index)
            }
        }
        notifyItemRangeRemoved(index, count)
    }

    fun move(fromPosition: Int, toPosition: Int, notifyItemChange: Boolean = false) {
        if (fromPosition !in 0 until objects.size) {
            Log.w("!fromPosition is must in 0..objects.size  Current fromPosition is [$fromPosition]")
            return
        }
        if (toPosition !in 0 until objects.size) {
            Log.w("!toPosition is must in 0..objects.size  Current toPosition is [$toPosition]")
            return
        }
//        Log.w("$fromPosition -> $toPosition")
        repeat(max(fromPosition, toPosition) - min(fromPosition, toPosition)) {
            val sign = (toPosition - fromPosition).sign
            val start = fromPosition + sign * it
            val end = start + sign
//            Log.e(start, end)
            synchronized(lock) {
                Collections.swap(objects, start, end)
            }
            notifyItemMoved(start, end)
        }
        if (notifyItemChange)
            notifyItemChanged(toPosition)
    }

    fun clear() {
        synchronized(lock) {
            objects.clear()
        }
        notifyDataSetChanged()
    }

    fun sort(comparator: Comparator<in VD>) {
        synchronized(lock) {
            Collections.sort(objects, comparator)
        }
        notifyDataSetChanged()
    }

    fun get(): List<VD> {
        return objects
    }

    //----------------------------------------------------------------------------------
    private var onItemClickListener: ((parent: RecyclerView, view: View, position: Int, data: VD) -> Unit)? = null

    fun setOnItemClickListener(onItemClickListener: (parent: RecyclerView, view: View, position: Int, data: VD) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

}