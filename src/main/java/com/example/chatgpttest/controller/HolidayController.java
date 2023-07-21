package com.example.chatgpttest.controller;

import com.example.chatgpttest.model.Holiday;
import com.example.chatgpttest.service.HolidayService;
import com.google.gson.Gson;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/holiday")
public class HolidayController {


    @Autowired
    private HolidayService holidayService;

    //generate a add holiday method
    @PostMapping("/add")
    public String addHolidays(@RequestBody List<Holiday> holidays) {
        // generate print statement
        try {
            holidayService.addHoliday(holidays);
            return "add holiday successfully";
        } catch (Exception e) {
            return "add holiday failed,error message:" + e.getMessage() ;
        }
    }

    //generate a delete holiday method
    @PostMapping("/delete")
    public String deleteHolidays(@RequestBody List<Holiday> holidays) {
        // generate print statement
        try {
            holidayService.deleteHoliday(holidays);
            return "delete holiday successfully";
        } catch (Exception e) {
            return "delete holiday failed,error message:" + e.getMessage() ;
        }
    }

    //generate a update holiday method
    @PostMapping("/update")
    public String updateHolidays(@RequestBody List<Holiday> holidays) {
        try {
            holidayService.updateHoliday(holidays);
            return "update holiday successfully";
        } catch (Exception e) {
            return "update holiday failed,error message:" + e.getMessage() ;
        }
    }
    //generate get all holidays method
    @GetMapping("/getAll")
    public List<Holiday> getAllHolidays() {
        return holidayService.getAllHolidays();
    }

    @GetMapping("/getHolidayByDate")
    public String getHolidayByDate(@RequestParam String date) {
        try {
            Gson gson = new Gson();
            return gson.toJson(holidayService.getHolidayByDate(date));
        } catch (Exception e) {
            return "getHolidayByDate error, error:" + e.getMessage();
        }
    }
    @GetMapping("/getNextYearHoliday")
    public List<Holiday> getNextYearHoliday() {
        List<Holiday> all = holidayService.getAllHolidays();
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
// 获取当月第一天
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        String date = format.format(calendar.getTime());
        all = all.stream().filter(x -> x.getHolidayDate().contains(date)).collect(Collectors.toList());

        return all;

    }

    @GetMapping("/getNextHolidayByCountry")
    public List<Holiday> getNextHolidayByCountry(@RequestParam String countryCode) {
        if(StringUtils.isEmpty(countryCode)){
            return new ArrayList<>();
        }

        List<Holiday> all = holidayService.getAllHolidays();
        all = all.stream().filter(x -> countryCode.equals(x.getCountryCode())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(all)){
            return new ArrayList<>();
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
// 获取当月第一天
        Calendar calendar = Calendar.getInstance();
        String currentDate = format.format(calendar.getTime());
        all = all.stream().filter(x ->
        {
            try {
                return format.parse(x.getHolidayDate()).after(format.parse(currentDate));
            } catch (ParseException e) {
                return false;
            }
        }).collect(Collectors.toList());

        return all;
    }



}
