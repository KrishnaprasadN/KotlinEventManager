package com.devteam.eventmanager.utils

import android.util.Log
import com.devteam.eventmanager.model.Event
import com.google.firebase.firestore.FirebaseFirestore

object FirestoreUtils {
    private const val COLLECTION_EVENTS = "events"

    fun addEvent(firestore: FirebaseFirestore, event: Event) {
        Log.d("KP", "*** FirestoreUtils - addEvent $firestore, event is $event" )

        firestore.collection(COLLECTION_EVENTS)
            .add(event)
            .addOnSuccessListener { documentReference ->
                Log.d("KP", "**** DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("KP", "**** Error adding document", e)
            }
    }
}