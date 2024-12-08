/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package org.fcai.cpu;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author Mohammed Essam
 */
public class FcaiCpu {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CpuSchedular cpuSchedular = new CpuSchedular();
        boolean firstRun = true;

        while (true) {
            if (firstRun || cpuSchedular.isEmpty()) {
                System.out.println("Choose input method:");
                System.out.println("1. Manual input");
                System.out.println("2. Input from file (input.txt)");
                System.out.println("3. Exit");

                int inputChoice = scanner.nextInt();

                switch (inputChoice) {
                    case 1:
                        inputProcesses(cpuSchedular);
                        break;
                    case 2:
                        inputProcessesFromFile(cpuSchedular);
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                        continue;
                }
                firstRun = false;
            }

            System.out.println("Choose a scheduling algorithm:");
            System.out.println("1. Non-preemptive Priority Scheduling");
            System.out.println("2. Non-preemptive Shortest Job First (SJF)");
            System.out.println("3. Shortest Remaining Time First (SRTF)");
            System.out.println("4. FCAI Scheduling");
            System.out.println("5. Drop all processes and start fresh");
            System.out.println("6. Exit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    cpuSchedular.nonPreemptivePriorityScheduling();
                    break;
                case 2:
                    cpuSchedular.nonPreemptiveSJF();
                    break;
                case 3:
                    cpuSchedular.shortestRemainingTimeFirst();
                    break;
                case 4:
                    cpuSchedular.FCAIScheduling();
                    break;
                case 5:
                    cpuSchedular = new CpuSchedular();
                    firstRun = true;
                    break;
                case 6:
                    System.out.println("Exiting....");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
                    continue;
            }

            System.out.println("Simulation completed. Do you want to run another simulation? (y/n)");
            String runAgain = scanner.next();
            if (!runAgain.equalsIgnoreCase("y")) {
                System.out.println("Exiting.");
                break;
            }
        }
    }

    private static void inputProcesses(CpuSchedular cpuSchedular) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int numProcesses = scanner.nextInt();

        for (int i = 0; i < numProcesses; i++) {
            System.out.println("Enter details for Process " + (i + 1) + ":");
            System.out.print("Name: ");
            String name = scanner.next();
            System.out.print("Color: ");
            String color = scanner.next();
            System.out.print("Arrival Time: ");
            int arrivalTime = scanner.nextInt();
            System.out.print("Burst Time: ");
            int burstTime = scanner.nextInt();
            System.out.print("Priority: ");
            int priority = scanner.nextInt();
            System.out.print("Quantum: ");
            int quantum = scanner.nextInt();

            cpuSchedular.addProcess(name, color, arrivalTime, burstTime, priority, quantum);
        }
    }

    private static void inputProcessesFromFile(CpuSchedular cpuSchedular) {
        try {
            File file = new File("input.txt");
            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                if (fileScanner.hasNext()) {
                    String name = fileScanner.next();
                    String color = fileScanner.next();
                    int arrivalTime = fileScanner.nextInt();
                    int burstTime = fileScanner.nextInt();
                    int priority = fileScanner.nextInt();
                    int quantum = fileScanner.nextInt();

                    cpuSchedular.addProcess(name, color, arrivalTime, burstTime, priority, quantum);
                }

            }

            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Exiting.");
        }
    }
}
