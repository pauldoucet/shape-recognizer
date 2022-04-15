package com.example.a4starter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class LibraryFragment : Fragment() {
    private var mViewModel: SharedViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val root: View = inflater.inflate(R.layout.fragment_library, container, false)
        //val textView = root.findViewById<TextView>(R.id.text_library)
        /*mViewModel!!.text.observe(viewLifecycleOwner,
            { s -> textView.text = "$s - Library" })*/

        mViewModel!!.shapes.observe(viewLifecycleOwner, { myShapes ->
            val listView = requireActivity().findViewById<ListView>(R.id.listView)
            val adapter = MyShapeAdapter(requireContext(), ArrayList(myShapes), mViewModel!!)
            listView.adapter = adapter
        })

        return root
    }
}