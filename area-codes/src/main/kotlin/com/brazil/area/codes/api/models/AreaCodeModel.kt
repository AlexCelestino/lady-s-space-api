package com.brazil.area.codes.api.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.NaturalId
import java.io.Serializable
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity
@Table(
        name = "tb_area_code",
        uniqueConstraints = [UniqueConstraint(name = "c_area_code_unique", columnNames = ["areaCode"])]
)
data class AreaCodeModel(

        @Id
        @JsonIgnore
        @Column(name = "area_code_id")
        @GeneratedValue(strategy = IDENTITY)
        val id: Int? = null,

        @NaturalId(mutable = false)
        @Column(length = 2, nullable = false)
        val areaCode: String,

        @ManyToOne
        @JsonProperty(value = "usedBy")
        @JoinColumn(name = "state_id", foreignKey = ForeignKey(name = "c_state_id"))
        val state: StateModel

) : Serializable