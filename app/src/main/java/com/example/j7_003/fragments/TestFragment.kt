package com.example.j7_003.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.j7_003.MainActivity
import com.example.j7_003.R
import com.example.j7_003.data.database.CalendarManager
import com.example.j7_003.data.database.database_objects.CalendarAppointment
import kotlinx.android.synthetic.main.fragment_day.*
import kotlinx.android.synthetic.main.fragment_test.view.*
import kotlinx.android.synthetic.main.row_term.view.*
import kotlinx.android.synthetic.main.row_term.view.tvTermItemInfo
import kotlinx.android.synthetic.main.row_term.view.tvTermItemTitle
import kotlinx.android.synthetic.main.row_term_day.view.*
import org.threeten.bp.LocalDate

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [TestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TestFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var position: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_test, container, false)

        //initialize own date
        val delta = -(Int.MAX_VALUE/2 - position!!)
        val newDate: LocalDate
        newDate = LocalDate.now().plusDays(delta.toLong())

        //Initialize Recyclerview and Adapter
        val myRecycler = myView.recycler_view_day
        val myAdapter = TermAdapterDay()
        myAdapter.setDate(newDate)
        myRecycler.adapter = myAdapter
        myRecycler.layoutManager = LinearLayoutManager(MainActivity.myActivity)

        //return inflated View
        return myView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment TestFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(position: Int) =
            TestFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, position)
                }
            }
    }
}
class TermAdapterDay() :
    RecyclerView.Adapter<TermAdapterDay.TermViewHolderDay>() {


    private lateinit var daylist: ArrayList<CalendarAppointment>
    fun setDate(date: LocalDate){
              daylist = CalendarManager.getDayView(date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TermViewHolderDay {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_term_day, parent, false)
        return TermViewHolderDay(itemView)
    }


    override fun onBindViewHolder(holder: TermViewHolderDay, position: Int) {

        val currentTerm = daylist[position]

        holder.itemView.setOnClickListener() {
            //todo start CreateTermFragment in EDIT mode
            MainActivity.myActivity.changeToCreateTerm()
        }

        holder.tvTitle.text = currentTerm.title
        holder.tvInfo.text = currentTerm.addInfo

        //hides end time of a term if its identical to start time
        if(currentTerm.startTime.equals(currentTerm.eTime)){
            holder.tvStartTime.text = currentTerm.startTime.toString()
            holder.tvEndTime.text = ""
            holder.tvDashUntil.visibility = View.INVISIBLE
        }else{
            holder.tvStartTime.text = currentTerm.startTime.toString()
            holder.tvEndTime.text = currentTerm.eTime.toString()
            holder.tvDashUntil.visibility = View.VISIBLE
        }

    }

    override fun getItemCount() = daylist.size


    class TermViewHolderDay(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /**
         * One instance of this class will contain one "instance" of row_term and meta data
         * like position, it also holds references to views inside of the layout
         */
        val tvTitle = itemView.tvTermItemTitle
        val tvInfo = itemView.tvTermItemInfo
        val tvStartTime = itemView.tvTermItemStartTime
        val tvEndTime = itemView.tvTermItemEndTime
        val tvDashUntil = itemView.tvDashUntil
    }

}
