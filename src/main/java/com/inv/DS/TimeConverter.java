package com.inv.DS;

public class TimeConverter {
    public static String convert(double time) {
        String total = "";
        if (time <= 1) {
            return "Now!";
        }
        int year = (int) time/(3600*24*365);
        int month = (int) (time%(3600*24*365))/(3600*24*30);
        int day = (int) ((time%(3600*24*365))%(3600*24*30))/(3600*24);
        int hour = ((int) ((time%(3600*24*365))%(3600*24*30))%(3600*24))/3600;
        int minute = ((int) (((time%(3600*24*365))%(3600*24*30))%(3600*24))%3600)/60;
        int second = ((int) (((time%(3600*24*365))%(3600*24*30))%(3600*24))%3600)%60;

        total += ((year != 0)? year + "y " : "") +
                ((month != 0)? month + "m " : "") +
                ((day != 0)? day + "d " : "") +
                ((hour != 0)? hour + "h " : "") +
                ((minute != 0)? minute + "m " : "") +
                ((second != 0)? second + "s " : "");
        return total;
    }

    public static double reverse(String time) {
        double total = 0, current = 0;
        for (int i = 0; i < time.length(); i++) {
            char c = time.charAt(i);
            if (c >= '0' && c <= '9') {
                current = current*10 + ((int) c - '0');
                System.out.println(((int) c - '0') + ": " + Double.toString(current));
            } else if (c == 'y' || c == 'o' || c == 'd' || c == 'h' || c == 'm' || c == 's') {
                switch (c) {
                    case 'y':
                        total += current*3600*24*365;
                        break;
                    case 'o':
                        total += current*3600*24*30;
                        break;
                    case 'd':
                        total += current*3600*24;
                        break;
                    case 'h':
                        total += current*3600;
                        break;
                    case 'm':
                        total += current*60;
                        break;
                    case 's':
                        total += current;
                        break;
                }
                current = 0;
            }
        }
        return total;
    }
}
