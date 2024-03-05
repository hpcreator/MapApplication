package com.creator.myapplication.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.creator.myapplication.R
import com.creator.myapplication.base.BaseFragment
import com.creator.myapplication.databinding.FragmentMapBinding
import com.creator.myapplication.model.LatLong
import com.creator.myapplication.utils.setSafeClickListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : BaseFragment(), LocationListener, OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    private lateinit var locationManager: LocationManager
    private var lats: Double = 0.0
    private var longs: Double = 0.0
    private var gMap: GoogleMap? = null
    private var state: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView(R.layout.fragment_map)
        state = savedInstanceState
    }

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getCurrentLocation()
            }
        }

    private fun getCurrentLocation() {
        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
    }

    override fun initView() {
        binding = getBinding()
        binding.apply {
            btnChoose.setSafeClickListener {
                val latLong = LatLong(lats, longs)
                findNavController().navigate(MapFragmentDirections.actionMapFragmentToWeatherInfoFragment(latLong))
            }
            checkLocationPermission()
            mapView.getMapAsync(this@MapFragment)
        }
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        } else {
            getCurrentLocation()
        }
    }

    override fun onLocationChanged(location: Location) {
        lats = location.latitude
        longs = location.longitude
        Log.e("TAG", "initView: $lats")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        gMap?.isMyLocationEnabled = true
        val position = LatLng(lats, longs)
        gMap?.addMarker(MarkerOptions().position(position))
        gMap?.moveCamera(CameraUpdateFactory.newLatLng(position))

    }
}