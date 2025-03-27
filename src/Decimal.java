class Decimal {
    public static String toHex(int d) {
        int dividend = d;
        String result = "";
        while (dividend > 0) {
            int remainder = dividend % 16;
            String rn = (remainder > 9) ? new String(new char[] {"ABCDEF".toCharArray()[remainder - 10]}) : "" + remainder;
            result = rn + result;

            dividend = dividend / 16;
        }
        return result;
    }
}