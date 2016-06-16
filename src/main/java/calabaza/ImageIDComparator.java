package calabaza;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageIDComparator implements Comparator<String> {

    @Override
    public int compare(String str1, String str2) {
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m1 = p.matcher(str1);
        Matcher m2 = p.matcher(str2);

        if (m1.find() && m2.find()) {
            int no1 = Integer.parseInt(m1.group());
            int no2 = Integer.parseInt(m2.group());
            if (no1 < no2) {
                return -1;
            } else if (no1 > no2) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }

    }

}
