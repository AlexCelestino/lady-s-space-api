package com.ladys.space.api.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity
@Table(name = "tb_address")
@JsonIgnoreProperties(value = ["user", "address_id"])
data class AddressModel(

        @Id
        @Column(name = "address_id")
        @JsonProperty(value = "address_id")
        @GeneratedValue(strategy = IDENTITY)
        val id: Int? = null,

        @Column(length = 8, nullable = false)
        var zipCode: String,

        @Column(length = 50, nullable = false)
        var address: String,

        @Column(length = 50, nullable = false)
        var neighbourhood: String,

        @Column(length = 50, nullable = false)
        var city: String,

        @Column(length = 50, nullable = false)
        var state: String,

        @OneToOne(mappedBy = "address")
        var user: UserModel? = null

) : Serializable