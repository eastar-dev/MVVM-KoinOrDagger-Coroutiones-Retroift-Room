package android.recycler

import android.log.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import java.util.*

@Suppress("unused", "UNUSED_PARAMETER", "MemberVisibilityCanBePrivate")
abstract class BindingAdapter<B : ViewDataBinding, VD> : RecyclerView.Adapter<Holder> {

    @LayoutRes
    private var mItemLayer: Int = 0
    private var mObjects: MutableList<VD?> = arrayListOf()
    private var mLayoutInflater: LayoutInflater? = null
    //----------------------------------------------------------------------------------
    private var mOnItemClickListener: OnItemClickListener<VD>? = null
    private var mOnItemClickCallback: ((parent: RecyclerView, view: View, position: Int, data: VD?) -> Unit)? = null
    private val mLock = Any()

    constructor(@LayoutRes itemLayer: Int) {
        mItemLayer = itemLayer
    }

    constructor(@LayoutRes itemLayer: Int, datas: Array<VD>) {
        mItemLayer = itemLayer
        mObjects.clear()
        mObjects.addAll(datas)
    }

    constructor(@LayoutRes itemLayer: Int, datas: Collection<VD>) {
        mItemLayer = itemLayer
        mObjects.clear()
        mObjects.addAll(datas)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<VD>) {
        mOnItemClickListener = onItemClickListener
    }

    fun setOnItemClickListener(onItemClickListener: (parent: RecyclerView, view: View, position: Int, data: VD?) -> Unit) {
        mOnItemClickCallback = onItemClickListener
    }

    interface OnItemClickListener<VD> {
        fun onItemClick(parent: RecyclerView, view: View, position: Int, data: VD?)
    }

    //----------------------------------------------------------------------------------
    fun inflate(parent: ViewGroup, viewType: Int): View {
        if (mLayoutInflater == null)
            mLayoutInflater = LayoutInflater.from(parent.context)
        return mLayoutInflater!!.inflate(mItemLayer, parent, false)
    }

    private fun setOnItemClick(parent: ViewGroup, itemView: View) {
        itemView.setOnClickListener { v ->
            val position = (parent as RecyclerView).getChildLayoutPosition(v)
            mOnItemClickListener?.onItemClick(parent, itemView, position, getItem(position))
            mOnItemClickCallback?.invoke(parent, itemView, position, getItem(position))
        }

//        if (mOnItemClickListener != null) {
//            itemView.setOnClickListener { v ->
//                val position = (parent as RecyclerView).getChildLayoutPosition(v)
//                mOnItemClickListener?.onItemClick(parent, itemView, position, getItem(position))
//            }
//        } else {
//            itemView.setOnClickListener(null)
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val itemView = inflate(parent, viewType)
        setOnItemClick(parent, itemView)
        return Holder(itemView)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        onBindViewHolder(DataBindingUtil.bind(holder.itemView)!!, getItem(position), holder, position)
    }

    abstract fun onBindViewHolder(bb: B, d: VD?, holder: RecyclerView.ViewHolder, position: Int)
    //----------------------------------------------------------
    override fun getItemCount(): Int {
        return mObjects.size
    }

    fun getItem(position: Int): VD? {
        return mObjects[position]
    }
    //----------------------------------------------------------

    fun add(item: VD?) {
        synchronized(mLock) {
            mObjects.add(item)
        }
        notifyItemInserted(mObjects.size)
    }

    fun addAll(collection: Collection<VD>) {
        val size = mObjects.size
        synchronized(mLock) {
            mObjects.addAll(collection)
        }
        notifyItemRangeInserted(size, mObjects.size - size)
    }

    //    public final void add(VD... items) {
    //        synchronized (mLock) {
    //            Collections.addAll(mObjects, items);
    //        }
    //        notifyDataSetChanged();
    //    }

    fun set(collection: Collection<VD>?) {
        synchronized(mLock) {
            mObjects.clear()
            collection?.let { mObjects.addAll(it) }

        }
        notifyDataSetChanged()
    }

    //    public void set(VD... items) {
    //        synchronized (mLock) {
    //            mObjects.clear();
    //            Collections.addAll(mObjects, items);
    //        }
    //        notifyDataSetChanged();
    //    }

    fun insert(item: VD?, position: Int) {
        synchronized(mLock) {
            mObjects.add(position, item)
        }
        notifyItemInserted(position)
    }

    //    public void remove(@Nullable VD object, int position) {
    //        boolean success;
    //        Log.w(mObjects.indexOf(object), position);
    //        synchronized (mLock) {
    //            success = mObjects.remove(object);
    //        }
    //        if (success)
    //            notifyItemRemoved(position);
    //    }

    fun remove(position: Int) {
        Log.w(position)
        if (!(0 <= position && position < mObjects.size))
            return
        synchronized(mLock) {
            mObjects.removeAt(position)
        }
        notifyItemRemoved(position)
    }

    fun clear() {
        synchronized(mLock) {
            mObjects.clear()
        }
        notifyDataSetChanged()
    }

    fun sort(comparator: Comparator<in VD?>?) {
        synchronized(mLock) {
            Collections.sort(mObjects, comparator)
        }
        notifyDataSetChanged()
    }
}

class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

//        private class NothingHolder extends DiffHolder {
//            public NothingHolder(View itemView) { super(itemView); }
//            @Override
//            public void bind(Object d) { }
//        }
