package com.example.healthyhabittracker.ui

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthyhabittracker.R
import com.example.healthyhabittracker.ui.components.WaterReminderReceiver
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Calendar

class MyViewModel(): ViewModel(){

    var notificationEnabled by mutableStateOf(true)
        private set

    fun setNotificationsEnabled(enabled: Boolean){
        notificationEnabled = enabled
    }

    private var _selectedTimes by mutableStateOf(listOf("You haven't set water reminder"))
    val selectedTimes: List<String> get() = _selectedTimes

    fun setSelectedTimes(newList: List<String>){
        _selectedTimes = newList
    }
    @RequiresApi(Build.VERSION_CODES.S)
    fun setWaterAlarm(context: Context, time: String) {

        if (!notificationEnabled) return

        val (hour, minute) = time.split(":").map { it.toInt() }
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) add(Calendar.DAY_OF_MONTH, 1) // 如果时间已过则第二天提醒
        }

        val intent = Intent(context, WaterReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            hour * 100 + minute, // 唯一 requestCode
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (alarmManager.canScheduleExactAlarms()){
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }

    }

    var _nickname by mutableStateOf("Username")
    val nickname: String get() = _nickname

    fun setNickname(name: String){
        _nickname = name
    }

    var _mobileNumber by mutableStateOf("You haven't set your number")
    val mobileNumber: String get() = _mobileNumber

    fun setMobileNumber(number: String){
        _mobileNumber = number
    }


    val defaultAvatarUri: Uri = Uri.parse("android.resource://com.example.healthyhabittracker/${R.drawable.photo_default}")

    var avatarUri: Uri by mutableStateOf(defaultAvatarUri)

    fun updateAvatar(uri: Uri?){
        if (uri != null) {
            avatarUri = uri
        }
    }

    var dateText by mutableStateOf("")
        private set

    var timeText by mutableStateOf("")
        private set

    init {
        setDate()
        setTime()
    }

    private fun setTime(){
        viewModelScope.launch {
            while (true){
                val now = LocalDateTime.now()

                val hours = now.hour
                val minutes = now.minute
                timeText = "%02d : %02d".format(hours, minutes)

                delay(1000)
            }
        }
    }

    private fun setDate(){
        val date = LocalDate.now()
        val years = date.year
        val months = date.month
        val days = date.dayOfMonth

        dateText = "$months $days $years"
    }
}

class SettingsViewModel : ViewModel() {
    private var _followSystem = mutableStateOf(true)

    val followSystem: Boolean get() = _followSystem.value

    var _darkModeEnabled = mutableStateOf(false)
    val darkModeEnabled: Boolean get() = _darkModeEnabled.value
    val isAppDarkTheme: Boolean
        @Composable
        get() = if (_followSystem.value) isSystemInDarkTheme() else _darkModeEnabled.value

    fun setFollowSystem(newValue: Boolean){
        _followSystem.value = newValue
    }

    fun setDarkModeEnabled(newValue: Boolean){
        _darkModeEnabled.value = newValue
    }

    fun onNotificationPermissionClicked(
        context: Context,
        requestPermission: () -> Unit
    ){
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        if (notificationManager.areNotificationsEnabled()){
            Toast.makeText(context, "Permission already granted", Toast.LENGTH_SHORT).show()
        } else {
            requestPermission()
        }
    }


}

class StepViewModel(application: Application) : AndroidViewModel(application), SensorEventListener {

    private var _stepGoal by mutableIntStateOf(0)
    val stepGoal: Int get() = _stepGoal

    fun setStepGoal(newGoal: Int){
        _stepGoal = newGoal
    }

    private val sensorManager = application.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    var stepCountToday by mutableIntStateOf(0)
        private set

    private var initialStepCount: Float? = null

    init {
        stepSensor?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            val totalSteps = event.values[0]

            if (initialStepCount == null) {
                initialStepCount = totalSteps
            }

            stepCountToday = ((totalSteps - (initialStepCount ?: 0f)).toInt()).coerceAtLeast(0)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onCleared() {
        super.onCleared()
        sensorManager.unregisterListener(this)
    }


    val stepProgress: Float
        get() = if (stepGoal == 0) 0f
        else stepCountToday.toFloat() / stepGoal.toFloat()
}


class WeatherViewModel : ViewModel() {

    var weatherFetched by mutableStateOf(false)
        private set

    fun setWeatherFetched() { weatherFetched = true }

    var lat by mutableDoubleStateOf(37.419734)
    var lon by mutableDoubleStateOf(-122.0827784)

    var temperature by mutableStateOf("--")
    var description by mutableStateOf("Loading...")
    var feelsLikeTemperature by mutableStateOf("--")
    var isDaytimeNow by mutableStateOf(true)
    var dominantPollutantText by mutableStateOf("--")
    var advice by mutableStateOf("")
    var airQualityIndex by mutableStateOf("--")

    private val client = OkHttpClient()

    // 在 WeatherViewModel 内部补充这个函数
    fun fetchWeatherWithLocation(
        fusedLocationClient: FusedLocationProviderClient,
        fineGranted: Boolean,
        coarseGranted: Boolean,
        apiKey: String
    ) {
        // 防止重复请求
        if (weatherFetched) return

        if (fineGranted || coarseGranted) {
            try {
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                    CancellationTokenSource().token
                ).addOnSuccessListener { location ->
                    if (location != null) {
                        Log.d("HTTP_DEBUG", "Real Latin: ${location.latitude}")
                        fetchWeatherAndAirQuality(location.latitude, location.longitude, apiKey)
                    } else {
                        Log.d("HTTP_DEBUG", "Empty Latin")
                        fetchWeatherAndAirQuality(31.23, 121.47, apiKey)
                    }
                }
            } catch (e: SecurityException) {
                fetchWeatherAndAirQuality(31.23, 121.47, apiKey)
            }
        } else {
            fetchWeatherAndAirQuality(31.23, 121.47, apiKey)
        }
    }

    fun fetchWeatherAndAirQuality(latitude: Double, longitude: Double, apiKey: String) {
        lat = latitude
        lon = longitude

        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("HTTP_DEBUG", "Starting request for Lat:$latitude, Lon:$longitude")

                val weatherUrl = "https://weather.googleapis.com/v1/currentConditions:lookup?key=$apiKey&location.latitude=$latitude&location.longitude=$longitude"
                Log.e("HTTP_DEBUG", "Weather URL: $weatherUrl")
                val weatherRequest = Request.Builder().url(weatherUrl).get().build()
                val weatherResponse = client.newCall(weatherRequest).execute()
                val weatherResult = weatherResponse.body?.string() ?: throw Exception("Weather body null")

                val airUrl = "https://airquality.googleapis.com/v1/currentConditions:lookup?key=$apiKey"
                Log.e("HTTP_DEBUG", "Air URL: $airUrl")
                val airJsonBody = """{"location": {"latitude": $latitude, "longitude": $longitude}}"""
                val airRequestBody = airJsonBody.toRequestBody("application/json".toMediaType())
                val airRequest = Request.Builder().url(airUrl).post(airRequestBody).build()
                val airResponse = client.newCall(airRequest).execute()
                val airResult = airResponse.body?.string() ?: throw Exception("Air body null")

                val wJson = JSONObject(weatherResult)
                Log.d("HTTP_DEBUG", "Weather JSON: $wJson")
                val isDaytime = wJson.optBoolean("isDaytime", true)
                val weatherDesc = wJson.optJSONObject("weatherCondition")?.optJSONObject("description")?.optString("text") ?: "Unknown"
                val temp = wJson.optJSONObject("temperature")?.optDouble("degrees", 0.0) ?: 0.0
                val feelsLike = wJson.optJSONObject("feelsLikeTemperature")?.optDouble("degrees", 0.0) ?: 0.0

                val aJson = JSONObject(airResult)
                Log.d("HTTP_DEBUG", "Air JSON: $aJson")
                val aqiIndex = aJson.optJSONArray("indexes")?.optJSONObject(0)
                val aqi = aqiIndex?.optInt("aqi", 0) ?: 0
                val aqiCategory = aqiIndex?.optString("category", "N/A") ?: "N/A"
                val dominantPollutant = aqiIndex?.optString("dominantPollutant", "N/A") ?: "N/A"

                val adviceText = if (temp in 15.0..30.0 && !weatherDesc.contains("rain", true) && aqi <= 100) {
                    "Good day to go out"
                } else {
                    "Better stay indoors"
                }

                withContext(Dispatchers.Main) {
                    temperature = "$temp°C"
                    feelsLikeTemperature = "$feelsLike°C"
                    description = weatherDesc
                    isDaytimeNow = isDaytime
                    airQualityIndex = "$aqi ($aqiCategory)"
                    dominantPollutantText = dominantPollutant.uppercase()
                    advice = adviceText
                    weatherFetched = true
                    Log.d("HTTP_DEBUG", "UI Updated Successfully")
                }

            } catch (e: Exception) {
                Log.e("HTTP_DEBUG", "Fetch error: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    description = "Error: ${e.message}"
                }
            }
        }
    }
}