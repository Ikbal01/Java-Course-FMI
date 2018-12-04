public class PatternMatcher {

    public static boolean match(String s, String p) {

        boolean isSubstring = false;

        int t = 0;

        while (p.charAt(t) == '*'){
            t++;
            if (t == p.length()){
                return true;
            }
        }

        for (int i = 0; i < s.length(); i++) {
            int iTemp = i;

            int index = t;
            while (index < p.length()){
                if (p.charAt(index) == s.charAt(iTemp) || p.charAt(index) == '?'){
                    isSubstring = true;

                    index++;
                    iTemp++;
                } else if (p.charAt(index) == '*'){
                    index++;
                    while (index < p.length() && iTemp < s.length() && p.charAt(index) != s.charAt(iTemp)){
                        if (p.charAt(index) == '?')
                        {
                            index++;
                            iTemp++;
                        } else if ( p.charAt(index) == '*'){
                            index++;
                        } else {
                            iTemp++;
                        }

                    }

                } else {
                    isSubstring = false;
                    break;
                }
                if (index == p.length()){
                    isSubstring = true;
                    break;
                }

                if (iTemp == s.length()){
                    if (p.charAt(index) == s.charAt(iTemp - 1) || p.charAt(index) == '*'){
                        isSubstring = check(p, index + 1);
                        break;
                    } else {
                        isSubstring = false;
                        break;
                    }

                }
            }
            if (isSubstring){
                return true;
            }
        }
        return false;
    }

    public static boolean check(String s, int index){
        for (int i = index; i < s.length(); i++) {
            if (s.charAt(i) != '*'){
                return false;
            }
        }
        return true;
    }
}
