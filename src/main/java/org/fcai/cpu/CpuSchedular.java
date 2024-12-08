package org.fcai.cpu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CpuSchedular {
    private static List<Process> processes = new ArrayList<>();

    public static void addProcess(String name, String color, int arrivalTime, int burstTime, int priority, int quantum) {
        processes.add(new Process(name, color, arrivalTime, burstTime, priority, quantum));
    }

    public static boolean isEmpty() {
        return processes.isEmpty();
    }

    public static void nonPreemptivePriorityScheduling() {
        processes.sort(Comparator.comparingInt((Process p) -> p.arrivalTime)
                .thenComparingDouble(p -> p.priority));

        System.out.println("Simulating Non-preemptive Priority Scheduling...");
        simulate();
    }

    public static void nonPreemptiveSJF() {
        processes.sort(Comparator.comparingInt((Process p) -> p.arrivalTime)
                .thenComparingInt(p -> p.burstTime));

        System.out.println("Simulating Non-preemptive Shortest Job First Scheduling...");
        simulate();
    }

    public static void shortestRemainingTimeFirst() {
        // TODO: Implement SRTF logic here
        processes.sort(Comparator.comparingInt((Process p) -> p.arrivalTime));

        System.out.println("Simulating Shortest Remaining Time First Scheduling...");

        int currentTime = 0;
        int completed = 0;
        int n = processes.size();
        int[] remainingBurstTime = new int[n];
        boolean[] isCompleted = new boolean[n];

        for (int i = 0; i < n; i++) {
            remainingBurstTime[i] = processes.get(i).burstTime;
        }

        while (completed != n) {
            int shortest = -1;
            int minBurstTime = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
            if (processes.get(i).arrivalTime <= currentTime && !isCompleted[i] && remainingBurstTime[i] < minBurstTime) {
                minBurstTime = remainingBurstTime[i];
                shortest = i;
            }
            }

            if (shortest == -1) {
            currentTime++;
            continue;
            }

            remainingBurstTime[shortest]--;
            currentTime++;

            if (remainingBurstTime[shortest] == 0) {
            isCompleted[shortest] = true;
            completed++;
            processes.get(shortest).waitingTime = currentTime - processes.get(shortest).arrivalTime - processes.get(shortest).burstTime;
            processes.get(shortest).turnaroundTime = currentTime - processes.get(shortest).arrivalTime;
            }
        }

        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        for (Process p : processes) {
            totalWaitingTime += p.waitingTime;
            totalTurnaroundTime += p.turnaroundTime;
            System.out.println("Process " + p.name + " executed with waiting time " + p.waitingTime + " and turnaround time " + p.turnaroundTime);
        }

        double avgWaitingTime = totalWaitingTime / n;
        double avgTurnaroundTime = totalTurnaroundTime / n;

        System.out.println("Average Waiting Time: " + avgWaitingTime);
        System.out.println("Average Turnaround Time: " + avgTurnaroundTime);
    
    }
        
    public static void FCAIScheduling() {
        
        int currentTime = 0;
        double V1 = getMaxArrivalTime() / 10.0;  // V1 Calculation
        double V2 = getMaxBurstTime() / 10.0;   // V2 Calculation

        Queue<Process> readyQueue = new LinkedList<>();
        List<Process> completedProcesses = new ArrayList<>();

        // Initialize FCAI Factor and quantum for each process
        for (Process p : processes) {
            p.remainingBurstTime = p.burstTime;
            p.turnaroundTime = 0;
            p.waitingTime = 0;
            p.priority = calculateFCAIFactor(p, V1, V2);
        }

        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            // Add processes to the ready queue based on arrival time
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime && !readyQueue.contains(p)) {
                    readyQueue.add(p);
                }
            }
            processes.removeIf(readyQueue::contains);

            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            // Pick the process with the highest FCAI priority factor
            Process current = selectProcessWithHighestPriority(readyQueue);
            readyQueue.remove(current);

            // Execute process non-preemptively for the first 40% of its quantum
            int quantum = Math.min(current.remainingBurstTime, (int) Math.ceil(0.4 * current.burstTime));
            currentTime += quantum;
            current.remainingBurstTime -= quantum;

            // If the process still has work left, update its FCAI factor and add it back to the queue
            if (current.remainingBurstTime > 0) {
                current.priority = calculateFCAIFactor(current, V1, V2);
                readyQueue.add(current);
            } else {
                // Process is completed
                current.turnaroundTime = currentTime - current.arrivalTime;
                current.waitingTime = current.turnaroundTime - current.burstTime;
                completedProcesses.add(current);
            }
        }

        // Print results
        printFCAIResults(completedProcesses);
    }

    private static double calculateFCAIFactor(Process p, double V1, double V2) {
        return (10 - p.priority) + (p.arrivalTime / V1) + (p.remainingBurstTime / V2);
    }

    private static Process selectProcessWithHighestPriority(Queue<Process> queue) {
        return queue.stream().min(Comparator.comparingDouble(p -> p.priority)).orElse(null);
    }

    private static int getMaxArrivalTime() {
        return processes.stream().mapToInt(p -> p.arrivalTime).max().orElse(1);
    }

    private static int getMaxBurstTime() {
        return processes.stream().mapToInt(p -> p.burstTime).max().orElse(1);
    }

    private static void printFCAIResults(List<Process> completedProcesses) {
        System.out.println("FCAI Scheduling Results:");
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        for (Process p : completedProcesses) {
            System.out.println("Process " + p.name + ": Waiting Time = " + p.waitingTime +
                    ", Turnaround Time = " + p.turnaroundTime);
            totalWaitingTime += p.waitingTime;
            totalTurnaroundTime += p.turnaroundTime;
        }

        System.out.println("Average Waiting Time: " + (totalWaitingTime / completedProcesses.size()));
        System.out.println("Average Turnaround Time: " + (totalTurnaroundTime / completedProcesses.size()));
    }

    private static void simulate() {
        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        for (Process p : processes) {
            if (currentTime < p.arrivalTime) {
                currentTime = p.arrivalTime;
            }
            p.waitingTime = currentTime - p.arrivalTime;
            p.turnaroundTime = p.waitingTime + p.burstTime;

            totalWaitingTime += p.waitingTime;
            totalTurnaroundTime += p.turnaroundTime;

            currentTime += p.burstTime;

            System.out.println("Process " + p.name + " executed from time " + 
                               (currentTime - p.burstTime) + " to " + currentTime);
        }

        double avgWaitingTime = (double) totalWaitingTime / processes.size();
        double avgTurnaroundTime = (double) totalTurnaroundTime / processes.size();

        System.out.println("Average Waiting Time: " + avgWaitingTime);
        System.out.println("Average Turnaround Time: " + avgTurnaroundTime);
    }
}
