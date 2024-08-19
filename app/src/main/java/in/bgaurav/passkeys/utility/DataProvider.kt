package `in`.bgaurav.passkeys.utility

import android.content.Context
import android.content.SharedPreferences

object DataProvider {

    private lateinit var sharedPreference: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private const val IS_SIGNED_IN = "isSignedIn"
    private const val IS_SIGNED_IN_THROUGH_PASSKEYS = "isSignedInThroughPasskeys"
    private const val PREF_NAME = "CREDMAN_PREF"

    fun initSharedPref(context: Context) {
        sharedPreference =
            context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = sharedPreference.edit()
    }

    //Set if the user is signed in or not
    private fun configureSignedInPref(flag: Boolean) {
        editor.putBoolean(IS_SIGNED_IN, flag)
        editor.commit()
    }

    //Set if signed in through passkeys
    fun setSignedInThroughPasskeys() {
        configureSignedInPref(true)
        editor.putBoolean(IS_SIGNED_IN_THROUGH_PASSKEYS, true)
        editor.commit()
    }

    //Set if signed in through password
    fun setSignedInThroughPassword() {
        configureSignedInPref(true)
        editor.putBoolean(IS_SIGNED_IN_THROUGH_PASSKEYS, false)
        editor.commit()
    }

    fun logout() {
        configureSignedInPref(false)
    }

    fun isSignedIn(): Boolean {
        return sharedPreference.getBoolean(IS_SIGNED_IN, false)
    }

    fun isSignedInThroughPasskeys(): Boolean {
        return sharedPreference.getBoolean(IS_SIGNED_IN_THROUGH_PASSKEYS, false)
    }
}
