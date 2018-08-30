package com.prolificinteractive.anchoredbutton

import android.os.Bundle
import android.support.design.widget.CoordinatorLayout.LayoutParams
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.PopupMenu
import com.prolificinteractive.anchoredbehavior.AnchoredBehavior
import kotlinx.android.synthetic.main.activity_main.button1
import kotlinx.android.synthetic.main.activity_main.button2
import kotlinx.android.synthetic.main.activity_main.button3
import kotlinx.android.synthetic.main.activity_main.cell1
import kotlinx.android.synthetic.main.activity_main.cell2
import kotlinx.android.synthetic.main.activity_main.cell3
import kotlinx.android.synthetic.main.item_parameters_button.view.description

class MainActivity : AppCompatActivity() {
  private lateinit var behavior: AnchoredBehavior<Button>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val defaultAnimationHeight = resources.getDimensionPixelSize(R.dimen.default_animation_height)

    // Set the AnchoredBehavior like so.
    val layoutParams = button1.layoutParams as LayoutParams
    layoutParams.behavior = AnchoredBehavior<FrameLayout>(defaultAnimationHeight)
    button1.layoutParams = layoutParams

    // Button 2
    val layoutParams2 = button2.layoutParams as LayoutParams
    layoutParams2.behavior = RoundedAnchoredBehavior(
        defaultAnimationHeight,
        resources.getDimensionPixelSize(R.dimen.spacing_standard)
    )
    button2.layoutParams = layoutParams2

    // Button 3
    val layoutParams3 = button3.layoutParams as LayoutParams
    behavior = AnchoredBehavior(resources.getDimensionPixelSize(R.dimen.animation_height))
    layoutParams3.behavior = behavior
    button3.layoutParams = layoutParams3

    // Listeners
    cell1.setOnClickListener {
      button1.visibility = View.VISIBLE
      button2.visibility = View.INVISIBLE
      button3.visibility = View.INVISIBLE
    }

    cell2.setOnClickListener {
      button1.visibility = View.INVISIBLE
      button2.visibility = View.VISIBLE
      button3.visibility = View.INVISIBLE
    }

    cell3.setOnClickListener {
      button1.visibility = View.INVISIBLE
      button2.visibility = View.INVISIBLE
      button3.visibility = View.VISIBLE

      val menu = PopupMenu(this, cell3.description)
      menu.menuInflater.inflate(R.menu.menu_interpolators, menu.menu)
      menu.setOnMenuItemClickListener { item -> change(item.itemId) }
      menu.show()
    }
  }

  private fun change(i: Int): Boolean {
    when (i) {
      InterpolatorType.DECELERATE.id -> behavior.interpolator = DecelerateInterpolator()
      InterpolatorType.ACCELERATE.id -> behavior.interpolator = AccelerateInterpolator()
      InterpolatorType.OVERSHOOT.id -> behavior.interpolator = OvershootInterpolator()
      InterpolatorType.LINEAR.id -> behavior.interpolator = LinearInterpolator()
      InterpolatorType.FAST_IN_SLOW_OUT.id -> behavior.interpolator = FastOutSlowInInterpolator()
      InterpolatorType.BOUNCE.id -> behavior.interpolator = BounceInterpolator()
    }
    return true
  }
}
