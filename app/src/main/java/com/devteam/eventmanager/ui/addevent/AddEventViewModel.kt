package com.devteam.eventmanager.ui.addevent

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import com.devteam.eventmanager.common.ObservableViewModel
import com.devteam.eventmanager.model.Event
import com.devteam.eventmanager.utils.FirestoreUtils
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class AddEventViewModel : ObservableViewModel() {

    var event: Event? = Event("", "", "", "", "", "", "")
    var title: String = ""
    var des: String = ""
    var category: String = ""
    var date: String = ""
    var start: String = ""
    var end: String = ""
    var location: String = ""

    // Firestore reference
    val mFirestore: FirebaseFirestore by lazy {
        Firebase.firestore
    }

    val onFinish = MutableLiveData<Boolean>()

    @Bindable
    fun getEventTitle(): String {
        return title
    }

    fun setEventTitle(value: String) {
        // Avoids infinite loops.
        if (title != value) {
            title = value

            // Notify observers of a new value.
            //notifyPropertyChanged(BR.viewModel)
        }
    }

    @Bindable
    fun getEventDes(): String {
        return title
    }

    fun setEventDes(value: String) {
        if (des != value) {
            des = value
        }
    }

    @Bindable
    fun getEventCategory(): String {
        return category
    }

    fun setEventCategory(value: String) {
        // Avoids infinite loops.
        if (category != value) {
            category = value
        }
    }

    @Bindable
    fun getEventDate(): String {
        return date
    }

    fun setEventDate(value: String) {
        // Avoids infinite loops.
        if (date != value) {
            date = value
        }
    }

    @Bindable
    fun getEventStart(): String {
        return start
    }

    fun setEventStart(value: String) {
        // Avoids infinite loops.
        if (start != value) {
            start = value
        }
    }

    @Bindable
    fun getEventEnd(): String {
        return end
    }

    fun setEventEnd(value: String) {
        // Avoids infinite loops.
        if (end != value) {
            end = value
        }
    }

    @Bindable
    fun getEventLocation(): String {
        return location
    }

    fun setEventLocation(value: String) {
        // Avoids infinite loops.
        if (location != value) {
            location = value
        }
    }

    fun addEvent() {
        Log.d(
            "KP", "*** Add Event " +
                    "title is $title" +
                    " desc is $des " +
                    " category is $category " +
                    " date is $date " +
                    " start is $start " +
                    " end is $end " +
                    " location is $location "
        )

        // create the event
        var event = Event(title, des, category, date, start, end, location)

        // add the event
        FirestoreUtils.addEvent(mFirestore, event)

        onFinish.value = true
    }

}

