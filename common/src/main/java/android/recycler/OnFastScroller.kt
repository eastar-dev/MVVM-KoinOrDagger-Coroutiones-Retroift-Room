package android.recycler

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max
import kotlin.math.min

class OnFastScroller @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var bubble: TextView
    private lateinit var handle: View
    private var recyclerView: RecyclerView? = null
    private var currentAnimator: ObjectAnimator? = null

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            updateBubbleAndHandlePosition()
        }
    }

    interface BubbleTextGetter {
        fun getTextToShowInBubble(position: Int): String
    }

    init {
        orientation = HORIZONTAL
        clipChildren = false
    }

    fun setViewsToUse(@LayoutRes layoutresId: Int, @IdRes bubbleresId: Int, @IdRes handleresId: Int) {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(layoutresId, this, true)
        bubble = findViewById(bubbleresId)
        bubble.visibility = View.INVISIBLE
        handle = findViewById(handleresId)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
//        height = h
        updateBubbleAndHandlePosition()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return super.onTouchEvent(event)
        val action = event.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                if (event.x < handle.x - ViewCompat.getPaddingStart(handle))
                    return false
                currentAnimator?.cancel()
                if (bubble.visibility == View.INVISIBLE)
                    showBubble()
                handle.isSelected = true
                val y = event.y
                setBubbleAndHandlePosition(y)
                setRecyclerViewPosition(y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val y = event.y
                setBubbleAndHandlePosition(y)
                setRecyclerViewPosition(y)
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                handle.isSelected = false
                hideBubble()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    fun setRecyclerView(recyclerView: RecyclerView?) {
        if (this.recyclerView !== recyclerView) {
            this.recyclerView?.removeOnScrollListener(onScrollListener)
            this.recyclerView = recyclerView
            this.recyclerView?.addOnScrollListener(onScrollListener)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        recyclerView?.removeOnScrollListener(onScrollListener)
        recyclerView = null
    }

    private fun setRecyclerViewPosition(y: Float) {
        recyclerView?.run {
            val itemCount = adapter!!.itemCount
            val proportion: Float = when {
                handle.y == 0f -> 0f
                handle.y + handle.height >= height - TRACK_SNAP_RANGE -> 1f
                else -> y / height.toFloat()
            }
            val targetPos = getValueInRange(0, itemCount - 1, (proportion * itemCount.toFloat()).toInt())
            (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(targetPos, 0)
            val bubbleText = (recyclerView!!.adapter as BubbleTextGetter).getTextToShowInBubble(targetPos)
            bubble.text = bubbleText
        }
    }

    private fun getValueInRange(min: Int, max: Int, value: Int): Int {
        val minimum = max(min, value)
        return min(minimum, max)
    }

    private fun updateBubbleAndHandlePosition() {
        if (handle.isSelected)
            return

        recyclerView?.run {
            val verticalScrollOffset = computeVerticalScrollOffset()
            val verticalScrollRange = computeVerticalScrollRange()
            val proportion = verticalScrollOffset.toFloat() / (verticalScrollRange.toFloat() - height)
            setBubbleAndHandlePosition(height * proportion)
        }
    }

    private fun setBubbleAndHandlePosition(y: Float) {
        val handleHeight = handle.height
        handle.y = getValueInRange(0, height - handleHeight, (y - handleHeight / 2).toInt()).toFloat()

        val bubbleHeight = bubble.height
        bubble.y = getValueInRange(0, height - bubbleHeight - handleHeight / 2, (y - bubbleHeight).toInt()).toFloat()
    }

    private fun showBubble() {
        bubble.visibility = View.VISIBLE
        currentAnimator?.cancel()
        currentAnimator = ObjectAnimator.ofFloat(bubble, "alpha", 0F, 1F).setDuration(BUBBLE_ANIMATION_DURATION.toLong())
        currentAnimator!!.start()
    }

    private fun hideBubble() {
        currentAnimator?.cancel()
        currentAnimator = ObjectAnimator.ofFloat(bubble, "alpha", 1F, 0F).setDuration(BUBBLE_ANIMATION_DURATION.toLong())
        currentAnimator!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                bubble.visibility = View.INVISIBLE
                currentAnimator = null
            }

            override fun onAnimationCancel(animation: Animator) {
                super.onAnimationCancel(animation)
                bubble.visibility = View.INVISIBLE
                currentAnimator = null
            }
        })
        currentAnimator!!.start()
    }

    companion object {
        private val BUBBLE_ANIMATION_DURATION = 100
        private val TRACK_SNAP_RANGE = 5
    }
}
