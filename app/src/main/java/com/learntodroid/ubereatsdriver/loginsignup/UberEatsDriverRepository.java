package com.learntodroid.ubereatsdriver.loginsignup;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;
import com.learntodroid.ubereatsdriver.sharedmodel.Driver;
import com.learntodroid.ubereatsdriver.sharedmodel.Order;

import java.util.ArrayList;
import java.util.List;

public class UberEatsDriverRepository {
    private static final UberEatsDriverRepository instance = new UberEatsDriverRepository();

    private FirebaseAuth firebaseAuth;
    private MutableLiveData<FirebaseUser> userLiveData;

    private MutableLiveData<Boolean> acceptingDeliveriesLiveData;

    private FirebaseFirestore db;

    private MutableLiveData<List<Order>> ordersLiveData;
    private MutableLiveData<List<String>> orderIdsLiveData;

    private MutableLiveData<Order> selectedOrderLiveData;

    private MutableLiveData<Location> restaurantDriverLocationLiveData;
    private MutableLiveData<Location> customerDriverLocationLiveData;
    private MutableLiveData<DirectionsResult> restaurantDirectionsResultLiveData;
    private MutableLiveData<DirectionsResult> customerDirectionsResultLiveData;

    private MutableLiveData<String> driverIdLiveData;
    private MutableLiveData<Driver> driverLiveData;

    public UberEatsDriverRepository() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.userLiveData = new MutableLiveData<>();
        if (firebaseAuth.getCurrentUser() != null) {
            userLiveData.postValue(firebaseAuth.getCurrentUser());
        }

        this.acceptingDeliveriesLiveData = new MutableLiveData<>(false);
        this.db = FirebaseFirestore.getInstance();
//        this.restaurantIdMutableLiveData = new MutableLiveData<>();
//        this.restaurantsLiveData = new MutableLiveData<>();
//        this.selectedRestaurantLiveData = new MutableLiveData<>();
        this.ordersLiveData = new MutableLiveData<>();
        this.orderIdsLiveData = new MutableLiveData<>();
        this.selectedOrderLiveData = new MutableLiveData<>();

        this.restaurantDriverLocationLiveData = new MutableLiveData<>();
        this.customerDriverLocationLiveData = new MutableLiveData<>();
        this.restaurantDirectionsResultLiveData = new MutableLiveData<>();
        this.customerDirectionsResultLiveData = new MutableLiveData<>();

        this.driverIdLiveData = new MutableLiveData<>();
        this.driverLiveData = new MutableLiveData<>();
    }

    public void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userLiveData.postValue(firebaseAuth.getCurrentUser());
                        }
                    }
                });
    }

    public void signUp(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userLiveData.postValue(firebaseAuth.getCurrentUser());
                        }
                    }
                });
    }

    public void toggleAcceptingDeliveries() {
        acceptingDeliveriesLiveData.postValue(!acceptingDeliveriesLiveData.getValue());
    }

    public void createDriver(Driver driver) {
        db.collection("drivers")
                .add(driver)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(UberEatsDriverRepository.class.getSimpleName(), "DocumentSnapshot added with ID: " + documentReference.getId());
                        driverIdLiveData.postValue(documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(UberEatsDriverRepository.class.getSimpleName(), "Error adding document", e);
                    }
                });
    }

    public void queryDriver() {
        String userId = firebaseAuth.getUid();

        db.collection("drivers")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.i(UberEatsDriverRepository.class.getSimpleName(), "Task successful: " + task.getResult().getDocuments().size());
                            if (task.getResult().getDocuments().size() > 0) {
                                Driver driver = task.getResult().getDocuments().get(0).toObject(Driver.class);
                                driverLiveData.postValue(driver);
                                String driverId = task.getResult().getDocuments().get(0).getId();
                                driverIdLiveData.setValue(driverId);
                            }
                        } else {
                            Log.i(UberEatsDriverRepository.class.getSimpleName(), "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void updateDriverLocation(Driver driver, double latitude, double longitude) {
        driver.setLatitude(latitude);
        driver.setLongitude(longitude);

        String driverId = driverIdLiveData.getValue();

        DocumentReference orderRef = db.collection("drivers").document(driverId);
        orderRef
                .set(driver)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(UberEatsDriverRepository.class.getSimpleName(), "DocumentSnapshot successfully updated!");
                        driverLiveData.postValue(driver);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(UberEatsDriverRepository.class.getSimpleName(), "Error updating document", e);
                    }
                });
    }

    public void queryOrders(String ordersStatus) {
        final List<Order> orders = new ArrayList<>();
        final List<String> orderIds = new ArrayList<>();

        db.collection("orders")
//                .whereEqualTo("restaurant.title", "McDonald's")
                .whereEqualTo("status", ordersStatus)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(UberEatsDriverRepository.class.getSimpleName(), document.getId() + " => " + document.getData());
                                Order order = document.toObject(Order.class);
                                orders.add(order);
                                orderIds.add(document.getId());
                            }
                            ordersLiveData.postValue(orders);
                            orderIdsLiveData.postValue(orderIds);
                        } else {
                            Log.d(UberEatsDriverRepository.class.getSimpleName(), "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void updateOrderStatus(Order order, String newStatus) {
        int orderIndex = ordersLiveData.getValue().indexOf(order);
        String orderId = orderIdsLiveData.getValue().get(orderIndex);
        String currentStatus = order.getStatus();

        order.setStatus(newStatus);

        DocumentReference orderRef = db.collection("orders").document(orderId);
        orderRef
                .set(order)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(UberEatsDriverRepository.class.getSimpleName(), "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(UberEatsDriverRepository.class.getSimpleName(), "Error updating document", e);
                    }
                });

        queryOrders(currentStatus);
    }

    public void setSelectedOrder(Order order) {
        selectedOrderLiveData.postValue(order);
    }

    public void updateRestaurantDriverLocation(Location location) {
        this.restaurantDriverLocationLiveData.postValue(location);
    }

    public void updateCustomerDriverLocation(Location location) {
        this.customerDriverLocationLiveData.postValue(location);

        Driver driver = driverLiveData.getValue();
        updateDriverLocation(driver, location.getLatitude(), location.getLongitude());
    }

    public void calculateRestaurantDirections(GeoApiContext geoApiContext, LatLng start, LatLng end) {
        DirectionsApiRequest directions = new DirectionsApiRequest(geoApiContext);
        directions.origin(start);
        directions.destination(end).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                Log.i(UberEatsDriverRepository.class.getSimpleName(), "DirectionsResult success");
                Log.i(UberEatsDriverRepository.class.getSimpleName(), "calc directions: routes: " + result.routes[0].toString());
                Log.i(UberEatsDriverRepository.class.getSimpleName(), "calc directions: duration: " + result.routes[0].legs[0].duration);
                Log.i(UberEatsDriverRepository.class.getSimpleName(), "calc directions: distance: " + result.routes[0].legs[0].distance);
                Log.i(UberEatsDriverRepository.class.getSimpleName(), "calc directions: geoCodedWayPoints: " + result.geocodedWaypoints[0].toString());
                restaurantDirectionsResultLiveData.postValue(result);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(UberEatsDriverRepository.class.getSimpleName(), "DirectionsResult failure: " + e.getMessage());
            }
        });
    }

    public void calculateCustomerDirections(GeoApiContext geoApiContext, LatLng start, LatLng end) {
        DirectionsApiRequest directions = new DirectionsApiRequest(geoApiContext);
        directions.origin(start);
        directions.destination(end).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                Log.i(UberEatsDriverRepository.class.getSimpleName(), "DirectionsResult success");
                Log.i(UberEatsDriverRepository.class.getSimpleName(), "calc directions: routes: " + result.routes[0].toString());
                Log.i(UberEatsDriverRepository.class.getSimpleName(), "calc directions: duration: " + result.routes[0].legs[0].duration);
                Log.i(UberEatsDriverRepository.class.getSimpleName(), "calc directions: distance: " + result.routes[0].legs[0].distance);
                Log.i(UberEatsDriverRepository.class.getSimpleName(), "calc directions: geoCodedWayPoints: " + result.geocodedWaypoints[0].toString());
                customerDirectionsResultLiveData.postValue(result);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(UberEatsDriverRepository.class.getSimpleName(), "DirectionsResult failure: " + e.getMessage());
            }
        });
    }

//
//    public void setSelectedRestaurant(Restaurant restaurant) {
//        selectedRestaurantLiveData.postValue(restaurant);
//    }
//

    public MutableLiveData<Order> getSelectedOrderLiveData() {
        return selectedOrderLiveData;
    }

    public MutableLiveData<Boolean> getAcceptingDeliveriesLiveData() {
        return acceptingDeliveriesLiveData;
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }
//
//    public MutableLiveData<List<Restaurant>> getRestaurantsLiveData() {
//        return restaurantsLiveData;
//    }
//
    public MutableLiveData<List<Order>> getOrdersLiveData() {
        return ordersLiveData;
    }

    public MutableLiveData<Location> getRestaurantDriverLocationLiveData() {
        return restaurantDriverLocationLiveData;
    }

    public MutableLiveData<Location> getCustomerDriverLocationLiveData() {
        return customerDriverLocationLiveData;
    }

    public MutableLiveData<DirectionsResult> getRestaurantDirectionsResultLiveData() {
        return restaurantDirectionsResultLiveData;
    }

    public MutableLiveData<DirectionsResult> getCustomerDirectionsResultLiveData() {
        return customerDirectionsResultLiveData;
    }

    public MutableLiveData<String> getDriverIdLiveData() {
        return driverIdLiveData;
    }

    public MutableLiveData<Driver> getDriverLiveData() {
        return driverLiveData;
    }

    public static UberEatsDriverRepository getInstance() {
        return instance;
    }
}
