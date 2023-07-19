package com.digicoffer.lauditor.Calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.digicoffer.lauditor.Calendar.Models.Event_Details_DO;
import com.digicoffer.lauditor.R;
import com.digicoffer.lauditor.Webservice.AsyncTaskCompleteListener;
import com.digicoffer.lauditor.Webservice.HttpResultDo;

import java.util.ArrayList;

public class Meetings extends Fragment implements AsyncTaskCompleteListener,View.OnClickListener, MonthlyCalendar.EventDetailsListener,WeeklyCalendar.EventDetailsListener {
    LinearLayoutCompat ll_view_type;
    TextView tv_create_event,tv_view_calendar,tv_day_view,tv_month_view;
    ArrayList<Event_Details_DO> existingList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
     View view = inflater.inflate(R.layout.calendar,container,false);
        ll_view_type= view.findViewById(R.id.ll_view_type);
        tv_create_event = view.findViewById(R.id.tv_create_event);
        tv_view_calendar = view.findViewById(R.id.tv_view_calendar);
        tv_day_view = view.findViewById(R.id.tv_day_view);
        tv_month_view = view.findViewById(R.id.tv_month_view);
        loadCreateEvent();
        tv_view_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_view_type.getVisibility() == View.VISIBLE) {
                    // If it is already visible, you can handle the click to hide the view here
                    ll_view_type.setVisibility(View.GONE);
                } else {
                    ll_view_type.setVisibility(View.VISIBLE);
                    loadMonthView();
                }
            }
        });
        tv_day_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_view_type.setVisibility(View.VISIBLE);
                loadWeekView();
            }
        });
        tv_month_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_view_type.setVisibility(View.VISIBLE);
                loadMonthView();
            }
        });
        tv_create_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCreateEvent();
            }
        });
        tv_view_calendar.setOnClickListener(this);
        tv_create_event.setOnClickListener(this);
     return view;
    }



    private void loadMonthView() {

        tv_month_view.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.button_right_green_count));
        tv_day_view.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.button_left_background));
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        MonthlyCalendar nonSubmittedTimesheets = new MonthlyCalendar();
        ft.replace(R.id.child_container_timesheets, nonSubmittedTimesheets);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void loadWeekView() {

        tv_month_view.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.button_right_background));
        tv_day_view.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.button_left_green_background));
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        WeeklyCalendar nonSubmittedTimesheets = new WeeklyCalendar();
        ft.replace(R.id.child_container_timesheets, nonSubmittedTimesheets);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

    //    public ArrayList<Event_Details_DO> getExcisitingArryaList() {
//        return  existingList;
//    }
//    public void loadEditEvent(ArrayList<Event_Details_DO> event_details_list) {
//
//        existingList = event_details_list;
//        Log.d("EventList",existingList.toString());
//        Fragment childFragment = new EditEvent();
//        Bundle args = new Bundle();
//
//        FragmentManager childFragmentManager = getChildFragmentManager();
//        childFragmentManager.beginTransaction().add(R.id.child_container_timesheets, childFragment).commit();
//    }
    private void loadView() {
        tv_view_calendar.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.button_right_green_count));
        tv_create_event.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.button_left_background));


    }
    private void loadCreateEvent(){
        tv_view_calendar.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.button_right_background));
        tv_create_event.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.button_left_green_background));
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        CreateEvent nonSubmittedTimesheets = new CreateEvent();
        ft.replace(R.id.child_container_timesheets, nonSubmittedTimesheets);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);

        ll_view_type.setVisibility(View.GONE);
        ft.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_view_calendar:
                tv_month_view.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.button_right_green_count));
                tv_day_view.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.button_left_background));
                ll_view_type.setVisibility(View.VISIBLE);
                loadMonthView();
                break;
            case R.id.tv_create_event:
                ll_view_type.setVisibility(View.GONE);
                loadCreateEvent();
                break;
        }
    }

    @Override
    public void onAsyncTaskComplete(HttpResultDo httpResult) {

    }

    @Override
    public void onEventDetailsPassed(ArrayList<Event_Details_DO> event_details_list, String calendar_Type) {
        EditEvent editEventFragment = new EditEvent();
        editEventFragment.setEventDetailsList(event_details_list,calendar_Type);
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.child_container_timesheets, editEventFragment)
                .commit();
    }
    public void loadViewEvent(String calendar_type){
        Fragment fragment = new Fragment();
        if (calendar_type=="Monthly") {
            fragment= new MonthlyCalendar();
        }else if (calendar_type=="Weekly"){
            fragment = new WeeklyCalendar();
        }else{
            fragment= new MonthlyCalendar();
        }
//        editEventFragment.setEventDetailsList(event_details_list);
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.child_container_timesheets, fragment)
                .commit();
    }
}
