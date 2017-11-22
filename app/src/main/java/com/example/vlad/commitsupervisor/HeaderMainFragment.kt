package com.example.vlad.commitsupervisor


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.example.vlad.commitsupervisor.uiutils.Views
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_header_main.*



class HeaderMainFragment : Fragment() {



    lateinit var progressBar : ProgressBar
    lateinit var user : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments.containsKey("user")) {
            user = arguments.getSerializable("user") as User
        }
        else {
            throw RuntimeException("You must provide user!")
        }
    }
    //setretaininstance

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        return inflater!!.inflate(R.layout.fragment_header_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progressBar = view.findViewById(R.id.progressBar) as ProgressBar


    }

    fun searchCompleted(user: User) {
        Views.setInvisible(progressBar)
        //change user if it is not the same
    }

}// Required empty public constructor
