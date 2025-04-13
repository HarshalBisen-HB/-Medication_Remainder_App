package bisen.harshal.medicationremainderapp.data.di

import android.content.Context
import bisen.harshal.medicationremainderapp.data.local.ReminderDao
import bisen.harshal.medicationremainderapp.data.local.ReminderDatabase
import bisen.harshal.medicationremainderapp.data.repository.ReminderRepoImpl
import bisen.harshal.medicationremainderapp.domain.repository.ReminderRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object DataModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): ReminderDatabase {
        return ReminderDatabase.getInstance(context)

    }

    @Provides
    fun provideDao(reminderDatabase: ReminderDatabase): ReminderDao {
        return reminderDatabase.getReminderDao()
    }

    @Provides
    fun provideReminderRepo(reminderDao: ReminderDao) : ReminderRepository {
        return ReminderRepoImpl(reminderDao)
    }

}