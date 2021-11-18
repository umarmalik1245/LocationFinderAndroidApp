package com.example.assignment2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.assignment2.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {


    String address; //creating variables used
    EditText address_input;//creating variables used

    TextView address_output;//creating variables used
    TextView latitude_output;//creating variables used
    TextView longitude_output;//creating variables used

private FragmentSecondBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        View fragmentSecondLayout = inflater.inflate(R.layout.fragment_second, container, false);
        address_output = fragmentSecondLayout.findViewById(R.id.Address_output);//Used to update the output as user pressed enter
        latitude_output = fragmentSecondLayout.findViewById(R.id.latitude_output);//Used to update the output as user pressed enter
        longitude_output = fragmentSecondLayout.findViewById(R.id.Longitude_output);//Used to update the output as user pressed enter
        return fragmentSecondLayout;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        address_input = (EditText) view.findViewById(R.id.address_input);//getting value the user inputs into the address input edit text
        view.findViewById(R.id.enterAddress_button).setOnClickListener(new View.OnClickListener() //on click listener for the enter address button
        {
            @Override
            public void onClick(View view)
            {
                address = address_input.getText().toString(); //converts the value into the correct form
                if(address.matches(""))//if nothing is entered prompt user to enter a address before clicking the button
                {
                    Toast.makeText(getActivity(),"Please Enter Address", Toast.LENGTH_LONG).show();//toast to tell user
                }
                else
                {
                    myHelper help = new myHelper(getActivity()); //creting helper object to access database
                    SQLiteDatabase database = help.getReadableDatabase(); //getting readable database
                    Cursor cursor = database.rawQuery("SELECT ADDRESS, LONGITUDE, LATITUDE FROM LOCATIONS", new String[]{}); //querying through database
                    if(cursor != null) //if database is not empty
                    {
                        cursor.moveToFirst(); //move to first value if not empty
                    }
                    StringBuilder b = new StringBuilder();// string builder for address
                    StringBuilder b2 = new StringBuilder();//string builder for latitude
                    StringBuilder b3 = new StringBuilder();//string builder for longitude
                    int flag = 0; //flag to check
                    do{
                        String addy = cursor.getString(0); //accessing first column of the first row and so on
                        double longitude = cursor.getDouble(1);//accessing second column of the first row and so on
                        double latitude = cursor.getDouble(2);//accessing third column of the first row and so on
                        if(addy.equalsIgnoreCase(address)) //If finds entered address
                        {
                            b.append(addy); //add address
                            b2.append(longitude);//add latitude
                            b3.append(latitude);//add longitude
                            address_output.setText(b); //outputting to screen
                            latitude_output.setText(b2);//outputting to screen
                            longitude_output.setText(b3);//outputting to screen
                            flag = 1; //changing flag
                            break;
                        }
                        else
                        {
                            flag = 0;
                        }
                    }while(cursor.moveToNext()); //run loop until the cursor has no more rows in the database
                    if(flag == 0) // if the flag is 0 there was no matching address in the database
                    {
                        Toast.makeText(getActivity(), "No Matching Address in Database!", Toast.LENGTH_LONG).show(); //output message
                    }
                }
            }
        });
    }


@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}