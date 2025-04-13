package bisen.harshal.medicationremainderapp.domain.use_cases

import bisen.harshal.medicationremainderapp.domain.model.Reminder
import bisen.harshal.medicationremainderapp.domain.repository.ReminderRepository
import javax.inject.Inject

class UpdateUseCase @Inject constructor(private val reminderRepository: ReminderRepository) {

    suspend operator fun invoke(reminder: Reminder)  = reminderRepository.update(reminder)


}