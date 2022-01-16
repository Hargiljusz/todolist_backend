package com.iwaniuk.todolist_auth.models;

public class Stats {

    private int numberOfBeans;
    private long numberOfUsers;
    private long numberOfToDos;
    private long jwtTime;
    private long refreshTokenTime;

    public Stats() {
    }

    public Stats(int numberOfBeans, long numberOfUsers, long numberOfToDos,  long jwtTime, long refreshTokenTime) {
        this.numberOfBeans = numberOfBeans;
        this.numberOfUsers = numberOfUsers;
        this.numberOfToDos = numberOfToDos;
        this.jwtTime = jwtTime;
        this.refreshTokenTime = refreshTokenTime;
    }

    public int getNumberOfBeans() {
        return numberOfBeans;
    }

    public void setNumberOfBeans(int numberOfBeans) {
        this.numberOfBeans = numberOfBeans;
    }

    public long getNumberOfUsers() {
        return numberOfUsers;
    }

    public void setNumberOfUsers(long numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }

    public long getNumberOfToDos() {
        return numberOfToDos;
    }

    public void setNumberOfToDos(long numberOfToDos) {
        this.numberOfToDos = numberOfToDos;
    }


    public long getJwtTime() {
        return jwtTime;
    }

    public void setJwtTime(long jwtTime) {
        this.jwtTime = jwtTime;
    }

    public long getRefreshTokenTime() {
        return refreshTokenTime;
    }

    public void setRefreshTokenTime(long refreshTokenTime) {
        this.refreshTokenTime = refreshTokenTime;
    }
}
