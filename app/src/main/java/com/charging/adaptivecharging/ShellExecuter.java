package com.charging.adaptivecharging;


import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ShellExecuter {

    public ShellExecuter() {

    }

    public void Executer(String ma) {

        String command="su -c chmod 666 /sys/class/power_supply/battery/constant_charge_current_max && echo "+ma+" > /sys/class/power_supply/battery/constant_charge_current_max && su -c chmod 444 /sys/class/power_supply/battery/constant_charge_current_max\n";
        try {
            Runtime.getRuntime().exec(command);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public String Get(String command) {

        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        String response = (output.toString()).substring(0,(output.toString()).length()-1);

        return response;

    }
}