package com.example.proyectosimagrow.fragments

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.proyectosimagrow.R
import com.example.proyectosimagrow.databinding.FragmentConfiguracionBinding
import java.util.Locale

class ConfiguracionFragment : Fragment() {

    private lateinit var binding: FragmentConfiguracionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfiguracionBinding.inflate(inflater, container, false)

        setupDarkMode()
        setupLanguage()

        return binding.root
    }

    private fun setupDarkMode() {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        binding.switchDarkMode.isChecked = currentNightMode == Configuration.UI_MODE_NIGHT_YES

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun setupLanguage() {
        val currentLocale = AppCompatDelegate.getApplicationLocales().get(0)?.language ?: Locale.getDefault().language
        when (currentLocale) {
            "ca" -> binding.rbCatalan.isChecked = true
            "en" -> binding.rbEnglish.isChecked = true
            else -> binding.rbSpanish.isChecked = true
        }

        binding.rgLanguage.setOnCheckedChangeListener { _, checkedId ->
            val localeCode = when (checkedId) {
                R.id.rbCatalan -> "ca"
                R.id.rbEnglish -> "en"
                else -> "es"
            }
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(localeCode)
            AppCompatDelegate.setApplicationLocales(appLocale)
        }
    }
}