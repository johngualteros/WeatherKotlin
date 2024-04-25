//package com.app.weatherkotlin.presentation.composables
//
//import android.Manifest
//import android.annotation.SuppressLint
//import android.content.pm.PackageManager
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.core.app.ActivityCompat
//import com.google.accompanist.permissions.ExperimentalPermissionsApi
//import com.google.accompanist.permissions.isGranted
//import com.google.accompanist.permissions.rememberMultiplePermissionsState
//import com.google.android.gms.location.Priority
//import com.google.android.gms.tasks.CancellationTokenSource
//
///**
// * Composable function to request location permissions and handle different scenarios.
// *
// * @param onPermissionGranted Callback to be executed when all requested permissions are granted.
// * @param onPermissionDenied Callback to be executed when any requested permission is denied.
// * @param onPermissionsRevoked Callback to be executed when previously granted permissions are revoked.
// */
//@OptIn(ExperimentalPermissionsApi::class)
//@Composable
//fun RequestLocationPermission(
//    onPermissionGranted: () -> Unit,
//    onPermissionDenied: () -> Unit,
//    onPermissionsRevoked: () -> Unit
//) {
//    // Initialize the state for managing multiple location permissions.
//    val permissionState = rememberMultiplePermissionsState(
//        listOf(
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_FINE_LOCATION,
//        )
//    )
//
//    // Use LaunchedEffect to handle permissions logic when the composition is launched.
//    LaunchedEffect(key1 = permissionState) {
//        // Check if all previously granted permissions are revoked.
//        val allPermissionsRevoked =
//            permissionState.permissions.size == permissionState.revokedPermissions.size
//
//        // Filter permissions that need to be requested.
//        val permissionsToRequest = permissionState.permissions.filter {
//            !it.status.isGranted
//        }
//
//        // If there are permissions to request, launch the permission request.
//        if (permissionsToRequest.isNotEmpty()) permissionState.launchMultiplePermissionRequest()
//
//        // Execute callbacks based on permission status.
//        if (allPermissionsRevoked) {
//            onPermissionsRevoked()
//        } else {
//            if (permissionState.allPermissionsGranted) {
//                onPermissionGranted()
//            } else {
//                onPermissionDenied()
//            }
//        }
//    }
//}
//
///**
// * Retrieves the current user location asynchronously.
// *
// * @param onGetCurrentLocationSuccess Callback function invoked when the current location is successfully retrieved.
// *        It provides a Pair representing latitude and longitude.
// * @param onGetCurrentLocationFailed Callback function invoked when an error occurs while retrieving the current location.
// *        It provides the Exception that occurred.
// * @param priority Indicates the desired accuracy of the location retrieval. Default is high accuracy.
// *        If set to false, it uses balanced power accuracy.
// */
//@SuppressLint("MissingPermission")
//private fun getCurrentLocation(
//    onGetCurrentLocationSuccess: (Pair<Double, Double>) -> Unit,
//    onGetCurrentLocationFailed: (Exception) -> Unit,
//    priority: Boolean = true
//) {
//    // Determine the accuracy priority based on the 'priority' parameter
//    val accuracy = if (priority) Priority.PRIORITY_HIGH_ACCURACY
//    else Priority.PRIORITY_BALANCED_POWER_ACCURACY
//
//    // Check if location permissions are granted
//    if (areLocationPermissionsGranted()) {
//        // Retrieve the current location asynchronously
//        fusedLocationProviderClient.getCurrentLocation(
//            accuracy, CancellationTokenSource().token,
//        ).addOnSuccessListener { location ->
//            location?.let {
//                // If location is not null, invoke the success callback with latitude and longitude
//                onGetCurrentLocationSuccess(Pair(it.latitude, it.longitude))
//            }
//        }.addOnFailureListener { exception ->
//            // If an error occurs, invoke the failure callback with the exception
//            onGetCurrentLocationFailed(exception)
//        }
//    }
//}
//
///**
// * Checks if location permissions are granted.
// *
// * @return true if both ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION permissions are granted; false otherwise.
// */
//private fun areLocationPermissionsGranted(): Boolean {
//    return (ActivityCompat.checkSelfPermission(
//        this, Manifest.permission.ACCESS_FINE_LOCATION
//    ) == PackageManager.PERMISSION_GRANTED &&
//            ActivityCompat.checkSelfPermission(
//                this, Manifest.permission.ACCESS_COARSE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED)
//}