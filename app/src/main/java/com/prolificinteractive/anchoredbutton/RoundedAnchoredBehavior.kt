package com.prolificinteractive.anchoredbutton

import android.graphics.drawable.GradientDrawable
import android.support.design.widget.CoordinatorLayout
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.prolificinteractive.anchoredbehavior.AnchoredBehavior

class RoundedAnchoredBehavior(
    animationHeight: Int,
    private val cornerRadiusMax: Int
) : AnchoredBehavior<Button>(animationHeight) {
  override fun animate(
      parent: CoordinatorLayout,
      child: Button,
      dependency: View,
      added: Int,
      coefficient: Float) {

    val params = child.layoutParams as CoordinatorLayout.LayoutParams
    val dependencyParams = dependency.layoutParams as ViewGroup.MarginLayoutParams
    val background: GradientDrawable


    if (child.background is GradientDrawable) {
      background = child.background as GradientDrawable
    } else {
      background = GradientDrawable()
      background.setColor(ContextCompat.getColor(child.context, R.color.colorAccent))
    }

    // Add margin to layout.
    params.width = parent.width - added

    params.leftMargin = applyCoefWithInterpolation(dependencyParams.leftMargin, coefficient)
    params.rightMargin = applyCoefWithInterpolation(dependencyParams.rightMargin, coefficient)

    // Change corner radius of background.
    background.cornerRadius = applyCoefWithInterpolation(cornerRadiusMax, coefficient).toFloat()

    child.layoutParams = params
    child.background = background
  }
}