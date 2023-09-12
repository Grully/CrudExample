package com.dot.employee;

public class Employee {

        protected int id;
        protected String name;
        protected String position;
        protected double tax;

    public Employee(){
    }

    public Employee(int id){
        this.id = id;
    }

    public Employee(String name, String position, double tax) {
        this.name = name;
        this.position = position;
        this.tax = tax;
    }

    public Employee(int id, String name, String position, double tax) {
        this(name, position, tax);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }
}
