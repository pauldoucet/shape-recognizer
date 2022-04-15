package com.example.a4starter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class MyShapeAdapter(context: Context, shapes: ArrayList<MyShape>, val sharedView: SharedViewModel) : ArrayAdapter<MyShape>(context, 0, shapes) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView

        // Are we reusing and old view ?
        if(convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.myshape, parent, false)
        }

        val shape = getItem(position)!!

        val textView = view!!.findViewById<TextView>(R.id.myShapeText)

        textView.text = shape.name

        val imageView = view!!.findViewById<ImageView>(R.id.imageView)
        imageView.setImageBitmap(shape.thumbnail)

        val button = view!!.findViewById<Button>(R.id.removeButton)
        button.setOnClickListener { _ ->
            //sharedView.shapes.value!!.removeAt(position)
            remove(getItem(position))
            notifyDataSetChanged()
            sharedView.shapes.value!!.removeAt(position)
        }

        return view
    }

}