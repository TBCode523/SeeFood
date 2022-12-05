package com.example.seefood.ui.main.dashboard

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seefood.DetailActivity
import com.example.seefood.R
import com.example.seefood.databinding.FragmentDashboardBinding
import com.example.seefood.utils.Food
import java.io.ByteArrayOutputStream

private  const val TAG = "dashAct"
class DashboardFragment : Fragment(),CustomAdapter.OnItemClickListener {

    private  var _binding: FragmentDashboardBinding? = null
    private lateinit var adapter: CustomAdapter
    var counter = 0




    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var dashboardViewModel: DashboardViewModel
    var item: MutableList<Food> = mutableListOf()


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
        }
        return root
    }




    private fun addItem(itm: Food) {
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
        val addDietaryFiber = v.findViewById<EditText>(R.id.add_dFiber)
        val addSugar = v.findViewById<EditText>(R.id.add_sugar)
        val addProtein = v.findViewById<EditText>(R.id.add_protein)
        val addServingSize =v.findViewById<EditText>(R.id.add_ServingSize)



        val addDialog = AlertDialog.Builder(requireContext())
        addDialog.setView(v)
        addDialog.setPositiveButton("Ok"){
                dialog,_->
            if (addName.text.isEmpty()) {
                Toast.makeText(requireContext(), "please add string for name", Toast.LENGTH_SHORT)
                    .show()
                dialog.dismiss()
            }else if(addServingSize.text.isEmpty()){
                Toast.makeText(requireContext(), "please add string for serving size", Toast.LENGTH_SHORT)
                    .show()
                dialog.dismiss()
            }else if(addCalories.text.isEmpty()||!(addCalories.text.isDigitsOnly())) {
                Toast.makeText(requireContext(),"make sure you input digits for calories",Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }else if(addCalories.text.isEmpty()||!(addTotalFat.text.isDigitsOnly())) {
                Toast.makeText(requireContext(),"make sure you input digits for total fat",Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }else if(addCholesterol.text.isEmpty()||!(addCholesterol.text.isDigitsOnly())) {
                Toast.makeText(requireContext(),"make sure you input digits for cholesterol",Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }else if(addSodium.text.isEmpty()||!(addSodium.text.isDigitsOnly())) {
                Toast.makeText(requireContext(),"make sure you input digits for sodium",Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else if(addCarbs.text.isEmpty()||!(addCarbs.text.isDigitsOnly())) {
                Toast.makeText(requireContext(),"make sure you input digits for carbs",Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else if(addDietaryFiber.text.isEmpty()||!(addDietaryFiber.text.isDigitsOnly())) {
                Toast.makeText(requireContext(),"make sure you input digits for dietary fiber",Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else if(addSugar.text.isEmpty()||!(addSugar.text.isDigitsOnly())) {
                Toast.makeText(requireContext(),"make sure you input digits for sugars",Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else if(addProtein.text.isEmpty()||!(addProtein.text.isDigitsOnly())) {
                Toast.makeText(requireContext(),"make sure you input digits for protein",Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }else {

                val newFood = Food(
                    addName.text.toString(),
                    /*addServingSize.text.toString(),
                    addCalories.text.toString(),
                    addTotalFat.text.toString(),
                    addCholesterol.text.toString(),
                    addSodium.text.toString(),
                    addCarbs.text.toString(),
                    addDietaryFiber.text.toString(),
                    addSugar.text.toString(),
                    addProtein.text.toString()*/
                    HashMap()
                )

                addItem(newFood)
                // add other things that will be saved
                adapter.notifyItemInserted(item.size - 1);
                Toast.makeText(
                    requireContext(),
                    "Adding User Information Success",
                    Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()
            }



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

        val intent = Intent(getActivity(), DetailActivity::class.java)
        intent.putExtra("name",clickedItem.name);
        /*intent.putExtra("ssize",clickedItem.ServingSize)
        intent.putExtra("calories",clickedItem.cal.toString());
        intent.putExtra("totalfat",clickedItem.totalfat.toString());
        intent.putExtra("cholesterol",clickedItem.cholesterol.toString());
        intent.putExtra("sodium",clickedItem.sodium.toString());
        intent.putExtra("carbs",clickedItem.carbs.toString());
        intent.putExtra("dfiber",clickedItem.dFiber.toString());
        intent.putExtra("sugar",clickedItem.sugar.toString());
        intent.putExtra("protein",clickedItem.protein.toString());
        */
        startActivity(intent)
        // this is the onclick listener for each card in the recycler view,
        // we will need to go to whatever activity or fragment will be used to display the data
        // with the firebase information
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}