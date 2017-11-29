package com.example.vlad.commitsupervisor


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.text.TextUtils
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

    interface Interaction { //it's strange that interface have variable (but it's method)
        val isSearching: Boolean
    }

    private lateinit var interactor : Interaction

    private lateinit var layout : ConstraintLayout
    private lateinit var progressBar : ProgressBar
    private lateinit var user : User
    private lateinit var loginTextView: TextView
    private lateinit var avatar : ImageView
    private lateinit var backButtonImageView : ImageView
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

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        interactor = context as Interaction
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_header_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        layout = view.findViewById(R.id.header_layout) as ConstraintLayout
        progressBar = view.findViewById(R.id.progressBar) as ProgressBar
        loginTextView = view.findViewById(R.id.login) as TextView
        avatar = view.findViewById(R.id.main_avatar) as ImageView
        loginTextView.text = user.login //set username from user input
        if (!TextUtils.isEmpty(user.avatarUrl)) {
            loadImage()
        }



        backButtonImageView = view.findViewById(R.id.back_button_image_main_fragment) as ImageView
        backButtonImageView.setOnClickListener {
            var intent = Intent(activity, WelcomeActivity::class.java)
            intent.putExtra("isSearchActivated", true) //TODO: ?
            activity.finish()
            //activity.startActivity(intent)

        }

        layout.setOnClickListener {
            if (!TextUtils.isEmpty(user.profileUrl)) {
                var intent = Intent(Intent.ACTION_VIEW, Uri.parse(user.profileUrl))
                startActivity(intent)
            }
        }

    }

    private fun changeFragmentState() {
        if (interactor.isSearching) {
            Views.setVisible(progressBar)
        }
        else {
            Views.setInvisible(progressBar)
        }
    }

    override fun onResume() {
        super.onResume()
        changeFragmentState()
    }

    fun searchCompleted(user: User) {
        changeFragmentState()
        if (this.user.avatarUrl != (user.avatarUrl)) {
            loadImage()
        }
        this.user = user
        loginTextView.text = user.login //reset username from "real" User object from service
    }

    fun searchCompleted() {
        changeFragmentState()
    }

    private fun loadImage() {
        Picasso.with(context)
                .load(user.avatarUrl)
                .fit()
                .placeholder(R.drawable.ic_person_40dp)
                .transform(CircleTransform())
                .into(avatar)
    }

}
