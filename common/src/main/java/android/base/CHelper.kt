package android.base

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle

class CHelper

fun CFragment.showProgress() = (requireActivity() as CActivity).showProgress()
fun CFragment.dismissProgress() = (requireActivity() as CActivity).dismissProgress()
fun CFragment.dismissProgressForce() = (requireActivity() as CActivity).dismissProgressForce()

fun CActivity.createProgress() = with(AlertDialog.Builder(this)) {
    setCancelable(true)
    setView(ProgressBar(context, null, android.R.attr.progressBarStyleLarge))
    create().apply {
        window?.setBackgroundDrawable(ColorDrawable(0x00ff0000))
        setCanceledOnTouchOutside(false)
    }
}

fun CActivity.showProgress() {
    if (lifecycle.currentState == Lifecycle.State.DESTROYED) return
    if (isFinishing) return
    progress.takeUnless { progress.isShowing }?.show()
}

fun CActivity.dismissProgress() = progress.takeIf { progress.isShowing }?.dismiss()
fun CActivity.dismissProgressForce() = dismissProgress()

@JvmOverloads
fun CActivity.showDialog(message: Any,
                         positiveButtonText: Any? = null, positiveListener: DialogInterface.OnClickListener? = null,
                         negativeButtonText: Any? = null, negativeListener: DialogInterface.OnClickListener? = null,
                         neutralButtonText: Any? = null, neutralListener: DialogInterface.OnClickListener? = null) =
        showDialog(null, null, message, null
                , positiveButtonText, positiveListener
                , negativeButtonText, negativeListener
                , neutralButtonText, neutralListener
                , null
        )

@JvmOverloads
fun CFragment.showDialog(message: Any,
                         positiveButtonText: Any? = null, positiveListener: DialogInterface.OnClickListener? = null,
                         negativeButtonText: Any? = null, negativeListener: DialogInterface.OnClickListener? = null,
                         neutralButtonText: Any? = null, neutralListener: DialogInterface.OnClickListener? = null) =
        showDialog(null, null, message, null
                , positiveButtonText, positiveListener
                , negativeButtonText, negativeListener
                , neutralButtonText, neutralListener
                , null
        )

fun CFragment.showDialog(icon: Any? = null, title: Any? = null, message: Any? = null, view: Any? = null,
                         positiveButtonText: Any? = null, positiveListener: DialogInterface.OnClickListener? = null,
                         negativeButtonText: Any? = null, negativeListener: DialogInterface.OnClickListener? = null,
                         neutralButtonText: Any? = null, neutralListener: DialogInterface.OnClickListener? = null,
                         dismissListener: DialogInterface.OnDismissListener? = null): Dialog =
        (requireActivity() as CActivity).showDialog(icon, title, message, view,
                positiveButtonText, positiveListener,
                negativeButtonText, negativeListener,
                neutralButtonText, neutralListener,
                dismissListener)

fun CActivity.showDialog(icon: Any? = null, title: Any? = null, message: Any? = null, view: Any? = null,
                         positiveButtonText: Any? = null, positiveListener: DialogInterface.OnClickListener? = null,
                         negativeButtonText: Any? = null, negativeListener: DialogInterface.OnClickListener? = null,
                         neutralButtonText: Any? = null, neutralListener: DialogInterface.OnClickListener? = null,
                         dismissListener: DialogInterface.OnDismissListener? = null): Dialog = getDialog(icon, title, message, view,
        positiveButtonText, positiveListener,
        negativeButtonText, negativeListener,
        neutralButtonText, neutralListener,
        dismissListener).apply {
    if (lifecycle.currentState == Lifecycle.State.DESTROYED) return@apply
    if (isFinishing) return@apply
    show()
}

fun Context.getDialog(icon: Any?, title: Any?, message: Any?, view: Any?,
                      positiveButtonText: Any?, positiveListener: DialogInterface.OnClickListener?,
                      negativeButtonText: Any?, negativeListener: DialogInterface.OnClickListener?,
                      neutralButtonText: Any?, neutralListener: DialogInterface.OnClickListener?,
                      onDismissListener: DialogInterface.OnDismissListener?
): Dialog {
    return AlertDialog.Builder(this).apply {

        (title as? CharSequence)?.let { setTitle(it) }
        (title as? @StringRes Int)?.let { setTitle(it) }

        (message as? CharSequence)?.let { setMessage(it) }
        (message as? @StringRes Int)?.let { setMessage(it) }

        (positiveButtonText as? CharSequence)?.let { setPositiveButton(it, positiveListener) }
        (positiveButtonText as? @StringRes Int)?.let { setPositiveButton(it, positiveListener) }
        (negativeButtonText as? CharSequence)?.let { setNegativeButton(it, negativeListener) }
        (negativeButtonText as? @StringRes Int)?.let { setNegativeButton(it, negativeListener) }
        (neutralButtonText as? CharSequence)?.let { setNeutralButton(it, neutralListener) }
        (neutralButtonText as? @StringRes Int)?.let { setNeutralButton(it, neutralListener) }

        (view as? View)?.let { setView(it) }
        (view as? @LayoutRes Int)?.let { setView(it) }

        (icon as? Drawable)?.let { setIcon(it) }
        (icon as? @LayoutRes Int)?.let { setIcon(it) }

        (title as? CharSequence)?.let { setOnDismissListener(onDismissListener) }
    }.create()
}