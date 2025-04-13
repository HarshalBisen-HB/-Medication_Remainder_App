package bisen.harshal.medicationremainderapp.data.repository

import bisen.harshal.medicationremainderapp.data.local.ReminderDao
import bisen.harshal.medicationremainderapp.domain.model.Reminder
import bisen.harshal.medicationremainderapp.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow

class ReminderRepoImpl(private val reminderDao: ReminderDao ) :  ReminderRepository {

    override suspend fun insert(reminder: Reminder) {
        reminderDao.insert(reminder)
    }

    override suspend fun update(reminder: Reminder) {
       reminderDao.update(reminder)
    }

    override suspend fun delete(reminder: Reminder) {
        reminderDao.delete(reminder)
    }

    override fun getAllReminders(): Flow<List<Reminder>>  = reminderDao.getAllReminder()

}