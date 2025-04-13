package bisen.harshal.medicationremainderapp.domain.use_cases

import bisen.harshal.medicationremainderapp.domain.repository.ReminderRepository
import javax.inject.Inject

class GetAllReminderUseCase @Inject constructor(private val reminderRepository: ReminderRepository) {

   operator fun invoke() =reminderRepository.getAllReminders()


}