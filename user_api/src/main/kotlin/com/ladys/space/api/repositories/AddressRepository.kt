package com.ladys.space.api.repositories

import com.ladys.space.api.models.AddressModel
import org.springframework.data.repository.CrudRepository

interface AddressRepository : CrudRepository<AddressModel, Int>