package com.example.accountingformtg.Activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.provider.ContactsContract
import android.support.v7.widget.LinearLayoutManager
import com.example.accountingformtg.Adapters.MainAdapter
import com.example.accountingformtg.App
import com.example.accountingformtg.BD.ContactDB
import com.example.accountingformtg.BD.Model.ContactInfo
import com.example.accountingformtg.R
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var MY_PERMISSIONS_REQUEST_READ_CONTACTS:Int = 0
    var PICK_CONTACT:Int = 0
    var contactDB = App.contactDB

    private lateinit var disposable: Disposable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pernition()

        initView()

        loadContacts()
    }

    fun initView(){

        fa_add.setOnClickListener {
            val intentChoseContact = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            startActivityForResult(intentChoseContact, PICK_CONTACT)
        }


        main_list.itemAnimator = null
        main_list.setHasFixedSize(true)
        main_list.layoutManager = LinearLayoutManager(this)

    }

    private fun loadContacts() {

        disposable = contactDB!!.ContactDAO().getContacts().
            subscribeOn(Schedulers.io()).
            observeOn(AndroidSchedulers.mainThread()).
            subscribe { contactList ->
                main_list.adapter = MainAdapter(contactList, this)
            }

    }

    private fun pernition() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                MY_PERMISSIONS_REQUEST_READ_CONTACTS
            )

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_CONTACT && resultCode == Activity.RESULT_OK){

            val contactUri = data?.data
            if(contactUri != null) {
                val cursor = contentResolver.query(contactUri, null, null, null, null)
                if (cursor != null) {
                    if(cursor.moveToFirst()) {

                        val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))

                        val phoneNumber = (cursor.getString(
                            cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))).toInt()

                        var phone = ""

                        if (phoneNumber > 0) {

                            val cursorPhone = contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", arrayOf(id), null

                            )

                            if (cursorPhone != null && cursorPhone.count > 0) {
                                cursorPhone.moveToNext()

                                phone = cursorPhone.getString(
                                    cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                )

                            }

                            cursorPhone?.close()

                        }

                        val contactInfo = ContactInfo(
                            id = id.toInt(),
                            name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),
                            phone = phone
                        )

                        Observable.fromCallable {
                            Runnable {
                                contactDB?.ContactDAO()?.insertContact(contactInfo)
                            }.run()
                        }
                            .subscribeOn(Schedulers.io())
                            .subscribe()

                    }
                }

                cursor?.close()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        ContactDB.destroyInstance()
    }
}
