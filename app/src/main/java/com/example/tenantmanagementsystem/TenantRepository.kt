package com.example.tenantmanagementsystem

// "object" means there is exactly ONE of these in the whole app.
// Every screen sees the same list of tenants.
object TenantRepository {

    // The shared list of saved tenants (lives in memory only)
    val tenants = mutableListOf<Tenant>()

    fun addTenant(tenant: Tenant) {
        tenants.add(tenant)
    }

    // Adds up every tenant's rent. If the text isn't a valid number, it counts as 0.0
    fun totalRent(): Double {
        return tenants.sumOf { it.rent.toDoubleOrNull() ?: 0.0 }
    }
}