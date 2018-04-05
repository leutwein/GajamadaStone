package id.co.gmst.gajamadastone

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.Charset


/**
 * Created by Shoma on 1/28/2018.
 */

object LibraryInspira {
    private val mainURL: String

    init {
        mainURL = GlobalVariables.getServiceURL()
    }

    fun fragmentAdding(fragmentManager: FragmentManager, fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(GlobalVariables.fragmentID, fragment)
        fragmentTransaction.commit()
    }

    fun fragmentReplace(fragmentManager: FragmentManager, fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(GlobalVariables.fragmentID, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun fragmentBack(fragmentManager: FragmentManager) {
        fragmentManager.popBackStack()
    }

    fun isInternetAvailable(mainContext: Context) : Boolean {
        val cm: ConnectivityManager = mainContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    fun postRequest(controller: String, jsonObject: JSONObject? ): JSONObject? {
        var jsonString = ""
        if (jsonObject != null)
            jsonString = jsonObject.toString()
        Log.d("json", jsonString)
        val (ignoredRequest, ignoredResponse, result) =
                Fuel.post(mainURL + controller).header("Content-Type" to "application/json").body(jsonString, Charset.forName("UTF-8")).responseString()
        val (data, error) = result
//        Log.d("data", data)
//        Log.d("request", ignoredRequest.toString())
//        Log.d("response", ignoredResponse.toString())
        if (error == null)
            return JSONObject(data!!.substring(data!!.indexOf("{"), data!!.lastIndexOf("}") + 1))
        Log.v("Error", error!!.toString())
        return null
    }

    fun requestMultiData(controller: String, jsonObject: JSONObject?): JSONArray? {
        var jsonString = ""
        if (jsonObject != null)
            jsonString = jsonObject.toString()
        Log.d("json", jsonString)
        val (ignoredRequest, ignoredResponse, result) =
                Fuel.post(mainURL + controller).header("Content-Type" to "application/json").body(jsonString, Charset.forName("UTF-8")).responseString()
        val (data, error) = result
//        Log.d("data", data)
//        Log.d("request", ignoredRequest.toString())
//        Log.d("response", ignoredResponse.toString())
        if (error == null)
            return JSONArray(data!!.substring(data!!.indexOf("["), data!!.lastIndexOf("]") + 1))
        Log.v("Error", error!!.toString())
        return null
    }

    fun getSharedString(preferences: SharedPreferences, key: String, defaultValue: String) : String {
        return preferences.getString(key, defaultValue)
    }

    fun setSharedString(preferences: SharedPreferences, key: String, param: String) {
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putString(key, param)
        editor.apply()
    }

    fun destroySession(preferences: SharedPreferences) {
        preferences.edit().clear().apply()
    }

    fun makeToast (message: String, context: Context) {
        Toast.makeText(context, message!!, Toast.LENGTH_SHORT).show()
    }

    fun makeToastLong (message: String, context: Context) {
        Toast.makeText(context, message!!, Toast.LENGTH_LONG).show()
    }
}