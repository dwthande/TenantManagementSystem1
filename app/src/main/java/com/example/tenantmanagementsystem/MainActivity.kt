package com.example.tenantmanagementsystem

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tenantmanagementsystem.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // The most recently saved tenant. Lost on rotation unless we save it
    private var lastTenant: Tenant? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Write a message to Logcat every time onCreate() runs
        Log.d("TENANT_LIFECYCLE", "MainActivity: onCreate")

        // Create the views from activity_main.xml and show them
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ── Lab 2: welcome Toast (only the first time, not after rotating) ──
        val email = intent.getStringExtra("EMAIL")
        if (email != null && savedInstanceState == null) {
            Toast.makeText(this, "Logged in as $email", Toast.LENGTH_LONG).show()
        }

        // ── RESTORE STATE (only after a rotation) ─────────────────
        // savedInstanceState is null the first time the Activity opens
        if (savedInstanceState != null) {
            val name = savedInstanceState.getString("tenant_name")
            val phone = savedInstanceState.getString("tenant_phone")
            val rent = savedInstanceState.getString("tenant_rent")

            if (name != null && phone != null && rent != null) {
                val tenant = Tenant(name, phone, rent)
                lastTenant = tenant      // so CALL and SHARE work again
                binding.tenant = tenant  // so the summary appears again
            }
        }

        // ── SAVE button (with validation) ─────────────────────────
        binding.saveButton.setOnClickListener {
            val name = binding.tenantNameEditText.text.toString().trim()
            val phone = binding.phoneEditText.text.toString().trim()
            val rent = binding.rentEditText.text.toString().trim()

            var isValid = true

            if (name.isEmpty()) {
                binding.tenantNameEditText.error = "Required"
                isValid = false
            }
            if (phone.isEmpty()) {
                binding.phoneEditText.error = "Required"
                isValid = false
            }
            if (rent.isEmpty()) {
                binding.rentEditText.error = "Required"
                isValid = false
            }

            if (!isValid) {
                return@setOnClickListener
            }

            val tenant = Tenant(name, phone, rent)
            binding.tenant = tenant  // Data Binding shows the summary
            lastTenant = tenant      // remember it for CALL and SHARE
        }

        // ── CALL TENANT button (implicit Intent) ──────────────────
        binding.callButton.setOnClickListener {
            val tenant = lastTenant
            if (tenant == null) {
                Toast.makeText(this, "Save a tenant first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${tenant.phone}"))
            startActivity(intent)
        }

        // ── SHARE button (implicit Intent) ────────────────────────
        binding.shareButton.setOnClickListener {
            val tenant = lastTenant
            if (tenant == null) {
                Toast.makeText(this, "Save a tenant first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, tenant.summary())
            }
            startActivity(Intent.createChooser(shareIntent, "Share tenant via"))
        }
    }

    // ── SAVE STATE (just before the Activity is destroyed) ────────
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("TENANT_LIFECYCLE", "MainActivity: onSaveInstanceState")

        // Copy into a val so Kotlin knows it can't change after the check
        val tenant = lastTenant
        if (tenant != null) {
            // Store each field under its own KEY
            outState.putString("tenant_name", tenant.name)
            outState.putString("tenant_phone", tenant.phone)
            outState.putString("tenant_rent", tenant.rent)
        }
    }

    // ── LIFECYCLE METHODS ─────────────────────────────────────────
    // The screen is now VISIBLE
    override fun onStart() {
        super.onStart()
        Log.d("TENANT_LIFECYCLE", "MainActivity: onStart")
    }

    // The screen is in FRONT and the user can interact with it
    override fun onResume() {
        super.onResume()
        Log.d("TENANT_LIFECYCLE", "MainActivity: onResume")
    }

    // The screen is LEAVING the front
    override fun onPause() {
        super.onPause()
        Log.d("TENANT_LIFECYCLE", "MainActivity: onPause")
    }

    // The screen is NO LONGER VISIBLE
    override fun onStop() {
        super.onStop()
        Log.d("TENANT_LIFECYCLE", "MainActivity: onStop")
    }

    // The user came BACK to a stopped screen
    override fun onRestart() {
        super.onRestart()
        Log.d("TENANT_LIFECYCLE", "MainActivity: onRestart")
    }

    // The screen is being DESTROYED
    override fun onDestroy() {
        super.onDestroy()
        Log.d("TENANT_LIFECYCLE", "MainActivity: onDestroy")
    }
}