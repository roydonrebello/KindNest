package com.kamui.fooddonation.volunteer

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.kamui.fooddonation.BassFragment
import com.kamui.fooddonation.FireStoreClass
import com.kamui.fooddonation.R
import com.kamui.fooddonation.data.Donation
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import java.io.IOException
import java.util.Locale

class TrackFragment : BassFragment() {
    private val donationsList = ArrayList<Donation>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var donationAdapter: DonationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_track, container, false)

        // Initialize the RecyclerView and the adapter
        recyclerView = view.findViewById(R.id.recycler_view)
        showProgressDialog("Fetching Data")

        // Set the layout manager and the adapter for the RecyclerView
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        // Load the donations from the server
//        loadMyDonations()

        val currentUserUid = FireStoreClass().getCurrentUserID()
        FirebaseFirestore.getInstance().collection("donations")
            .whereEqualTo("donorId", currentUserUid)
            .whereEqualTo("status", "claimedByVol")
            .get()
            .addOnSuccessListener {
                Log.d("PendingData", currentUserUid)
                getDonationList()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error querying donations", e)
            }

        donationAdapter = DonationAdapter(requireContext(), donationsList,this)
        recyclerView.adapter = donationAdapter

        // Set the listener for the "Claim" button in the adapter
        donationAdapter.setOnTrackClickListener(object : DonationAdapter.OnTrackClickListener {
            @SuppressLint("MissingInflatedId", "SetTextI18n")
            override fun onTrackClick(position: Int) {
                val selectedDonation = donationsList[position]
                val donorAddress = selectedDonation.pickupAddress
                val receiverAddress = selectedDonation.destAddress

                if (donorAddress != null && receiverAddress != null) {
                    val donorLat = donorAddress.latitude
                    val donorLng = donorAddress.longitude
                    val receiverLat = receiverAddress.latitude
                    val receiverLng = receiverAddress.longitude

                    val uri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=$donorLat,$donorLng&destination=$receiverLat,$receiverLng&travelmode=driving")
                    val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    startActivity(mapIntent)

                } else {
                    Toast.makeText(requireContext(), "Unable to get donation location.", Toast.LENGTH_SHORT).show()
                }
            }
        })

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getDonationList() {
        FireStoreClass().listenForDonationUpdates("claimedByVol",
            onUpdate = { donations ->
                // Update the adapter with the new donations data
//                donationsList.clear()
                for (donation in donations) {
                    // Fetch the donor address and receiver address from the latitude and longitude using geocoder
                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    var donAddresses: List<Address?>? = null
                    var recAddresses: List<Address?>? = null
                    try {
                        donAddresses = geocoder.getFromLocation(donation.pickupAddress?.latitude!!,
                            donation.pickupAddress!!.longitude, 1)

                        recAddresses = geocoder.getFromLocation(donation.destAddress?.latitude!!,
                            donation.destAddress.longitude, 1)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    // Update the donation object with the address
                    val address = donAddresses?.get(0)
                    donation.donorAddress = "${address?.locality ?: ""}, ${address?.subLocality ?: ""}"

                    // Update the donation object with the address
                    val recAddress = recAddresses?.get(0)
                    donation.receiverAddress = "${recAddress?.locality ?: ""}, ${recAddress?.subLocality ?: ""}"

                    // Add the updated donation to the list
                    donationsList.addAll(donations)
                }
                donationsList.addAll(donations)
                donationAdapter.updateDonations(donations)
                Log.d("DonationListUpdate",donationsList.toString())
                hideProgressDialog()
            },
            onError = { error ->
                // Handle the error, for example by displaying an error message
                Toast.makeText(requireContext(), "Error listening for donation updates", Toast.LENGTH_SHORT).show()
                Log.e("Firestore", "Error listening for donation updates", error)
            }
        )
    }
}
