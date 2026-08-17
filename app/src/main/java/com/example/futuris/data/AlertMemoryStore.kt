package com.example.futuris.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class AlertItem(
    val id: String,
    val title: String,
    val message: String,
    val timeLabel: String,
    val category: String,
    val isNew: Boolean = true
)

object AlertMemoryStore {

    private const val PREFS_NAME = "FuturisAlerts"
    private const val KEY_ALERTS = "alerts_list"

    fun addAlert(
        context: Context,
        alert: AlertItem
    ) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val existingJson = prefs.getString(KEY_ALERTS, "[]").orEmpty()
        val array = JSONArray(existingJson)

        val obj = JSONObject().apply {
            put("id", alert.id)
            put("title", alert.title)
            put("message", alert.message)
            put("timeLabel", alert.timeLabel)
            put("category", alert.category)
            put("isNew", alert.isNew)
        }

        array.put(obj)

        prefs.edit()
            .putString(KEY_ALERTS, array.toString())
            .apply()
    }

    fun getAlerts(
        context: Context
    ): List<AlertItem> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_ALERTS, "[]").orEmpty()
        val array = JSONArray(json)

        val list = mutableListOf<AlertItem>()

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)

            list.add(
                AlertItem(
                    id = obj.optString("id"),
                    title = obj.optString("title"),
                    message = obj.optString("message"),
                    timeLabel = obj.optString("timeLabel"),
                    category = obj.optString("category"),
                    isNew = obj.optBoolean("isNew", true)
                )
            )
        }

        return list.reversed()
    }

    fun clearAlerts(
        context: Context
    ) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        prefs.edit()
            .remove(KEY_ALERTS)
            .apply()
    }

    fun hasAlerts(
        context: Context
    ): Boolean {
        return getAlerts(context).isNotEmpty()
    }
}