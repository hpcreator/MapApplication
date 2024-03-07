package com.creator.myapplication.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.creator.myapplication.R
import com.creator.myapplication.base.BaseFragment
import com.creator.myapplication.databinding.FragmentMapBinding
import com.creator.myapplication.model.LatLong
import com.creator.myapplication.utils.setSafeClickListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.Locale


class MapFragment : BaseFragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentLocation: Location? = null
    private lateinit var latLong: LatLong
    private var mapFragment: SupportMapFragment? = null
    private var currentMarker: Marker? = null
    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView(R.layout.fragment_map)
    }

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                if (isLocationEnabled()) {
                    getCurrentLocation()
                } else {
                    showLocationAlert()
                }
            }
        }

    private fun showLocationAlert() {
        MaterialAlertDialogBuilder(requireContext()).setTitle("Location Service")
            .setMessage("This app requires location service to be enable to work properly. Press OK to grant location permission.")
            .setPositiveButton("OK") { dialog, _ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                dialog.dismiss()
            }.show()
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) currentLocation = location
            latLong = LatLong(location.latitude, location.longitude)

            mapFragment?.getMapAsync { map ->
                drawMarker(map, LatLng(latLong.lat, latLong.long))
            }
            mapFragment?.getMapAsync(this)
        }
    }

    @Suppress("DEPRECATION")
    private fun getAddress(latLng: LatLng): String {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        return addresses?.first()?.getAddressLine(0).toString()
    }

    private fun isLocationEnabled(): Boolean {
        val locationMode: Int = try {
            Settings.Secure.getInt(requireContext().contentResolver, Settings.Secure.LOCATION_MODE)
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return locationMode != Settings.Secure.LOCATION_MODE_OFF
    }

    override fun initView() {
        binding = getBinding()
        locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        binding.apply {
            btnChoose.setSafeClickListener {
                findNavController().navigate(
                    MapFragmentDirections.actionMapFragmentToWeatherInfoFragment(
                        latLong
                    )
                )
            }
            mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        }
    }

    override fun onResume() {
        super.onResume()
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            if (isLocationEnabled()) {
                getCurrentLocation()
            } else {
                showLocationAlert()
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        map.setOnMarkerDragListener(object : OnMarkerDragListener {
            override fun onMarkerDrag(p0: Marker) {
            }

            override fun onMarkerDragEnd(marker: Marker) {
                if (currentMarker != null) {
                    currentMarker?.remove()
                }
                val newLoc = LatLng(marker.position.latitude, marker.position.longitude)
                drawMarker(map, newLoc)
                latLong = LatLong(newLoc.latitude, newLoc.longitude)
            }

            override fun onMarkerDragStart(p0: Marker) {
            }
        })
    }

    private fun drawMarker(map: GoogleMap, latLng: LatLng) {
        map.clear()
        val markerOptions = MarkerOptions().position(latLng).title("You are here.")
            .snippet(getAddress(LatLng(latLng.latitude, latLng.longitude))).draggable(true)
        map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(latLng.latitude, latLng.longitude)))
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    latLng.latitude, latLng.longitude
                ), 15f
            )
        )
        map.uiSettings.isZoomControlsEnabled = true
        currentMarker = map.addMarker(markerOptions)
        currentMarker?.showInfoWindow()
    }
}