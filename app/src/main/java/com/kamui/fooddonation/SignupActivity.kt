package com.kamui.fooddonation

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.kamui.fooddonation.admin.AHomePage
import com.kamui.fooddonation.data.AdminData
import com.kamui.fooddonation.data.NgoData
import com.kamui.fooddonation.data.ReceiverData
import com.kamui.fooddonation.data.RestaurantData
import com.kamui.fooddonation.data.VolunteerData
import com.kamui.fooddonation.ngo.NHomePage
import com.kamui.fooddonation.receiver.RcHomePage
import com.kamui.fooddonation.restaurant.RHomePage
import com.kamui.fooddonation.volunteer.VHomePage
import java.util.Locale
import java.util.UUID


private operator fun Boolean.invoke() {

}

class SignupActivity : BaseActivity() {

    companion object {
        private const val FILE_PICKER_REQUEST = 1001
    }

    //        common fields
    private lateinit var tvFirst: TextView
    private lateinit var tvSecond: TextView
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var fullNameInput: EditText
    private lateinit var addressInput: EditText
    private lateinit var locationIcon: EditText
    private lateinit var phoneInput: EditText

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var suggestionsListView: ListView
    private lateinit var api: NominatimApi
    private var selectedFileUri: Uri? = null
    private val storageRef = FirebaseStorage.getInstance().reference
    var textNgoRegistration: String? = null
    var downloadUrl: String? = null
    lateinit var ngoProgressBar: ProgressBar
    lateinit var ngoCertificateName: TextView

    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val signUpButton = findViewById<Button>(R.id.button2)

        //Admin controls
        val adminDesignation = findViewById<EditText>(R.id.admin_designation)
        val radioGroupAccess = findViewById<RadioGroup>(R.id.access_level_radio)

        //Restaurants controls
        val restaurantName = findViewById<EditText>(R.id.restaurant_name_input)
        val radioGroupResCuisine = findViewById<RadioGroup>(R.id.res_cuisine_radio)
        val radioGroupResFoodType = findViewById<RadioGroup>(R.id.res_food_type_radio)
        val radioGroupResOtherType = findViewById<RadioGroup>(R.id.res_other_type_radio)
        val radioGroupResFoodPickup = findViewById<RadioGroup>(R.id.res_food_pickup_radio)
        val radioGroupResOtherPickup = findViewById<RadioGroup>(R.id.res_other_pickup_radio)

        //NGO controls
        val ngoName = findViewById<EditText>(R.id.ngo_name_input)
        val radioGroupNgoFoodType = findViewById<RadioGroup>(R.id.ngo_food_type_radio)
        val radioGroupNgoOtherType = findViewById<RadioGroup>(R.id.ngo_other_type_radio)
        val radioGroupNgoOrganization = findViewById<RadioGroup>(R.id.ngo_organization_radio)
        val radioGroupNgoFoodPickup = findViewById<RadioGroup>(R.id.ngo_food_pickup_radio)
        val radioGroupNgoOtherPickup = findViewById<RadioGroup>(R.id.ngo_other_pickup_radio)
        val ngoRegistration = findViewById<EditText>(R.id.ngo_registration_no)
        val ngoCertificate = findViewById<Button>(R.id.btn_upload_certificate)
        ngoProgressBar = findViewById<ProgressBar>(R.id.progressBar)
        ngoCertificateName = findViewById<TextView>(R.id.tv_certificate_filename)

        //Receiver controls
        val revPickupTime = findViewById<EditText>(R.id.rev_pickup_time)

        //Volunteer controls
        val radioGroupVolAvailability = findViewById<RadioGroup>(R.id.vol_availability_radio)
        val radioGroupVolPreferences = findViewById<RadioGroup>(R.id.vol_pref_radio)

        //all Five Fields
        val restaurantFields = findViewById<LinearLayout>(R.id.restaurant_fields)
        val receiverFields = findViewById<LinearLayout>(R.id.receiver_fields)
        val ngoFields = findViewById<LinearLayout>(R.id.ngo_fields)
        val volunteerFields = findViewById<LinearLayout>(R.id.volunteer_fields)
        val adminFields = findViewById<LinearLayout>(R.id.admin_fields)


        tvFirst = findViewById(R.id.tv_firstText)
        tvSecond = findViewById(R.id.tv_second)
        emailInput = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)
        confirmPasswordInput = findViewById(R.id.confirm_password_input)
        fullNameInput = findViewById(R.id.full_name_input)
        addressInput = findViewById(R.id.address_input)
        locationIcon = findViewById(R.id.location_icon)
        phoneInput = findViewById(R.id.phone_input)

        suggestionsListView = findViewById(R.id.suggestions_list_view)
        api = NominatimApi()

        Log.d("fullNameInput", fullNameInput.toString())
        Log.d("addressInput", addressInput.toString())

        // Get the location icon drawable
        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_location)

// Set the drawable to the left of the EditText
        locationIcon.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)

        var currentAddress: String

        locationIcon.setOnTouchListener { _, event ->
            showProgressDialog(resources.getString(R.string.please_wait))
            if (event.action == MotionEvent.ACTION_UP) {
                // Get the user's current location
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ),
                        100
                    )
                }
                else {
                    fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                        if (location != null) {
                            val geocoder = Geocoder(this, Locale.getDefault())
                            try {
                                val addresses =
                                    geocoder.getFromLocation(location.latitude, location.longitude, 1)
                                if (addresses.isNotEmpty()) { // Check that the addresses list is not empty
                                    currentAddress = addresses[0].getAddressLine(0)
                                    addressInput.setText(currentAddress)
                                } else {
                                    // Handle the case where no address is found

                                }
                            }catch (error:Exception){
                                Toast.makeText(this,"Error to fetch location",Toast.LENGTH_LONG).show()
                            }
                            addressInput.isEnabled = true // Make the EditText editable
                        }
                        else {
                            Toast.makeText(this, "Unable to retrieve address", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                }
                hideProgressDialog()
            }
            hideProgressDialog()
            true
        }

        when(intent.getStringExtra("previousActivity")){
            "RHomePage" -> {
                restaurantFields.visibility = View.VISIBLE
            }
            "RcHomePage" -> {
                receiverFields.visibility = View.VISIBLE
            }
            "NHomePage" -> {
                ngoFields.visibility = View.VISIBLE
            }
            "ALoginPage" -> {
                adminFields.visibility = View.VISIBLE
            }
            "VHomePage" -> {
                volunteerFields.visibility = View.VISIBLE
            }
        }
        ngoCertificate.setOnClickListener {
            openFilePicker()
        }

        signUpButton.setOnClickListener {

            val selectedAdminAccessId = radioGroupAccess.checkedRadioButtonId
            val radioButtonAdminAccess=findViewById<RadioButton>(selectedAdminAccessId)

            val selectedResCuisineId = radioGroupResCuisine.checkedRadioButtonId
            val radioButtonResCuisine=findViewById<RadioButton>(selectedResCuisineId)
            val selectedResFoodTypeId = radioGroupResFoodType.checkedRadioButtonId
            val radioButtonResFoodType =findViewById<RadioButton>(selectedResFoodTypeId)
            val selectedResOtherTypeId = radioGroupResOtherType.checkedRadioButtonId
            val radioButtonResOtherType = findViewById<RadioButton>(selectedResOtherTypeId)
            val selectedResFoodPickupId = radioGroupResFoodPickup.checkedRadioButtonId
            val radioButtonResFoodPickup =findViewById<RadioButton>(selectedResFoodPickupId)
            val selectedResOtherPickupTypeId = radioGroupResOtherPickup.checkedRadioButtonId
            val radioButtonResOtherPickup = findViewById<RadioButton>(selectedResOtherPickupTypeId)

            val selectedNgoFoodTypeId = radioGroupNgoFoodType.checkedRadioButtonId
            val radioButtonNgoFoodType=findViewById<RadioButton>(selectedNgoFoodTypeId)
            val selectedNgoOtherTypeId = radioGroupNgoOtherType.checkedRadioButtonId
            val radioButtonNgoOtherType = findViewById<RadioButton>(selectedNgoOtherTypeId)
            val selectedNgoOrganizationId = radioGroupNgoOrganization.checkedRadioButtonId
            val radioButtonNgoOrganization=findViewById<RadioButton>(selectedNgoOrganizationId)
            val selectedNgoFoodPickupId = radioGroupNgoFoodPickup.checkedRadioButtonId
            val radioButtonNgoFoodPickup=findViewById<RadioButton>(selectedNgoFoodPickupId)
            val selectedNgoOtherPickupId = radioGroupNgoOtherPickup.checkedRadioButtonId
            val radioButtonNgoOtherPickup = findViewById<RadioButton>(selectedNgoOtherPickupId)

            val selectedVolAvailabilityId = radioGroupVolAvailability.checkedRadioButtonId
            val radioButtonVolAvailability=findViewById<RadioButton>(selectedVolAvailabilityId)
            val selectedVolPreferencesId = radioGroupVolPreferences.checkedRadioButtonId
            val radioButtonVolPreferences=findViewById<RadioButton>(selectedVolPreferencesId)

            //obtain the entered data
            val textAdminDesignation = adminDesignation.text.toString()
            val textRestaurantName = restaurantName.text.toString()
            val textNgoName = ngoName.text.toString()
            textNgoRegistration = ngoRegistration.text.toString()
            val textRevPickupTime = revPickupTime.text.toString()

            var textAdminAccess: String?
            var textResCuisine: String?
            var textResFoodType: String?
            var textResOtherType: String?
            var textResPickupTime : String?
            var textResOtherPickupTime: String?
            var textNgoFoodType: String?
            var textNgoOtherType: String?
            var textNgoOrganizationType: String?
            var textNgoFoodPickup: String?
            var textNgoOtherPickup: String?
            var textVolAvailability: String?
            var textVolPreferences: String?

            val textFullName = fullNameInput.text.toString().trim {it <= ' '}
            val textEmail = emailInput.text.toString().trim {it <= ' '}
            val textPassword = passwordInput.text.toString().trim {it <= ' '}
            val textConfirmPwd = confirmPasswordInput.text.toString().trim {it <= ' '}
            val textPhone = phoneInput.text.toString().trim {it <= ' '}
            val textAddress = addressInput.text.toString().trim {it <= ' '}

            // Use Geocoder to get latitude and longitude from location string
            val geocoder = Geocoder(this)
            try {
                val addresses = geocoder.getFromLocationName(textAddress, 1)
                val lat = addresses[0].latitude
                val lng = addresses[0].longitude

                // Save the user's location to Firestore
                val userLocation = GeoPoint(lat, lng)


            if(validateCommonField(textFullName,textEmail,textPassword,textConfirmPwd,textPhone,userLocation)){
                showProgressDialog(resources.getString(R.string.please_wait))
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(textEmail,textPassword).addOnCompleteListener { task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!

                        // Authentication successful, retrieve the user's document from the "users" collection
                        val userUid = firebaseUser.uid // Retrieve the user's UID
//                        val userUid = "r23fKsDlCbMnWap4xJZ2FUQmhnq2"
                        val registeredEmail = firebaseUser.email!!

                        val role = when (intent.getStringExtra("previousActivity")) {
                            "RHomePage" -> "Restaurant"
                            "NHomePage" -> "Ngo"
                            "ALoginPage" -> "Admin"
                            "RcHomePage" -> "Receiver"
                            "VHomePage" -> "Volunteer"
                            // Add cases for other activities
                            else -> "default_role"
                        }
                        when (intent.getStringExtra("previousActivity")) {
                            "RHomePage" -> {
                                textResCuisine =radioButtonResCuisine.text.toString()
                                textResFoodType = radioButtonResFoodType.text.toString()
                                textResOtherType = radioButtonResOtherType.text.toString()
                                textResPickupTime = radioButtonResFoodPickup.text.toString()
                                textResOtherPickupTime = radioButtonResOtherPickup.text.toString()
                                // For Restaurant
                                FirebaseApp.initializeApp(this)
                                FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                                    // Send the token to your server so it can send FCM messages to this app

                                    val restaurantData = RestaurantData(
                                        id = userUid,
                                        fcmToken = token,
                                        name = textFullName,
                                        email = registeredEmail,
                                        address = textAddress,
                                        location = userLocation,
                                        phone = textPhone,
                                        restaurantName = textRestaurantName,
                                        cuisine = textResCuisine!!,
                                        foodType = textResFoodType!!,
                                        otherType = textResOtherType!!,
                                        pickTime = textResPickupTime!!,
                                        otherPickTime = textResOtherPickupTime!!,
                                        password = textPassword,
                                        role = role
                                    )
//                                FireStoreClass.registerUser(this,restaurantData,userUid)
                                    FireStoreClass().addUserData(
                                        "restaurant",
                                        userUid,
                                        restaurantData
                                    ) {
                                        val rHomeIntent = Intent(this, RHomePage::class.java)
                                        rHomeIntent.flags =
                                            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(rHomeIntent)
                                        finish()
                                    }
                                }
                            }

                            "NHomePage" -> {

                                textNgoFoodType = radioButtonNgoFoodType.text.toString()
                                textNgoOtherType = radioButtonNgoOtherType.text.toString()
                                textNgoOrganizationType = radioButtonNgoOrganization.text.toString()
                                textNgoFoodPickup = radioButtonNgoFoodPickup.text.toString()
                                textNgoOtherPickup = radioButtonNgoOtherPickup.text.toString()
                                val ngoData = NgoData(
                                    ngoId=userUid,
                                    name = textFullName,
                                    email = textEmail,
                                    location = userLocation,
                                    ngoAddress = textAddress,
                                    phone = textPhone,
                                    ngoName = textNgoName,
                                    foodType = textNgoFoodType!!,
                                    otherType = textNgoOtherType!!,
                                    organizationType = textNgoOrganizationType!!,
                                    foodPickup = textNgoFoodPickup!!,
                                    otherPickup = textNgoOtherPickup!!,
                                    registration = textNgoRegistration,
                                    certificate = downloadUrl,
                                    password = textPassword,
                                    role = role
                                )
//                                FireStoreClass.registerUser(this,ngoData,userUid)
                                FireStoreClass().addUserData("ngo",userUid,ngoData){
                                    val nHomeIntent = Intent(this, NHomePage::class.java)
                                    nHomeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(nHomeIntent)
                                    finish()
                                }
                            }

                            "ALoginPage" -> {
                                textAdminAccess = radioButtonAdminAccess.text.toString()
                                // For Admin
                                val adminData = AdminData(
                                    id = userUid,
                                    name = textFullName,
                                    email = registeredEmail,
                                    address = textAddress,
                                    location = userLocation,
                                    phone = textPhone,
                                    designation = textAdminDesignation,
                                    adminAccess = textAdminAccess!!,
                                    password = textPassword,
                                    role = role
                                )
                                Log.d("adminPage","Admin reached successfully")
//                                FireStoreClass.registerUser(this,adminData,userUid)
                                FireStoreClass().addUserData("admin",userUid,adminData){
                                    Log.d("AdminIntent","Admin Intent Reached Successfully")
                                    val aHomeIntent = Intent(this, AHomePage::class.java)
                                    aHomeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(aHomeIntent)
                                    finish()
                                }
                            }

                            "RcHomePage" -> {
                                // For Receiver
                                val receiverData = ReceiverData(
                                    id=userUid,
                                    name = textFullName,
                                    email = textEmail,
                                    address = textAddress,
                                    location = userLocation,
                                    phone = textPhone,
                                    pickupTime = textRevPickupTime,
                                    password = textPassword,
                                    role = role
                                )
//                                FireStoreClass.registerUser(this,receiverData,userUid)
                                FireStoreClass().addUserData("receiver",userUid,receiverData){
                                    val rrHomeIntent = Intent(this, RcHomePage::class.java)
                                    rrHomeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(rrHomeIntent)
                                    finish()
                                }
                            }

                            "VHomePage" -> {
                                textVolAvailability = radioButtonVolAvailability.text.toString()
                                textVolPreferences = radioButtonVolPreferences.text.toString()
                                // For Volunteer
                                val volunteerData = VolunteerData(
                                    id = userUid,
                                    name = textFullName,
                                    email = textEmail,
                                    address = textAddress,
                                    location = userLocation,
                                    phone = textPhone,
                                    availability = textVolAvailability!!,
                                    preferences = textVolPreferences!!,
                                    password = textPassword,
                                    role = role
                                )
//                                FireStoreClass.registerUser(this,volunteerData,userUid)
                                FireStoreClass().addUserData("volunteer",userUid,volunteerData){
                                    val vHomeIntent = Intent(this, VHomePage::class.java)
                                    vHomeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(vHomeIntent)
                                    finish()
                                }
                            }
                        }
                    }
                    else{
                        Toast.makeText(this,task.exception!!.message,Toast.LENGTH_LONG).show()
                    }
                }
            }
            else{
                Toast.makeText(this,"Unable to create account",Toast.LENGTH_LONG).show()
            }
            }catch (e: Exception) {
                Log.e(TAG, "Error while getting location from address: ${e.message}")
                Toast.makeText(
                    this,
                    "Please enter correct address or copy your address from google",
                    Toast.LENGTH_SHORT
                ).show()
                // Handle the error as needed
            }
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"  // Restrict to PDF files
        startActivityForResult(intent, FILE_PICKER_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {

            selectedFileUri = data?.data
            selectedFileUri?.let {
                ngoCertificateName.text = getFileName(it)
                uploadFileToFirebase(it)
            }
        }
    }

    private fun getFileName(uri: Uri): String {
        var fileName = "Unknown File"
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1) {
                cursor.moveToFirst()
                fileName = cursor.getString(nameIndex)
            }
        }
        return fileName
    }

    private fun uploadFileToFirebase(fileUri: Uri) {

        if (textNgoRegistration?.isEmpty() == true) {
            Toast.makeText(this, "Enter NGO Registration Number", Toast.LENGTH_SHORT).show()
            return
        }

        ngoProgressBar.visibility = ProgressBar.VISIBLE

        val fileName = "certificates/${UUID.randomUUID()}.pdf"
        val fileRef = storageRef.child(fileName)

        fileRef.putFile(fileUri)
            .addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener { uri ->
                     downloadUrl = uri.toString()
                }
            }
            .addOnFailureListener {
                ngoProgressBar.visibility = ProgressBar.GONE
                Toast.makeText(this, "Upload Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun validateCommonField(
        textFullName:String, textEmail: String,
        textPassword: String,
        textConfirmPwd: String,
        textPhone: String,
        userLocation: GeoPoint
    ): Boolean {
        return when {
            (TextUtils.isEmpty(textFullName))->{
                showErrorSnackBar("Enter your full name")
                fullNameInput.requestFocus()
                false
            }
            TextUtils.isEmpty(textEmail)->{
                showErrorSnackBar("Enter your email")
                emailInput.requestFocus()
                false
            }
            (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches())->{
                showErrorSnackBar("Re-enter your email")
                emailInput.requestFocus()
                false
            }
            TextUtils.isEmpty(userLocation.toString())->{
                showErrorSnackBar("Enter your Address")
                addressInput.requestFocus()
                false
            }
            (isLocationValid(userLocation))-> {
                showErrorSnackBar("Please Enter a valid address")
                addressInput.requestFocus()
                false
            }
            TextUtils.isEmpty(textPhone)->{
                showErrorSnackBar("Please Enter your phone number")
                phoneInput.requestFocus()
                false
            }
            (textPhone.length!=10) ->{
                showErrorSnackBar("Phone no should be 10 digits")
                phoneInput.requestFocus()
                false
            }
            TextUtils.isEmpty(textPassword)->{
                showErrorSnackBar("Please enter your password")
                passwordInput.requestFocus()
                false
            }
            (textPassword.length<8)->{
                showErrorSnackBar("Password should be at least 8 digits")
                passwordInput.requestFocus()
                false
            }
            TextUtils.isEmpty(textConfirmPwd)->{
                showErrorSnackBar("Please confirm your password")
                confirmPasswordInput.requestFocus()
                false
            }
            (textPassword != textConfirmPwd)->{
                showErrorSnackBar("Password not match")
                confirmPasswordInput.requestFocus()
                //clear the entered passwords
                passwordInput.clearComposingText()
                confirmPasswordInput.clearComposingText()
                false
            }

            else -> {
                true
            }
        }
    }

    private fun isLocationValid(address: GeoPoint): Boolean {
        val geocoder = Geocoder(this, Locale.getDefault())
        val locations = geocoder.getFromLocationName(address.toString(), 1)

        if (locations.isEmpty()) {
            // Location not found
            return false
        }

        val location = locations[0]
        val latitude = location.latitude
        val longitude = location.longitude

        if (latitude == 0.0 && longitude == 0.0) {
            // Location coordinates are invalid
            return false
        }

        // Location is valid
        return true
    }

}