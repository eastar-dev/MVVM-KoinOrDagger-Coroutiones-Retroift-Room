package dev.eastar.branch.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.eastar.branch.R
import eastar.base.BFragment

class BranchMain : BFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.branch_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentManager?.run {
            beginTransaction()
                    .replace(R.id.branch_container, BranchMap())
                    .commitAllowingStateLoss()
        }
//        fragmentManager?.run {
//            beginTransaction()
//                    .replace(R.id.branch_container, BranchList())
//                    .commitAllowingStateLoss()
//        }
    }
}
