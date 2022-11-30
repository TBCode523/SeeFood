package com.example.seefood.ui.main.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seefood.databinding.FragmentDashboardBinding

private  const val TAG = "dashAct"
class DashboardFragment : Fragment(),CustomAdapter.OnItemClickListener {

    private  var _binding: FragmentDashboardBinding? = null
    private lateinit var adapter: CustomAdapter
    var counter = 0




    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var dashboardViewModel: DashboardViewModel
    var item: MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)



        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = _binding!!.root

        dashboardViewModel.text.observe(viewLifecycleOwner) {
            item.addAll(it)

        }
        Log.i(TAG, item.toString())
        val recyclerview: RecyclerView = binding.recyclerview
        adapter = CustomAdapter(requireContext(),item,this)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(requireContext())

        binding.fab.setOnClickListener{
            if (counter == 0) {
                addItem("tomato")
            }
            if (counter == 1){
                addItem("sharkbait")
            }
            if (counter == 2){
                addItem("squid")
            }
            if (counter == 3){
                addItem("dragon")
            }
            counter ++
        }
        return root
    }




    private fun addItem(itm: String) {
        dashboardViewModel.click(itm)
        item.add(itm)

        adapter.notifyItemInserted(item.size - 1);

    }

    override fun onItemClick(position: Int) {
        val clickedItem = item[position]
        Toast.makeText(requireContext(), "item $clickedItem clicked", Toast.LENGTH_SHORT).show()

        // this is the onclick listener for each card in the recycler view,
        // we will need to go to whatever activity or fragment will be used to display the data
        // with the firebase information
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}