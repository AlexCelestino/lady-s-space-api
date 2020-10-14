package com.ladys.space.api.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity
@Table(name = "tb_emergency_contact")
data class EmergencyContactModel(

        @Id
        @GeneratedValue(strategy = IDENTITY)
        @JsonProperty(value = "emergency_contact_id")
        @Column(name = "emergency_contact_id")
        val id: Int? = null,

        @Column(length = 30, nullable = true)
        var name: String? = null,

        @Column(length = 2, nullable = true)
        var areaCode: String? = null,

        @Column(length = 9, nullable = true)
        var emergencyPhone: String? = null,

        @ManyToOne
        @JoinColumn(name = "user_id", foreignKey = ForeignKey(name = "c_user_id"))
        var user: UserModel? = null

) : Serializable