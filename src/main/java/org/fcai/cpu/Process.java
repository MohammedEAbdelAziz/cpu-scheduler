package org.fcai.cpu;

public class Process {
    String name;
    String color; 
    int arrivalTime;
    int burstTime;
    double priority;
    int waitingTime = 0;
    int turnaroundTime = 0;
    int remainingBurstTime;
    int quantum = 0;

    public Process(String name, String color, int arrivalTime, int burstTime, double priority, int quantum) {
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingBurstTime = burstTime;
        this.quantum = quantum;
    }
    public void changeRemainingBurstTime(int time) {
        this.remainingBurstTime -= time;
    }
}
