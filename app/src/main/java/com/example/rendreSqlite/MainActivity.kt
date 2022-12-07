package com.example.rendreSqlite

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var NomPrenom: TextInputLayout
    lateinit var nom_prenom: EditText
    lateinit var DateNaissance: TextInputLayout
    lateinit var date_naissance: EditText
    lateinit var AdresseEmail: TextInputLayout
    lateinit var adresse_email: EditText
    var user_id: Int? = null
    lateinit var Tel: TextInputLayout
    lateinit var tel: EditText
    lateinit var Login: TextInputLayout
    lateinit var login: EditText
    lateinit var Password: TextInputLayout
    lateinit var password: EditText
    lateinit var AddUser: Button
    lateinit var cancelButton: Button
    lateinit var userDbHelper: UserDbHelper
    lateinit var listOfInputs: List<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userDbHelper = UserDbHelper(this)

        init()
        listOfInputs = listOf(nom_prenom, date_naissance, adresse_email, tel, login, password)
        listOfInputs.forEach { listenOnInput(it) }

        cancelButton.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(this)
            if (listOfInputs.any { it.text.isNotBlank() }) {
                alertDialogBuilder.setTitle("Quit")
                alertDialogBuilder.setMessage("Are you sure you want to clear data?")
                alertDialogBuilder.setPositiveButton("Yes") { dialog, which ->
                    listOfInputs.forEach {
                        it.text.clear()
                        it.error = null
                    }
                    dialog.dismiss()
                }
                alertDialogBuilder.setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                alertDialogBuilder.show()
            } else {
                alertDialogBuilder.setTitle("Quit")
                alertDialogBuilder.setMessage("Are you sure you want to quit?")
                alertDialogBuilder.setPositiveButton("Yes") { dialog, which ->
                    super.onBackPressed()
                    finish()
                }
                alertDialogBuilder.setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                alertDialogBuilder.show()
            }

        }

        val alertDialogBuilder = AlertDialog.Builder(this)
        AddUser.setOnClickListener {
            if (allValid()) {
                alertDialogBuilder.setTitle("Submit")
                alertDialogBuilder.setMessage(
                    "Do you wanna submit this data ?\n" +
                            "Nom et Prénom : ${nom_prenom.text}\n" +
                            "Date de naissance : ${date_naissance.text}\n" +
                            "Adresse email : ${adresse_email.text}\n"
                )
                alertDialogBuilder.setPositiveButton("Yes") { dialog, which ->
                    addUser()
                }
                alertDialogBuilder.setNegativeButton("No") { dialog, which ->
                    Toast.makeText(
                        this,
                        "Canceled", Toast.LENGTH_SHORT
                    ).show()
                }
                alertDialogBuilder.show()
            } else if(listOfInputs.any { it.text.isBlank() }) {
                alertDialogBuilder.setTitle("Error")
                alertDialogBuilder.setMessage("Please fill all fields")
                alertDialogBuilder.setPositiveButton("Ok") { dialog, which ->
                    dialog.dismiss()
                }
                alertDialogBuilder.show()
            } else{
                    alertDialogBuilder.setTitle("Error")
                alertDialogBuilder.setMessage("Please check your inputs")
                alertDialogBuilder.setPositiveButton("Ok") { dialog, which ->
                    dialog.dismiss()
                }
                alertDialogBuilder.show()
            }
        }
    }

    fun init() {

        NomPrenom = findViewById(R.id.NomPrenom)
        nom_prenom = findViewById(R.id.nom_prenom)
        DateNaissance = findViewById(R.id.DateNaissance)
        date_naissance = findViewById(R.id.date_naissance)
        AdresseEmail = findViewById(R.id.AdresseEmail)
        adresse_email = findViewById(R.id.adresse_email)
        Tel = findViewById(R.id.Tel)
        tel = findViewById(R.id.tel)
        Login = findViewById(R.id.Login)
        login = findViewById(R.id.login)
        Password = findViewById(R.id.Password)
        password = findViewById(R.id.password)
        AddUser = findViewById(R.id.AddUser)
        cancelButton = findViewById(R.id.annuler)
        this.initDatePicker()
        if (intent.hasExtra("user_id")) {
            user_id = intent.getIntExtra("user_id", 0)
            val user = userDbHelper.getUser(user_id!!)
            nom_prenom.setText(user.nom_prenom)
            date_naissance.setText(user.date_naissance)
            adresse_email.setText(user.adresse_email)
//            tel.setText(user.tel)
//            login.setText(user.login)
//            password.setText(user.password)
//
            AddUser.text = "Update"
        }
    }

    private fun initDatePicker() {
        date_naissance.setOnClickListener {
            // the instance of our calendar.
            val c = Calendar.getInstance()

            // on below line we are getting
            // our day, month and year.
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // on below line we are creating a
            // variable for date picker dialog.
            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our edit text.
                    val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    date_naissance.setText(dat)
                },
                // on below line we are passing year, month
                // and day for the selected date in our date picker.
                year,
                month,
                day
            )
            // at last we are calling show
            // to display our date picker dialog.
            datePickerDialog.show()
        }
        DateNaissance.setOnClickListener {
            // the instance of our calendar.
            val c = Calendar.getInstance()

            // on below line we are getting
            // our day, month and year.
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // on below line we are creating a
            // variable for date picker dialog.
            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our edit text.
                    val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    date_naissance.setText(dat)
                },
                // on below line we are passing year, month
                // and day for the selected date in our date picker.
                year,
                month,
                day
            )
            // at last we are calling show
            // to display our date picker dialog.
            datePickerDialog.show()
        }
    }

    fun listenOnInput(field: EditText) {
        field.setOnFocusChangeListener { _, _ ->
            validate(field)
        }
        field.addTextChangedListener {
            validate(field)
        }
    }

    fun validate(field: EditText) {
        val fieldLayout = field.parent.parent as TextInputLayout
        var error = false
        if (field == date_naissance) {
            try {

                val sdf = SimpleDateFormat("dd-MM-yyyy")
                val date = sdf.parse(field.text.toString())
                if (date.after(Date())) {
                    fieldLayout.error = "Date doit être inférieur à la date d'aujourd'hui"
                    error = true
                }
            } catch (e: Exception) {
                fieldLayout.error = "Date doit être au format dd-MM-yyyy"
                error = true
            }
        }  else if (field == adresse_email) {
            if (!Patterns.EMAIL_ADDRESS.matcher(field.text.toString()).matches()) {
                fieldLayout.error = "Adresse email invalide"
                error = true
            }
        } else if (field.text.isEmpty()) {
            fieldLayout.error = "Ce champ est obligatoire"
            error = true
        } else if (field == password ){
            if (field.text.length < 8) {
                fieldLayout.error = "Mot de passe doit contenir au moins 8 caractères"
                error = true
            }
        } else if (field == tel) {
            if (field.text.length != 8) {
                fieldLayout.error = "Numéro de téléphone doit contenir exactement 8 chiffres"
                error = true
            }
            else if (!field.text.toString().matches(Regex("[0-9]+"))) {
                fieldLayout.error = "Numéro de téléphone doit contenir que des chiffres"
                error = true
            }
        }else if (field == login) {
            if (field.text.length < 4) {
                fieldLayout.error = "Login doit contenir au moins 4 caractères"
                error = true
            }
        } else {
            fieldLayout.error = null
        }
        if (!error) {
            fieldLayout.error = null
        }
    }

    fun allValid(): Boolean {
        var valid = true
        if (NomPrenom.error != null) {
            valid = false
        }
        if (DateNaissance.error != null) {
            valid = false
        }
        if (AdresseEmail.error != null) {
            valid = false
        }
        if (Tel.error != null) {
            valid = false
        }
        if (Login.error != null) {
            valid = false
        }
        if (Password.error != null) {
            valid = false
        }
        return valid
    }

    fun addUser() {
        val nom = nom_prenom.text.toString()
        val date = date_naissance.text.toString()
        val email = adresse_email.text.toString()
        val tel = tel.text.toString()
        val login = login.text.toString()
        val password = password.text.toString()
        val db = userDbHelper.db
        val values = ContentValues()
        values.put(DBContract.UserEntry.COLUMN_EMAIL, email)
        values.put(DBContract.UserEntry.COLUMN_NAME, nom)
        values.put(DBContract.UserEntry.COLUMN_DATE, date)
        values.put(DBContract.UserEntry.COLUMN_TEL, tel)
        values.put(DBContract.UserEntry.COLUMN_LOGIN, login)
        values.put(DBContract.UserEntry.COLUMN_PASSWORD, password)
        if (user_id == null) {
            val newRowId = db.insert(DBContract.UserEntry.TABLE_NAME, null, values)
            if (newRowId == -1L) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Erreur lors de l'ajout de l'utilisateur",
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                val intent = intent
                intent.putExtra("message", "Utilisateur ajouté avec succès")
                //setResult(RESULT_OK, intent)
                //finish()
                listOfInputs.forEach{
                    it.text.clear()
                    it.error = null
                }
            }
        } else {
            val selection = DBContract.UserEntry.COLUMN_USER_ID + " = ?"
            val selectionArgs = arrayOf(user_id.toString())

            val count = db.update(
                DBContract.UserEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
            )
            if (count == 0) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "User not found",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                val intent = intent
                intent.putExtra("message", "Utilisateur modifié avec succès")
                listOfInputs.forEach{
                    it.text.clear()
                    it.error = null
                }
            //setResult(RESULT_OK, intent)
                //finish()
            }
        }


    }
}