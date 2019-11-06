package ph.roadtrip.roadtrip.bookingmodule;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.time.LocalDateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ph.roadtrip.roadtrip.BaseActivity;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.DashboardActivity;
import ph.roadtrip.roadtrip.MapsActivity;
import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.classes.SliderBookServiceActivity;

public class BookServiceActivity extends BaseActivity {

    private static final String KEY_START_DATE = "startDate";
    private static final String KEY_END_DATE = "endDate";
    private static final String KEY_START_TIME = "startTime";
    private static final String KEY_RETURN_TIME = "returnTime";
    private static final String KEY_SERVICE_TYPE = "serviceType";


    private SessionHandler session;
    private Intent load;
    private GridLayout mainGrid;

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView startDate, endDate;
    private int year, month, day;
    private String sdate, edate;
    private String myDate, myDate2, myDate3;

    Button AccessTime, ReturnTime;
    TextView DisplayTime, DisplayReturnTime;
    private int CalendarHour, CalendarMinute;
    String format;
    Calendar calendar_time;
    TimePickerDialog timepickerdialog;
    Spinner ServiceSpinner;

    CardView sedan, hatchback, MPV, SUV, pickup;

    private String dateStart, dateEnd, startTime, returnTime, serviceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_book_service, null, false);
        drawer.addView(contentView, 0);


        mainGrid = (GridLayout) findViewById(R.id.mainGrid);

        startDate = (TextView) findViewById(R.id.start_date);
        endDate = (TextView) findViewById(R.id.end_date);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        AccessTime = (Button)findViewById(R.id.btnPickupTime);
        DisplayTime = (TextView)findViewById(R.id.pickup_time);
        ReturnTime = (Button)findViewById(R.id.btnReturnTime);
        DisplayReturnTime = (TextView)findViewById(R.id.return_time);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

        //CardView Initialize
        sedan = findViewById(R.id.cardSedan);
        hatchback = findViewById(R.id.cardHatchback);
        MPV = findViewById(R.id.cardMPV);
        SUV = findViewById(R.id.cardSUV);
        pickup = findViewById(R.id.cardPickup);

        ServiceSpinner = findViewById(R.id.ServiceSpinner);

        //Convert DateTime
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
        Date current = new Date();
        final String currentDateFinal = formatter.format(current);

        //Cardview On Clicks
        sedan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateStart = startDate.getText().toString();
                dateEnd = endDate.getText().toString();
                startTime = DisplayTime.getText().toString();
                returnTime = DisplayReturnTime.getText().toString();
                serviceType = ServiceSpinner.getSelectedItem().toString();

                sdate = startDate.getText().toString();
                edate = endDate.getText().toString();

                DateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
                DateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String startDateTime = dateStart + " " + startTime;
                String endDateTime = dateEnd + " " + returnTime;

                Date date = null;
                Date date2 = null;
                Date date3 = null;

                try {
                    date = inFormat.parse(startDateTime);
                    date2 = inFormat.parse(endDateTime);
                    date3 = formatter.parse(currentDateFinal);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String myDate, myDate2;

                if (date != null && date2 != null) {
                    myDate = outFormat.format(date);
                    myDate2 = outFormat.format(date2);

                    if (date.after(date3)) {
                        if (!date2.before(date)) {
                            Intent i = new Intent(BookServiceActivity.this, MapsActivity.class);
                            i.putExtra("KEY_START", sdate);
                            i.putExtra("KEY_END", edate);
                            i.putExtra("KEY_LBL_START", startDateTime);
                            i.putExtra("KEY_LBL_END", endDateTime);
                            i.putExtra("KEY_START_DATE", myDate);
                            i.putExtra("KEY_END_DATE", myDate2);
                            i.putExtra("KEY_SERVICE_TYPE", serviceType);
                            i.putExtra("KEY_CAR_TYPE", "Sedan");
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "End Date must not be before Start Date", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Start Date must not be before the Current Date", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        hatchback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateStart = startDate.getText().toString();
                dateEnd = endDate.getText().toString();
                startTime = DisplayTime.getText().toString();
                returnTime = DisplayReturnTime.getText().toString();
                serviceType = ServiceSpinner.getSelectedItem().toString();

                sdate = startDate.getText().toString();
                edate = endDate.getText().toString();

                //Convert DateTime
                DateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
                DateFormat outFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
                String startDateTime = dateStart + " " + startTime;
                String endDateTime = dateEnd + " " + returnTime;

                Date date = null;
                Date date2 = null;
                Date date3 = null;
                try {
                    date = inFormat.parse(startDateTime);
                    date2 = inFormat.parse(endDateTime);
                    date3 = formatter.parse(currentDateFinal);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (date != null && date2 != null) {
                    myDate = outFormat.format(date);
                    myDate2 = outFormat.format(date2);
                    myDate3 = outFormat.format(date3);

                    if (date.after(date3)) {
                    if (date2.after(date)){
                        Intent i = new Intent(BookServiceActivity.this, MapsActivity.class);
                        i.putExtra("KEY_START", sdate);
                        i.putExtra("KEY_END", edate);
                        i.putExtra("KEY_LBL_START", startDateTime);
                        i.putExtra("KEY_LBL_END", endDateTime);
                        i.putExtra("KEY_START_DATE", myDate);
                        i.putExtra("KEY_END_DATE", myDate2);
                        i.putExtra("KEY_SERVICE_TYPE", serviceType);
                        i.putExtra("KEY_CAR_TYPE", "Hatchback");
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "End Date must not be before Start Date", Toast.LENGTH_LONG).show();
                    }

                } else {
                        Toast.makeText(getApplicationContext(), "Start Date must not be before the Current Date", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        MPV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateStart = startDate.getText().toString();
                dateEnd = endDate.getText().toString();
                startTime = DisplayTime.getText().toString();
                returnTime = DisplayReturnTime.getText().toString();
                serviceType = ServiceSpinner.getSelectedItem().toString();

                sdate = startDate.getText().toString();
                edate = endDate.getText().toString();


                //Convert DateTime
                DateFormat inFormat = new SimpleDateFormat( "yyyy-MM-dd hh:mm aa");
                DateFormat outFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
                String startDateTime = dateStart + " " + startTime;
                String endDateTime = dateEnd + " " + returnTime;
                //Convert DateTime
                Date date = null;
                Date date2 = null;
                Date date3 = null;
                try {
                    date = inFormat.parse(startDateTime);
                    date2 = inFormat.parse(endDateTime);
                    date3 = formatter.parse(currentDateFinal);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                if (date != null && date2 != null) {
                    myDate = outFormat.format(date);
                    myDate2 = outFormat.format(date2);
                    myDate3 = outFormat.format(date3);

                    if (date.after(date3)) {
                        if (date2.after(date)) {
                            Intent i = new Intent(BookServiceActivity.this, MapsActivity.class);
                            i.putExtra("KEY_START", sdate);
                            i.putExtra("KEY_END", edate);
                            i.putExtra("KEY_LBL_START", startDateTime);
                            i.putExtra("KEY_LBL_END", endDateTime);
                            i.putExtra("KEY_START_DATE", myDate);
                            i.putExtra("KEY_END_DATE", myDate2);
                            i.putExtra("KEY_SERVICE_TYPE", serviceType);
                            i.putExtra("KEY_CAR_TYPE", "MPV");
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "End Date must not be before Start Date", Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "Start Date must not be before the Current Date", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        SUV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateStart = startDate.getText().toString();
                dateEnd = endDate.getText().toString();
                startTime = DisplayTime.getText().toString();
                returnTime = DisplayReturnTime.getText().toString();
                serviceType = ServiceSpinner.getSelectedItem().toString();
                sdate = startDate.getText().toString();
                edate = endDate.getText().toString();

                //Convert DateTime
                DateFormat inFormat = new SimpleDateFormat( "yyyy-MM-dd hh:mm aa");
                DateFormat outFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
                String startDateTime = dateStart + " " + startTime;
                String endDateTime = dateEnd + " " + returnTime;

                Date date = null;
                Date date2 = null;
                Date date3 = null;
                try {
                    date = inFormat.parse(startDateTime);
                    date2 = inFormat.parse(endDateTime);
                    date3 = formatter.parse(currentDateFinal);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                if (date != null && date2 != null) {
                    myDate = outFormat.format(date);
                    myDate2 = outFormat.format(date2);
                    myDate3 = outFormat.format(date3);

                    if (date.after(date3)) {
                        if (date2.after(date)) {
                            Intent i = new Intent(BookServiceActivity.this, MapsActivity.class);
                            i.putExtra("KEY_START", sdate);
                            i.putExtra("KEY_END", edate);
                            i.putExtra("KEY_LBL_START", startDateTime);
                            i.putExtra("KEY_LBL_END", endDateTime);
                            i.putExtra("KEY_START_DATE", myDate);
                            i.putExtra("KEY_END_DATE", myDate2);
                            i.putExtra("KEY_SERVICE_TYPE", serviceType);
                            i.putExtra("KEY_CAR_TYPE", "SUV");
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "End Date must not be before Start Date", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Start Date must not be before the Current Date", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateStart = startDate.getText().toString();
                dateEnd = endDate.getText().toString();
                startTime = DisplayTime.getText().toString();
                returnTime = DisplayReturnTime.getText().toString();
                serviceType = ServiceSpinner.getSelectedItem().toString();
                sdate = startDate.getText().toString();
                edate = endDate.getText().toString();


                //Convert DateTime
                DateFormat inFormat = new SimpleDateFormat( "yyyy-MM-dd hh:mm aa");
                DateFormat outFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
                String startDateTime = dateStart + " " + startTime;
                String endDateTime = dateEnd + " " + returnTime;

                Date date = null;
                Date date2 = null;
                Date date3 = null;
                try {
                    date = inFormat.parse(startDateTime);
                    date2 = inFormat.parse(endDateTime);
                    date3 = formatter.parse(currentDateFinal);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                if (date != null && date2 != null) {
                    myDate = outFormat.format(date);
                    myDate2 = outFormat.format(date2);
                    myDate3 = outFormat.format(date3);

                    if (date.after(date3)) {
                        if (date2.after(date)) {
                            Intent i = new Intent(BookServiceActivity.this, MapsActivity.class);
                            i.putExtra("KEY_START", sdate);
                            i.putExtra("KEY_END", edate);
                            i.putExtra("KEY_LBL_START", startDateTime);
                            i.putExtra("KEY_LBL_END", endDateTime);
                            i.putExtra("KEY_START_DATE", myDate);
                            i.putExtra("KEY_END_DATE", myDate2);
                            i.putExtra("KEY_SERVICE_TYPE", serviceType);
                            i.putExtra("KEY_CAR_TYPE", "Pickup");
                            startActivity(i);
                        } else {
                            Toast.makeText(getApplicationContext(), "End Date must not be before Start Date", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Start Date must not be before the Current Date", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        AccessTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
                CalendarMinute = calendar.get(Calendar.MINUTE);

                timepickerdialog = new TimePickerDialog(BookServiceActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                if (hourOfDay == 0) {
                                    hourOfDay += 12;
                                    format = "AM";
                                } else if (hourOfDay == 12) {
                                    format = "PM";
                                } else if (hourOfDay > 12) {
                                    hourOfDay -= 12;
                                    format = "PM";
                                } else {
                                    format = "AM";
                                }
                                int hour = hourOfDay % 12;
                                DisplayTime.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
                                        minute, format));
                            }
                        }, CalendarHour, CalendarMinute, false);
                timepickerdialog.show();
            }
        });

        ReturnTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
                CalendarMinute = calendar.get(Calendar.MINUTE);

                timepickerdialog = new TimePickerDialog(BookServiceActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                if (hourOfDay == 0) {
                                    hourOfDay += 12;
                                    format = "AM";
                                } else if (hourOfDay == 12) {
                                    format = "PM";
                                } else if (hourOfDay > 12) {
                                    hourOfDay -= 12;
                                    format = "PM";
                                } else {
                                    format = "AM";
                                }
                                int hour = hourOfDay % 12;
                                DisplayReturnTime.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
                                        minute, format));
                            }
                        }, CalendarHour, CalendarMinute, false);
                timepickerdialog.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_info).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_info:
                load = new Intent(getApplicationContext(), SliderBookServiceActivity.class);
                startActivity(load);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);

    }

    @SuppressWarnings("deprecation")
    public void setEndDate(View view) {
        showDialog(998);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        if (id == 998){
            return new DatePickerDialog(this,
                    myEndDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private DatePickerDialog.OnDateSetListener myEndDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showEndDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        startDate.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }

    private void showEndDate(int year, int month, int day) {
        endDate.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }

}




