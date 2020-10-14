package com.ladys.space.api.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity
@Table(name = "tb_contact")
data class ContactModel(

        @Id
        @Column(name = "contact_id")
        @JsonProperty(value = "contact_id")
        @GeneratedValue(strategy = IDENTITY)
        val id: Int? = null,

        @Column(length = 2, nullable = false)
        var areaCode: String,

        @Column(length = 9, nullable = false)
        var phoneNumber: String,

        @OneToOne(mappedBy = "contact")
        var user: UserModel? = null

) : Serializable