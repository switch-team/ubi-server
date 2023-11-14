package dev.jombi.ubi.auth.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "authority")
data class Authority(
    @Id
    @Column(name = "authority", length = 64)
    val authorityName: String
)
