package com.example.vlad.commitsupervisor


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.vlad.commitsupervisor.events.Event


class ListMainFragment : Fragment() {

//    interface Interaction { //it's strange that interface have variable (but it's method)
//        val searchResult: SearchResult?
//    }
//
//    lateinit var interaction : Interaction

    private lateinit var events : List<Event>

    private lateinit var eventsRecyclerView : RecyclerView
    private lateinit var eventsAdapter : JSONAdapter//TODO: create new adapter
    private lateinit var layoutManager: RecyclerView.LayoutManager




    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_list_main, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
//        interaction = context as Interaction
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eventsRecyclerView = view.findViewById(R.id.events_recycler_view) as RecyclerView
        eventsRecyclerView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this.context) //?
        eventsRecyclerView.layoutManager = layoutManager


//        eventsAdapter = JSONAdapter(interaction.searchResult!!.events)
//        eventsRecyclerView.adapter = eventsAdapter
//        loginTextView.text = interaction.searchResult!!.user.login


    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    fun searchCompleted(eventsList : List<Event>) {
        events = eventsList     //TODO: how to make this.events = events
        eventsAdapter = JSONAdapter(events) //TODO: do i need external field?
        eventsRecyclerView.adapter = eventsAdapter
    }
}
