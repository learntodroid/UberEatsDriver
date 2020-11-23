package com.learntodroid.ubereatsdriver.driver;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.learntodroid.ubereatsdriver.R;

public class DriverFragment extends Fragment {
    private DriverViewModel driverViewModel;
    private Button acceptingDeliveriesButton;

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



        acceptingDeliveriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                driverViewModel.toggleAcceptingDeliveries();
            }
        });

        return view;
    }
}
