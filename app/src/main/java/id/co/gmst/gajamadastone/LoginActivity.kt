package id.co.gmst.gajamadastone

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.github.kittinunf.fuel.httpPost
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    val global: GlobalVariables = GlobalVariables

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        global.initialize(this)

        //get application version
        val pInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
        checkNewVersion(pInfo.versionName).execute()

        //check if there is an user session
        if (LibraryInspira.getSharedString(global.userpreferences!!, global.user.hash, "") != "") {
            var jsonObject: JSONObject = JSONObject()
            jsonObject.put("hash", LibraryInspira.getSharedString(global.userpreferences!!, global.user.hash, ""))
            sessionCheck(jsonObject, this).execute("Login/checkUser/")
        }

        val buttonLogin: Button = findViewById(R.id.sign_in_button)
        buttonLogin.setOnClickListener { onLogin() }
    }

    override fun onResume() {
        super.onResume()
        username.setText("")
        password.setText("")
        password.clearFocus()
    }

    private fun onLogin() {
        var jsonObject: JSONObject = JSONObject()
        val userText: String = (findViewById(R.id.username) as EditText).text.toString()
        val passText: String = (findViewById(R.id.password) as EditText).text.toString()
        val token: String = LibraryInspira.getSharedString(global.userpreferences!!, global.user.token, "")
//        val tipe: String = LibraryInspira.getSharedString(global.userpreferences!!, global.user.tipe, "")
        val tipe: String = "1"

        if (userText != "" || passText != "") {
            jsonObject.put("username", userText)
            jsonObject.put("password", passText)
            jsonObject.put("token", token)

            loginProcess(jsonObject, global, this).execute("Login/loginCustomer/")
        }
        else {
            LibraryInspira.makeToastLong("Type Your Username and Password First!", this)
        }
    }

    private class loginProcess(params: JSONObject, globalVariable: GlobalVariables, mainContext: Context): AsyncTask<String, Void, JSONObject>() {
        val jsonObject: JSONObject = params
        val global: GlobalVariables = globalVariable
        val context: Context = mainContext

        override fun doInBackground(vararg controller: String?): JSONObject {
            return LibraryInspira.postRequest(controller[0]!!, jsonObject)!!
        }

        override fun onPostExecute(result: JSONObject?) {
            super.onPostExecute(result)
            if (result != null) {
                if (result.has("query"))
                    LibraryInspira.makeToastLong("Username atau Password tidak sesuai", context)
                else {
                    LibraryInspira.setSharedString(global.userpreferences!!, global.user.nomor, result.get("user_nomor").toString())
                    LibraryInspira.setSharedString(global.userpreferences!!, global.user.email, result.get("user_email").toString())
                    LibraryInspira.setSharedString(global.userpreferences!!, global.user.nama, result.get("user_nama").toString())
                    LibraryInspira.setSharedString(global.userpreferences!!, global.user.alamat, result.get("user_alamat").toString())
                    LibraryInspira.setSharedString(global.userpreferences!!, global.user.telepon, result.get("user_telepon").toString())
                    LibraryInspira.setSharedString(global.userpreferences!!, global.user.hash, result.get("user_hash").toString())
                    LibraryInspira.setSharedString(global.userpreferences!!, global.user.token, result.get("user_token").toString())
                    LibraryInspira.setSharedString(global.userpreferences!!, global.user.fax, result.get("user_fax").toString())
                    LibraryInspira.setSharedString(global.userpreferences!!, global.user.namapengiriman, result.get("user_namapengiriman").toString())
                    LibraryInspira.setSharedString(global.userpreferences!!, global.user.alamatpengiriman, result.get("user_alamatpengiriman").toString())
                    LibraryInspira.setSharedString(global.userpreferences!!, global.user.teleponpengiriman, result.get("role_teleponpengiriman").toString())
                    LibraryInspira.setSharedString(global.userpreferences!!, global.user.hp, result.get("role_hp").toString())
                    val intent = Intent(this.context, MainActivity::class.java)
                    this.context.startActivity(intent)

                }
            }
            else
                LibraryInspira.makeToastLong("Terjadi Kesalahan\nError: 0x01\nAPI not returning anything", context)
        }
    }

    private class sessionCheck(params: JSONObject, mainContext: Context) : AsyncTask<String, Void, JSONObject>() {
        val jsonObject: JSONObject = params
        val context: Context = mainContext

        override fun doInBackground(vararg controller: String?): JSONObject {
            return LibraryInspira.postRequest(controller[0]!!, jsonObject)!!
        }

        override fun onPostExecute(result: JSONObject?) {
            super.onPostExecute(result)
            if (result != null) {
                if (result.get("success") == "true") {
                    val intent = Intent(this.context, MainActivity::class.java)
                    this.context.startActivity(intent)
                }
            }
            else
                LibraryInspira.makeToastLong("Terjadi Kesalahan\nError: 0x01\nAPI not returning anything", context)
        }
    }

    private class checkNewVersion(currentVersion: String) : AsyncTask<Void, Void, JSONObject>() {
        val currentVersion = currentVersion

        override fun doInBackground(vararg params: Void?): JSONObject? {
            val jsonString = LibraryInspira.postRequest("Login/getVersion/", null)!!
            return jsonString
        }

        override fun onPostExecute(result: JSONObject?) {
            super.onPostExecute(result)
            if (result != null) {
                Log.d("url", result.get("url").toString())
                if (result.get("externalversion").toString().equals(currentVersion)) {
                    //download new version and install
                }
            }
        }
    }
}
