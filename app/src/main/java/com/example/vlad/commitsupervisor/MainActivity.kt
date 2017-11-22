package com.example.vlad.commitsupervisor

import android.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import com.example.vlad.commitsupervisor.events.Event
import com.example.vlad.commitsupervisor.uiutils.Views
import kotlinx.android.synthetic.main.fragment_header_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), ListMainFragment.Interaction {

    private val headerMainFragmentTag = HeaderMainFragment::class.java.simpleName
    private val listMainFragmentTag = ListMainFragment::class.java.simpleName

    private val commitSupervisorApp: CommitSupervisorApp
        get() = application as CommitSupervisorApp

    private val headerMainFragment : HeaderMainFragment?
        get() = supportFragmentManager.findFragmentByTag(headerMainFragmentTag) as? HeaderMainFragment

    private val listMainFragment : ListMainFragment?
        get() = supportFragmentManager.findFragmentByTag(listMainFragmentTag) as? ListMainFragment

    override var searchResult: SearchResult? = null //TODO: how to make it non-null?

    private val searchBroadcastReceiver = object : SearchBroadcastReceiver() {
        override fun onCompleted(bundle: Bundle) {
            Toast.makeText(this@MainActivity, "Success, loaded " + bundle.get("eventsCount") + " events", Toast.LENGTH_SHORT).show()
            searchResult = commitSupervisorApp.searchService.searchResult!!
            supportFragmentManager.beginTransaction()
                    .detach(listMainFragment)
                    .attach(listMainFragment)
                    .commit()
            headerMainFragment?.searchCompleted(searchResult!!.user)
        }

        override fun onError() {
            Toast.makeText(this@MainActivity, "Error!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        searchBroadcastReceiver.onRegister(this)

        initFragments()
    }

    override fun onStart() {
        super.onStart()
        preFetchUserActivity()
    }

    fun initFragments() {
        val fragmentBundle = Bundle()
        val user = User()
        fragmentBundle.putSerializable("user", user)
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

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'", Locale.US)
        val date = Date()
        commitSupervisorApp.searchService.fetchUserActivity(intent.getStringExtra("username"), date) //TODO: redo username type and date filtration

    }



    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(searchBroadcastReceiver)
    }
}


