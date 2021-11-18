package com.example.assignment2;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.assignment2.databinding.FragmentThirdBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ThirdFragment extends Fragment {

    private FragmentThirdViewModel mViewModel;
    EditText address_delete_input;//creating variables used
    EditText address_add_input;
    EditText old_update_address_input;
    EditText new_address_update_input;
    String address_delete;
    String address_add;
    String old_address_input;
    String new_address_input;

    private FragmentThirdBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        View fragmentThirdLayout = inflater.inflate(R.layout.fragment_third, container, false);
        return fragmentThirdLayout;

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.view_database_button).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {
                myHelper help = new myHelper(getActivity());//creting helper object to access database
                SQLiteDatabase database = help.getReadableDatabase();//getting readable database
                Cursor cursor = database.rawQuery("SELECT ADDRESS, LONGITUDE, LATITUDE FROM LOCATIONS", new String[]{});//querying through database
                if(cursor != null)//if database is not empty
                {
                    cursor.moveToFirst();//move to first value if not empty
                }
                StringBuilder b = new StringBuilder();//create string builder object
                AlertDialog.Builder builder = new AlertDialog.Builder((getActivity()));//create alert dialog object
                do{
                    String addy = cursor.getString(0);//get address from database
                    double longitude = cursor.getDouble(1);//get longitude from database
                    double latitude = cursor.getDouble(2);//get latitude from database
                    b.append("Address: "+addy + "\nLatitude: "+latitude+"\nLongitude: "+longitude+"\n\n");//create string


                }while(cursor.moveToNext());//run loop until the cursor has no more rows in the database
                builder.setCancelable(true);//setting if the user can click off of it
                builder.setTitle("Database Entities");//title of alert dialog
                builder.setMessage(b);//passing message to alert dialog
                builder.show();//showing alert dialog
            }
        });
        address_delete_input = (EditText) view.findViewById(R.id.address_delete_input);//gets input
        view.findViewById(R.id.address_delete_button).setOnClickListener(new View.OnClickListener()
        {

            @Override

            public void onClick(View view) {

                address_delete = address_delete_input.getText().toString();//converts the value into the correct form

                myHelper help = new myHelper(getActivity());//creating myHelper object to utilize database
                SQLiteDatabase database = help.getReadableDatabase(); //creating database object
                Cursor cursor = database.rawQuery("SELECT ADDRESS, LONGITUDE, LATITUDE FROM LOCATIONS", new String[]{});//creating cursor to querey data base
                if(cursor != null)//checking if database has items
                {
                    cursor.moveToFirst();//going to first cursor item
                }
                int flag = 0; //intializing flag
                do{
                    String addy = cursor.getString(0); //getting address from database

                    if(addy.equalsIgnoreCase(address_delete))//checking database if the entred address is there
                    {
                        database.delete("LOCATIONS", "ADDRESS = ?", new String[]{address_delete});//deleting row in database that contains the given address
                        Toast.makeText(getActivity(), "Deleted " + address_delete, Toast.LENGTH_LONG).show();//outputting delete message
                        flag = 1;//changing flag
                        break;
                    }
                    else
                    {
                        flag = 0;//changing flag
                    }
                }while(cursor.moveToNext());//run loop until the cursor has no more rows in the database
                if(flag == 0)// if the flag is 0 there was no matching address in the database
                {
                    Toast.makeText(getActivity(), "No Matching Address in Database!", Toast.LENGTH_LONG).show();//output message
                }

            }
        });

        address_add_input = (EditText) view.findViewById(R.id.address_add_input);//finding input values and button to listen too
        view.findViewById(R.id.address_add_button).setOnClickListener(new View.OnClickListener()
        {

            @Override

            public void onClick(View view) {
                address_add = address_add_input.getText().toString();//converts the value into the correct form
                Geocoder geoCoder = new Geocoder(getActivity());//creating geocoder object
                myHelper help = new myHelper(getActivity());//creting helper object to access database
                SQLiteDatabase database = help.getWritableDatabase();//getting writeable database
                Cursor cursor = database.rawQuery("SELECT ADDRESS, LONGITUDE, LATITUDE FROM LOCATIONS", new String[]{});//querying through database
                if(cursor != null)//if database is not empty
                {
                    cursor.moveToFirst();//move to first value if not empty
                }
                List<Address> AL = new ArrayList<>();//creating Array list for geocoder
                AlertDialog.Builder builder = new AlertDialog.Builder((getActivity()));//creting alert dialog for error output
                Address addy;
                if(address_add.matches(""))//if no valid addresses inputted
                {
                    Toast.makeText(getActivity(),"Please Enter Address", Toast.LENGTH_LONG).show();//if no valid addresses inputted
                }
                else
                {
                    try {
                        AL = geoCoder.getFromLocationName(address_add, 1);//getting addresses information
                    } catch (IOException e) {
                        builder.setCancelable(true);//creting alert dialog for error output
                        builder.setTitle("Error!");//creting alert dialog for error output
                        builder.setMessage(e.getMessage());//creting alert dialog for error output
                        builder.show();//creting alert dialog for error output
                    }
                    addy = AL.get(0);//getting geo coder output
                    StringBuilder lat = new StringBuilder();//creating latitude string builder
                    StringBuilder longi = new StringBuilder();//creating longitude string builder
                    lat.append(String.valueOf(addy.getLatitude()));//getting latitude
                    longi.append(String.valueOf(addy.getLongitude()));//getting longitude
                    int flag = 0;//initializing flag
                    do{
                        String add = cursor.getString(0);//getting addresses from database
                        if(add.equalsIgnoreCase(address_add))// if the address entered is in the database
                        {
                            Toast.makeText(getActivity(), "Address Already in Database!", Toast.LENGTH_LONG).show(); //outputting that its already in database
                            flag = 1; //changing flag
                            break;
                        }
                    }while(cursor.moveToNext()); //run until no more rows in database
                    if(flag == 0)
                    {
                        help.insertData(address_add, addy.getLatitude(), addy.getLongitude(), database); //add values to database
                        Toast.makeText(getActivity(), lat + ", " + longi + " of Entered Address", Toast.LENGTH_LONG).show(); //output message
                    }

                }

            }
        });

        old_update_address_input = (EditText) view.findViewById(R.id.old_update_address_input); //finding input values and button to listen too
        new_address_update_input = (EditText) view.findViewById(R.id.new_address_update_input);
        view.findViewById(R.id.update_address_button).setOnClickListener(new View.OnClickListener()
        {

            @Override

            public void onClick(View view) {
                old_address_input = old_update_address_input.getText().toString();//converts the value into the correct form
                new_address_input = new_address_update_input.getText().toString();//converts the value into the correct form
                Geocoder geoCoder = new Geocoder(getActivity()); //creating geocoder object
                myHelper help = new myHelper(getActivity());//creting helper object to access database
                SQLiteDatabase database = help.getWritableDatabase();//getting writeable database
                Cursor cursor = database.rawQuery("SELECT ADDRESS, LONGITUDE, LATITUDE FROM LOCATIONS", new String[]{});//querying through database
                if(cursor != null)//if database is not empty
                {
                    cursor.moveToFirst();//move to first value if not empty
                }
                List<Address> AL = new ArrayList<>(); //creating Array list for geocoder
                AlertDialog.Builder builder = new AlertDialog.Builder((getActivity()));//creting alert dialog for error output
                Address addy;
                if(new_address_input.matches("") || old_address_input.matches(""))  //if no valid addresses inputted
                {
                    Toast.makeText(getActivity(),"Please Enter Address", Toast.LENGTH_LONG).show(); //if no valid addresses inputted
                }
                else
                {
                    try {
                        AL = geoCoder.getFromLocationName(new_address_input, 1); //getting addresses information
                    } catch (IOException e) {
                        builder.setCancelable(true);//creting alert dialog for error output
                        builder.setTitle("Error!");//creting alert dialog for error output
                        builder.setMessage(e.getMessage());//creting alert dialog for error output
                        builder.show(); //creting alert dialog for error output
                    }
                    addy = AL.get(0); //getting geo coder output
                    StringBuilder lat = new StringBuilder(); //creating latitude string builder
                    StringBuilder longi = new StringBuilder(); //creating longitude string builder
                    lat.append(String.valueOf(addy.getLatitude()));//getting latitude
                    longi.append(String.valueOf(addy.getLongitude())); //getting longitude
                    int flag = 0; //initializing flag
                    do{
                        String add = cursor.getString(0); //getting addresses from database
                        if(add.equalsIgnoreCase(old_address_input)) // if the old address entered is in the database
                        {
                            ContentValues locations = new ContentValues(); //creating Content Values object
                            locations.put("ADDRESS", new_address_input);//adding update
                            locations.put("LATITUDE", addy.getLatitude());//adding update
                            locations.put("LONGITUDE", addy.getLongitude());//adding changed
                            database.update("LOCATIONS", locations, "ADDRESS = ?", new String[]{old_address_input}); //pushing changes to database
                            Toast.makeText(getActivity(), "Address Updated in Database!", Toast.LENGTH_LONG).show(); //output message notifying of update
                            flag = 1; //changing falg
                            break;
                        }
                    }while(cursor.moveToNext());//run loop until the cursor has no more rows in the database
                    if(flag == 0)// if the flag is 0 there was no matching address in the database
                    {
                        Toast.makeText(getActivity(), "No Matching Address in Database!", Toast.LENGTH_LONG).show();//output message
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


