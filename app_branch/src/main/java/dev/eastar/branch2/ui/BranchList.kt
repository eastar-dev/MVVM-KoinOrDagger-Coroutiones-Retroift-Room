package dev.eastar.branch2.ui

import android.log.Log
import android.os.Bundle
import android.recycler.BindingViewArrayAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.eastar.branch.R
import dev.eastar.branch.databinding.BranchListBinding
import dev.eastar.branch.databinding.BranchListItemBinding
import dev.eastar.branch2.model.BranchEntity
import dev.eastar.branch2.model.icon
import dev.eastar.branch2.ui.BranchViewModel
import eastar.base.BFragment

class BranchList : BFragment() {
    private lateinit var bb: BranchListBinding
    private val vm: BranchViewModel by activityViewModels()
    private val adapter by lazyOf(DataAdapter())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        BranchListBinding.inflate(inflater, container, false).let {
            bb = it
            it.vm = vm
            it.root
        }

    override fun onLoadOnce() {
        adapter.setOnItemClickListener { parent, view, position, data ->
            Log.e(data.toString())
//            parent.visibility = View.GONE
//            selectedItem(data)
        }
    }

//    fun showBranch(items: List<BranchEntity>) {
//        adapter.addAll(items)
//    }
//
//    fun selectedItem(item: BranchEntity) {
//        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, item.intent)
//    }

//    fun setCenterItem(): Boolean {
//        mCenterItem ?: return false
//        Log.e("이동위치있음", mCenterItem)
//        selectedItem(mCenterItem!!)
//        return true
//    }

    inner class DataAdapter : BindingViewArrayAdapter<BranchListItemBinding, BranchEntity>(R.layout.branch_list_item) {
        override fun onBindViewHolder(bb: BranchListItemBinding, d: BranchEntity, holder: RecyclerView.ViewHolder, position: Int) {
            d.let {
                Glide.with(this@BranchList).load(d.icon).into(bb.icon)
                bb.name.text = it.name
                bb.distance.text = it.distanceText
            }
        }
    }
}
