package com.prolificinteractive.anchoredbehavior

import android.support.design.widget.CoordinatorLayout
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator

/**
 * Behavior for a view to be anchored and animated according to a moving dependency.
 *
 * @param interpolator Interpolator for the animation.
 * @param animationHeight Height that the animation should be perform between.
 */
open class AnchoredBehavior<T : View>(
    private val animationHeight: Int,
    var interpolator: Interpolator = DecelerateInterpolator(2f)
) : CoordinatorLayout.Behavior<T>() {

  private var prev: Int = 0

  override fun onDependentViewChanged(
      parent: CoordinatorLayout,
      child: T,
      dependency: View
  ): Boolean {

    val diffWidth = parent.width - dependency.width
    val coefY = coefficient(parent.height - child.height, child.top)
    val added = applyCoefWithInterpolation(diffWidth, coefY)

    if (added != prev) {
      // New added size is different, button size should change.
      prev = added
      animate(parent, child, dependency, added, coefY)
      return true
    }
    return false
  }

  /**
   * Animate the child view.
   *
   * @param added Margin of the button calculated based on the coefficient.
   * @param coefficient Coefficient to apply based on the Y position of the dependency.
   */
  open fun animate(
      parent: CoordinatorLayout,
      child: T,
      dependency: View,
      added: Int,
      coefficient: Float
  ) {
    val params = child.layoutParams as CoordinatorLayout.LayoutParams
    val dependencyParams = dependency.layoutParams as ViewGroup.MarginLayoutParams

    // Add margin to layout.
    params.width = parent.width - added

    params.leftMargin = applyCoefWithInterpolation(dependencyParams.leftMargin, coefficient)
    params.rightMargin = applyCoefWithInterpolation(dependencyParams.rightMargin, coefficient)

    child.layoutParams = params
  }

  /**
   * Apply an interpolation to a coefficient and get the result value once the coefficient applied.
   *
   * @param value The value to apply the coefficient to.
   * @param coefficient The coefficient input for the interpolation.
   * @return Return the value with coefficient and interpolation applied.
   */
  fun applyCoefWithInterpolation(value: Int, coefficient: Float): Int {
    // The interpolation output needs to be reversed.
    return value - (value * (1f - interpolator.getInterpolation(1f - coefficient))).toInt()
  }

  /**
   * Calculate coefficient based on position of the view ansd the dependency. It's also using the
   * [:animationHeight][AnchoredBehavior] for the height that the button should animate.
   *
   * @param childDefaultYPosition Child position on the Y axis.
   * @param dependencyYPosition Dependency position on the Y axis.
   * @return Coefficient from 0 to 1 (0 when the button is all the way at the bottom)
   */
  private fun coefficient(childDefaultYPosition: Int, dependencyYPosition: Int): Float {
    val diff = childDefaultYPosition - dependencyYPosition

    if (diff < 0) {
      return 1.0f
    }
    val coef = animationHeight - diff
    return if (coef < 0.0) {
      0.0f
    } else coef / animationHeight.toFloat()
  }
}