package com.charging.adaptivecharging;


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
}