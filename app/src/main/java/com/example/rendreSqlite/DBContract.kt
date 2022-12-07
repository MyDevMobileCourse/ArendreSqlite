package com.example.rendreSqlite

import android.provider.BaseColumns

object DBContract {
    class UserEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "users"
            val COLUMN_USER_ID = "id"
            val COLUMN_NAME = "name"
            val COLUMN_DATE = "date"
            val COLUMN_EMAIL = "email"
            val COLUMN_PASSWORD = "password"
            val COLUMN_LOGIN = "login"
            val COLUMN_TEL = "tel"

        }
    }
}