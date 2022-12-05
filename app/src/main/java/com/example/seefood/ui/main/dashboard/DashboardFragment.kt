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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


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
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var foodLst = ArrayList<Food>()
        auth = Firebase.auth
        //Reference to a list of Food data objects
        dbRef = FirebaseDatabase.getInstance().reference.child("Users").child(auth.uid!!).child("foods")




        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        Log.d("Food Nut", "foodLst not null: $foodLst")
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

        dbRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    item.clear()
                    for (userSnapshot in snapshot.children){



                        var foodhm = userSnapshot.child("nutrients").value as java.util.HashMap<*, *>
                        var foodnm = userSnapshot.child("name").value

                        Log.i("retrieve data", foodnm.toString())
                        Log.i("retrieve data", foodhm.toString())

                        var hm = HashMap<String, Float>()
                        for ((key,value)in foodhm){
                            hm.put(key.toString(),value.toString().toFloat())
                        }
                        var temp = Food(foodnm.toString(),hm)
                        var flag = true
                        for (valu in item){
                            if (valu.getHM().equals(hm) && valu.getName().equals(foodnm.toString())){
                                flag = false
                            }
                        }
                        Log.i("retrieve item", item.toString())
                        if (flag) {
                            item.add(temp);
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })




        binding.fab.setOnClickListener{
            addItemtoRec()
        }
        return root
    }




    private fun addItem(itm: Food) {
        //dashboardViewModel.click(itm)
        item.add(itm)

        adapter.notifyDataSetChanged()
        dbRef.get()

    }

    fun fdelete(itm: Int,id:String){
        //dashboardViewModel.delete(itm)

        val dbr = FirebaseDatabase.getInstance().reference.child("Users").child(auth.uid!!).child("foods")
        dbr.orderByChild("name").equalTo(id).addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children){
                    data.ref.removeValue()
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })



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
            var hm: HashMap<String, Float> = HashMap<String, Float>();
            if (addName.text.isEmpty()) {
                Toast.makeText(requireContext(), "please add string for name", Toast.LENGTH_SHORT)
                    .show()
                dialog.dismiss()
            }

            if(addServingSize.text.isEmpty()|| !addServingSize.text.isDigitsOnly()) {
                hm.set("Serving Size",0F);
            }else if (addCalories.text.isDigitsOnly()){
                hm.set("Serving Size",addCalories.text.toString().toFloat());
            }


            if(addCalories.text.isEmpty()|| !addCalories.text.isDigitsOnly()) {
                hm.set("Calories",0F);
            }else if (addCalories.text.isDigitsOnly()){
                hm.set("Calories",addCalories.text.toString().toFloat());
            }

            if(addTotalFat.text.isEmpty()||!(addTotalFat.text.isDigitsOnly())) {
                hm.set("Total Fat",0F);
            }else{
                hm.set("Total Fat",addTotalFat.text.toString().toFloat());
            }

            if(addCholesterol.text.isEmpty()||!(addCholesterol.text.isDigitsOnly())) {
                hm.set("Cholesterol",0F);
            }else{
                hm.set("Cholesterol",addCholesterol.text.toString().toFloat());
            }

            if(addSodium.text.isEmpty()||!(addSodium.text.isDigitsOnly())) {
                hm.set("Sodium",0F);
            }else{
                hm.set("Sodium",addSodium.text.toString().toFloat());
            }

            if(addCarbs.text.isEmpty()||!(addCarbs.text.isDigitsOnly())) {
                hm.set("Total Carbohydrates",0F);
            }else{
                hm.set("Total Carbohydrates",addCarbs.text.toString().toFloat());
            }

            if(addDietaryFiber.text.isEmpty()||!(addDietaryFiber.text.isDigitsOnly())) {
                hm.set("Dietary Fibers",0F);
            }else{
                hm.set("Dietary Fibers",addDietaryFiber.text.toString().toFloat());
            }

            if(addSugar.text.isEmpty()||!(addSugar.text.isDigitsOnly())) {
                hm.set("Total Sugar",0F);
            }else{
                hm.set("Total Sugar",addSugar.text.toString().toFloat())
            }
            if(addProtein.text.isEmpty()||!(addProtein.text.isDigitsOnly())) {
                hm.set("Protein",0F);
            }else{
                hm.set("Protein",addProtein.text.toString().toFloat())
            }

            //*
                val newFood = Food(
                    addName.text.toString(),
                    hm
                );
            var foodLst = ArrayList<Food>()
            dbRef.get().addOnCompleteListener { it ->
                Log.d("Saving", "Data: ${it.result.value}")
                Log.d("Saving", "Children count: ${it.result.childrenCount}")
                if (it.result.value != null){
                    foodLst = it.result.value as ArrayList<Food>
                    Log.d("Saving", "foodLst not null: $foodLst")
                }
                foodLst.add(newFood)
                Log.d("Saving", "New foodLst: $foodLst")
                dbRef.setValue(foodLst)
            }
                addItem(newFood)
                // add other things that will be saved
                adapter.notifyItemInserted(item.size - 1);
                Toast.makeText(
                    requireContext(),
                    "Adding User Information Success",
                    Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()
                //*/
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
        //*
        val intent = Intent(getActivity(), DetailActivity::class.java)
        intent.putExtra("name",clickedItem.getName());
        val hm = clickedItem.getHM();
        intent.putExtra("ssize",hm.get("Serving Size").toString())
        intent.putExtra("calories",hm.get("Calories").toString());
        intent.putExtra("totalfat",hm.get("Total Fat").toString());
        intent.putExtra("cholesterol",hm.get("Cholesterol").toString());
        intent.putExtra("sodium",hm.get("Sodium").toString());
        intent.putExtra("carbs",hm.get("Total Carbohydrates").toString());
        intent.putExtra("dfiber",hm.get("Dietary Fibers").toString());
        intent.putExtra("sugar",hm.get("Total Sugar").toString());
        intent.putExtra("protein",hm.get("Protein").toString());

        startActivity(intent)
        // this is the onclick listener for each card in the recycler view,
        // we will need to go to whatever activity or fragment will be used to display the data
        // with the firebase information

         //*/
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}