package com.learntodroid.ubereatsdriver.driver;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseUser;
import com.learntodroid.ubereatsdriver.R;
import com.learntodroid.ubereatsdriver.sharedmodel.Driver;

public class DriverFragment extends Fragment {
    private DriverViewModel driverViewModel;
    private Button acceptingDeliveriesButton;
    private Button createDriverButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        driverViewModel = new ViewModelProvider(this).get(DriverViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver, container, false);

        acceptingDeliveriesButton = view.findViewById(R.id.fragment_driver_acceptingdeliveries);
        createDriverButton = view.findViewById(R.id.fragment_driver_createDriver);

        driverViewModel.getAcceptingDeliveriesLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean acceptingDeliveries) {
                if (acceptingDeliveries != null) {
                    if (acceptingDeliveries) {
                        acceptingDeliveriesButton.setText("Disable New Deliveries");
                    } else {
                        acceptingDeliveriesButton.setText("Enable New Deliveries");
                    }
                }
            }
        });

        driverViewModel.getDriverLiveData().observe(getViewLifecycleOwner(), new Observer<Driver>() {
            @Override
            public void onChanged(Driver driver) {
                if (driver != null) {
                    createDriverButton.setVisibility(View.GONE);
                    Log.i(DriverFragment.class.getSimpleName(), "Driver Info: " + driver.getName());
                }
            }
        });

        createDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                driverViewModel.createDriver(new Driver("Bob", driverViewModel.getUserLiveData().getValue().getUid(), 0, 0));
            }
        });

        acceptingDeliveriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                driverViewModel.toggleAcceptingDeliveries();
            }
        });

        return view;
    }
}
