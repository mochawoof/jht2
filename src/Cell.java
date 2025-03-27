class Cell {
    public byte b;
    public int i;
    public Cell(byte by, int in) {
        b = by;
        i = in;
    }
    public String toString() {
        return String.format("%02X", ((int) b) & 0xff);
    }
}