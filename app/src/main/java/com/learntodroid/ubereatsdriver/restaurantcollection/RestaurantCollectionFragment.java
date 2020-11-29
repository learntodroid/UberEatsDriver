package com.learntodroid.ubereatsdriver.restaurantcollection;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.maps.GaeRequestHandler;
import com.google.maps.GeoApiContext;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.learntodroid.ubereatsdriver.R;
import com.learntodroid.ubereatsdriver.sharedmodel.Order;
import com.learntodroid.ubereatsdriver.sharedmodel.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantCollectionFragment extends Fragment implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSIONS_REQUEST = 2000;
    private static final int UPDATE_INTERVAL = 5000;

    private RestaurantCollectionViewModel restaurantCollectionViewModel;
    private MapView mapView;
    private Marker driverMarker, restrauntMarker;
    private Polyline routePolyline;
    private GoogleMap googleMap;
    private GeoApiContext geoApiContext;

    private TextView currentLocationTextView;

    private FusedLocationProviderClient locationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        restaurantCollectionViewModel = new ViewModelProvider(this).get(RestaurantCollectionViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurantcollection, container, false);

        currentLocationTextView = view.findViewById(R.id.fragment_restaurantcollection_currentlocation);

        setupLocationServices();

        view.findViewById(R.id.fragment_restaurantcollection_getlocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLocationUpdates();
            }
        });

        view.findViewById(R.id.fragment_restaurantcollection_stoplocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopLocationUpdates();
            }
        });

        view.findViewById(R.id.fragment_restaurantcollection_calculatedirections).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order order = restaurantCollectionViewModel.getSelectedOrderLiveData().getValue();
                Restaurant restaurant = order.getRestaurant();

                Location currentLocation = restaurantCollectionViewModel.getDriverLocationLiveData().getValue();

                com.google.maps.model.LatLng start = new com.google.maps.model.LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                com.google.maps.model.LatLng end = new com.google.maps.model.LatLng(restaurant.getLatitude(), restaurant.getLongitude());

                restaurantCollectionViewModel.calculateDirections(geoApiContext, start, end);
            }
        });

        restaurantCollectionViewModel.getSelectedOrderLiveData().observe(getViewLifecycleOwner(), new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                if (order != null) {
                    Toast.makeText(getContext(), order.getRestaurant().getAddress(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        restaurantCollectionViewModel.getDriverLocationLiveData().observe(getViewLifecycleOwner(), new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                if (location != null) {
                    currentLocationTextView.setText(String.format("%f, %f", location.getLatitude(), location.getLongitude()));

                    LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
                    if (driverMarker == null) {
                        driverMarker = googleMap.addMarker(
                                new MarkerOptions()
                                        .position(position)
                                        .title("Driver")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                                        )
                        );
                    } else {
                        driverMarker.setPosition(position);
                    }
                }
            }
        });

        restaurantCollectionViewModel.getRestaurantDirectionsResultLiveData().observe(getViewLifecycleOwner(), new Observer<DirectionsResult>() {
            @Override
            public void onChanged(DirectionsResult result) {
                if (result != null) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(RestaurantCollectionFragment.class.getSimpleName(), "run: result routes: " + result.routes.length);

                            for(DirectionsRoute route: result.routes){
                                Log.d(RestaurantCollectionFragment.class.getSimpleName(), "run: leg: " + route.legs[0].toString());
                                List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                                List<LatLng> newDecodedPath = new ArrayList<>();

                                // This loops through all the LatLng coordinates of ONE polyline.
                                for(com.google.maps.model.LatLng latLng: decodedPath){

                                    newDecodedPath.add(new LatLng(
                                            latLng.lat,
                                            latLng.lng
                                    ));
                                }

                                if (routePolyline == null) {
                                    routePolyline = googleMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                                } else {
                                    routePolyline.remove();
                                    routePolyline = googleMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                                }

                                routePolyline.setColor(Color.GRAY);
                                routePolyline.setClickable(true);

                            }
                        }
                    });
                }
            }
        });

        mapView = view.findViewById(R.id.fragment_restaurantcollection_mapview);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(getResources().getString(R.string.google_maps_api_key));
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        geoApiContext = new GeoApiContext.Builder()
                .apiKey(getResources().getString(R.string.google_directions_api_key))
                .build();

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Order order = restaurantCollectionViewModel.getSelectedOrderLiveData().getValue();
        Restaurant restaurant = order.getRestaurant();

        LatLng restaurantPosition = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());

        restrauntMarker = googleMap.addMarker(new MarkerOptions().position(restaurantPosition).title(restaurant.getTitle()));

        PolylineOptions polylineOptions = new PolylineOptions()
                .add(restaurantPosition);

        googleMap.addPolyline(polylineOptions);

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(restaurantPosition, 15f)));

        this.googleMap = googleMap;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        stopLocationUpdates();
    }

    public void setupLocationServices() {
        locationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
                if (locationAvailability.isLocationAvailable()) {
                    Log.i(RestaurantCollectionFragment.class.getSimpleName(),"Location isLocationAvailable");
                } else {
                    Log.i(RestaurantCollectionFragment.class.getSimpleName(),"Location is unavailable");
                }
            }

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Log.i(RestaurantCollectionFragment.class.getSimpleName(),"Location onLocationResult");
                restaurantCollectionViewModel.updateDriverLocation(locationResult.getLastLocation());
            }
        };
    }

    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            locationProviderClient.requestLocationUpdates(locationRequest,locationCallback, getActivity().getMainLooper());
            locationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    restaurantCollectionViewModel.updateDriverLocation(location);
                }
            });

            locationProviderClient.getLastLocation().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i(RestaurantCollectionFragment.class.getSimpleName(), "Exception while getting the location: "+e.getMessage());
                }
            });
        } else {
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)){
                Toast.makeText(getContext(), "Permission needed", Toast.LENGTH_LONG).show();
            } else {
                requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSIONS_REQUEST);
            }
        }
    }

    public void stopLocationUpdates() {
        locationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Toast.makeText(getContext(), "onRequestPermissionsResult: " + requestCode, Toast.LENGTH_SHORT).show();
        if (requestCode == LOCATION_PERMISSIONS_REQUEST) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(getContext(), "Location permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}
