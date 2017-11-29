package com.example.vlad.commitsupervisor

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.Toast

import com.example.vlad.commitsupervisor.events.Event

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LogActivity : AppCompatActivity() {

    private var events: List<Event>? = null
    private val stringArray: Array<String>? = null

    private lateinit var recyclerView: RecyclerView
    private var adapter: RecyclerView.Adapter<*>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null


    private val searchBroadcastReceiver = object : SearchBroadcastReceiver() {
        override fun onCompleted(bundle: Bundle) {
            Toast.makeText(this@LogActivity, "Success, loaded " + bundle.get("eventsCount") + " events", Toast.LENGTH_SHORT).show()
            events = commitSupervisorApp.searchService.searchResult!!.events
            adapter = EventsAdapter(events)
            recyclerView.adapter = adapter

        }

        override fun onError() {
            Toast.makeText(this@LogActivity, "Error!", Toast.LENGTH_SHORT).show()
        }
    }

    private val commitSupervisorApp: CommitSupervisorApp
        get() = application as CommitSupervisorApp




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        searchBroadcastReceiver.onRegister(this)

        preFetchUserActivity()

        setContentView(R.layout.activity_log)
        recyclerView = findViewById(R.id.my_recycler_view) as RecyclerView

        recyclerView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager


        //separators
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, LinearLayout.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)
        val functionRef = this::preFetchUserActivity
        events?.forEachIndexed { index, event ->  }

    }

    override fun onResume() {
        super.onResume()

    }

    private fun preFetchUserActivity() {
        //get user activity

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'", Locale.US)
        val date = Date()
        val user = intent.getSerializableExtra("user") as User
        commitSupervisorApp.searchService.fetchUserActivity(user.login, date) //TODO: redo username type and date filtration

    }

    override fun onDestroy() {
        super.onDestroy()
        searchBroadcastReceiver.onUnregister(this)

    }

}

