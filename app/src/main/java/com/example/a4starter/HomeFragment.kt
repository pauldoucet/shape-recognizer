package com.example.a4starter

import android.graphics.drawable.shapes.Shape
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow
import kotlin.math.sqrt

class HomeFragment : Fragment() {
    private var mViewModel: SharedViewModel? = null

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        /*val textView = root.findViewById<TextView>(R.id.text_home)
        mViewModel!!.text.observe(viewLifecycleOwner,
            { s -> textView.text = "$s - Recognition" })*/

        val canvasView = root.findViewById<CanvasView>(R.id.canvasRecognize)

        val clearButton = root.findViewById<Button>(R.id.clearButton2)
        val recognizeButton = root.findViewById<Button>(R.id.recognizeButton)

        clearButton.setOnClickListener{ canvasView.clear()}
        recognizeButton.setOnClickListener{
            val cleanedPoints = PathSampler.fitInSquare(PathSampler.rotateToZero(canvasView.sample()),
                Point(canvasView.width / 2f, canvasView.height / 2f), 200f)
            val closestShapes = closestShapes(cleanedPoints)
            val listView = root.findViewById<ListView>(R.id.listView2)

            val adapter = MyShapeAdapter(requireContext(), closestShapes, mViewModel!!)
            listView.adapter = adapter
        }

        return root
    }

    private fun closestShapes(points: ArrayList<Point>) : ArrayList<MyShape> {
        val shapes = mViewModel!!.shapes.value
        //var minDist = Double.MAX_VALUE
        //var index = 0
        val map = TreeMap<Double, ArrayList<Int>>()

        for(i in 0 until shapes!!.size) {
            val dist = distance(points, shapes[i].points)
            val list = map.getOrDefault(dist, ArrayList())
            list.add(i)
            map[dist] = list
        }

        var output = ArrayList<Int>()
        for(entry in map.entries) {
            output.addAll(entry.value)
        }

        var closestShapes = ArrayList<MyShape>()
        for(i in 0 until output.size) {
            closestShapes.add(shapes[output[i]])
        }

        return closestShapes
    }

    private fun distance(x1: ArrayList<Point>, x2: ArrayList<Point>) : Double {
        var sum = 0.0
        val n = x1.size
        for(k in 0 until n) {
            sum += sqrt((x1[k].x - x2[k].x).pow(2).toDouble() + (x1[k].y - x2[k].y).pow(2).toDouble())
        }
        return sum / n
    }
}