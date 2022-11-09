package org.cream.corutines_practice_red.main.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(private val fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        TODO("프래그먼트를 생성해야합니다.")
    }

    override fun getItemCount(): Int {
        return 2
    }
}