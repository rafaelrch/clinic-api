package com.clinicapi.domain.person;

public abstract class Person {

    private Long id;
    private String name;
    private String email;
    private String phone;

    public Long getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getEmail(){
        return this.email;
    }

    public String getPhone(){
        return this.phone;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public Person(){

    }

    public Person(String name, String email, String phone){
        this.name = name;
        this.email = email;
        this.phone = phone;
    }



    @Override
    public String toString() {
        return "Id: " + getId() + ", Name: " + getName();
    }
}
