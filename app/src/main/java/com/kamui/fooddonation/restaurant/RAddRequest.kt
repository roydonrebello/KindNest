
package com.kamui.fooddonation.restaurant

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import com.kamui.fooddonation.BaseActivity
import com.kamui.fooddonation.FireStoreClass
import com.kamui.fooddonation.R
import com.kamui.fooddonation.data.Donation
import com.kamui.fooddonation.data.RestaurantData
import java.io.IOException
import java.util.Locale
import java.util.UUID

class RAddRequest : BaseActivity() {

    private val datePattern = "^(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/\\d{4}$"
    private val validFoodTypes = listOf("veg", "non-veg", "perishable", "non-perishable")
    private lateinit var imageView: ImageView
    private var selectedBitmap: Bitmap? = null

    // Create Activity Result Launcher for selecting images
    private val imageSelectionLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            selectedBitmap = getBitmapFromUri(uri)
            imageView.setImageBitmap(selectedBitmap)
        }
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_radd_request)

        // Set the title and enable the back button
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.action_bar_title)

        val title = findViewById<TextView>(R.id.action_bar_title)
        title.text = "Add Food Request"

        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)


        // Set up the listeners for the food item edit text fields
        // Find ImageView and set default image
        imageView = findViewById(R.id.imageView)
        imageView.setImageResource(R.drawable.mercy_04)
        imageView.setOnClickListener {
            // Launch image selection activity
            imageSelectionLauncher.launch("image/*")
        }
        val foodNameInput = findViewById<EditText>(R.id.food_name_input)
        val foodNameLabel = findViewById<TextView>(R.id.foodNameLabel)
        val foodTypeSpinner: Spinner = findViewById(R.id.foodTypeSpinner)
        val foodTypeLabel: TextView = findViewById(R.id.foodTypeLabel)

        val foodTypes1 = arrayOf("Select Food Type", "veg", "non-veg", "perishable", "non-perishable")
        val foodTypeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, foodTypes1)
        foodTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        foodTypeSpinner.adapter = foodTypeAdapter

        foodTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                foodTypeLabel.visibility = if (position > 0) View.VISIBLE else View.GONE
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                foodTypeLabel.visibility = View.GONE
            }
        }
        val quantitySpinner: Spinner = findViewById(R.id.foodQuantitySpinner)
        val quantityLabel: TextView = findViewById(R.id.quantityLabel)

        val quantityOptions = arrayOf("Select Quantity", "1", "2", "3", "4", "5", "10", "20", "50")
        val quantityAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, quantityOptions)
        quantityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        quantitySpinner.adapter = quantityAdapter
        quantitySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                quantityLabel.visibility = if (position > 0) View.VISIBLE else View.GONE
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                quantityLabel.visibility = View.GONE
            }
        }
        val expirationDateInput = findViewById<EditText>(R.id.expirationDateInput)
        val expirationDateLabel = findViewById<TextView>(R.id.expirationDateLabel)

        setOnFocusChangeListener(foodNameInput, "Food Name", foodNameLabel)
        setOnFocusChangeListener(expirationDateInput, "Expiry Date", expirationDateLabel)


        val foodTypes = arrayOf("kg", "litre", "piece", "slices")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, foodTypes)

        val foodUnitQuantitySpinner: Spinner = findViewById(R.id.foodUnitQuantitySpinner)
        foodUnitQuantitySpinner.adapter = adapter
        val selectedFoodUnitType = foodUnitQuantitySpinner.selectedItem as String

        val addDonationBtn = findViewById<Button>(R.id.uploadBtn)

        addDonationBtn.setOnClickListener {
            // create an instance of FirestoreClass
            val fireStoreClass = FireStoreClass()
            val textFoodName = foodNameInput.text.toString()

            val selectedFoodType = foodTypeSpinner.selectedItem as String
            val textFoodType = selectedFoodType
            Log.d("Testing","food type is $selectedFoodType")

            val selectedFoodQuantity = quantitySpinner.selectedItem as String
            val textFoodQuantity = selectedFoodQuantity+selectedFoodUnitType
            val textExpirationDate = expirationDateInput.text.toString()
            val moduleData = RestaurantData::class.java
            // replace with the actual user ID
            val userUid =fireStoreClass.getCurrentUserID()
            val moduleCollection = "restaurant" // replace with the actual module collection name
            // Clear the input fields after the donation has been added
            if (validateForm(textFoodName, textFoodType, textFoodQuantity, textExpirationDate)) {
                showProgressDialog(resources.getString(R.string.please_wait))
                foodNameInput.text.clear()
                expirationDateInput.text.clear()
                imageView.setImageResource(R.drawable.mercy_04)
                fireStoreClass.getUserData(moduleCollection, moduleData,userUid) { userData ->
                    // handle the user data here
                    val restaurantName = userData?.restaurantName.toString()
                    val pickUpAddress = userData?.location!!
                    userData.fcmToken
                    // Fetch the address from the latitude and longitude using geocoder
                    val geocoder = Geocoder(this, Locale.getDefault())
                    var addresses: List<Address?>? = null
                    try {

                        addresses = geocoder.getFromLocation(pickUpAddress.latitude,pickUpAddress.longitude, 1
                        )
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    // Update the donation object with the address
                    val donorAddress = addresses?.get(0)?.getAddressLine(0)
                    val pickupDate = Timestamp.now()

                    // Use the new ID to create the donation
                    val donation = Donation(
                        donor = restaurantName,
                        donorId = fireStoreClass.getCurrentUserID(),
                        pickupAddress = pickUpAddress,
                        donorAddress = donorAddress,
                        status = "unapproved",
                        foodName = textFoodName,
                        foodQuantity = textFoodQuantity,
                        foodType = textFoodType,
                        expiryDate = textExpirationDate,
                        pickupDate = pickupDate.toDate()
                    )
                    // call the addDonation function with the Donation object and a completion block
                    fireStoreClass.addDonation(donation,selectedBitmap) {
                        // Handle success
                        val message = RemoteMessage.Builder("Admin-app")
                            .setMessageId(UUID.randomUUID().toString())
                            .setData(
                                mapOf(
                                    "title" to "New donation waiting for approval",
                                    "body" to "Restaurant ${donation.donor} has made a new donation that needs to be approved",
                                    "donationId" to donation.donationId,
                                    "restaurantId" to donation.donorId
                                )
                            )
                            .build()

                        FirebaseMessaging.getInstance().send(message)


                        hideProgressDialog()
                        Toast.makeText(
                            this,
                            "Donation added successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // If the home button is pressed, finish the activity
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun validateForm(textFoodName:String,textFoodType:String,textFoodQuantity:String,textExpirationDate:String): Boolean {
        return when {
            ((TextUtils.isEmpty(textFoodName))||(TextUtils.isEmpty(textFoodType))||(TextUtils.isEmpty(textFoodQuantity))||(TextUtils.isEmpty(textExpirationDate)))->{
                showErrorSnackBar("Please filled every field")
                false
            }
            (!textExpirationDate.matches(Regex(datePattern)))->{
                showErrorSnackBar("Please enter date in correct format eg.(dd/mm/yyyy)")
                false
            }
            else -> {
                true
            }
        }
    }
}


