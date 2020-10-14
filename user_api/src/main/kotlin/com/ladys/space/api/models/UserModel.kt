package com.ladys.space.api.models

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.NaturalId
import java.io.Serializable
import java.time.LocalDate
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(value = ["address", "contact", "emergencyContacts", "dependent"])
@Table(name = "tb_user", uniqueConstraints = [UniqueConstraint(name = "c_email", columnNames = ["email"])])
data class UserModel(

        @Id
        @Column(name = "user_id")
        @JsonProperty(value = "user_id")
        @GeneratedValue(strategy = IDENTITY)
        val id: Int? = null,

        @Column(length = 20, nullable = false)
        val name: String,

        @Column(length = 30, nullable = false)
        val lastName: String,

        @NaturalId(mutable = false)
        @Column(length = 70, nullable = false)
        val email: String,

        @Column(nullable = false)
        @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
        val birthDate: LocalDate,

        @Column(length = 1, nullable = false)
        val gender: Char,

        @Column(length = 150, nullable = false)
        val password: String,

        @OneToOne
        @JoinColumn(
                name = "contact_contact_id",
                referencedColumnName = "contact_id",
                foreignKey = ForeignKey(name = "c_contact_id")
        )
        var contact: ContactModel? = null,

        @OneToOne
        @JoinColumn(
                name = "address_address_id",
                referencedColumnName = "address_id",
                foreignKey = ForeignKey(name = "c_address_id")
        )
        var address: AddressModel? = null,

        @OneToMany(mappedBy = "user")
        val emergencyContacts: List<EmergencyContactModel>? = null

) : Serializable