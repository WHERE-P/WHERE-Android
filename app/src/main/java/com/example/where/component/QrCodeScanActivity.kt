package com.example.where.component

import QRCodeScane
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.where.databinding.ActivityQrCodeScaneBinding

class QrCodeScanActivity : AppCompatActivity() {
    private lateinit var binding : ActivityQrCodeScaneBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrCodeScaneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var qrCodeScan = QRCodeScane(this)

        /** Click */
        binding.tvQrScan.setOnClickListener {
            qrCodeScan.startQRScan()
        }
    }
}