package com.example.seefood.ui.main.dashboard

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seefood.R
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
        adapter = CustomAdapter(requireContext(),item,this,::fdelete)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(requireContext())

        binding.fab.setOnClickListener{
            addItemtoRec()
            /*if (counter == 0) {
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
            counter ++*/
        }
        return root
    }




    private fun addItem(itm: String) {
        dashboardViewModel.click(itm)
        item.add(itm)

        adapter.notifyItemInserted(item.size - 1);

    }

    fun fdelete(itm: Int){
        dashboardViewModel.delete(itm)

        adapter.notifyDataSetChanged()

    }

    private fun addItemtoRec(){
        val inflter = LayoutInflater.from(requireContext())
        val v = inflter.inflate(R.layout.add_item,null)
        val addName = v.findViewById<EditText>(R.id.add_name)
        val addCalories = v.findViewById<EditText>(R.id.add_calories)
        val addTotalFat = v.findViewById<EditText>(R.id.add_totFat)

        val addCholesterol = v.findViewById<EditText>(R.id.add_cholesterol)
        val addSodium = v.findViewById<EditText>(R.id.add_sodium)
        val addCarbs = v.findViewById<EditText>(R.id.add_carbs)


        val addDialog = AlertDialog.Builder(requireContext())
        addDialog.setView(v)
        addDialog.setPositiveButton("Ok"){
                dialog,_->
            val names = addName.text.toString()
            val calories = addCalories.text.toString()
            val totalFat = addTotalFat.text.toString()
            val chol = addCholesterol.text.toString()
            val sod = addSodium.text.toString()
            val carbs = addCarbs.text.toString()

            addItem(names)
            // add other things that will be saved
            adapter.notifyItemInserted(item.size - 1);
            Toast.makeText(requireContext(),"Adding User Information Success",Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        addDialog.setNegativeButton("Cancel"){
                dialog,_->
            dialog.dismiss()
            Toast.makeText(requireContext(),"Cancel",Toast.LENGTH_SHORT).show()

        }
        addDialog.create()
        addDialog.show()
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