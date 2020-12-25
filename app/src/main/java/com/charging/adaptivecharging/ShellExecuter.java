package com.charging.adaptivecharging;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

public class ShellExecuter {
    BufferedWriter bw;
    BufferedReader br;
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


    public  boolean RootCheck()
    {
        boolean retval = false;
        Process suProcess;
        try
        {
            suProcess = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
            DataInputStream osRes = new DataInputStream(suProcess.getInputStream());

            if (null != os && null != osRes)
            {
                os.writeBytes("id\n");
                os.flush();

                String currUid = osRes.readLine();
                boolean exitSu = false;
                if (null == currUid)
                {
                    retval = false;
                }
                else if (true == currUid.contains("uid=0"))
                {
                    retval = true;
                    exitSu = true;

                }
                else
                {
                    retval = false;
                    exitSu = true;
                }

                if (exitSu)
                {
                    os.writeBytes("exit\n");
                    os.flush();
                }
            }
        }
        catch (Exception e)
        {

            retval = false;
        }

        return retval;
    }
}