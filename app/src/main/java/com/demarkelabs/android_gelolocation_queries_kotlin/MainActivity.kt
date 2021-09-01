package com.demarkelabs.android_gelolocation_queries_kotlin

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.parse.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    var queryNear: Button? = null
    var queryWithinKm:Button? = null
    var queryWithinPolygon:Button? = null
    var clearResults:Button? = null

    private var adapter: ResultAdapter? = null
    private var progressDialog: ProgressDialog? = null
    private var resultList: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        queryNear = findViewById(R.id.queryNear)
        queryWithinKm = findViewById(R.id.queryWithinKm)
        queryWithinPolygon = findViewById(R.id.queryWithinPolygon)
        resultList = findViewById(R.id.resultList)
        clearResults = findViewById(R.id.clearResults)
        progressDialog = ProgressDialog(this)

        queryNear?.setOnClickListener { view: View? ->
            progressDialog!!.show()
            val query = ParseQuery<ParseObject>("City")
            query.whereNear("location", ParseGeoPoint(18.018086950599134, -76.79894232253473))
            query.findInBackground { objects: List<ParseObject>?, e: ParseException? ->
                progressDialog!!.dismiss()
                if (e == null) {
                    initData(objects!!)
                } else {
                    Toast.makeText(this@MainActivity, e.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        queryWithinKm?.setOnClickListener { view: View? ->
            progressDialog!!.show()
            val query = ParseQuery<ParseObject>("City")
            query.whereWithinKilometers(
                "location",
                ParseGeoPoint(18.018086950599134, -76.79894232253473),
                3000.0
            )
            query.findInBackground { objects: List<ParseObject>?, e: ParseException? ->
                progressDialog!!.dismiss()
                if (e == null) {
                    initData(objects!!)
                } else {
                    Toast.makeText(this@MainActivity, e.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        queryWithinPolygon?.setOnClickListener { view: View? ->
            progressDialog!!.show()
            val query = ParseQuery<ParseObject>("City")
            val geoPoint1 = ParseGeoPoint(15.822238344514378, -72.42845934415942)
            val geoPoint2 = ParseGeoPoint(-0.7433770196268968, -97.44765968406668)
            val geoPoint3 = ParseGeoPoint(-59.997149373299166, -76.52969196322749)
            val geoPoint4 = ParseGeoPoint(-9.488786415007201, -18.346101586021952)
            val geoPoint5 = ParseGeoPoint(15.414859532811047, -60.00625459569375)
            val list: MutableList<ParseGeoPoint> =
                ArrayList()
            list.add(geoPoint1)
            list.add(geoPoint2)
            list.add(geoPoint3)
            list.add(geoPoint4)
            list.add(geoPoint5)
            query.whereWithinPolygon("location", list)
            query.findInBackground { objects: List<ParseObject>?, e: ParseException? ->
                progressDialog!!.dismiss()
                if (e == null) {
                    initData(objects!!)
                } else {
                    Toast.makeText(this@MainActivity, e.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        clearResults?.setOnClickListener { view: View? -> adapter!!.clearList() }
    }

    private fun initData(objects: List<ParseObject>) {
        adapter = ResultAdapter(this, objects)
        resultList!!.layoutManager = LinearLayoutManager(this)
        resultList!!.adapter = adapter
    }
}