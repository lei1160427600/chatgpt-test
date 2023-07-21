package com.example.chatgpttest.model;

public class Holiday {
    private String countryCode;
    private String countryDesc;
    private String holidayDate;
    private String holidayName;

    //generate getter and setter methods
    public String getCountryCode() {
        // generate return statement
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        // generate setter method
        this.countryCode = countryCode;
    }

    public String getCountryDesc() {
        // generate return statement
        return countryDesc;
    }

    public void setCountryDesc(String countryDesc) {
        // generate setter method
        this.countryDesc = countryDesc;
    }

    public String getHolidayDate() {
        // generate return statement
        return holidayDate;
    }

    public void setHolidayDate(String holidayDate) {
        // generate setter method
        this.holidayDate = holidayDate;
    }

    public String getHolidayName() {
        // generate return statement
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        // generate setter method
        this.holidayName = holidayName;
    }

    //genrate a toString method
    @Override
    public String toString() {
        // generate return statement
        return countryCode + "," + countryDesc + "," + holidayDate + "," + holidayName;
    }


}
