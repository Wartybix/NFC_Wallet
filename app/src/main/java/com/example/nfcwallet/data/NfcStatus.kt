package com.example.nfcwallet.data

enum class NfcStatus {
    Enabled, //For when NFC is enabled in the system settings
    Disabled, //For when NFC is supported by the device, but disabled in the settings
    Unsupported //For when NFC is unsupported by the device.
}