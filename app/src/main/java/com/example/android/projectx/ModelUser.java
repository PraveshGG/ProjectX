package com.example.android.projectx;

/**
 * Created by Pravesh on 1/28/2018.
 */

public class ModelUser {

    String fName,mName,lName, dateOfBirth,age,birthPlace,designation,additionalContacts,gender, status,education,bloodGroup,work;

    public ModelUser(String fName, String mName, String lName, String dateOfBirth, String age, String birthPlace, String designation, String additionalContacts, String gender, String status, String education, String bloodGroup, String work) {
        this.fName = fName;
        this.mName = mName;
        this.lName = lName;
        this.dateOfBirth = dateOfBirth;
        this.age = age;
        this.birthPlace = birthPlace;
        this.designation = designation;
        this.additionalContacts = additionalContacts;
        this.gender = gender;
        this.status = status;
        this.education = education;
        this.bloodGroup = bloodGroup;
        this.work = work;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }





    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getAdditionalContacts() {
        return additionalContacts;
    }

    public void setAdditionalContacts(String additionalContacts) {
        this.additionalContacts = additionalContacts;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
}

