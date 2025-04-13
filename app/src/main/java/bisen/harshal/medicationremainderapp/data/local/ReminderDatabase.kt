package bisen.harshal.medicationremainderapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import bisen.harshal.medicationremainderapp.domain.model.Reminder
import dagger.hilt.android.internal.Contexts

@Database([Reminder::class], version = 1)
abstract class ReminderDatabase : RoomDatabase() {

     companion object{
         fun getInstance(context: Context)  = Room.databaseBuilder(context,ReminderDatabase::class.java,"reminder")
             .build()
     }

    abstract fun getReminderDao(): ReminderDao



}