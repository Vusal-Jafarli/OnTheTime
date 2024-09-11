package com.example.onthetime.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onthetime.R
import com.example.onthetime.model.MenuItem

// MoreViewModel.kt
class MoreViewModel : ViewModel() {

    private val _menuItems = MutableLiveData<List<MenuItem>>()
    val menuItems: LiveData<List<MenuItem>> get() = _menuItems

    init {
        // Initialize or load data here. For example:
        loadMenuItems()
    }

    private fun loadMenuItems() {
        // This could be from a repository or hardcoded data
        val items = listOf(
            MenuItem("Profile", R.drawable.profile_icon_recyclerview),
            MenuItem("Announcements", R.drawable.announcements_icon),
            MenuItem("Settings", R.drawable.settings_icon),
            MenuItem("Employees", R.drawable.employees_icon),
            MenuItem("Positions", R.drawable.position_icon),
            MenuItem("Locations", R.drawable.location_icon),
            MenuItem("Groups", R.drawable.groups_icon),
            MenuItem("Give us Feedback", R.drawable.feedback_icon),
            MenuItem("Help & Support", R.drawable.help_icon),
            MenuItem("Share OnTheTime", R.drawable.share_icon),
            MenuItem("Delete Account", R.drawable.delete_account_icon),
            MenuItem("Log Out", R.drawable.logout_icon),
        )
        _menuItems.value = items
    }
}
