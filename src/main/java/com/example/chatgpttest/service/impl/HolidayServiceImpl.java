package com.example.chatgpttest.service.impl;

import com.example.chatgpttest.model.Holiday;
import com.example.chatgpttest.service.HolidayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HolidayServiceImpl implements HolidayService {

    //generate Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(HolidayServiceImpl.class);


    // generate a add holiday method
    @Override
    public void addHoliday(List<Holiday> holidays) throws Exception {
        removeInvalidData(holidays);
        List<String> allHolidays = Files.readAllLines(ResourceUtils.getFile("classpath:holiday.csv").toPath());
        holidays.forEach(holiday -> allHolidays.add(holiday.toString()));
        Files.write(ResourceUtils.getFile("classpath:holiday.csv").toPath(), allHolidays);
    }

    // generate a delete holiday method
    @Override
    public void deleteHoliday(List<Holiday> holidays) throws Exception {
        List<Holiday> all = getAllHolidays();
        if(!CollectionUtils.isEmpty(all)){
            Iterator<Holiday> it = all.iterator();
            while(it.hasNext()){
                Holiday x = it.next();
                if(holidays.stream().filter(holiday -> holiday.getCountryCode().equals(x.getCountryCode()) && holiday.getHolidayDate().equals(x.getHolidayDate())).findAny().isPresent()){
                    it.remove();
                }
            }
            List<String> allColumns = all.stream().map(x->x.toString()).collect(Collectors.toList());
            Files.write(ResourceUtils.getFile("classpath:holiday.csv").toPath(), allColumns);
        }

    }

    // generate a update holiday method
    @Override
    public void updateHoliday(List<Holiday> holidays) throws Exception{
        removeInvalidData(holidays);
        List<Holiday> all = getAllHolidays();
        if(!CollectionUtils.isEmpty(all)){
            for (Holiday holiday : all) {
                Optional<Holiday> optional = holidays.stream().filter(x -> holiday.getCountryCode().equals(x.getCountryCode()) && holiday.getHolidayDate().equals(x.getHolidayDate())).findFirst();
                if(optional.isPresent()){
                    Holiday newHoliday = optional.get();
                    holiday.setCountryCode(newHoliday.getCountryCode());
                    holiday.setCountryDesc(newHoliday.getCountryDesc());
                    holiday.setHolidayName(newHoliday.getHolidayName());
                    holiday.setHolidayDate(newHoliday.getHolidayDate());
                }

            }
            List<String> allColumns = all.stream().map(x->x.toString()).collect(Collectors.toList());
            Files.write(ResourceUtils.getFile("classpath:holiday.csv").toPath(), allColumns);
        }

        List<String> allHolidays = Files.readAllLines(ResourceUtils.getFile("classpath:holiday.csv").toPath());
        holidays.forEach(holiday -> allHolidays.add(holiday.toString()));
    }

    // generate a get holiday method
    @Override
    public void getHoliday() {
        // generate read data from holiday csv file
        try {
            List<String> allHolidays = Files.readAllLines(Paths.get("holiday.csv"));
            //print all holidays
            for (String holiday : allHolidays) {
                LOGGER.info("Holiday: {}", holiday);
            }

        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("Error reading file", e);
        }
    }



    // generate get all holidays method
    @Override
    public List<Holiday> getAllHolidays() {
        // generate read data from holiday csv file
        List<Holiday> holidays = new ArrayList<>();
        try {
            List<String> allHolidays = Files.readAllLines(ResourceUtils.getFile("classpath:holiday.csv").toPath());
            //print all holidays
            for (String holiday : allHolidays) {
                String[] column = holiday.split(",");
                Holiday holiday1 = new Holiday();
                holiday1.setCountryCode(column[0]);
                holiday1.setCountryDesc(column[1]);
                holiday1.setHolidayDate(column[2]);
                holiday1.setHolidayName(column[3]);
                holidays.add(holiday1);
            }
            return holidays;
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("Error reading file", e);
        }
        return holidays;
    }

    @Override
    public Map<String, Object> getHolidayByDate(String date) throws Exception{
        Holiday holiday = new Holiday();
        holiday.setHolidayDate(date);
        holiday.setCountryCode("test");
        if(!isValidDate(holiday)){
            throw new Exception("invalid holiday!");
        }

        List<Holiday> allHoliday = getAllHolidays();
        Set<String> holidayCountries = new HashSet<>();
        Set<String> noneHolidayCountries = new HashSet<>();
        for (Holiday holiday1 : allHoliday) {
            if(Objects.equals(holiday1.getHolidayDate(),holiday.getHolidayDate())){
                holidayCountries.add(holiday1.getCountryCode());
            } else {
                noneHolidayCountries.add(holiday1.getCountryCode());
            }
            noneHolidayCountries.removeIf(x -> holidayCountries.contains(x));
            noneHolidayCountries.remove("Country Code");
        }
        Map<String, Object> result = new HashMap<>();
        result.put("holidayCountries", holidayCountries);
        result.put("noneHolidayCountries", noneHolidayCountries);
        return result;
    }

    private void removeInvalidData(List<Holiday> holidays) throws Exception {
        if(CollectionUtils.isEmpty(holidays)){
            throw new Exception("holiday is invalid");
        }
        holidays = holidays.stream().filter(holiday -> isValidDate(holiday)).collect(Collectors.toList());
        // remove duplicate holiday
        holidays = holidays.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(
                                () -> new TreeSet<>(Comparator.comparing(Holiday::getCountryCode).thenComparing(Holiday::getHolidayDate))), ArrayList::new));
    }

    private boolean isValidDate(Holiday holiday) {
        if(holiday == null || StringUtils.isEmpty(holiday.getHolidayDate()) || StringUtils.isEmpty(holiday.getCountryCode())){
            return false;
        }

        final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(holiday.getHolidayDate());
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
