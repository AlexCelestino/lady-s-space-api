package com.brazil.area.codes.api.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.NaturalId
import java.io.Serializable
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity
@JsonIgnoreProperties(value = ["id", "areaCodes"])
@Table(
        name = "tb_states",
        uniqueConstraints = [UniqueConstraint(name = "c_state_unique", columnNames = ["state", "federativeUnit"])]
)
data class StateModel(

        @Id
        @Column(name = "state_id")
        @GeneratedValue(strategy = IDENTITY)
        val id: Int? = null,

        @NaturalId(mutable = false)
        @Column(length = 30, nullable = false)
        val state: String,

        @NaturalId(mutable = false)
        @JsonProperty(value = "code")
        @Column(length = 2, nullable = false)
        val federativeUnit: String,

        @OneToMany(mappedBy = "state")
        val areaCodes: List<AreaCodeModel>? = null

) : Serializable