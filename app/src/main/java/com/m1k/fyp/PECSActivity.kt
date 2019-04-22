package com.m1k.fyp

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_pecs.*

class PECSActivity : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    override fun onResume() {
        if (GlobalApp.vib) {
            findViewById<View>(R.id.pecs).setOnTouchListener { v, event ->
                GlobalApp.vibrate(20, v.context)
                super.onTouchEvent(event)
            }
        }
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pecs)


        layoutManager = GridLayoutManager(this, 2)
        PECSList.layoutManager = layoutManager

        adapter = RecyclerAdapter(this)
        PECSList.adapter = adapter



        findViewById<RecyclerView>(R.id.PECSList).addItemDecoration(MarginItemDecoration(70))


    }


    //from https://medium.com/@elye.project/right-way-of-setting-margin-on-recycler-views-cell-319da259b641
    inner class MarginItemDecoration(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect, view: View,
            parent: RecyclerView, state: RecyclerView.State
        ) {
            with(outRect) {
                if (parent.getChildAdapterPosition(view) == 0 || parent.getChildAdapterPosition(view) == 1) {
                    top = spaceHeight
                }
                left = spaceHeight
                right = spaceHeight
                bottom = spaceHeight
            }
        }
    }
    //https://medium.com/@elye.project/right-way-of-setting-margin-on-recycler-views-cell-319da259b641
}

class RecyclerAdapter(private val pecsActivity: PECSActivity) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    var pecs: MutableList<Int> = mutableListOf()
    var food: MutableList<Int> = mutableListOf()
    var things: MutableList<Int> = mutableListOf()
    var clothes: MutableList<Int> = mutableListOf()

    init {
        //tedious until a better way can be found...
        //...but does allow for ez sorting
        food.add(R.drawable.p_apple)
        food.add(R.drawable.p_banana)
        food.add(R.drawable.p_bread)
        food.add(R.drawable.p_cereal)
        food.add(R.drawable.p_cheese)
        food.add(R.drawable.p_crackers)
        food.add(R.drawable.p_fish)
        food.add(R.drawable.p_pasta)
        food.add(R.drawable.p_pizza)
        food.add(R.drawable.p_raisins)

        for (i: Int in food) {
            pecs.add(i)
        }

        things.add(R.drawable.p_car)
        things.add(R.drawable.p_cd)
        things.add(R.drawable.p_monkey)
        things.add(R.drawable.p_teddy)
        things.add(R.drawable.p_toilet)
        things.add(R.drawable.p_home)
        things.add(R.drawable.p_garden)
        things.add(R.drawable.p_music)

        for (i: Int in things) {
            pecs.add(i)
        }

        clothes.add(R.drawable.p_coat)
        clothes.add(R.drawable.p_dressinggown)
        clothes.add(R.drawable.p_jeans)
        clothes.add(R.drawable.p_pyjamas)
        clothes.add(R.drawable.p_scarf)
        clothes.add(R.drawable.p_shirt)
        clothes.add(R.drawable.p_tshirt)
        clothes.add(R.drawable.p_shoes)
        clothes.add(R.drawable.p_socks)
        clothes.add(R.drawable.p_vest)

        for (i: Int in clothes) {
            pecs.add(i)
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.pecsImage.setImageResource(pecs[i])
    }

    override fun getItemCount(): Int {
        return pecs.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.pecs_holder, viewGroup, false)
        return ViewHolder(v)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var pecsImage: ImageView = itemView.findViewById(R.id.pecs_image)

        init {
            itemView.setOnClickListener {
                val im = ImageView(itemView.context)
                im.setImageResource(pecs[adapterPosition])
                AlertDialog.Builder(itemView.context).setView(im).create().show()
            }
        }
    }

}
