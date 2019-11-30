package ph.roadtrip.roadtrip.bookingmodule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

import ph.roadtrip.roadtrip.R;

public class SuccessRefundFragment extends Fragment {

    Button btnMyBookings;
    private TextView tvReference, tvTotalAmountRefunded, tvRefundFee, tvTotalAmount;
    private Double totalRefund, paypalFee, totalAmount;
    private String reference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_success_refund, container, false);

        //Retrieve the value
        totalRefund = getArguments().getDouble("totalRefund");
        totalAmount = getArguments().getDouble("totalAmount");
        paypalFee = getArguments().getDouble("paypalFee");
        reference = getArguments().getString("reference");

        btnMyBookings = view.findViewById(R.id.btnMyBookings);
        tvReference = view.findViewById(R.id.tvReference);
        tvTotalAmountRefunded = view.findViewById(R.id.tvTotalAmountRefunded);
        tvTotalAmount = view.findViewById(R.id.tvTotalAmount);
        tvRefundFee = view.findViewById(R.id.tvRefundFee);

        DecimalFormat df = new DecimalFormat("#,###.00");

        tvReference.setText(reference);
        tvTotalAmountRefunded.setText(String.valueOf(df.format(totalRefund)));
        tvTotalAmount.setText(String.valueOf(df.format(totalAmount)));
        tvRefundFee.setText(String.valueOf(df.format(paypalFee)));

        btnMyBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new BookingFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

}
