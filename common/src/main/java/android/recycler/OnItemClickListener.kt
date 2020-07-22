package android.recycler

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.SoundEffectConstants
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView

class OnItemClickListener(context: Context, private val mListener: OnItemClickListener?) : RecyclerView.OnItemTouchListener {
    var mGestureDetector: GestureDetectorCompat = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent?) = true
    })

    override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
        val childView = view.findChildViewUnder(e.x, e.y)
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildLayoutPosition(childView))
            childView.playSoundEffect(SoundEffectConstants.CLICK)
            return true
        }
        return false
    }

    override fun onTouchEvent(view: RecyclerView, motionEvent: MotionEvent) {}
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }
}