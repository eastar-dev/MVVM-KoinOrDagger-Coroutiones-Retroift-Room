package dev.eastar.branch.ui

import android.app.Activity
import android.content.SharedPreferences
import android.log.Log
import android.os.Bundle
import android.recycler.BindingAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.eastar.branch.R
import dev.eastar.branch.data.BranchDao
import dev.eastar.branch.data.BranchEntity
import dev.eastar.branch.data.icon
import dev.eastar.branch.data.intent
import dev.eastar.branch.databinding.BranchListBinding
import dev.eastar.branch.databinding.BranchListItemBinding
import dev.eastar.branch.presentation.BranchViewModel
import org.koin.android.ext.android.inject

import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.java.KoinJavaComponent.inject
import smart.base.BFragment

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class BranchList : BFragment() {
    private var mCenterItem: BranchEntity? = null

    //    private val vm: BranchViewModel by inject() //not same!!
//    private val vm by sharedViewModel<BranchViewModel>()
    private lateinit var bb: BranchListBinding
    private val vm: BranchViewModel by sharedViewModel()
    private val adapter by lazyOf(DataAdapter())
//    private var 반경설정선택값 = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return BranchListBinding.inflate(inflater, container, false).apply { viewModel = vm }.also { bb = it }.root
    }

    override fun onLoadOnce() {
        adapter.setOnItemClickListener { parent, view, position, data ->
            parent.visibility = View.GONE
            data?.let { selectedItem(it) }
        }

//        adapter.setOnItemClickListener(object : BindingAdapter.OnItemClickListener<BranchEntity> {
//            override fun onItemClick(parent: RecyclerView, view: View, position: Int, data: BranchEntity?) {
//                parent.visibility = View.GONE
//                data?.let { selectedItem(it) }
//            }
//        })
    }

//    val database : BranchDatabase by inject()
    val dao : BranchDao by inject()
//    val db : BranchDBSource by inject()

    override fun onLoad() {
        Log.e("vm.getBranch()")
        vm.getBranch()
    }

    //    @Injecter.Injected
    fun showBranch(items: List<BranchEntity>) {
        adapter.addAll(items)
    }

    fun selectedItem(item: BranchEntity) {
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, item.intent)
    }

    fun setCenterItem(): Boolean {
        mCenterItem ?: return false
        Log.e("이동위치있음", mCenterItem)
        selectedItem(mCenterItem!!)
        return true
    }

    inner class DataAdapter : BindingAdapter<BranchListItemBinding, BranchEntity>(R.layout.branch_list_item) {
        override fun onBindViewHolder(bb: BranchListItemBinding, d: BranchEntity?, holder: RecyclerView.ViewHolder, position: Int) {

            d?.let {
                Glide.with(this@BranchList).load(d.icon).into(bb.icon)
                bb.name.text = it.name
                bb.distance.text = it.distance_text
            }
        }
    }
}
