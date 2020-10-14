package com.ladys.space.api.repositories

import com.ladys.space.api.models.UserModel
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface UserRepository : CrudRepository<UserModel, Int> {

    fun findByEmail(@Param("email") email: String): UserModel?

}