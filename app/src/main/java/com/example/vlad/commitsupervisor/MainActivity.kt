package com.example.vlad.commitsupervisor

import android.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val headerMainFragment = HeaderMainFragment()
        val listMainFragment = ListMainFragment()
        val manager = supportFragmentManager
        manager.beginTransaction()
                .replace(R.id.fragment_header_main, headerMainFragment, headerMainFragment.tag)
                .replace(R.id.fragment_list_main, listMainFragment, listMainFragment.tag)
                .commit()

    }
}
