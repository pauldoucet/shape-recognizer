package com.example.a4starter

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin


class AdditionFragment : Fragment() {
    private var mViewModel: SharedViewModel? = null
    //private var dialog = AlertDialog.Builder(context)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val root: View = inflater.inflate(R.layout.fragment_addition, container, false)
        /*val textView = root.findViewById<TextView>(R.id.text_addition)
        mViewModel!!.text.observe(viewLifecycleOwner,
            { s -> textView.text = "$s - Addition" })*/

        // Setup buttons
        val addButton = root.findViewById<Button>(R.id.addButton)
        val clearButton = root.findViewById<Button>(R.id.clearButton)

        val canvasView = root.findViewById<CanvasView>(R.id.canvasRecognize)

        addButton.setOnClickListener { v -> add(canvasView) }
        clearButton.setOnClickListener { _ -> clear(canvasView) }

        //initDialog(canvasView)

        return root
    }

    private fun createDialog(canvasView: CanvasView, points: ArrayList<Point>):  AlertDialog.Builder {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Enter a name for this shape:")

        val editText = EditText(context)
        dialog.setView(editText)

        //val points = PathSampler.samplePath(canvasView.path)

        dialog.setPositiveButton("OK") { _, _ ->
            mViewModel!!.shapes.value!!.add(MyShape(editText.text.toString(), points, getBitmapFromView(canvasView)!!))
            canvasView.clear()
        }

        dialog.setNegativeButton("Cancel") { d, _ ->
            d.cancel()
            canvasView.clear()
        }

        return dialog
    }

    private fun add(canvasView: CanvasView) {
        val points = PathSampler.samplePath(canvasView.path)
        val rotatedPoints = PathSampler.rotateToZero(points)
        val fittedAndRotatedPoints = PathSampler.fitInSquare(rotatedPoints, Point(canvasView.width.toFloat() / 2, canvasView.height.toFloat() / 2), 200f)
        //canvasView.pointMode = true
        //canvasView.pointArray = fittedAndRotatedPoints
        //canvasView.invalidate()
        val dialog = createDialog(canvasView, fittedAndRotatedPoints)
        dialog.show()
        canvasView.clear()
    }

    private fun clear(canvasView: CanvasView) {
        canvasView.clear()
    }

    /*
     * Solution taken from:
     * https://stackoverflow.com/questions/5536066/convert-view-to-bitmap-on-android
     */

    fun getBitmapFromView(view: View): Bitmap? {
        //Define a bitmap with the same size as the view
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        //Bind a canvas to it
        val canvas = Canvas(returnedBitmap)
        //Get the view's background
        val bgDrawable = view.background
        if (bgDrawable != null) //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas) else  //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.CYAN)
        // draw the view on the canvas
        view.draw(canvas)
        //return the bitmap
        return returnedBitmap
    }
}