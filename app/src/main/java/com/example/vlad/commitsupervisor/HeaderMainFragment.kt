package com.example.vlad.commitsupervisor


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.example.vlad.commitsupervisor.uiutils.Views
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_header_main.*
import android.util.DisplayMetrics





class HeaderMainFragment : Fragment() {



    private lateinit var progressBar : ProgressBar
    lateinit var user : User
    private lateinit var loginTextView: TextView
    private lateinit var avatar : ImageView
    private var metrics : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        metrics = arguments.getInt("metrics")
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
        loginTextView = view.findViewById(R.id.login) as TextView
        avatar = view.findViewById(R.id.main_avatar) as ImageView

        Picasso.with(context).load(R.drawable.ic_account_box_black_24dp).transform(CircularTransformation(30 * metrics)).into(avatar)
        loginTextView.text = user.login //set username from user input

    }

    fun searchCompleted(user: User) {
        Views.setInvisible(progressBar)
        this.user = user
        loginTextView.text = user.login //reset username from "real" User object from service
        //change user if it is not the same

        Picasso.with(context).load(user.avatarUrl).placeholder(R.drawable.ic_account_box_black_24dp).transform(CircularTransformation(30 * metrics)).into(avatar)
    }

}// Required empty public constructor
