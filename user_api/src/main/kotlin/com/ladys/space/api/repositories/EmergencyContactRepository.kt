package com.ladys.space.api.repositories

import com.ladys.space.api.models.EmergencyContactModel
import org.springframework.data.repository.CrudRepository

interface EmergencyContactRepository : CrudRepository<EmergencyContactModel, Int>