package com.m1k.fyp

import android.graphics.Rect
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.m1k.fyp.GlobalApp.t2s
import com.m1k.fyp.GlobalApp.t2sSw
import kotlinx.android.synthetic.main.activity_pecs.*
import java.util.*

class PECSActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    //initialise text to speech engine
    private var tts: TextToSpeech? = null

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // set UK English as language for tts
            tts?.language = Locale.UK
        }
    }

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    override fun onResume() {
        if (tts == null)
            tts = TextToSpeech(this, this)
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

    override fun onDestroy() {
        if (tts != null) {
            tts?.stop()
            tts?.shutdown()
        }
        super.onDestroy()
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


    inner class RecyclerAdapter(private val pecsActivity: PECSActivity) :
        RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
        var pecs: MutableList<Int> = mutableListOf()
        var food: MutableList<Int> = mutableListOf()
        var things: MutableList<Int> = mutableListOf()
        var clothes: MutableList<Int> = mutableListOf()
        var speakD: SparseArray<String> = SparseArray()


        init {
            //tedious until a better way can be found...
            //...but does allow for ez sorting
            food.add(R.drawable.p_apple)
            speakD.append(R.drawable.p_apple, "apple")

            food.add(R.drawable.p_banana)
            speakD.append(R.drawable.p_banana, "banana")

            food.add(R.drawable.p_bread)
            speakD.append(R.drawable.p_bread, "bread")

            food.add(R.drawable.p_cereal)
            speakD.append(R.drawable.p_cereal, "cereal")

            food.add(R.drawable.p_cheese)
            speakD.append(R.drawable.p_cheese, "cheese")

            food.add(R.drawable.p_crackers)
            speakD.append(R.drawable.p_crackers, "crackers")

            food.add(R.drawable.p_fish)
            speakD.append(R.drawable.p_fish, "fish fingers")

            food.add(R.drawable.p_pasta)
            speakD.append(R.drawable.p_pasta, "Pasta")

            food.add(R.drawable.p_pizza)
            speakD.append(R.drawable.p_pizza, "pizza")

            food.add(R.drawable.p_raisins)
            speakD.append(R.drawable.p_raisins, "raisins")

            for (i: Int in food) {
                pecs.add(i)
            }

            things.add(R.drawable.p_car)
            speakD.append(R.drawable.p_car, "car")

            things.add(R.drawable.p_cd)
            speakD.append(R.drawable.p_cd, "cd")

            things.add(R.drawable.p_monkey)
            speakD.append(R.drawable.p_monkey, "monkey")

            things.add(R.drawable.p_teddy)
            speakD.append(R.drawable.p_teddy, "teddy")

            things.add(R.drawable.p_toilet)
            speakD.append(R.drawable.p_toilet, "toilet")

            things.add(R.drawable.p_home)
            speakD.append(R.drawable.p_home, "home")


            things.add(R.drawable.p_garden)
            speakD.append(R.drawable.p_garden, "garden")

            things.add(R.drawable.p_music)
            speakD.append(R.drawable.p_music, "music")

            for (i: Int in things) {
                pecs.add(i)
            }

            clothes.add(R.drawable.p_coat)
            speakD.append(R.drawable.p_coat, "coat")

            clothes.add(R.drawable.p_dressinggown)
            speakD.append(R.drawable.p_dressinggown, "dressing gown")

            clothes.add(R.drawable.p_jeans)
            speakD.append(R.drawable.p_jeans, "jeans")

            clothes.add(R.drawable.p_pyjamas)
            speakD.append(R.drawable.p_pyjamas, "pyjamas")

            clothes.add(R.drawable.p_scarf)
            speakD.append(R.drawable.p_scarf, "scarf")

            clothes.add(R.drawable.p_shirt)
            speakD.append(R.drawable.p_shirt, "shirt")

            clothes.add(R.drawable.p_tshirt)
            speakD.append(R.drawable.p_tshirt, "t-shirt")

            clothes.add(R.drawable.p_shoes)
            speakD.append(R.drawable.p_shoes, "trainers")

            clothes.add(R.drawable.p_socks)
            speakD.append(R.drawable.p_socks, "socks")

            clothes.add(R.drawable.p_vest)
            speakD.append(R.drawable.p_vest, "vest")

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
                    if (GlobalApp.vib)
                        GlobalApp.vibrate(30, this@PECSActivity)
                    im.setImageResource(pecs[adapterPosition])
                    AlertDialog.Builder(itemView.context).setView(im).create().show()

                }

                itemView.setOnLongClickListener {
                    if (t2sSw) {
                        t2s(speakD[pecs[adapterPosition]], tts)
                    }
                    false
                }
            }
        }
    }
}
