package com.example.chatgpttest.service;

import com.example.chatgpttest.model.Holiday;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

@Service
public interface HolidayService {

    //generate a add holiday method
    void addHoliday(List<Holiday> holidays) throws Exception;

    //generate a delete holiday method
    void deleteHoliday(List<Holiday> holidays) throws Exception;

    //generate a update holiday method
    void updateHoliday(List<Holiday> holidays) throws Exception;

    //generate a get holiday method
    void getHoliday();

    //generate get all holidays method
    List<Holiday> getAllHolidays();

    Map<String, Object> getHolidayByDate(String date) throws Exception;
}
