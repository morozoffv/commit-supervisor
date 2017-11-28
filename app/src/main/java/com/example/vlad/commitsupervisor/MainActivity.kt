package com.example.vlad.commitsupervisor

import android.app.FragmentManager
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import com.example.vlad.commitsupervisor.events.Event
import com.example.vlad.commitsupervisor.uiutils.Views
import kotlinx.android.synthetic.main.fragment_header_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), HeaderMainFragment.Interaction {

    private val headerMainFragmentTag = HeaderMainFragment::class.java.simpleName
    private val listMainFragmentTag = ListMainFragment::class.java.simpleName

    private val commitSupervisorApp: CommitSupervisorApp
        get() = application as CommitSupervisorApp

    private val headerMainFragment : HeaderMainFragment?
        get() = supportFragmentManager.findFragmentByTag(headerMainFragmentTag) as? HeaderMainFragment

    private val listMainFragment : ListMainFragment?
        get() = supportFragmentManager.findFragmentByTag(listMainFragmentTag) as? ListMainFragment

    var searchResult: SearchResult? = null //TODO: how to make it non-null?

    override var isSearching = false

    private val searchBroadcastReceiver = object : SearchBroadcastReceiver() {
        override fun onCompleted(bundle: Bundle) {
            isSearching = false
            Toast.makeText(this@MainActivity, "Success, loaded " + bundle.get("eventsCount") + " events", Toast.LENGTH_SHORT).show()
            searchResult = commitSupervisorApp.searchService.searchResult!!
            headerMainFragment?.searchCompleted(searchResult!!.user)
            listMainFragment?.searchCompleted(ArrayList(searchResult!!.events))

        }

        override fun onError() {
            isSearching = false
            Toast.makeText(this@MainActivity, "Error!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        searchBroadcastReceiver.onRegister(this)

        if (savedInstanceState == null) { //first activity launch
            initFragments()
            isSearching = true
            preFetchUserActivity() //replace from onStart, can it cause problems?
        }
        else {
            isSearching = savedInstanceState.getBoolean("isSearching") //for rotating
        }
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState!!.putBoolean("isSearching", isSearching)
        super.onSaveInstanceState(outState)
    }

    fun initFragments() {
        val fragmentBundle = Bundle()
        val user = intent.getSerializableExtra("user") as User
        fragmentBundle.putSerializable("user", user)
        fragmentBundle.putInt("metrics", resources.displayMetrics.densityDpi)
        val headerMainFrag = HeaderMainFragment()
        val listMainFrag = ListMainFragment()
        headerMainFrag.arguments = fragmentBundle
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_header_main, headerMainFrag, headerMainFragmentTag)
                .replace(R.id.fragment_list_main, listMainFrag, listMainFragmentTag)
                .commit()


    }

    private fun preFetchUserActivity() {
        //get user activity
        //isSearching = true
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'", Locale.US)
        val date = Date()
        val user = intent.getSerializableExtra("user") as User
        commitSupervisorApp.searchService.fetchUserActivity(user.login, date) //TODO: redo username type and date filtration

    }

    override fun onBackPressed() {
        var intent = Intent(this, WelcomeActivity::class.java)
        intent.putExtra("isSearchActivated", true) //TODO: ?
        this.startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(searchBroadcastReceiver)
    }
}


