package android.base

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle

class CProgressDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return runCatching {
            with(AlertDialog.Builder(requireContext())) {
                setCancelable(true)
                setView(ProgressBar(context, null, android.R.attr.progressBarStyleLarge))
                create().apply {
                    window?.setBackgroundDrawable(ColorDrawable(0x00ff0000))
                    setCanceledOnTouchOutside(false)
                }
            }
        }.getOrThrow()
    }

    companion object {
        const val TAG = "PROGRESS_DIALOG"
    }
}

fun CActivity.createProgress() = CProgressDialog()

fun CActivity.showProgress() {
    if (lifecycle.currentState == Lifecycle.State.DESTROYED) return
    if (isFinishing) return
    val progress: DialogFragment = supportFragmentManager.findFragmentByTag(CProgressDialog.TAG) as DialogFragment
    if (progress.isVisible)
        return
    progress.show(supportFragmentManager, CProgressDialog.TAG)
}

fun CActivity.dismissProgress() {
    val progress: DialogFragment = supportFragmentManager.findFragmentByTag(CProgressDialog.TAG) as DialogFragment
    if (progress.isVisible)
        progress.dismiss()
}

fun CActivity.dismissProgressForce() = dismissProgress()

fun CFragment.createProgress(): DialogFragment = CProgressDialog()

fun CFragment.showProgress() {
    if (lifecycle.currentState == Lifecycle.State.DESTROYED) return
    val progress: DialogFragment = childFragmentManager.findFragmentByTag(CProgressDialog.TAG) as DialogFragment
    if (progress.isVisible)
        return
    progress.show(childFragmentManager, CProgressDialog.TAG)
}

fun CFragment.dismissProgress() {
    val progress: DialogFragment = childFragmentManager.findFragmentByTag(CProgressDialog.TAG) as DialogFragment
    if (progress.isVisible)
        progress.dismiss()
}

fun CFragment.dismissProgressForce() = dismissProgress()

