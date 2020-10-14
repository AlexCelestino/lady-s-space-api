package com.ladys.space.api.repositories

import com.ladys.space.api.models.ContactModel
import org.springframework.data.repository.CrudRepository

interface ContactRepository : CrudRepository<ContactModel, Int>